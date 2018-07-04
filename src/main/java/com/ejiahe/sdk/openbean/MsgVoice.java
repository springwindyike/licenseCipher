package com.ejiahe.sdk.openbean;


/**
 * 媒体消息块
 * 
 * 
 * @author MaiJingFeng
 */
public class MsgVoice implements Msg{
	
	private String file_id;

	public String getFile_id() {
		return file_id;
	}


	public void setFile_id(String file_id) {
		this.file_id = file_id;
	}

	@Override
	public String getMsg_type() {
		return Msg.TYPE_VOICE;
	}

	
}
