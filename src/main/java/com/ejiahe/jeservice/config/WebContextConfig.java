package com.ejiahe.jeservice.config;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.nio.charset.Charset;

/**
 * EIMWeb应用上下文配置;
 * 在Spring容器启动时,会加载该配置;
 * @author MaiJingFeng
 */
//@Configuration
//@ControllerAdvice
//@Order(1)
public class WebContextConfig {

    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    public static final String PAGE_NOT_FOUND_VIEW = "page-not-found";

    private static org.slf4j.Logger LOG = LoggerFactory.getLogger(WebContextConfig.class);

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception e) {
        LOG.error(e.getMessage(), e);
        ModelAndView view = new ModelAndView();
//        if (plugin.isDebugModel()) {
            view.addObject("exception", e);
//        }
        view.setViewName("default-exception-page");
        return view;
    }

}

