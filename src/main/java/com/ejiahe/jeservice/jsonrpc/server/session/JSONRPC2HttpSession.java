package com.ejiahe.jeservice.jsonrpc.server.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 该类抽象了http jsonrpc2 读写通道,
 * 提供对jsonrpc2消息的读写功能
 * @author MaiLingFeng
 *
 */
public class JSONRPC2HttpSession extends JSONRPC2AbstractSession{
	private Logger Log = LoggerFactory.getLogger(JSONRPC2HttpSession.class);
	private HttpServletResponse resp;
	private HttpServletRequest req;
	
	private String clientId="";
	
	private static final String default_contenType = "application/json;charset=UTF-8";
	private static final String default_encoding = "UTF-8";
	
	private Map<String,Object> attrs = new HashMap<String,Object>();
	
	public JSONRPC2HttpSession(HttpServletRequest req , HttpServletResponse resp){
		this.resp = resp;
		this.req = req;
		resp.setCharacterEncoding(JSONRPC2HttpSession.default_encoding);
		resp.setContentType(JSONRPC2HttpSession.default_contenType);

		Enumeration attrNames = req.getSession().getAttributeNames();
		while (attrNames.hasMoreElements()){
			String attrName = String.valueOf(attrNames.nextElement());
			attrs.put(attrName,req.getAttribute(attrName));
		}

	}
	
	/**
	 * 设置HttpResponse的characterEncoding
	 * @param characterEncoding
	 */
	public void setHttpCharacterEncoding(String characterEncoding){
		if(characterEncoding != null){
			resp.setCharacterEncoding(characterEncoding);
		}
		
	}
	
	/**
	 * 设置HttpResponse的contentType
	 * @param contentType
	 */
	public void setHttpContentType(String contentType){
		if(contentType != null){
			resp.setContentType(contentType);
		}
	}
	
	@Override
	public void close() {
		//ignore;
	}

	
	@Override
	public void write(Object o) {
		try {
			Writer writer = resp.getWriter();
			String respJSONStr = o.toString();
			Log.debug("the response data : " + respJSONStr);
			
			//提供前端ajax跨域访问
			String queryStr = req.getQueryString();
			if(queryStr != null && queryStr.contains("callback")){
				String callbackFunction = req.getParameter("callback");
				respJSONStr = callbackFunction + "(" + respJSONStr + ")";
				writer.write(respJSONStr);
			}else{
				writer.write(respJSONStr);
			}
			
		} catch (IOException e) {
			Log.error(e.getMessage());
		}
	}

	@Override
	public String getHostAddress() {
		
		return req.getRemoteAddr();
	}

	@Override
	public String getHostName() {
		return req.getRemoteHost();
	}

	/**
	 * 获取HTPP SESSION
	 * @return
	 */
	public HttpSession getHttpSession(){
		return req.getSession();
	}
	
	/**
	 * 获取HTTP SESSION, 如果当前会话不存在session实例的时候, 
	 * create=false,则会返回null, 
	 * 若create=true,则会new a session
	 * @param create
	 * @return
	 */
	public HttpSession getHttpSession(boolean create){
		return req.getSession(create);
	}

	/**
	 * 获取HTTP Servlet Response
	 * @return
	 */
	public HttpServletResponse getHttpResponse(){
		return resp;
	}
	
	/**
	 * 获取HTTP Servlet Request
	 * @return
	 */
	public HttpServletRequest getHttpRequest(){
		return req;
	}

	@Override
	public String getClientId() {
		
		return clientId;
	}
	
	
	public void setClientId(String clientId){
		this.clientId = clientId;
	}

	@Override
	public Object getAttr(String name) {
		return attrs.get(name);
	}

	@Override
	public void putAttr(String name, Object value) {
		if(name != null && value != null){
			attrs.put(name, value);
		}
	}

	@Override
	public Set<String> getAttrNames() {
		return attrs.keySet();
	}

	@Override
	public void write(InputStream input) {
		byte[] buf = new byte[2048];
		int len;
		try {
			len = input.read(buf);
			OutputStream output = resp.getOutputStream();
			while(len != -1){
				output.write(buf, 0, len);
				len = input.read(buf);
			}
		} catch (IOException e) {
			Log.error(e.getMessage());
		} finally{
			if(input != null){
				try {
					input.close();
				} catch (IOException e) {
					//ignore;
				}
			}
		}
	}
	
}
