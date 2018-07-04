package com.ejiahe.jeservice.web.debug;

import com.ejiahe.jeservice.jsonrpc.packet.JSONRPCError;
import com.ejiahe.jeservice.jsonrpc.util.JSONRPCUtil;
import com.ejiahe.jeservice.util.StatusCode;
import com.ejiahe.jeservice.util.exception.HttpException;
import com.ejiahe.jeservice.util.http.HttpSender;
import com.google.common.base.Strings;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;

@Controller
public class OpenApiDebugRequestor{

    private static final String URL_BASE = "http://127.0.0.1:8080";

    @RequestMapping("/debug")
    public String debug(){
        return "/debug/request-sender";
    }

    @RequestMapping("/debug/handle")
    @ResponseBody
    public String handleDebug(HttpServletRequest request, @RequestParam(value="api_url",required=false,defaultValue="")String apiUrl,
                              @RequestParam(value="post_data",required=false)String postData, @RequestParam(value="sessionId",required=false)String sessionId){

        if(Strings.isNullOrEmpty(apiUrl)){
            return new JSONRPCError(StatusCode.ARGUMENT_ILLEGAL, "The apiUrl can not empty").toJSONString();
        }

        String url = null;
        if(apiUrl.startsWith("http") || apiUrl.startsWith("https")){
            url = apiUrl;
        }else{
            url = request.getScheme() + "://" + request.getLocalAddr() + ":" + request.getLocalPort() + request.getContextPath() + apiUrl;
        }

        HttpSender sender = HttpSender.getHttpSender();
        try {
            if(Strings.isNullOrEmpty(postData)){
                Collection<Header> headers = createHeaders(sessionId);
                return sender.httpGet(url, headers);
            }else{
                Collection<Header> headers = createHeaders(sessionId);
//                JSONObject params = JSON.parseObject(postData);
                return sender.httpPost(url, postData, headers);
            }
        } catch (HttpException e) {
            return JSONRPCUtil.createError(StatusCode.GATEWAY_TIMEOUT, e.getMessage()).toJSONString();
        }
    }

    private Collection<Header> createHeaders(String sessionId) {
        Collection<Header> headers = new ArrayList<Header>();
        if(!Strings.isNullOrEmpty(sessionId)){
            headers.add(new BasicHeader("headerSessionId", sessionId));
        }
        return headers;
    }

}