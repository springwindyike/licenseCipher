package com.ejiahe.jeservice.jsonrpc.client;

import com.alibaba.fastjson.JSONObject;
import com.ejiahe.jeservice.jsonrpc.client.auth.JSONRPCTokenService;
import com.ejiahe.jeservice.jsonrpc.config.JSONRPCRabbitConfiguration;
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
import com.google.common.base.Charsets;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 基于RabbitMQ的JSONRPC请求发送器;
 * Created by mjf on 2016/11/7.
 */
@Named
public class JSONRPCRabbitSender {

    private static final Logger LOG = LoggerFactory.getLogger(JSONRPCRabbitSender.class);

    @Inject
    private RabbitTemplate amqpTemplate;

    @Inject
    private JSONRPCTokenService tokenServ;

    /**
     * 发送请求
     * @param service RPC服务模块名
     * @param method 方法名
     * @param params 方法参数
     * @return
     */
    public JSONRPCResponse send(String service, String method, JSONObject params){
        JSONRPCRequest request = new JSONRPCRequest(method, params, RandomStrUtils.getRandomStr());
        return send(service, request);
    }


    /**
     * 发送JSONRPC2Request
     * @param service RPC服务模块名
     * @param request
     * @return
     */
    public JSONRPCResponse send(String service, JSONRPCRequest request){
        try {
            String response = sendRemote(service, request.toString());
            JSONRPCResponse jsonrpcResp = JSONRPCResponse.parse(response);

            return jsonrpcResp;
        } catch (Exception e) {
            LOG.error(e.getMessage(),e);
        }
        return null;
    }


    private String sendRemote(String service, String request){

        String corrId = RandomStrUtils.getRandomStr();

        MessageProperties properties = MessagePropertiesBuilder.newInstance()
                .setCorrelationIdStringIfAbsent(corrId)
                .setCorrelationIdIfAbsent(corrId.getBytes(Charsets.UTF_8))
                .build();

        Message amqpMsg = MessageBuilder.withBody(request.getBytes(Charsets.UTF_8))
                .andProperties(properties)
                .build();

        Message result = amqpTemplate.sendAndReceive(JSONRPCRabbitConfiguration.JSONRPCExchangeName,
                service, amqpMsg);

        LOG.info("Message result:{}", result);
        return new String(result.getBody(),Charsets.UTF_8);
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
