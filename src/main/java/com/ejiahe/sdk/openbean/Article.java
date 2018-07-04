package com.ejiahe.sdk.openbean;


/**
 * 图文item中的单条文章
 */
public class Article {
	
	protected String title;
	protected String description;
	protected String url;
	protected String picUrl;
	
	public Article() {
		
	}
	
	
	public Article(String title, String url, String picUrl, String description) {
		super();
		this.title = title;
		this.url = url;
		this.picUrl = picUrl;
		this.description = description;
	}
	
	/**
	 * 文章标题
	 * @return
	 */
	public String getTitle() {
		return title != null?title:"";
	}
	
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * 文章摘要
	 * @return
	 */
	public String getDescription() {
		return description != null?description:"";
	}
	
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * 跳转链接
	 * @return
	 */
	public String getUrl() {
		return url != null?url:"";
	}
	
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * 图片下载信息
	 * @return
	 */
	public String getPicUrl() {
		return picUrl != null?picUrl:"";
	}
	
	
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}


	public String getPicurl() {
		return picUrl != null?picUrl:"";
	}


	public void setPicurl(String picurl) {
		this.picUrl = picurl;
	}
	
}
