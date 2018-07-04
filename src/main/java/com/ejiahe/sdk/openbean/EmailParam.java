package com.ejiahe.sdk.openbean;

/**
 * Created by liaofei on 2018/3/8.
 */
public class EmailParam {
    String appName;
    String tenement_id;
    String to_name;
    String to_email;
    String subject;
    String text_body;
    String html_body;
    public String getTenement_id() {
        return tenement_id;
    }
    public void setTenement_id(String tenement_id) {
        this.tenement_id = tenement_id;
    }
    public String getTo_name() {
        return to_name;
    }
    public void setTo_name(String to_name) {
        this.to_name = to_name;
    }
    public String getTo_email() {
        return to_email;
    }
    public void setTo_email(String to_email) {
        this.to_email = to_email;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getText_body() {
        return text_body;
    }
    public void setText_body(String text_body) {
        this.text_body = text_body;
    }
    public String getHtml_body() {
        return html_body;
    }
    public void setHtml_body(String html_body) {
        this.html_body = html_body;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public String toString() {
        return "EmailParam [tenement_id=" + tenement_id + ", to_name="
                + to_name + ", to_email=" + to_email + ", subject="
                + subject + ", text_body=" + text_body + ", html_body="
                + html_body + "]";
    }
}
