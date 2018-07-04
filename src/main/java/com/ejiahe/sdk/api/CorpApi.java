package com.ejiahe.sdk.api;

import com.alibaba.fastjson.JSONObject;
import com.ejiahe.jeservice.util.StatusCode;
import com.ejiahe.jeservice.util.StrUtils;
import com.ejiahe.jeservice.util.common.GZBApiException;
import com.ejiahe.jeservice.util.common.GZBResultParseUtil;
import com.ejiahe.jeservice.util.exception.HttpException;
import com.ejiahe.jeservice.util.http.HttpSender;
import com.ejiahe.sdk.config.GZBApiConfig;
import com.ejiahe.sdk.openbean.AppAgentInfo;
import com.ejiahe.sdk.openbean.CorpInfo;

public class CorpApi {

	public static CorpInfo getCorpInfo(String access_token) throws GZBApiException {
		String url = StrUtils.arrayFormat(GZBApiConfig.getInstance().getEimBaseUrl()
				+ "/corp/get?access_token={}", access_token);
		
		try {
			return GZBResultParseUtil.parseObject(
					HttpSender.getHttpSender().httpGet(url),
					CorpInfo.class);
		} catch (HttpException e) {
			throw new GZBApiException(StatusCode.GATEWAY_TIMEOUT,e);
		}
	}
	public static AppAgentInfo getAppAgentInfo(String access_token, String appId, String corpId) throws GZBApiException {
		String url = StrUtils.arrayFormat(GZBApiConfig.getInstance().getEimBaseUrl()
				+ "/ext/get_agent?app_access_token={}", access_token);
		JSONObject jReq = new JSONObject();
		jReq.put("app_id", appId);
		jReq.put("corp_id", corpId);
		try {
			return GZBResultParseUtil.parseObject(
					HttpSender.getHttpSender().httpPost(url, jReq),
					AppAgentInfo.class);
		} catch (HttpException e) {
			throw new GZBApiException(StatusCode.GATEWAY_TIMEOUT,e);
		}
	}
}
