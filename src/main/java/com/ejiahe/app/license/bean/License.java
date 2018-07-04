package com.ejiahe.app.license.bean;

import java.util.Date;

public class License {


    // 许可ID，对应的企业唯一表示码
    private String nonce;
    //失效日期
    private Long expireDate;

    //证书生成策略 ： 解密策略&签名策略
//    private String strategy;

    private String extFields;

    private String reqSign;


    public String getReqSign() {
        return reqSign;
    }

    public void setReqSign(String reqSign) {
        this.reqSign = reqSign;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public Long getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Long expireDate) {
        this.expireDate = expireDate;
    }

    public String getExtFields() {
        return extFields;
    }

    public void setExtFields(String extFields) {
        this.extFields = extFields;
    }

}