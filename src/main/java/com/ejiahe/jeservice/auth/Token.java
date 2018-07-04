package com.ejiahe.jeservice.auth;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.Date;

/**
 * Token格式:
 * deadline:{"xxx":"xxx",...}
 * 
 * @author MaiJingFeng
 */
public interface Token extends Serializable{
	/**
	 * 调用方身份类型
	 */
	public static final String CLIENT_TYPE = "client_type";
	
	/**
	 * 调用方身份标识
	 */
	public static final String CLIENT_ID = "client_id";
	/**
	 * 调用方身份标识name
	 */
	public static final String CLIENT_NAME = "client_name";

	/**
	 * 获取授权类型或终端
	 * <p>
	 * 对于Client_Type=user时,该值常为客户端的类型,例如"qxAndroid,qxWindows"等
	 */
	public static final String GRANT_TYPE = "grant_type";
	
	/**
	 * 过期时间
	 */
	public static final String DEADLINE = "deadline";
	
	/**
	 * 生成时间
	 */
	public static final String CREATION_DATE = "creation_date";
	
	/**
	 * 随机数
	 */
	public static final String NONCE = "nonce";

	/**
	 * 调用方身份：普通用户
	 */
	public static final String CLIENT_TYPE_USER = "user";

	/**
	 * 调用方身份：应用代理
	 */
	public static final String CLIENT_TYPE_APP_AGENT = "app_agent";
	
	/**
	 * 调用方身份：第三方应用套件
	 */
	public static final String CLIENT_TYPE_THIRD_SUITE = "third_suite";
	
	/**
	 * 调用方身份：第三方应用套件
	 */
	public static final String CLIENT_TYPE_SDKAPP = "sdk_app";
	
	
	/**
	 * 获取令牌的过期时间
	 * @return
	 *
	 */
	public Date getDeadline();
	
	/**
	 * 获取令牌的生成时间
	 * @return
	 *
	 */
	public Date getCreationDate();
	
	/**
	 * 获取调用者身份类型
	 * @return
	 *
	 */
	public String getClientType();
	
	/**
	 * 获取调用者身份标识(ID)
	 * @return
	 *
	 */
	public String getClientId();

	public String getClientName();

	/**
	 * 获取授权类型或终端;
	 * @return
	 *
	 */
	public String getGrantType();
	
	/**
	 * 获取随机数
	 * @return
	 *
	 */
	public String getNonce();
	
	/**
	 * 获取令牌的参数列表
	 * @return
	 *
	 */
	public JSONObject getProperties();

	/**
	 * 获取属性
	 * @return
     */
	public String getPropertyAsString(String key);


	/**
	 * 获取属性
	 * @return
     */
	public Object getProperty(String key);

	/**
	 * 是否已失效
	 * @return
	 *
	 */
	public boolean isDead();
}
