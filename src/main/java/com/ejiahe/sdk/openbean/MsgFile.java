package com.ejiahe.sdk.openbean;


/**
 * 媒体消息块
 * 
 * 
 * @author MaiJingFeng
 */
public class MsgFile implements Msg {
	
	private String file_id;

	public MsgFile(String fileId){
		this.file_id = fileId;
	}
	
	public String getFile_id() {
		return file_id;
	}


	public void setFile_id(String file_id) {
		this.file_id = file_id;
	}


	@Override
	public String getMsg_type() {
		return Msg.TYPE_FILE;
	}

	
}
