package com.ejiahe.sdk.openbean;

import java.util.List;

/**
 * Created by focus on 2018/6/15.
 */
public class ChatRoomUpdateVO {
    private String chatroom_id;
    private String subject;
    private String description;
    private String admin;
    private List<String> add_users;
    private List<String> del_users;
    private ChatRoomPrivilegeVO privileges;

    public String getChatroom_id() {
        return chatroom_id;
    }

    public void setChatroom_id(String chatroom_id) {
        this.chatroom_id = chatroom_id;
    }

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

    public List<String> getAdd_users() {
        return add_users;
    }

    public void setAdd_users(List<String> add_users) {
        this.add_users = add_users;
    }

    public List<String> getDel_users() {
        return del_users;
    }

    public void setDel_users(List<String> del_users) {
        this.del_users = del_users;
    }

    public ChatRoomPrivilegeVO getPrivileges() {
        return privileges;
    }

    public void setPrivileges(ChatRoomPrivilegeVO privileges) {
        this.privileges = privileges;
    }
}
