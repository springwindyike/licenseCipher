package com.ejiahe.sdk.api.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ejiahe.sdk.openbean.EventNotification;

import java.util.List;

/**
 * 推送消息的映射
 * 
 * 
 * @author MaiJingFeng
 */
public class RequestEventSend extends BaseRequest {
	
	
	@JSONField(name = "to_users")
	private List<String> toUsers;
	
	@JSONField(name = "to_member_aliases")
	private List<String> toMemberAliases;
	
	@JSONField(name = "event_type")
	private String eventType;
	
	@JSONField(name = "agent_id")
	private String agentId;

	@JSONField(name= "app_id")
	private String appId;
	
	@JSONField(name = "app_notification")
	private EventNotification appNotification;

	public List<String> getToUsers() {
		return toUsers;
	}

	public void setToUsers(List<String> toUsers) {
		this.toUsers = toUsers;
	}

	public List<String> getToMemberAliases() {
		return toMemberAliases;
	}

	public void setToMemberAliases(List<String> toMemberAliases) {
		this.toMemberAliases = toMemberAliases;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public EventNotification getAppNotification() {
		return appNotification;
	}

	public void setAppNotification(EventNotification appNotification) {
		this.appNotification = appNotification;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
}
