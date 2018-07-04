package com.ejiahe.sdk.jsonrpcbean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by liaofei on 2018/1/26.
 */
@ApiModel(value="UserSimpleInfo", description = "用户简单信息")
public class UserSimpleInfo {

    @ApiModelProperty("用户Id")
    private String userId;

    @ApiModelProperty("用户名")
    private String name;

    @ApiModelProperty("手机号码")
    private String mobile;

    public UserSimpleInfo(){}

    public UserSimpleInfo(UserInfo info){
        this.userId = info.getUserId();
        this.name = info.getName();
        this.mobile = info.getMobile();
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
