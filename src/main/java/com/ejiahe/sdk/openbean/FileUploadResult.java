package com.ejiahe.sdk.openbean;

import com.ejiahe.jeservice.util.CodeResult;

import java.util.Date;

public class FileUploadResult extends CodeResult {

	private String type;
	private String id;
	private String down_url;
	private String fileName;
	private Date creation_date;
	
	private Long fileSize;
	
	public FileUploadResult(){
		
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDown_url() {
		return down_url;
	}

	public void setDown_url(String down_url) {
		this.down_url = down_url;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getCreation_date() {
		return creation_date;
	}

	public void setCreation_date(Date creation_date) {
		this.creation_date = creation_date;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	@Override
	public String toString() {
		return "FileUploadResult [id=" + id + ", type=" + type + ", down_url="
				+ down_url + ", creation_date=" + creation_date + "]";
	}
	
	
}
