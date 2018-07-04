package com.ejiahe.jeservice.jsonrpc.server;

import com.ejiahe.jeservice.jsonrpc.packet.JSONRPCPacket;
import com.ejiahe.jeservice.jsonrpc.server.session.JSONRPC2Session;

import javax.inject.Named;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Named
public class JSONRPC2InterceptorManager {

	private Map<String,JSONRPC2Interceptor> interceptors = new ConcurrentHashMap<String,JSONRPC2Interceptor>();
	
	
	/**
	 * 添加JSONRPC2 Message拦截器
	 * <br/>
	 * 通过加载拦截器，可以进行相关切面性业务，例如，
	 * 可以有认证拦截器、黑名单拦截器、日志记录器等
	 * @param interceptor 
	 */
	public void addJSONRPC2Interceptor(JSONRPC2Interceptor interceptor){
		if(interceptor != null && interceptor.getName() != null){
			interceptors.put(interceptor.getName(), interceptor);
		}
	}
	
	
	public Collection<JSONRPC2Interceptor> getInterceptors(){
		return interceptors.values();
	}
	
	public void invokeInterceptors(JSONRPC2Session session , JSONRPCPacket message, boolean incoming) throws JSONRPC2MessageRejectException {
		//业务前拦截处理
		for(JSONRPC2Interceptor interceptor : getInterceptors()){
			interceptor.intercept(session, message, incoming);
		}
	}
	
	
	public void removeAll(){
		interceptors.clear();
	}
	
}
