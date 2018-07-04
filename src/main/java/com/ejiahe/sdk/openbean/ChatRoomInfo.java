package com.ejiahe.sdk.openbean;

import com.ejiahe.jeservice.util.CodeResult;

import java.util.List;


public class ChatRoomInfo extends CodeResult {

	private String chatroom_id;
	private String subject;

	private List<String> users;

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

	public List<String> getUsers() {
		return users;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}
}
