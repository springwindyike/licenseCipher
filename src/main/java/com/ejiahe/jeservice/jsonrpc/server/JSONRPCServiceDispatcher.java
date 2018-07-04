package com.ejiahe.jeservice.jsonrpc.server;

import com.ejiahe.jeservice.jsonrpc.packet.*;
import com.ejiahe.jeservice.jsonrpc.server.session.JSONRPC2Session;
import com.ejiahe.jeservice.util.ClassUtil;
import com.ejiahe.jeservice.util.StatusCode;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.collections.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.dao.DataAccessException;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 该类是jsonrpc2服务中心, 主要提供远程调用方法到对应jsonrpc2服务的导航<br/>
 * 所有的jsonrpc2子服务应该均在此中心下注册<br/>
 * 采用遍历查询的方式<br/>
 * 
 * @author MaiLingFeng
 * 
 */
@Named
public class JSONRPCServiceDispatcher implements ApplicationListener{
	
	private static Logger Log = LoggerFactory.getLogger(JSONRPCServiceDispatcher.class);
	private static final String SCOPE_SPERATOR = ".";
	
	private boolean isStart = false;

	@Inject
	private JSONRPC2InterceptorManager interceptorManager;

	/**
	 * jsonrpc服务组件列表
	 */
	private List<JSONRPC2Service> notScopeServices = new ArrayList<JSONRPC2Service>();
	
	private Map<String, JSONRPC2Service> scopeServices = new ConcurrentHashMap<String, JSONRPC2Service>();

	
	public JSONRPCServiceDispatcher() {
		
	}
	
	
	/**
	 * 添加JSONRPC2服务<br/>
	 * 
	 * @param service
	 */
	public void registerJSONRPC2Service(JSONRPC2Service service) {
		if (service != null) {
			JSONRPC2Service old = null;
			String serviceScope = service.getScope();
			if (serviceScope != null && !serviceScope.isEmpty()) {
				old = scopeServices.remove(serviceScope);
				scopeServices.put(serviceScope, service);
			} else {
				notScopeServices.add(service);
			}

			try{
				service.start();
			}catch (Exception e){
				Log.warn(e.getMessage(),e);
			}

			try{
                if(old != null){
                    old.stop();
                }
			}catch (Exception e){
				Log.warn(e.getMessage(),e);
			}
		}
	}

	/**
	 * 取消JSONRPC2服务<br/>
	 *
	 * @param service
	 */
	public void unRegisterJSONRPC2Service(JSONRPC2Service service) {
		if (service != null) {
			String serviceScope = service.getScope();
			if (serviceScope != null && !serviceScope.isEmpty()) {
				scopeServices.remove(serviceScope);
			} else {
				notScopeServices.remove(service);
			}

			try{
				service.stop();
			}catch (Exception e){
				Log.warn(e.getMessage(),e);
			}

		}
	}
	

//	
//	/**
//	 * 获取服务中心所持有的所有服务方法功能
//	 * 
//	 * @return
//	 */
//	public Set<String> getServiceMethods() {
//		Set<String> serviceMethods = new HashSet<String>();
//		for (JSONRPC2Service service : notScopeServices) {
//			for (String method : service.getAllServiceMethods()) {
//				serviceMethods.add(method);
//			}
//		}
//		
//		return serviceMethods;
//	}
	
	
	/**
	 * 处理JSONRPC2消息
	 * 
	 * @param session
	 * @param message
	 */
	public void processJSONRPC2Message(JSONRPC2Session session, JSONRPCPacket message) {

		try {
			Log.debug("the request data : " + message.toString());

			//拦截处理
			interceptorManager.invokeInterceptors(session, message, true);

			if (message instanceof JSONRPCRequest) {
				handleJSONRPC2Request(session, (JSONRPCRequest) message);
			} else if (message instanceof JSONRPCNotification) {
				handleJSONRPC2Notification(session, (JSONRPCNotification) message);
			} else if (message instanceof JSONRPCResponse) {
				handleJSONRPC2Response(session, (JSONRPCResponse) message);
			} else {
				Log.error("unknown jsonrpc2 message type");
			}

		} catch (Exception e) {
			Log.error(e.getMessage(), e);

			//若是request , 则默认返回exception错误码
			if (message instanceof JSONRPCRequest) {
				JSONRPCError error = null;
				if (e instanceof JSONRPCError) {
					error = (JSONRPCError) e;
				} else {
					error = new JSONRPCError(JSONRPCError.INTERNAL_ERROR.getCode(), e.getMessage());
				}
				session.write(error, ((JSONRPCRequest) message).getId());
			}
		}

		Log.debug("accept a jsonrpc2 message and ready handle it");
		// json服务

	}
	
	
	/**
	 * 处理JSONRPC2请求
	 * 
	 * @param session
	 * @param request
	 */
	public void handleJSONRPC2Request(JSONRPC2Session session, JSONRPCRequest request) {
		long startTime = System.currentTimeMillis();
		long startTicks = System.nanoTime();
		try {
			if (isScopeRequest(request)) {
				handleRequestByScopeService(session, request);
			} else {
				handleRequestByNoScopeService(session, request);
			}
		} catch (Exception e) {
			Throwable temp = e.getCause();
			if (temp != null && temp instanceof UndeclaredThrowableException) {
				temp = ((UndeclaredThrowableException) temp).getCause();
			}
			
			if(temp == null){
				temp = e;
			}
			
			JSONRPCError error = null;
			
			//若是JSONRPCError异常,则直接写会到客户端
			if (temp instanceof JSONRPCError) {
				error = (JSONRPCError) temp;
				Log.warn("{} - {}",session.getClientId() + ":" + session.getHostAddress() + ":" + request.getMethod(),temp.getMessage());
			} else{
				Log.error(session.getClientId() + ":" + session.getHostAddress() + ":" + request.getMethod() + " - " + temp.getMessage(), temp);
				
				if(temp instanceof DataAccessException){
					error = JSONRPCError.INTERNAL_ERROR;
				} else{
					error = new JSONRPCError(StatusCode.INTERNAL_SERVER_ERROR,temp.getMessage());
				}
			}
			
			session.write(error, request.getId());
				
		}
	}
	
	

	
	
	/**
	 * 处理JSONRPC2回复
	 * 
	 * @param session
	 * @param response
	 */
	public void handleJSONRPC2Response(JSONRPC2Session session, JSONRPCResponse response) {
		//ignore;
	}
	
	
	/**
	 * 处理JSONRPC2通知
	 * 
	 * @param session
	 * @param notification
	 */
	public void handleJSONRPC2Notification(JSONRPC2Session session,
			JSONRPCNotification notification) {
		//ignore;
	}
	
	public void stop() {
		isStart = false;
		stopAllJSONRPC2Service();
		Log.info("jeJSONRPC2Service stoped");
	}
	
	
	private boolean isScopeRequest(JSONRPCRequest request){
		if (request.getMethod().lastIndexOf(SCOPE_SPERATOR) != -1) {
			return true;
		}else{
			return false;
		}
	}
	
	
	private void handleRequestByScopeService(JSONRPC2Session session, JSONRPCRequest request) throws JSONRPCError, InvocationTargetException{
		String methodName = request.getMethod();
		int separatorIndex = methodName.lastIndexOf(SCOPE_SPERATOR);
		int fullMethodLength = methodName.length();
		if (separatorIndex > 0 && separatorIndex <= (fullMethodLength - 1)) {
			String scope = methodName.substring(0, separatorIndex);
			String subMethodName = methodName.substring(separatorIndex + 1);
			
			Object service = scopeServices.get(scope);
			if(service == null || subMethodName == null){
				throw new JSONRPCError(JSONRPCError.METHOD_NOT_FOUND.getCode(),"the jsonrpc service can not found");
			}
			
			Object[] params = {request , session};
			try {
				MethodUtils.invokeMethod(service, subMethodName, params);
			} catch (NoSuchMethodException e) {
				throw JSONRPCError.METHOD_NOT_FOUND;
			} catch (IllegalAccessException e) {
				throw JSONRPCError.METHOD_NOT_FOUND;
			} catch (InvocationTargetException e) {
				throw e;
			}
		}else{
			throw new IllegalArgumentException("this request is not a scope jsonrpc request");
		}
		
	}
	
	
	private void handleRequestByNoScopeService(JSONRPC2Session session, JSONRPCRequest request) throws JSONRPCError, InvocationTargetException{
		String methodName = request.getMethod();
		// 根method,直接分派
		for (JSONRPC2Service service : notScopeServices) {
			if (isJSONRPCServiceHasMethod(service,methodName)) {
				Object[] params = {request , session};
				try {
					MethodUtils.invokeMethod(service, methodName, params);
					break;
				} catch (NoSuchMethodException e) {
					throw JSONRPCError.METHOD_NOT_FOUND;
				} catch (IllegalAccessException e) {
					throw JSONRPCError.METHOD_NOT_FOUND;
				} catch (InvocationTargetException e) {
					throw e;
				}
			}
		}
		
	}
	
	
	
	private boolean isJSONRPCServiceHasMethod(JSONRPC2Service service , String targetMethod){
		List<String> ownPublicMethods = ClassUtil.getAllPublicMethodNameWithOwn(service.getClass());
		List<String> parentPublicMethods = ClassUtil.getAllPublicMethodName(JSONRPC2Service.class);
		List<String> serviceMethods = ListUtils.subtract(ownPublicMethods, parentPublicMethods);
		return serviceMethods.contains(targetMethod);
	}
	
	
	/**
	 * 扫描并装载JSONRPC2Service
	 * 
	 *
	 */
	private void scanAndLoadJSONRPCService(ApplicationContext context){
		Map<String,JSONRPC2Service> existService = context
				.getBeansOfType(JSONRPC2Service.class);
		for(JSONRPC2Service service : existService.values()){
			registerJSONRPC2Service(service);
		}
	}
	
	/**
	 * 停止并移除所有已装载的JSONRPC2Service
	 * 
	 *
	 */
	private void stopAllJSONRPC2Service() {
		if (scopeServices != null) {
			for (JSONRPC2Service service : scopeServices.values()) {
				service.stop();
			}
			
			scopeServices.clear();
		}
		
		if (notScopeServices != null) {
			for (JSONRPC2Service service : notScopeServices) {
				service.stop();
			}
			
			notScopeServices.clear();
		}
	}


    @Override
    public void onApplicationEvent(ApplicationEvent event) {

        if(event instanceof ContextStartedEvent || event instanceof ContextRefreshedEvent){
            stopAllJSONRPC2Service();
            ApplicationContext context = (ApplicationContext)event.getSource();
            scanAndLoadJSONRPCService(context);
        }

    }
}
