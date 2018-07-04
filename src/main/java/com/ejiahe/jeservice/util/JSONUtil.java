package com.ejiahe.jeservice.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @Author MaiLingFeng
 * @Date 18-2-2
 */
public class JSONUtil {

    public static final String PROP_CODE = "code";
    public static final String PROP_MESSAGE = "message";


    public static JSONObject createError(int code, String message){

        JSONObject result = new JSONObject();
        result.put(PROP_CODE, code);
        result.put(PROP_MESSAGE, message);

        return result;
    }


    public static <T> T toJavaObject(JSONObject temp, Class<T> targetClass){
        return JSON.toJavaObject(temp, targetClass);
    }

}
