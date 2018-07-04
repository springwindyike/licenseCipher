package com.ejiahe.sdk.api;

import com.alibaba.fastjson.JSONObject;
import com.ejiahe.jeservice.util.StatusCode;
import com.ejiahe.jeservice.util.StrUtils;
import com.ejiahe.jeservice.util.common.GZBApiException;
import com.ejiahe.jeservice.util.common.GZBResultParseUtil;
import com.ejiahe.jeservice.util.http.HttpSender;
import com.ejiahe.sdk.api.request.RequestMsgSend;
import com.ejiahe.sdk.config.GZBApiConfig;
import com.ejiahe.sdk.openbean.MsgSendResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageApi {

	private static Logger LOG = LoggerFactory.getLogger(MessageApi.class);

	public static MsgSendResult send(String access_token, RequestMsgSend req) throws GZBApiException {
		String url = StrUtils.arrayFormat(GZBApiConfig.getInstance().getEimBaseUrl()
				+ "/message/send?access_token={}", access_token);
		
		JSONObject parmas = req.asJSON();

		LOG.info("发送消息推送开始，parameters={}",parmas);

		try {
			return GZBResultParseUtil.parseObject(
					HttpSender.getHttpSender().httpPost(url, parmas),
					MsgSendResult.class);
		} catch (Exception e) {
			throw new GZBApiException(StatusCode.GATEWAY_TIMEOUT,e);
		}finally {
			LOG.info("发送消息推送完成，parameters={}",parmas);
		}
	}
	
}
