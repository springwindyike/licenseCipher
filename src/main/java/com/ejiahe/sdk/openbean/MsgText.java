package com.ejiahe.sdk.openbean;

/**
 * 推送消息中的文本消息块
 * 
 * 
 * @author MaiJingFeng
 */
public class MsgText implements Msg{
	
	private String content;

	public MsgText(){
		
	}
	
	public MsgText(String content){
		this.content = content;
	}
	
	public String getContent() {
		return content;
	}

	
	public void setContent(String content) {
		this.content = content;
	}


	@Override
	public String getMsg_type() {
		return Msg.TYPE_TEXT;
	}
	
}
