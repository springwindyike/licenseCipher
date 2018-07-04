package com.ejiahe.sdk.config;

/**
 * 系统属性表
 * Created by liaofei on 2018/1/2.
 */
public class SystemPropertyDO {
    private String id;
    private String propName;
    private String propValue;

    public SystemPropertyDO(){}

    public SystemPropertyDO(String propName, String propValue) {
        this.propName = propName;
        this.propValue = propValue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    public String getPropValue() {
        return propValue;
    }

    public void setPropValue(String propValue) {
        this.propValue = propValue;
    }
}
