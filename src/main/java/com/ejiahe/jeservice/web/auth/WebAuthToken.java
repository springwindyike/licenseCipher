package com.ejiahe.jeservice.web.auth;

public class WebAuthToken {

    public static final String NAME = "webAuthToken";

    public static final String USER_TYPE_USER = "user";
    public static final String USER_TYPE_MANAGER = "manager";

    private String userId;

    private String userType;

    private String name;

    private String tenementId;

    public WebAuthToken() {}

    public WebAuthToken(String userId, String userType, String name, String tenementId) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getTenementId() {
        return tenementId;
    }

    public void setTenementId(String tenementId) {
        this.tenementId = tenementId;
    }

}
