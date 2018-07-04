package com.ejiahe.jeservice.jsonrpc.client;

import com.alibaba.fastjson.JSONObject;
import com.ejiahe.jeservice.jsonrpc.client.auth.JSONRPCTokenService;
import com.ejiahe.jeservice.jsonrpc.packet.JSONRPCRequest;
import com.ejiahe.jeservice.jsonrpc.packet.JSONRPCResponse;
import com.ejiahe.jeservice.util.RandomStrUtils;
import com.ejiahe.jeservice.util.StatusCode;
import com.ejiahe.jeservice.util.StrUtils;
import com.ejiahe.jeservice.util.common.GZBApiException;
import com.ejiahe.jeservice.util.common.GZBResultParseUtil;
import com.ejiahe.jeservice.util.exception.HttpException;
import com.ejiahe.jeservice.util.http.HttpSender;
import com.ejiahe.sdk.config.GZBApiConfig;
import com.ejiahe.sdk.openbean.FileUploadResult;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 基于HTTP的JSONRPC请求发送器
 * Created by mjf on 2016/11/7.
 */
@Named
public class JSONRPCHttpSender {

    private static final Logger LOG = LoggerFactory.getLogger(JSONRPCHttpSender.class);

    @Inject
    private JSONRPCClientConfig config;

    @Inject
    private JSONRPCTokenService tokenServ;

    /**
     * 发送请求
     * @param method 方法名
     * @param params 方法参数
     *
     * @return
     */
    public JSONRPCResponse send(String method, JSONObject params){
        JSONRPCRequest request = new JSONRPCRequest(method, params, RandomStrUtils.getRandomStr());
        return send(request);
    }


    /**
     * 发送JSONRPC2Request
     * @param request
     * @return
     */
    public JSONRPCResponse send(JSONRPCRequest request){
        return send(request, true, false);
    }


    /**
     * 发送JSONRPC2Request
     * @param request
     * @return
     */
    public JSONRPCResponse send(JSONRPCRequest request, boolean useToken, boolean resend){
        try {
            String response = sendRemote(request.toString(), useToken);
            JSONRPCResponse jsonrpcResp = JSONRPCResponse.parse(response);


            //鉴权失败,重新获取Token并自动重发请求;
            if(useToken && !tokenServ.checkJsonRpcResult(jsonrpcResp) && !resend){
                return send(request,useToken,true);
            }

            return jsonrpcResp;
        } catch (Exception e) {
            LOG.warn("request:{}, error:{}", request, e.getMessage(), e);
        }
        return null;
    }


    private String sendRemote(String request, boolean useToken) throws HttpException {
        HttpSender sender = HttpSender.getHttpSender();

        String sendUrl = null;

        if(useToken){
            sendUrl = config.getServiceUrl() + "?accessToken=" + tokenServ.getToken();
        }else{
            sendUrl = config.getServiceUrl();
        }

        String response = sender.httpPost(sendUrl, request, config.getRequestTimeout());
        return response;
    }


    public FileUploadResult upload(String type, InputStream input, String filename) throws GZBApiException {
        String url = StrUtils.arrayFormat(GZBApiConfig.getInstance().getEimFileBaseUrl()
                + "/upload?accessToken={}&type={}", tokenServ.getToken(), type);

        Map<String,ContentBody> parts = new HashMap<>();
        parts.put("data", new InputStreamBody(input, filename));

        try {
            return GZBResultParseUtil.parseObject(
                    HttpSender.getHttpSender().httpMultipart(url, parts),
                    FileUploadResult.class);
        } catch (HttpException e) {
            throw new GZBApiException(StatusCode.GATEWAY_TIMEOUT,e);
        }
    }
}
