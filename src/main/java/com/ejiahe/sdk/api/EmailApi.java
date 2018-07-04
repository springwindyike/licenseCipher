package com.ejiahe.sdk.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ejiahe.jeservice.util.CodeResult;
import com.ejiahe.jeservice.util.StatusCode;
import com.ejiahe.jeservice.util.StrUtils;
import com.ejiahe.jeservice.util.common.GZBApiException;
import com.ejiahe.jeservice.util.common.GZBResultParseUtil;
import com.ejiahe.jeservice.util.exception.HttpException;
import com.ejiahe.jeservice.util.http.HttpSender;
import com.ejiahe.sdk.config.GZBApiConfig;
import com.ejiahe.sdk.openbean.EmailParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 邮件发送
 * @author focus
 * @date 2016年8月19日 下午6:04:43
 */
public class EmailApi {

	static Logger logger = LoggerFactory.getLogger(EmailApi.class);
	public static void sendEmail(String access_token, EmailParam param) throws GZBApiException {
		String url = StrUtils.arrayFormat(GZBApiConfig.getInstance().getEimBaseUrl()
				+ "/email/send?access_token={}", access_token);
		
		JSONObject req = (JSONObject) JSONObject.toJSON(param);
		
		try {
			JSONObject resp = JSON.parseObject(HttpSender.getHttpSender().httpPost(url,req));
			CodeResult result = GZBResultParseUtil.parseObject(
					resp.toJSONString(),
					CodeResult.class);
			if(!result.isSuccess())
				logger.error("发送邮箱失败：param:" + param + " cause:"+ result.getResp_msg());
		} catch (HttpException e) {
			throw new GZBApiException(StatusCode.GATEWAY_TIMEOUT,e);
		}
		
		
	}
}
