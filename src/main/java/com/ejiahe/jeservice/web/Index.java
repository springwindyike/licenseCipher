package com.ejiahe.jeservice.web;

import com.alibaba.fastjson.JSONObject;
import com.ejiahe.jeservice.config.WebContextConfig;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;

/**
 * @author MaiLingFeng
 */
@Controller
public class Index {


    @RequestMapping("/"+ WebContextConfig.PAGE_NOT_FOUND_VIEW)
    public String notFound(){
        return WebContextConfig.PAGE_NOT_FOUND_VIEW;
    }

    @RequestMapping("/")
    public String index(){
        return "forward:/p/";
    }

    @RequestMapping("/test")
    @ResponseBody
    public String test(HttpServletRequest request){

        System.out.println(request.getServletContext().getRealPath("/"));
        System.out.println(request.getServletContext().getRealPath("/static/static/css/default.css"));

        JSONObject result = new JSONObject();
        try {
            BeanUtils.populate(request,result);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return result.toJSONString();
    }
}
