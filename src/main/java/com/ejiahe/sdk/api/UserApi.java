package com.ejiahe.sdk.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ejiahe.jeservice.util.StatusCode;
import com.ejiahe.jeservice.util.StrUtils;
import com.ejiahe.jeservice.util.common.GZBApiException;
import com.ejiahe.jeservice.util.common.GZBResultParseUtil;
import com.ejiahe.jeservice.util.exception.HttpException;
import com.ejiahe.jeservice.util.http.HttpSender;
import com.ejiahe.sdk.config.GZBApiConfig;
import com.ejiahe.sdk.openbean.UserBaseInfo;
import com.ejiahe.sdk.openbean.UserBaseInfoBatch;
import com.ejiahe.sdk.openbean.UserBaseInfoSearch;
import com.ejiahe.sdk.openbean.UserSearchCommand;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * 用户管理相关API
 * @author MaiLingFeng
 *
 */
public class UserApi {
	
	static Logger LOG = LoggerFactory.getLogger(UserApi.class);
	
	public static UserBaseInfo getById(String access_token, String userId) throws GZBApiException {
		String url = StrUtils.arrayFormat(GZBApiConfig.getInstance().getEimBaseUrl()
				+ "/user/get?access_token={}&user_id={}", access_token, userId);
		
		try {
			return GZBResultParseUtil.parseObject(
					HttpSender.getHttpSender().httpGet(url),
					UserBaseInfo.class);
		} catch (HttpException e) {
			throw new GZBApiException(StatusCode.GATEWAY_TIMEOUT,e);
		}
	}
	
	
	public static UserBaseInfo getByKey(String access_token, String key) throws GZBApiException {
		String url = StrUtils.arrayFormat(GZBApiConfig.getInstance().getEimBaseUrl()
				+ "/user/get?access_token={}&key={}", access_token, key);
		
		try {
			return GZBResultParseUtil.parseObject(
					HttpSender.getHttpSender().httpGet(url),
					UserBaseInfo.class);
		} catch (HttpException e) {
			throw new GZBApiException(StatusCode.GATEWAY_TIMEOUT,e);
		}
	}

	public static UserBaseInfoBatch getByBatch(String access_token, Collection<String> user_ids, String order_by) throws GZBApiException {
		String url = StrUtils.arrayFormat(GZBApiConfig.getInstance().getEimBaseUrl()
				+ "/user/get_batch?access_token={}", access_token);

		JSONObject prams = new JSONObject();
		prams.put("user_ids",user_ids);
		prams.put("return_type", UserSearchCommand.RETURN_TYPE_SIMPLE);
		if(!Strings.isNullOrEmpty(order_by)){
			prams.put("order_by",order_by);
		}

		try {
			return GZBResultParseUtil.parseObject(
					HttpSender.getHttpSender().httpPost(url, prams.toJSONString()),
					UserBaseInfoBatch.class);
		} catch (HttpException e) {
			throw new GZBApiException(StatusCode.GATEWAY_TIMEOUT,e);
		}
	}
	

	public static UserBaseInfoSearch searchUsers(String access_token, UserSearchCommand command)throws GZBApiException {
		String url = StrUtils.arrayFormat(GZBApiConfig.getInstance().getEimBaseUrl()
				+ "/user/search?access_token={}", access_token);

		try {
			return GZBResultParseUtil.parseObject(
					HttpSender.getHttpSender().httpPost(url, JSON.toJSONString(command)),
					UserBaseInfoSearch.class);
		} catch (HttpException e) {
			throw new GZBApiException(StatusCode.GATEWAY_TIMEOUT,e);
		}
	}
	
}
