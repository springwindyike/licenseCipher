package com.ejiahe.jeservice.data.util;

import com.ejiahe.jeservice.util.Encryptions;

/**
 * 数据库配置;
 *
 *
 * @author MaiJingFeng
 */
public class DataSourceConfig {

    private String driverClassName = "com.mysql.jdbc.Driver";

    private String url = "jdbc:mysql://127.0.0.1:3306/tly?rewriteBatchedStatements=true&useUnicode=true&characterEncoding=UTF-8&characterSetResults=UTF-8";

    private String username;

    private String password;

    private int minConnections = 1;

    private int maxConnections = 100;

    private int acquireIncrement = 5;

    /**
     * 连接超时时间,即可使用的时间,单位毫秒,缺省不限制;
     */
    private int connectionTimeout = 0;

    /**
     * 连接获取的超时时间,单位毫秒
     */
    private int checkoutTimeout = 15 * 1000;

    /**
     * 连接闲置时间,单位秒,处于闲置状态的连接可能会被回收;
     */
    private int maxIdleTime = 60;

    /**
     * 闲置连接的扫描周期,单位秒;
     */
    private int idleConnectionTestPeriod = 300;

    /**
     * 测试SQL,仅当testBeforeUse或testAfterUse为true时有效;
     */
    private String testSQL = "select 1";

    private boolean testBeforeUse = true;

    private boolean testAfterUse = false;

    private boolean isPasswordEntrypted = false;

    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMinConnections() {
        return minConnections;
    }

    public void setMinConnections(int minConnections) {
        this.minConnections = minConnections;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public int getAcquireIncrement() {
        return acquireIncrement;
    }

    public void setAcquireIncrement(int acquireIncrement) {
        this.acquireIncrement = acquireIncrement;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getCheckoutTimeout() {
        return checkoutTimeout;
    }

    public void setCheckoutTimeout(int checkoutTimeout) {
        this.checkoutTimeout = checkoutTimeout;
    }

    public int getMaxIdleTime() {
        return maxIdleTime;
    }

    public void setMaxIdleTime(int maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
    }

    public int getIdleConnectionTestPeriod() {
        return idleConnectionTestPeriod;
    }

    public void setIdleConnectionTestPeriod(int idleConnectionTestPeriod) {
        this.idleConnectionTestPeriod = idleConnectionTestPeriod;
    }

    public String getTestSQL() {
        return testSQL;
    }

    public void setTestSQL(String testSQL) {
        this.testSQL = testSQL;
    }

    public boolean isTestBeforeUse() {
        return testBeforeUse;
    }

    public void setTestBeforeUse(boolean testBeforeUse) {
        this.testBeforeUse = testBeforeUse;
    }

    public boolean isTestAfterUse() {
        return testAfterUse;
    }

    public void setTestAfterUse(boolean testAfterUse) {
        this.testAfterUse = testAfterUse;
    }


    public boolean isPasswordEntrypted() {
        return isPasswordEntrypted;
    }

    public void setPasswordEntrypted(boolean passwordEntrypted) {
        isPasswordEntrypted = passwordEntrypted;
    }

    public String getDecryptedPassword(){
        if(isPasswordEntrypted){
            return Encryptions.decrypt(this.password);
        }else{
            return this.password;
        }
    }

    public void setPassword(String password, boolean doEncrypt){
        if(doEncrypt){
            this.password = Encryptions.encrypt(password);
            this.isPasswordEntrypted = true;
        }else{
            this.password = password;
            this.isPasswordEntrypted = false;
        }
    }

    protected void doEncrypt(){
        if(!this.isPasswordEntrypted){
            this.password = Encryptions.encrypt(this.password);
            this.isPasswordEntrypted = true;
        }
    }

}
