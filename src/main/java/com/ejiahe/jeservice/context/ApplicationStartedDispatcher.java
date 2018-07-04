package com.ejiahe.jeservice.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.inject.Named;

/**
 * Created by mjf on 2016/12/17.
 */
@Named
public class ApplicationStartedDispatcher implements ApplicationListener<ContextRefreshedEvent>{

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationStartedDispatcher.class);

    private ApplicationContext context;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext context = event.getApplicationContext();

        LOG.info("Context Refresh: {}",context);

        if(context.getParent() == null){
            for(PluggableBean bean : context.getBeansOfType(PluggableBean.class).values()){
                try{
                    bean.applicationStarted();
                }catch (Exception e){
                    LOG.error(e.getMessage(),e);
                }
            }
        }


    }

}
