package com.ejiahe.sdk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ejiahe.Application;
import com.ejiahe.app.license.uitls.LicenseUtil;
import com.ejiahe.jeservice.jsonrpc.client.JSONRPCHttpSender;
import com.ejiahe.jeservice.jsonrpc.packet.JSONRPCResponse;
import com.ejiahe.jeservice.util.StatusCode;
import com.ejiahe.jeservice.util.common.GZBApiException;
import com.ejiahe.sdk.config.GZBApiConfig;
import com.ejiahe.sdk.openbean.FileUploadResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by liaofei on 2018/1/8.
 */
@Named
public class GZBJsonRpcApi {
    private static Logger LOG = LoggerFactory.getLogger(GZBJsonRpcApi.class);

    @Inject
    private JSONRPCHttpSender jsonrpcHttpSender;
    public static final String serviceName = "eim";

    private <T>List<T> parseListFromArray(JSONArray array, Class<T> clazz){
        List<T> list = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = array.getJSONObject(i);
            if(obj.isEmpty()){
                continue;
            }
            T javaObject = JSON.toJavaObject(obj, clazz);
            list.add(javaObject);
        }
        return list;
    }


    /**
     * 获取eim http接口地址，并持久化到本地
     */
    public void getEimServiceURL(){
        JSONObject params = new JSONObject();
        JSONRPCResponse resp = jsonrpcHttpSender.send("open.config.getServiceURL", params);
        if(resp != null && resp.isSuccess()){
            if(resp.getResult() != null){
                JSONObject object = resp.getResult();
                LOG.info("获取eim http接口地址, {}", object.toString());
                GZBApiConfig.getInstance().setEimBaseUrl(object.getString("baseURL"));
                GZBApiConfig.getInstance().setEimFileBaseUrl(object.getString("fileBaseURL"));
                GZBApiConfig.getInstance().setEimAuthBaseUrl(object.getString("oauthBaseURL"));
            }
        }else{
            LOG.warn("获取eim http接口地址失败");
        }
    }



    public JSONArray getUserMessagees(String userId,String chatRoomId){
        JSONObject params = new JSONObject();
        params.put("userId", userId);
        params.put("withBareJID", chatRoomId);
        params.put("startTime", LicenseUtil.date2String(LicenseUtil.YYYY_MM_DD_HH_MM_SS, new Date(0)));
        params.put("endTime", LicenseUtil.date2String(LicenseUtil.YYYY_MM_DD_HH_MM_SS, new Date()));
        params.put("appId",GZBApiConfig.getInstance().getLicenseAppId());
        params.put("agentId", GZBApiConfig.getInstance().getLicenseAppId());

        JSONRPCResponse resp = jsonrpcHttpSender.send("sms.audit.getUserMessages", params);
        if(resp.isSuccess()){
            LOG.info("get UserMessages: ", resp.toJSONObject());
            return JSONArray.parseArray(resp.getResult().getString("items"));
        }

        return null;
    }

    public JSONObject deleteChatroom(String chatroomId,String userId){
        JSONObject params = new JSONObject();
        params.put("operator", userId);
        params.put("chatroomIds", Arrays.asList(chatroomId));
        JSONRPCResponse resp = jsonrpcHttpSender.send("chatroom.delete", params);
        if(resp.isSuccess()){
            LOG.info("delete chatRoom success chatroomId:{}",chatroomId);
        }
        return resp.getResult();
    }
    /**
     * 设置app可见范围
     * @param userIds
     */
    public void setAppScope(Collection<String> userIds) throws GZBApiException {
        JSONObject agentJSON = new JSONObject();
        String appId =  GZBApiConfig.getInstance().getLicenseAppId();
        agentJSON.put("appId",appId);

        JSONObject subscription = new JSONObject();
        subscription.put("users",userIds);
        agentJSON.put("subscription",subscription);

        String method = "open.third.updateApp";
        JSONRPCResponse resp = jsonrpcHttpSender.send( method, agentJSON);
        if(resp.getError() != null){
            LOG.error(resp.getError().toJSONString());
            throw new GZBApiException(StatusCode.COMMON_ERROR,"设置范围失败");
        }

    }

    public void setTenementAPP(){

        GZBApiConfig config = GZBApiConfig.getInstance();

        String tenementId = config.getLicensetTenementId();
        String appId = config.getLicenseAppId();
        JSONObject params = new JSONObject();
        params.put("tenementId",tenementId);
        params.put("thirdAppId",appId);
        JSONRPCResponse resp = jsonrpcHttpSender.send( "open.agent.createCropAgent", params);
        if(resp != null && resp.isSuccess()){
            if(resp.getResult() != null){
                JSONObject object = resp.getResult();
                LOG.info("许可审批, {}", object.toString());
                GZBApiConfig.getInstance().setLicenseAgentId(object.getString("agentId"));
            }
        }else{
            LOG.warn("初始化并获取许可审批应用信息失败");
        }

    }

    private FileUploadResult uploadHeadImg(String fileResourcePath) {
        FileUploadResult fileUploadResult = null;
        ResourceLoader resourceLoader = Application.getContext();
        Resource resource = resourceLoader.getResource(fileResourcePath);

        if (resource == null) {
            return null;
        }
        InputStream input = null;
        try {
            input = resource.getInputStream();
//            LOG.info("input:{}", input.toString());
            fileUploadResult = jsonrpcHttpSender.upload("headImg", input, "");
        } catch (IOException e) {
            LOG.error("resource.getInputStream失败,{}", e.getMessage());
        } catch (GZBApiException e) {
            LOG.error("logo图片上传失败,{}", e.getMessage());
        } finally {
            try {
                if(input != null)
                    input.close();
            }catch (Exception e) {
                // Ignore.
            }
        }
        return fileUploadResult;
    }

    /**
     * 判断应用是否存在
     */
    public boolean isAppExist(String appId){
        JSONObject params = new JSONObject();
        params.put("appId", appId);//SmsConstant.BILL_APPID
        JSONRPCResponse resp = jsonrpcHttpSender.send("open.third.getApp", params);
        if(resp != null && resp.isSuccess()){
            return true;
        }else{
            return false;
        }
    }
}
