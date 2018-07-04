package com.ejiahe.jeservice.util;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;

/**
 * Created by mjf on 2016/12/2.
 */
@Named
public class JEGlobals {

    public static final String PROP_APP_HOME = "APP_HOME";

    private static final Logger LOG = LoggerFactory.getLogger(JEGlobals.class);

    private String home;

    @Inject
    private Environment env;

    public void setHomeDirectory(String homePath){

        File homeDirectory = new File(homePath);
        if (!homeDirectory.exists()) {
            LOG.error("Error - the specified home directory does not exist (" + homePath + ")");
        }
        else if (!homeDirectory.canRead() || !homeDirectory.canWrite()) {
            LOG.error("Error - the user running this application can not read " +
                    "and write to the specified home directory (" + homePath + "). " +
                    "Please grant the executing user read and write permissions.");
        }
        else {
            home = homePath;
        }
    }

    public String getHomeDirectory(){
        if (Strings.isNullOrEmpty(home)) {
            return env.getProperty(PROP_APP_HOME);
        }

        return home;
    }


    public String getConfigDirectory(){
        return getHomeDirectory() + File.separator + "config";
    }


    public String getProperty(String key){
        return env.getProperty(key);
    }

    public String getProperty(String key, String defaultValue){
        return env.getProperty(key,defaultValue);
    }

    public boolean getBooleanProperty(String key){
        return getBooleanProperty(key,false);
    }

    public boolean getBooleanProperty(String key, boolean defaultValue){
        String value = env.getProperty(key);
        if(value == null){
            return defaultValue;
        }else{
            return Boolean.parseBoolean(value);
        }
    }

    public int getIntProperty(String key, int defaultValue){
        String value = env.getProperty(key);
        if(value == null){
            return defaultValue;
        }else{
            return Integer.parseInt(value);
        }
    }

    public int getIntProperty(String key){
        return getIntProperty(key,0);
    }


    public long getLongProperty(String key, long defaultValue){
        String value = env.getProperty(key);
        if(value == null){
            return defaultValue;
        }else{
            return Long.parseLong(key);
        }
    }

    public long getLongProperty(String key){
        return getLongProperty(key,0);
    }



}
