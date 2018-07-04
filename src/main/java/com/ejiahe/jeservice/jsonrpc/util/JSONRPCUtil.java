package com.ejiahe.jeservice.jsonrpc.util;

import com.alibaba.fastjson.JSONObject;
import com.ejiahe.jeservice.jsonrpc.packet.JSONRPCError;
import com.ejiahe.jeservice.jsonrpc.packet.JSONRPCRequest;
import com.ejiahe.jeservice.jsonrpc.packet.JSONRPCResponse;

/**
 * JSONRPC2Service工具类
 * @author MaiJingFeng
 *
 */
public class JSONRPCUtil {

	
	public static JSONObject emptyResp(){
		return new JSONObject();
	}

    public static JSONRPCResponse createResponse(JSONRPCRequest request){
        JSONRPCResponse resp = new JSONRPCResponse(request.getId());
        return resp;
    }

    public static JSONRPCResponse createError(int code, String message){
        return createError(code,message,null);
    }

    public static JSONRPCResponse createError(int code, String message, Object data){
        JSONRPCError error = new JSONRPCError(code,message);
        return new JSONRPCResponse(error,null);
    }

	public static String getLastSubMethodName(String complexMethodName){
		if(complexMethodName == null) return null;
		
		String subMethodName = null;
		
		int separatorIndex = complexMethodName.lastIndexOf(".");
		
		//若请求的method是为xxx.xxxx.xxxx...的格式,则进行域与真实method的解析
		if(separatorIndex != -1){
			int fullMethodLength = complexMethodName.length();
			if(separatorIndex > 0 && separatorIndex <= (fullMethodLength-1)){
				subMethodName = complexMethodName.substring(separatorIndex + 1);
			}
		}else{
			subMethodName = complexMethodName;
		}
		
		
		return subMethodName;
	}
	
	
	public static String getPrefixSubMethodName(String complexMethodName){
		if(complexMethodName == null) return null;
		
		String scope = null;
		
		int separatorIndex = complexMethodName.lastIndexOf(".");
		
		//若请求的method是为xxx.xxxx.xxxx...的格式,则进行域与真实method的解析
		if(separatorIndex != -1){
			int fullMethodLength = complexMethodName.length();
			if(separatorIndex > 0 && separatorIndex <= (fullMethodLength-1)){
				scope = complexMethodName.substring(0, separatorIndex);
			}
		}else{
			scope = null;
		}
		
		return scope;
	}

}
