package com.ejiahe.sdk.openbean;

public interface Msg {

	public static final String TYPE_TEXT = "text";
	public static final String TYPE_IMAGE = "image";
	public static final String TYPE_VOICE = "voice";
	public static final String TYPE_NEWS = "news";
	public static final String TYPE_FILE = "file";
	public static final String TYPE_FUNCTION = "function";
	public static final String TYPE_CUSTOM = "custom";
	
	public String getMsg_type();
	
}
