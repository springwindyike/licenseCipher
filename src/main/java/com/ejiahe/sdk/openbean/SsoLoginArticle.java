package com.ejiahe.sdk.openbean;

import com.ejiahe.jeservice.util.StrUtils;
import com.ejiahe.sdk.config.GZBApiConfig;

import java.net.URLEncoder;


/**
 * 图文item中的单条文章
 */
public class SsoLoginArticle extends Article {
	
	private String responseType;
	
	private String state;
	
	private String corpId;

	private String appId;
	
	public SsoLoginArticle(String title, String url, String picUrl, String description,
			String appId, String corpId) {
		this(title,url,picUrl,description,appId,corpId,"",null);
	}
	
	public SsoLoginArticle(String title, String url, String picUrl, String description,
			String appId, String corpId, String state, String responseType) {
		super();
		if(responseType == null){
			responseType = "code";
		}
		this.title = title;
		this.url = url;
		this.picUrl = picUrl;
		this.description = description;
		this.responseType = responseType;
		this.state = state;
		this.appId = appId == null ? "" : appId;
		this.corpId = corpId == null ? "" : corpId;
	}

	@SuppressWarnings("deprecation")
	@Override
	public String getUrl() {
		return StrUtils.arrayFormat(
				GZBApiConfig.getInstance().getEimAuthBaseUrl() +
						"?app_id={}&corp_id={}&redirect_uri={}&response_type={}&state={}#chat_redirect",
				appId, corpId, URLEncoder.encode(url), responseType, state);
	}

	public String getResponseType() {
		return responseType;
	}

	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
}
