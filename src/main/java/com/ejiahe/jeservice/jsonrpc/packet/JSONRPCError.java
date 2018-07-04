package com.ejiahe.jeservice.jsonrpc.packet;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by mjf on 2018/1/5.
 */
public class JSONRPCError extends Exception{

    public static final JSONRPCError PARSE_ERROR = new JSONRPCError(-32700, "JSON parse error");
    public static final JSONRPCError INVALID_REQUEST = new JSONRPCError(-32600, "Invalid request");
    public static final JSONRPCError METHOD_NOT_FOUND = new JSONRPCError(-32601, "Method not found");
    public static final JSONRPCError INVALID_PARAMS = new JSONRPCError(-32602, "Invalid parameters");
    public static final JSONRPCError INTERNAL_ERROR = new JSONRPCError(-32603, "Internal error");

    private int code;

    private String message;

    private JSONObject data;

    public JSONRPCError() {}

    public JSONRPCError(int code, String message) {
        super(message);
        this.code = code;
    }

    public JSONRPCError(int code, String message, JSONObject data) {
        this(code,message);
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public JSONObject toJSONObject() {
        JSONObject out = new JSONObject();

        out.put("code", code);
        out.put("message", getMessage());
        if (data != null)
            out.put("data", data);

        return out;
    }

    public String toJSONString(){
        return toJSONObject().toJSONString();
    }
}
