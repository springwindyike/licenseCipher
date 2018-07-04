package com.ejiahe.sdk.jsonrpcbean;

/**
 * 公司信息
 * Created by liaofei on 2018/1/10.
 */
public class TenementInfo {
    private String tenementId;
    private String tenementName;
    private String superAdminId;
    private String shortName;
    private String sipCode;
    private String tenementStatus;
    private long creationDate;
    private long lastActiveDate;
    private String contactPhone;
    private String contactEmail;
    private String contactName;
    private int maxUserCount;
    private int userCount;
    private int joinedUserCount;
    private int activedUserCount;

    public String getTenementId() {
        return tenementId;
    }

    public void setTenementId(String tenementId) {
        this.tenementId = tenementId;
    }

    public String getTenementName() {
        return tenementName;
    }

    public void setTenementName(String tenementName) {
        this.tenementName = tenementName;
    }

    public String getSuperAdminId() {
        return superAdminId;
    }

    public void setSuperAdminId(String superAdminId) {
        this.superAdminId = superAdminId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getSipCode() {
        return sipCode;
    }

    public void setSipCode(String sipCode) {
        this.sipCode = sipCode;
    }

    public String getTenementStatus() {
        return tenementStatus;
    }

    public void setTenementStatus(String tenementStatus) {
        this.tenementStatus = tenementStatus;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public long getLastActiveDate() {
        return lastActiveDate;
    }

    public void setLastActiveDate(long lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public int getMaxUserCount() {
        return maxUserCount;
    }

    public void setMaxUserCount(int maxUserCount) {
        this.maxUserCount = maxUserCount;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public int getJoinedUserCount() {
        return joinedUserCount;
    }

    public void setJoinedUserCount(int joinedUserCount) {
        this.joinedUserCount = joinedUserCount;
    }

    public int getActivedUserCount() {
        return activedUserCount;
    }

    public void setActivedUserCount(int activedUserCount) {
        this.activedUserCount = activedUserCount;
    }
}
