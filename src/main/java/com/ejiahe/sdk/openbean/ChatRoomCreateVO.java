package com.ejiahe.sdk.openbean;



import java.util.List;

public class ChatRoomCreateVO {
    private String subject;
    private String description;
    private String admin;
    private List<String> users;
    private ChatRoomPrivilegeVO privileges;

    private String agent_id;
    private String app_id;
    private String url;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public ChatRoomPrivilegeVO getPrivileges() {
        return privileges;
    }

    public void setPrivileges(ChatRoomPrivilegeVO privileges) {
        this.privileges = privileges;
    }

    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }


}
