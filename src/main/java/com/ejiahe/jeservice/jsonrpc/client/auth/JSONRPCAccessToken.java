package com.ejiahe.jeservice.jsonrpc.client.auth;

/**
 * Created by mjf on 2018/1/5.
 */
public class JSONRPCAccessToken {

    private String accessToken;
    private long deadline;

    public JSONRPCAccessToken(){

    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }
}
