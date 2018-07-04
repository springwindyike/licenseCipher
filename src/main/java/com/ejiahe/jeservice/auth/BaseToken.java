package com.ejiahe.jeservice.auth;

import com.alibaba.fastjson.JSONObject;
import com.ejiahe.jeservice.util.RandomStrUtils;

import java.util.Date;
import java.util.Map;

/**
 * 基础Token, 内部通过JSONObject保存所有参数数据
 * 
 * 
 * @author MaiJingFeng
 */
public class BaseToken implements Token {

	protected JSONObject properties;
	
	public BaseToken(){
		properties = new JSONObject();
		properties.put(CREATION_DATE, new Date().getTime());
		addNonce();
	}
	
	public BaseToken(JSONObject properties){
		this.properties = properties;
		
		if(this.properties.getString(NONCE) == null){
			addNonce();
		}
	}
	
	private void addNonce(){
		StringBuilder nonce = new StringBuilder(RandomStrUtils.getRandomAlphanumeric(12))
									.append(RandomStrUtils.getRandomStr());
		properties.put(NONCE, nonce.toString());
	}
	
	public void addProperty(String property, Object value){
		this.properties.put(property, value);
	}
	
	public void addAllProperty(Map<String,Object> props){
		if(props != null){
			this.properties.putAll(props);
		}
	}
	
	public Object removeProperty(String property){
		return properties.remove(property);
	}
	
	@Override
	public Date getDeadline() {
		return new Date(this.properties.getLongValue(DEADLINE));
	}
	
	public void setDeadline(Date deadline){
		this.properties.put(DEADLINE, deadline.getTime());
	}
	
	

	@Override
	public Date getCreationDate() {
		return new Date(this.properties.getLongValue(CREATION_DATE));
	}
	
	public void setCreationDate(Date date){
		this.properties.put(CREATION_DATE, date.getTime());
	}

	@Override
	public String getClientType() {
		return this.properties.getString(CLIENT_TYPE);
	}
	
	public void setClientType(String clientType){
		this.properties.put(CLIENT_TYPE, clientType);
	}

	@Override
	public String getClientId() {
		return this.properties.getString(CLIENT_ID);
	}
	@Override
	public String getClientName() {
		return this.properties.getString(CLIENT_NAME);
	}

	public void setClientId(String clientId){
		this.properties.put(CLIENT_ID, clientId);
	}

	public void setClientName(String clientName){
		this.properties.put(CLIENT_NAME, clientName);
	}

	@Override
	public String getGrantType() {
		return this.properties.getString(GRANT_TYPE);
	}

	public void setGrantType(String grantType){
		this.properties.put(GRANT_TYPE, grantType);
	}
	
	@Override
	public JSONObject getProperties() {
		return this.properties;
	}

	@Override
	public String getPropertyAsString(String key) {
		return properties.getString(key);
	}

	@Override
	public Object getProperty(String key) {
		return properties.get(key);
	}

	@Override
	public String toString() {
		return "BaseToken [properties=" + properties + "]";
	}

	@Override
	public String getNonce() {
		return this.properties.getString(NONCE);
	}

	public void setNonce(String nonce){
		this.properties.put(NONCE, nonce);
	}

	@Override
	public boolean isDead() {
		Date date = getDeadline();
		if(System.currentTimeMillis() > date.getTime()){
			return true;
		}else{
			return false;
		}
	}
	
}
