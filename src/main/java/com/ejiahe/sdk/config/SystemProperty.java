package com.ejiahe.sdk.config;

import com.ejiahe.Application;
import com.ejiahe.jeservice.context.PluggableBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 系统属性
 * Created by liaofei on 2018/1/2.
 */
//@Named
public class SystemProperty implements PluggableBean {

    private static Logger LOG = LoggerFactory.getLogger(SystemProperty.class);

    private static SystemProperty instance = null;
    private Map<String, String> properties;


    public synchronized static SystemProperty getInstance() {
        if (instance == null) {
            SystemProperty props = Application.getContext().getBean(SystemProperty.class);
            props.init();
            instance = props;
        }
        return instance;
    }

    private void init() {
        if (properties == null) {
            properties = new ConcurrentHashMap<String, String>();
        }
        else {
            properties.clear();
        }

        loadProperties();
    }

    private void loadProperties() {
    }

    @Override
    public void applicationStarted() {
        init();
    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public void setProperty(String name, String value){
        properties.put(name, value);
    }

    public void deleteProperty(String name){
        if(properties.containsKey(name)){
            properties.remove(name);
        }
    }
    public String getProperty(String name) {
        return properties.get(name);
    }

    public String getProperty(String name, String defaultValue) {
        String value = properties.get(name);
        if (value != null) {
            return value;
        }
        else {
            return defaultValue;
        }
    }

    public int getIntProperty(String name, int defaultValue) {
        String value = getProperty(name);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            }
            catch (NumberFormatException nfe) {
                // Ignore.
            }
        }
        return defaultValue;
    }

    public long getLongProperty(String name, long defaultValue) {
        String value = getProperty(name);
        if (value != null) {
            try {
                return Long.parseLong(value);
            }
            catch (NumberFormatException nfe) {
                // Ignore.
            }
        }
        return defaultValue;
    }

    //    public boolean getBooleanProperty(String name) {
//        return Boolean.valueOf(getProperty(name));
//    }
    public boolean getBooleanProperty(String name, boolean defaultValue) {
        String value = getProperty(name);
        if (value != null) {
            return Boolean.valueOf(value);
        }
        else {
            return defaultValue;
        }
    }
}
