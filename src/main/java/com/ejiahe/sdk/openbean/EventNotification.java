package com.ejiahe.sdk.openbean;

public class EventNotification {

	private boolean notify = true;
	private String badge;
	public boolean isNotify() {
		return notify;
	}
	public void setNotify(boolean notify) {
		this.notify = notify;
	}
	public String getBadge() {
		return badge;
	}
	public void setBadge(String badge) {
		this.badge = badge;
	}
	
}
