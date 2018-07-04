package com.ejiahe.app.license.uitls;

/**
 * 企信客户端类型
 * 
 * @author MaiJingFeng
 */
public enum JEClientType {
	
	/**
	 * 企信CMS客户端
	 */
	qxCMS("qx_CMS"),
	
	/**
	 * 企信EMS客户端
	 */
	qxEMS("qx_EMS"),
	
	/**
	 * 企信EEMS客户端
	 */
	qxEEMS("qx_EEMS"),
	
	
	/**
	 * 企信SMS客户端
	 */
	qxSMS("qx_SMS"),
	
	/**
	 * 企信EIM客户端
	 */
	qxEIM("qx_EIM"),
	
	/**
	 * 企信PC客户端
	 */
	qxWindows("uc"),
	
	/**
	 * 企信Android客户端
	 */
	qxAndroid("UC_Mobile"),
	
	/**
	 * 企信Android话机客户端
	 */
	qxAndroidFT("qx_and_ft"),
	
	/**
	 * 企信IOS客户端
	 */
	qxIOS("qx_iOS"),
	/**
	 * 企信MAC客户端
	 */
	qxMAC("qx_mac");
	
	
	// private static final String qxPCResource = "uc";
	// private static final String qxMobileResource = "UC_Mobile";
	//
	// private static final String qxWindowsResource = "uc";
	// private static final String qxAndroidResource = "UC_Mobile";
	// private static final String qxIOSResource = "qx_iOS";
	
	private String resource;
	
	
	private JEClientType(String resource) {
		this.resource = resource;
	}
	
	
	/**
	 * 获取企信客户端的资源号
	 * 
	 * @return
	 */
	public String getResource() {
		return resource;
	}
	
	/**
	 * 是否为APP终端类型;
	 * @return
	 *
	 */
	public boolean isAppClient(){
		if(this == qxAndroid || this == qxWindows || this == qxIOS){
			return true;
		}else{
			return false;
		}
	}
	
	
	/**
	 * 将字符串类型(qxPC等)转换为企信客户端抽象类型
	 * 
	 * @param type 抽象类型的字符串
	 * @return 若非企信客户端类型字符串,则转换失败,返回null;
	 */
	public static JEClientType parseClientType(String type) {
		if (type == null || type.isEmpty()) {
			return null;
		}
		
		try {
			return valueOf(type);
		} catch (Exception e) {
			return null;
		}
	}
	
	
	/**
	 * 将XMPP Resource转换为企信客户端抽象类型
	 * 
	 * @param resource XMPP资源号
	 * @return 不识别的资源号则返回null
	 */
	public static JEClientType parseClientResource(String resource) {
		if (resource != null && !resource.isEmpty()) {
			for (JEClientType type : JEClientType.values()) {
				if (resource.equals(type.getResource())) {
					return type;
				}
			}
		}
		
		return null;
	}
	
	
	/**
	 * 将XMPP Resource快速转换为字符串的企信客户端抽象类型
	 * 
	 * @param resource XMPP资源号
	 * @return 当非官方客户端资源号时,会原样地返回resource
	 */
	public static String exchangeClientResource2Type(String resource) {
		JEClientType type = parseClientResource(resource);
		if (type != null) {
			return type.name();
		} else {
			return resource;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(parseClientType("qxWindows"));
	}
}
