package com.ejiahe.sdk.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ejiahe.jeservice.util.StatusCode;
import com.ejiahe.jeservice.util.StrUtils;
import com.ejiahe.jeservice.util.common.GZBApiException;
import com.ejiahe.jeservice.util.common.GZBResultParseUtil;
import com.ejiahe.jeservice.util.exception.HttpException;
import com.ejiahe.jeservice.util.http.HttpSender;
import com.ejiahe.sdk.config.GZBApiConfig;
import com.ejiahe.sdk.openbean.UserInfo;
import com.ejiahe.sdk.openbean.UserLoginInfo;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 用户管理相关API
 * @author MaiLingFeng
 *
 */
public class MemberApi {
	
	static Logger logger = LoggerFactory.getLogger(MemberApi.class);
	
	public static UserLoginInfo getByCode(String access_token, String code) throws GZBApiException {
		String url = StrUtils.arrayFormat(GZBApiConfig.getInstance().getEimBaseUrl()
				+ "/member/getByCode?access_token={}&code={}", access_token, code);
		
		try {
			return GZBResultParseUtil.parseObject(
					HttpSender.getHttpSender().httpGet(url),
					UserLoginInfo.class);
		} catch (HttpException e) {
			throw new GZBApiException(StatusCode.GATEWAY_TIMEOUT,e);
		}
	}
	
	
	public static UserInfo getByKey(String access_token, String key) throws GZBApiException {
		String url = StrUtils.arrayFormat(GZBApiConfig.getInstance().getEimBaseUrl()
				+ "/member/get?access_token={}&key={}", access_token, key);
		
		try {
			return GZBResultParseUtil.parseObject(
					HttpSender.getHttpSender().httpGet(url),
					UserInfo.class);
		} catch (HttpException e) {
			throw new GZBApiException(StatusCode.GATEWAY_TIMEOUT,e);
		}
	}
	
	public static UserInfo getByUserId(String access_token, String userId)throws GZBApiException {
		String url = StrUtils.arrayFormat(GZBApiConfig.getInstance().getEimBaseUrl()
				+ "/member/get?access_token={}&user_id={}&return_type={}", access_token, userId, "detail");
		try {
			UserInfo userInfo =  GZBResultParseUtil.parseObject(
					HttpSender.getHttpSender().httpGet(url), UserInfo.class);
			if(userInfo != null && Strings.isNullOrEmpty(userInfo.getAvartar())){
				if(logger.isDebugEnabled()){
					logger.info(" EIM api 取不到头像  url = {}", url);
				}
			}
			return userInfo;
		} catch (HttpException e) {
			throw new GZBApiException(StatusCode.GATEWAY_TIMEOUT,e);
		}
	}
	public static List<UserInfo> searchUsers(String access_token, String tenementId, String key,
			int offset, int limit, boolean isDetail)throws GZBApiException {
		String returnType = isDetail ? "detail" : "simple";
		String url = StrUtils.arrayFormat(GZBApiConfig.getInstance().getEimBaseUrl()
				+ "/member/search?access_token={}", access_token);
		List<UserInfo> users = new ArrayList<UserInfo>();
		try {
			JSONObject jReq = new JSONObject();
			jReq.put("tenement_id", tenementId);
			jReq.put("key", key);
			jReq.put("as_name", true);
			jReq.put("as_mobile", true);
			jReq.put("as_custom_id", true);
			jReq.put("as_email", true);
			jReq.put("as_user_id", false);
//			jReq.put("user_status", new String[]{"normal"});
			jReq.put("start_index", offset);
			jReq.put("max_items", limit);
			jReq.put("return_type", returnType);
			JSONArray array = GZBResultParseUtil.parseObject(HttpSender.getHttpSender().httpPost(url, jReq));
			for (int i = 0; i < array.size(); i++) {
				JSONObject obj = array.getJSONObject(i);
				if(obj.isEmpty()){
					continue;
				}
				UserInfo user = JSON.toJavaObject(obj, UserInfo.class);
//				if(AppUtil.isNotEmpty(user.getAvartar())){
//					user.setAvartar(SystemUtils.adjustUrl(user.getAvartar(), false));
//				}
				users.add(user);
			}
		} catch (HttpException e) {
			throw new GZBApiException(StatusCode.GATEWAY_TIMEOUT, e);
		}
		return users;
	}
	
	public static List<UserInfo> getBatch(String access_token, Collection<String> userIds,
			boolean isDetail, boolean orderByShortPy) throws GZBApiException {
		String url = StrUtils.arrayFormat(GZBApiConfig.getInstance().getEimBaseUrl()
				+ "/member/get_batch?access_token={}", access_token);
		String returnType = isDetail ? "detail" : "simple";
		JSONObject req = new JSONObject();
		JSONArray array = new JSONArray();
		for(String userId : userIds){
			array.add(userId);
		}
		
		req.put("user_ids", array);
		req.put("return_type", returnType);
		if(orderByShortPy){
			req.put("order_by", "short_py");
		}
		
		List<UserInfo> users = new ArrayList<UserInfo>();
		try {
			JSONArray arrs = GZBResultParseUtil.parseObject(HttpSender.getHttpSender().httpPost(url, req));
			for(int i = 0,len = arrs.size(); i < len; i ++){
				JSONObject json = arrs.getJSONObject(i);
				if(json.isEmpty()){
					continue;
				}
				UserInfo user = JSON.toJavaObject(json, UserInfo.class);
				users.add(user);
			}
		} catch (HttpException e) {
			throw new GZBApiException(StatusCode.GATEWAY_TIMEOUT,e);
		}
		return users;
	}
	
}
