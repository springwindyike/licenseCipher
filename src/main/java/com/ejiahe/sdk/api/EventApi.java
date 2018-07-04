package com.ejiahe.sdk.api;

import com.alibaba.fastjson.JSONObject;
import com.ejiahe.jeservice.util.CodeResult;
import com.ejiahe.jeservice.util.StatusCode;
import com.ejiahe.jeservice.util.StrUtils;
import com.ejiahe.jeservice.util.common.GZBApiException;
import com.ejiahe.jeservice.util.common.GZBResultParseUtil;
import com.ejiahe.jeservice.util.exception.HttpException;
import com.ejiahe.jeservice.util.http.HttpSender;
import com.ejiahe.sdk.api.request.RequestEventSend;
import com.ejiahe.sdk.config.GZBApiConfig;

/**
 * 消息推送API
 * @author MaiLingFeng
 *
 */
public class EventApi {

	public static CodeResult send(String access_token, RequestEventSend req) throws GZBApiException {
		String url = StrUtils.arrayFormat(GZBApiConfig.getInstance().getEimBaseUrl()
				+ "/event/send?access_token={}", access_token);
		
		JSONObject parmas = req.asJSON();
		
		try {
			return GZBResultParseUtil.parseObject(
					HttpSender.getHttpSender().httpPost(url, parmas),
					CodeResult.class);
		} catch (HttpException e) {
			throw new GZBApiException(StatusCode.GATEWAY_TIMEOUT,e);
		}
	}
	
}
