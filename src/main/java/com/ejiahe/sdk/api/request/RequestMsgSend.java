package com.ejiahe.sdk.api.request;

import com.alibaba.fastjson.JSONObject;
import com.ejiahe.sdk.openbean.Msg;

import java.util.List;

/**
 * 推送消息的映射
 * 
 * 
 * @author MaiJingFeng
 */
public class RequestMsgSend extends BaseRequest{
	
	private String from;
	
	private String from_type;
	
	private boolean to_all = false;
	
	private List<String> to_users;
	
	private boolean sync_with_from = true;
	
	private boolean processed_with_to = false;
	
	private List<String> to_departments;
	
	private List<String> to_member_aliases;
	
	private List<String> to_chatrooms;
	
	private String agent_id;

	private String app_id;
	
	private Msg msg;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getFrom_type() {
		return from_type;
	}

	public void setFrom_type(String from_type) {
		this.from_type = from_type;
	}

	public boolean isTo_all() {
		return to_all;
	}

	public void setTo_all(boolean to_all) {
		this.to_all = to_all;
	}

	public List<String> getTo_users() {
		return to_users;
	}

	public void setTo_users(List<String> to_users) {
		this.to_users = to_users;
	}

	public boolean isSync_with_from() {
		return sync_with_from;
	}

	public void setSync_with_from(boolean sync_with_from) {
		this.sync_with_from = sync_with_from;
	}

	public boolean isProcessed_with_to() {
		return processed_with_to;
	}

	public void setProcessed_with_to(boolean processed_with_to) {
		this.processed_with_to = processed_with_to;
	}

	public List<String> getTo_departments() {
		return to_departments;
	}

	public void setTo_departments(List<String> to_departments) {
		this.to_departments = to_departments;
	}

	public List<String> getTo_member_aliases() {
		return to_member_aliases;
	}

	public void setTo_member_aliases(List<String> to_member_aliases) {
		this.to_member_aliases = to_member_aliases;
	}

	public List<String> getTo_chatrooms() {
		return to_chatrooms;
	}

	public void setTo_chatrooms(List<String> to_chatrooms) {
		this.to_chatrooms = to_chatrooms;
	}

	public String getMsg_type() {
		return msg != null ? msg.getMsg_type() : "";
	}

	public String getAgent_id() {
		return agent_id;
	}

	public void setAgent_id(String agent_id) {
		this.agent_id = agent_id;
	}
	
	public Msg getMsg(){
		return this.msg;
	}
	
	public void setMsg(Msg msg){
		this.msg = msg;
	}

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	@Override
	public JSONObject asJSON() {
		JSONObject o = (JSONObject) JSONObject.toJSON(this);
		if(this.msg != null){
			o.remove("msg");
		}
		
		if(msg != null){
			o.put(msg.getMsg_type(), msg);
		}
		
		return o;
	}

	
}
