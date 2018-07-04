package com.ejiahe.sdk.jsonrpcbean;

/**
 * 鉴权后的用户信息
 * Created by liaofei on 2018/2/1.
 */
public class UserAuthInfo {
    private String userId;
    private String userType;//manager: 管理员;user: 普通用户
    private String name;
    private String tenementId;
    private String clientType;

    public UserAuthInfo() {}

    public UserAuthInfo(String userId, String userType, String name, String tenementId) {
        this.userId = userId;
        this.userType = userType;
        this.name = name;
        this.tenementId = tenementId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTenementId() {
        return tenementId;
    }

    public void setTenementId(String tenementId) {
        this.tenementId = tenementId;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }
}
