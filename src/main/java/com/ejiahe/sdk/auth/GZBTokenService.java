package com.ejiahe.sdk.auth;

import com.ejiahe.jeservice.util.JEConstants;
import com.ejiahe.jeservice.util.LockManager;
import com.ejiahe.jeservice.util.common.GZBApiException;
import com.ejiahe.sdk.api.TokenApi;
import com.ejiahe.sdk.openbean.AccessToken;
import com.ejiahe.sdk.openbean.SuiteToken;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class GZBTokenService {

	private static GZBTokenService tokenServ = new GZBTokenService();

	private static final String PREFIX_AGENT = "agent_";
	private static final String PREFIX_APP = "app_";

	private SuiteToken suiteToken;

	private Cache<String,AccessToken> tokens;

	private GZBTokenService(){
		tokens = CacheBuilder.newBuilder().expireAfterWrite(1 * JEConstants.HOUR
				, TimeUnit.MILLISECONDS).build();

	}
	
	public static GZBTokenService getInstance(){
		return tokenServ;
	}

	public Cache<String, AccessToken> getTokens() {
		return tokens;
	}


//	public SuiteToken getSuiteToken() throws GZBApiException {
//		if(suiteToken != null && !suiteToken.isExpire()){
//			return suiteToken;
//		}else{
//			GZBApiConfig config = GZBApiConfig.getInstance();
//			SuiteToken temp = TokenApi.getSuiteToken(config.getSuiteId(),config.getSuiteSecret());
//			if(temp.isSuccess()){
//				suiteToken = temp;
//				return temp;
//			}else{
//				throw new GZBApiException(temp);
//			}
//		}
//	}


	/**
	 * 适用于企业应用代理级别 的AccssToken获取
	 * @return
	 * @throws GZBApiException
	 */
	public AccessToken getAgentAccessToken(String agentId, String secret) throws GZBApiException {
		AccessToken token = tokens.getIfPresent(getAgentTokenKey(agentId));
		if(token == null || token.isExpire()){
			Lock lock = LockManager.getInstance().getLocalLock("GZBToken", agentId);
			lock.lock();
			try{
				token = tokens.getIfPresent(getAgentTokenKey(agentId));
				if(token == null || token.isExpire()){
					token = askAgentToken(agentId,secret);
					if(token != null && token.isSuccess()){
						tokens.put(getAgentTokenKey(agentId), token);
					}else{
						throw new GZBApiException(token);
					}
				}
			}finally{
				lock.unlock();
			}
		}

		return token;
	}
	
	/**
	 * 获取公司授权的AccessToken,可能是应用代理AccessToken,也可能是套件应用的AccessToken
	 * @return
	 * @throws GZBApiException
	 */
	public AccessToken getAccessToken(String appId, String secret, String corp_id) throws GZBApiException {
		AccessToken token = tokens.getIfPresent(getAppTokenKey(appId, corp_id));
		if(token == null || token.isExpire()){
			Lock lock = LockManager.getInstance().getLocalLock("GZBToken", corp_id);
			lock.lock();
			try{
				token = tokens.getIfPresent(getAppTokenKey(appId, corp_id));
				if(token == null || token.isExpire()){
					token = askToken(appId,secret, corp_id);
					if(token != null && token.isSuccess()){
						tokens.put(getAppTokenKey(appId, corp_id), token);
					}else{
						throw new GZBApiException(token);
					}
				}
			}finally{
				lock.unlock();
			}
		}
		
		return token;
	}
	
	
	/**
	 * 获取应用的AccessToken
	 * @param appId 应用id
	 * @param corp_id 公司id
	 * @return
	 * @throws GZBApiException
	 */
	private AccessToken askToken(String appId, String secret, String corp_id) throws GZBApiException {
		return TokenApi.getAccessTokenByThirdApp(appId, secret, corp_id);
	}

	private AccessToken askAgentToken(String agentId, String secret) throws GZBApiException {
		return TokenApi.getAccessToken(agentId, secret);
	}

	private String getAgentTokenKey(String agentId){
		return PREFIX_AGENT + agentId;
	}

	private String getAppTokenKey(String appId, String corpId){
		return PREFIX_APP + appId + "_" + corpId;
	}

}
