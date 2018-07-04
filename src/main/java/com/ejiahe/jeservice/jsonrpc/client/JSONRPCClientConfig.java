package com.ejiahe.jeservice.jsonrpc.client;

import com.ejiahe.jeservice.util.http.HttpSender;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Named;

/**
 * Created by mjf on 2018/1/5.
 */
@Named
public class JSONRPCClientConfig {

//    @Value("${jsonrpc.url}")
    private String serviceUrl;

//    @Value("${jsonrpc.id}")
    private String appId;

//    @Value("${jsonrpc.pwd}")
    private String appSecret;

    private int requestTimeout = HttpSender.DEFAULT_TIME_OUT;


    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public int getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }
}
