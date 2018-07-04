package com.ejiahe.sdk.api.request;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class BaseRequest {

	public JSONObject asJSON(){
		return (JSONObject) JSON.toJSON(this);
	}
}
