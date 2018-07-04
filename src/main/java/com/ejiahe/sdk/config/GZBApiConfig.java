package com.ejiahe.sdk.config;

import com.ejiahe.Application;
import com.ejiahe.release.Packager;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Named;
import java.io.File;
import java.util.Properties;

/**
 * bms应用配置属性
 * 加载application.properties属性
 * @author liaofei
 * @date 2018年1月5日
 */
@Named
public class GZBApiConfig {

	//GZBApp内置属性配置，上下文，端口
	@Value("${server.port}")
	private int httpsPort;
	@Value("${server.context-path}")
	private String contextPath;
//	@Value("${server.httpOuterIp}")
	private String httpOuterIp;
//	@Value("${server.pdf.file.path}")
	private String pdfFilePath;

	private static final String PROP_LICENSE_AGENTID = "license.agentId";
	//应用Id
	private static final String PROP_LICENSE_APPID = "license.appId";
	//应用秘钥
	private static final String PROP_LICENSE_APPSECRET = "license.appSecret";
	private static final String PROP_LICENSE_TENEMENTID = "license.tenementId";
	//eim baseURL
	private static final String PROP_EIM_BASEURL = "eim.baseURL";
	//eim fileBaseURL
	private static final String PROP_EIM_FILEBASEURL = "eim.fileBaseURL";
	//eim authBaseURL
	private static final String PROP_EIM_AUTHBASEURL = "eim.authBaseURL";

	public static GZBApiConfig getInstance(){
		return Application.getContext().getBean(GZBApiConfig.class);
	}

	public String getEimBaseUrl() {
		return SystemProperty.getInstance().getProperty(PROP_EIM_BASEURL);
	}
	public void setEimBaseUrl(String eimBaseUrl) {
		SystemProperty.getInstance().setProperty(PROP_EIM_BASEURL, eimBaseUrl);
	}
	public String getEimFileBaseUrl() {
		return SystemProperty.getInstance().getProperty(PROP_EIM_FILEBASEURL);
	}
	public void setEimFileBaseUrl(String eimFileBaseUrl) {
		SystemProperty.getInstance().setProperty(PROP_EIM_FILEBASEURL, eimFileBaseUrl);
	}
	public String getEimAuthBaseUrl() {
		return SystemProperty.getInstance().getProperty(PROP_EIM_AUTHBASEURL);
	}
	public void setEimAuthBaseUrl(String eimAuthBaseUrl) {
		SystemProperty.getInstance().setProperty(PROP_EIM_AUTHBASEURL, eimAuthBaseUrl);
	}

	public String getLicenseAppId(){
		return SystemProperty.getInstance().getProperty(PROP_LICENSE_APPID);
	}

	public void setLicenseAppId(String appId){
		SystemProperty.getInstance().setProperty(PROP_LICENSE_APPID, appId);
	}

	public void setLicenseAgentId(String agentId){
		SystemProperty.getInstance().setProperty(PROP_LICENSE_AGENTID,agentId);
	}

	public String getLicenseAgentId(){
		return SystemProperty.getInstance().getProperty(PROP_LICENSE_AGENTID);
	}
	public String getLicenseAppSecret(){
		return SystemProperty.getInstance().getProperty(PROP_LICENSE_APPSECRET);
	}
	public void setLicenseTenementId(String tenementId){
		SystemProperty.getInstance().setProperty(PROP_LICENSE_TENEMENTID,tenementId);
	}
	public String getLicensetTenementId() {
		return SystemProperty.getInstance().getProperty(PROP_LICENSE_TENEMENTID,"880001");
	}
	public void setLicenseAppSecret(String appSecret){
		SystemProperty.getInstance().setProperty(PROP_LICENSE_APPSECRET, appSecret);
	}
	public int getHttpsPort(){
		return httpsPort;
	}


	public String getHttpOuterIp() {
		return httpOuterIp;
	}

	public String getContextPath(){
		return contextPath;
	}

	public String getPdfFilePath(){
		return pdfFilePath;
	}
	public String getLicenseBaseUrl(){
		return "https://"+getHttpOuterIp()+":"+getHttpsPort()+getContextPath();
	}


	public static boolean isOSLinux() {

		Properties prop = System.getProperties();

		String os = prop.getProperty("os.name");
		if (os != null && os.toLowerCase().indexOf("linux") > -1) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isWindows() {
		return System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1;
	}
}
