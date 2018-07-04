package com.ejiahe.jeservice.jsonrpc.server.session;

import com.alibaba.fastjson.JSONObject;
import com.ejiahe.jeservice.jsonrpc.packet.JSONRPCError;
import com.ejiahe.jeservice.jsonrpc.packet.JSONRPCResponse;

import java.io.InputStream;
import java.util.Set;

public interface JSONRPC2Session {
	
	
	
	/**
	 * 写入数据到client端
	 * @param o
	 */
	public void write(Object o);
	
	/**
	 * 将输入流写入client端
	 * @param input
	 */
	public void write(InputStream input);


	/**
	 * 返回Error给client端
	 * @param error
	 * @param id
	 */
	public void write(JSONRPCError error, Object id);

	/**
	 * 返回Response给client端
	 * @param response
	 */
	public void write(JSONRPCResponse response);

	/**
	 * 返回Response给client端
	 * @param params
	 * @param id
	 */
	public void write(JSONObject params, Object id);

	/**
	 * 回收资源并关闭该session
	 */
	public void close();
	
	/**
	 * 获取session对应的client端IP
	 * @return
	 */
	public String getHostAddress();
	
	/**
	 * 获取session对应的client端主机名
	 * @return
	 */
	public String getHostName();
	
	
	/**
	 * 获取连接对应的调用端id
	 * @return
	 *
	 */
	public String getClientId();
	
	
	/**
	 * 获取属性;
	 * @param name 属性名
	 * @return
	 *
	 */
	public Object getAttr(String name);
	
	/**
	 * 设置属性
	 * @param name 属性名
	 * @param value 属性值
	 *
	 */
	public void putAttr(String name, Object value);
	
	/**
	 * 获取属性名列表
	 * @return
	 *
	 */
	public Set<String> getAttrNames();
	
	
}
