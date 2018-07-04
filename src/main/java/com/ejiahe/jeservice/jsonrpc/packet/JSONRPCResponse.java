package com.ejiahe.jeservice.jsonrpc.packet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ejiahe.jeservice.util.RandomStrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mjf on 2018/1/5.
 */
public class JSONRPCResponse extends JSONRPCPacket{

    private static final Logger LOG = LoggerFactory.getLogger(JSONRPCResponse.class);

    private Object id;

    private JSONObject result;

    private JSONRPCError error;


    public static JSONRPCResponse parse(final String response) throws JSONRPCParseException {
        try{
            return JSON.toJavaObject(JSON.parseObject(response), JSONRPCResponse.class);
        }catch (Exception e){
            throw new JSONRPCParseException("Invalid JSON: " + e.getMessage() , JSONRPCParseException.JSON, response);
        }
    }

    public static JSONRPCResponse createResult(JSONRPCRequest request){
        return new JSONRPCResponse(request.getId());
    }


    public JSONRPCResponse(){
        this(RandomStrUtils.getRandomStr());
    }


    public JSONRPCResponse(JSONObject result, Object id) {
        this.result = result;
        this.id = id;
    }

    public JSONRPCResponse(Object id) {
        this.id = id;
        this.result = new JSONObject();
    }

    public JSONRPCResponse(JSONRPCError error, Object id) {
        this.error = error;
        this.id = id;
    }


    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public JSONObject getResult() {
        return result;
    }

    public <T> T getResultAsObject(Class<T> tClass){
        if(result == null) return null;

        try{
            return JSON.toJavaObject(result,tClass);
        }catch (Exception e){
            LOG.warn(e.getMessage());
            return null;
        }
    }

    public void setResult(JSONObject result) {
        if(result != null){
            this.result = result;
            this.error = null;
        }
    }

    public JSONRPCError getError() {
        return error;
    }

    public void setError(JSONRPCError error) {
        if(error != null){
            this.error = error;
            this.result = null;
        }
    }

    public boolean isSuccess(){
        if(error != null){
            return false;
        }
        return true;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject out = new JSONObject();

        // Result and error are mutually exclusive
        if (error != null) {
            out.put("error", error.toJSONObject());
        }
        else {
            out.put("result", result);
        }

        out.put("id", id);

        out.put("jsonrpc", "2.0");

        return out;
    }
}
