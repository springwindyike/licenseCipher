package com.ejiahe.sdk.api.request;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * 红点消息
 * @author focus
 * @date 2016年3月7日
 * @time 上午9:59:44
 */
public class RequestRedMsgSend extends BaseRequest{

	private String agent_id;

	private String badge = "0";
	
	private String event_type = "app_notification";
	
	private boolean notify = true;
	
	private List<String> to_users;

	public String getAgent_id() {
		return agent_id;
	}

	public void setAgent_id(String agent_id) {
		this.agent_id = agent_id;
	}

	public String getBadge() {
		return badge;
	}

	public void setBadge(String badge) {
		this.badge = badge;
	}

	public String getEvent_type() {
		return event_type;
	}

	public void setEvent_type(String event_type) {
		this.event_type = event_type;
	}

	public boolean isNotify() {
		return notify;
	}

	public void setNotify(boolean notify) {
		this.notify = notify;
	}

	public List<String> getTo_users() {
		return to_users;
	}

	public void setTo_users(List<String> to_users) {
		this.to_users = to_users;
	}
	
	
	public JSONObject asJSON() {
		JSONObject o = (JSONObject) JSONObject.toJSON(this);
		return o;
	}
	
}
