package com.ejiahe.sdk.api;

import com.alibaba.fastjson.JSONObject;
import com.ejiahe.jeservice.util.CodeResult;
import com.ejiahe.jeservice.util.StatusCode;
import com.ejiahe.jeservice.util.StrUtils;
import com.ejiahe.jeservice.util.common.GZBApiException;
import com.ejiahe.jeservice.util.common.GZBResultParseUtil;
import com.ejiahe.jeservice.util.exception.HttpException;
import com.ejiahe.jeservice.util.http.HttpSender;
import com.ejiahe.sdk.config.GZBApiConfig;
import com.ejiahe.sdk.openbean.ChatRoomCreateVO;
import com.ejiahe.sdk.openbean.ChatRoomInfo;
import com.ejiahe.sdk.openbean.ChatRoomUpdateVO;

/**
 * Created by Administrator on 2017/3/10.
 */
public class ChatRoomApi {

    public static ChatRoomInfo getChatRoom(String access_token, String chatRoomId) throws GZBApiException {
        String url = StrUtils.arrayFormat(GZBApiConfig.getInstance().getEimBaseUrl()
                + "/chatroom/get?access_token={}&chatroom_id={}", access_token, chatRoomId);

        try {
            return GZBResultParseUtil.parseObject(
                    HttpSender.getHttpSender().httpGet(url),
                    ChatRoomInfo.class);
        } catch (HttpException e) {
            throw new GZBApiException(StatusCode.GATEWAY_TIMEOUT,e);
        }
    }

    public static ChatRoomInfo createChatRoom(String access_token, ChatRoomCreateVO chatRoomCreateVO) throws GZBApiException {
        String url = StrUtils.arrayFormat(GZBApiConfig.getInstance().getEimBaseUrl()
                + "/chatroom/create?access_token={}", access_token);

        JSONObject param = JSONObject.parseObject(JSONObject.toJSONString(chatRoomCreateVO));

        try {
            return GZBResultParseUtil.parseObject(
                    HttpSender.getHttpSender().httpPost(url, param),
                    ChatRoomInfo.class);
        } catch (HttpException e) {
            throw new GZBApiException(StatusCode.GATEWAY_TIMEOUT,e);
        }
    }

    public static CodeResult updateChatRoom(String access_token, ChatRoomUpdateVO chatRoomUpdateVO) throws GZBApiException {
        String url = StrUtils.arrayFormat(GZBApiConfig.getInstance().getEimBaseUrl()
                + "/chatroom/update?access_token={}", access_token);

        JSONObject param = JSONObject.parseObject(JSONObject.toJSONString(chatRoomUpdateVO));

        try {
            return GZBResultParseUtil.parseObject(
                    HttpSender.getHttpSender().httpPost(url, param),
                    CodeResult.class);
        } catch (HttpException e) {
            throw new GZBApiException(StatusCode.GATEWAY_TIMEOUT,e);
        }
    }
}
