package com.ejiahe.jeservice.util.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * JSONObject必填参数方法获取类
 * 对参数为空抛出异常
 * @author liaofei
 * @date 2015年12月4日
 */
public class JSONRequiredObject {
	
	public static String getRequiredStr(JSONObject params, String key) {
		String value = params.getString(key);
	
		if (value != null) {
			return value;
		}

		throw new JSONException("parameter [" + (key)+ "] can not be empty.");
	}
	
	public static Integer getRequiredInteger(JSONObject params, String key) {
		Integer value = params.getInteger(key);
	
		if (value != null) {
			return value;
		}

		throw new JSONException("parameter [" + (key)+ "] can not be empty.");
	}
	
	public static Long getRequiredLong(JSONObject params, String key) {
		Long value = params.getLong(key);
	
		if (value != null) {
			return value;
		}

		throw new JSONException("parameter [" + (key)+ "] can not be empty.");
	}
	
	public static Boolean getRequiredBoolean(JSONObject params, String key) {
		Boolean value = params.getBoolean(key);
	
		if (value != null) {
			return value;
		}

		throw new JSONException("parameter [" + (key)+ "] can not be empty.");
	}
	
	public static JSONArray getRequiredJSONArray(JSONObject params, String key) {
		JSONArray value = params.getJSONArray(key);
	
		if (value != null) {
			return value;
		}

		throw new JSONException("parameter [" + (key)+ "] can not be empty.");
	}

	public static JSONObject getRequiredObject(JSONObject params, String key) {
		JSONObject value = params.getJSONObject(key);
		if (value != null) {
			return value;
		}

		throw new JSONException("parameter [" + (key)+ "] can not be empty.");
	}

}
