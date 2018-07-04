package com.ejiahe.jeservice.config;

import com.ejiahe.jeservice.util.eventbus.EventBus;
import com.ejiahe.jeservice.util.eventbus.EventBusManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;

/**
 * Created by mjf on 2016/10/19.
 */
//@Configuration
//@Order(4)
public class UtilsConfiguration {


    /**
     * 默认的系统事件总线
     * @return
     *
     */
    @Bean
    public EventBus eventBus(){
        return EventBusManager.getInstance().getEventBus();
    }

    /**
     * 事件总线管理器
     * @return
     *
     */
    @Bean
    @Scope("prototype")
    public EventBusManager eventBusManager(){
        return EventBusManager.getInstance();
    }

}
