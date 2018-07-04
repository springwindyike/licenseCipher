package com.ejiahe.jeservice.web.uitls;

import com.alibaba.fastjson.JSONObject;
import com.ejiahe.jeservice.util.JSONUtil;
import com.ejiahe.jeservice.util.StatusCode;
import com.ejiahe.jeservice.util.exception.SimpleCodeException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author MaiLingFeng
 * @Date 18-2-2
 */
public class AbstractJSONExceptionHandlerController {

    @ExceptionHandler(SimpleCodeException.class)
    @ResponseBody
    public String handleException(SimpleCodeException e){
        JSONObject temp = JSONUtil.createError(e.getCode(), e.getMessage());
        return temp.toString();
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String handleException(Exception e){
        JSONObject temp = JSONUtil.createError(StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
        return temp.toString();
    }


    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public String handleException(RuntimeException e){
        JSONObject temp = JSONUtil.createError(StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
        return temp.toString();
    }

}
