package com.ejiahe.sdk.api;

import com.alibaba.fastjson.JSONObject;
import com.ejiahe.jeservice.util.StatusCode;
import com.ejiahe.jeservice.util.StrUtils;
import com.ejiahe.jeservice.util.common.GZBApiException;
import com.ejiahe.jeservice.util.common.GZBResultParseUtil;
import com.ejiahe.jeservice.util.exception.HttpException;
import com.ejiahe.jeservice.util.http.HttpSender;
import com.ejiahe.sdk.config.GZBApiConfig;
import com.ejiahe.sdk.openbean.AccessToken;
import com.ejiahe.sdk.openbean.AppToken;
import com.ejiahe.sdk.openbean.SuiteToken;

/**
 * Token API
 * @author MaiLingFeng
 *
 */
public class TokenApi{

	public static final String GET_SUITE_TOKEN_PATH = "/ext/get_suite_token";
	public static final String GET_APP_TOKEN_PATH = "/ext/get_app_token";
	public static final String GET_SUITE_ACCESS_TOKEN_PATH = "/ext/get_access_token?suite_access_token={}";
	public static final String GET_APP_ACCESS_TOKEN_PATH = "/ext/get_access_token?app_access_token={}";
	public static final String GET_ACCESS_TOKEN_PATH = "/get_token?agent_id={}&agent_secret={}";
	public static final String GET_ACCESS_TOKEN_BY_APP_PATH = "/get_token?app_id={}&app_secret={}&corp_id={}";

	public static final String PROP_SUITE_ID = "suite_id";//套件Id
	public static final String PROP_SUITE_SECRET = "suite_secret";//套件密码

	public static final String PROP_APP_ID = "app_id";//应用Id
	public static final String PROP_APP_SECRET = "app_secret";//应用密码
	
	public static SuiteToken getSuiteToken(String suiteId, String suiteSecret) throws GZBApiException {
		JSONObject params = new JSONObject();
		params.put(PROP_SUITE_ID, suiteId);
		params.put(PROP_SUITE_SECRET, suiteSecret);
		
		try {
			return GZBResultParseUtil.parseObject(
					HttpSender.getHttpSender().httpPost(GZBApiConfig.getInstance().getEimBaseUrl() + GET_SUITE_TOKEN_PATH, params),
					SuiteToken.class);
		} catch (HttpException e) {
			throw new GZBApiException(StatusCode.GATEWAY_TIMEOUT,e);
		}
	}

	public static AppToken getAppToken(String appId, String appSecret) throws GZBApiException {
		JSONObject params = new JSONObject();
		params.put(PROP_APP_ID, appId);
		params.put(PROP_APP_SECRET, appSecret);

		try {
			return GZBResultParseUtil.parseObject(
					HttpSender.getHttpSender().httpPost(GZBApiConfig.getInstance().getEimBaseUrl() + GET_APP_TOKEN_PATH, params),
					AppToken.class);
		} catch (HttpException e) {
			throw new GZBApiException(StatusCode.GATEWAY_TIMEOUT,e);
		}
	}


	
	public static AccessToken getAccessToken(String suiteId, String suiteSecret, String corp_id) throws GZBApiException {
		SuiteToken token;
		try {
			token = getSuiteToken(suiteId, suiteSecret);
			String url = StrUtils.arrayFormat(GZBApiConfig.getInstance().getEimBaseUrl()
					+ GET_SUITE_ACCESS_TOKEN_PATH, token.getSuite_access_token());
			
			JSONObject params = new JSONObject();
			params.put("corp_id", corp_id);
			
			return GZBResultParseUtil.parseObject(
					HttpSender.getHttpSender().httpPost(url, params),
					AccessToken.class);
		} catch (HttpException e) {
			throw new GZBApiException(StatusCode.GATEWAY_TIMEOUT,e);
		}
	}

	/**
	 * 通过应用代理获取AccessToken
	 * @param agentId
	 * @param agentSecret
	 * @return
	 * @throws GZBApiException
     */
	public static AccessToken getAccessToken(String agentId, String agentSecret) throws GZBApiException {
		String url = StrUtils.arrayFormat(GZBApiConfig.getInstance().getEimBaseUrl()
				+ GET_ACCESS_TOKEN_PATH, agentId, agentSecret);
		
		try {
			return GZBResultParseUtil.parseObject(
					HttpSender.getHttpSender().httpGet(url),
					AccessToken.class);
		} catch (HttpException e) {
			throw new GZBApiException(StatusCode.GATEWAY_TIMEOUT,e);
		}
	}

	/**
	 * 通过第三方应用获取AccessToken
	 * @param appId 第三方应用id
	 * @param appSecret 第三方应用secret
	 * @param corpId 公司id: 若平台应用,则该值传入null,若未企业级应用,则应传入对应公司id;
	 * @return
	 * @throws GZBApiException
     */
	public static AccessToken getAccessTokenByThirdApp(String appId, String appSecret, String corpId) throws GZBApiException {
		String url = StrUtils.arrayFormat(GZBApiConfig.getInstance().getEimBaseUrl()
				+ GET_ACCESS_TOKEN_BY_APP_PATH, appId, appSecret, corpId);

		try {
			return GZBResultParseUtil.parseObject(
					HttpSender.getHttpSender().httpGet(url),
					AccessToken.class);
		} catch (HttpException e) {
			throw new GZBApiException(StatusCode.GATEWAY_TIMEOUT,e);
		}
	}

	/**
	 * 判断企业是否有账单应用的授权
	 * @param corpId
	 * @return
	 */
	public static boolean isGrantApp(String corpId){
		try {
			AccessToken accessToken = getAccessTokenByThirdApp(GZBApiConfig.getInstance().getLicenseAppId(),
					GZBApiConfig.getInstance().getLicenseAppSecret(), corpId);
		} catch (GZBApiException e) {
			return false;
		}
		return true;
	}

	
	
}
