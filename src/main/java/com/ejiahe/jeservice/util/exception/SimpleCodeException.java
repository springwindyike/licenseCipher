package com.ejiahe.jeservice.util.exception;

import java.util.Collections;
import java.util.Map;

/**
 * 简易错误码异常;
 * 没有带“预设消息模板”
 */
public class SimpleCodeException extends CodeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SimpleCodeException(int code, Object... args) {
		super(code, args);
	}

	public SimpleCodeException(int code, String msg, Throwable cause) {
		super(code, msg, cause);
	}

	public SimpleCodeException(int code, String msg) {
		super(code, msg);
	}

	public SimpleCodeException(int code, Throwable cause, Object... args) {
		super(code, cause, args);
	}

	public SimpleCodeException(int code, Throwable cause) {
		super(code, cause);
	}

	public SimpleCodeException(int code) {
		super(code);
	}

	@Override
	public Map<Integer, String> getMsgTemplate() {
		return Collections.emptyMap();
	}

}
