package com.ejiahe.jeservice.util.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ejiahe.jeservice.util.CodeResult;
import com.ejiahe.jeservice.util.StatusCode;
import com.ejiahe.sdk.auth.GZBTokenService;

public class GZBResultParseUtil {

	private GZBResultParseUtil(){}

	public static <T extends CodeResult> T parseObject(String json,Class<T> clazz) throws GZBApiException {
		T temp = JSON.parseObject(json, clazz);

		if(!((CodeResult)temp).isSuccess()){

			if(temp.getResp_code() == StatusCode.UNAUTHORIZED){
				GZBTokenService.getInstance().getTokens().invalidateAll();
			}

			throw new GZBApiException(temp);
		}
		return temp;
	}

	public static JSONArray parseObject(String json) throws GZBApiException {
		JSONObject resp = JSON.parseObject(json);
		int respCode = resp.getIntValue("resp_code");
		if(respCode != StatusCode.SUCCESS) {
			if(respCode == StatusCode.UNAUTHORIZED){
				GZBTokenService.getInstance().getTokens().invalidateAll();
			}
			throw new GZBApiException(respCode, "");
		}
		return resp.getJSONArray("items");
	}

	public static String toJSONString(Object object){
		return JSON.toJSONString(object);
	}
}
