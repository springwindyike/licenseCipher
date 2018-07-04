package com.ejiahe.jeservice.jsonrpc.server.auth;

import java.io.Serializable;
import java.util.Date;

/**
 * JSONRPCToken = urlsafe_base64_encode(clientId:secretKey:deadline)
 * 其中secretKey为client login后产生的, 而deadline为时间毫米数,表示令牌的过期时间.
 * 
 * @author MaiJingFeng
 */
public interface JSONRPCToken extends Serializable{

	/**
	 * 获取客户端id
	 * @return
	 *
	 */
	public String getClientId();
	
	/**
	 * 获取客户端的jsonrpc登录秘钥
	 * @return
	 *
	 */
	public String getSecretKey();
	
	/**
	 * 获取令牌的过期时间
	 * @return
	 *
	 */
	public Date getDeadline();
	
	
	/**
	 * 刷新令牌的过期时间
	 * @param date
	 *
	 */
	public void refreshDeadline(Date date);
	
	
	/**
	 * 输出经过base64编码的token,即urlsafe_base64_encode(clientId:secretKey:deadline)
	 * @return
	 *
	 */
	public String asEncodedToken();
	
}
