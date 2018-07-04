package com.ejiahe.jeservice.util.exception;

import java.util.Collections;
import java.util.Map;

/**
 * 简易错误码运行时异常;
 * 没有带“预设消息模板”
 */
public class SimpleCodeRuntimeException extends CodeRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SimpleCodeRuntimeException(int code, Object... args) {
		super(code, args);
	}

	public SimpleCodeRuntimeException(int code, String msg, Throwable cause) {
		super(code, msg, cause);
	}

	public SimpleCodeRuntimeException(int code, String msg) {
		super(code, msg);
	}

	public SimpleCodeRuntimeException(int code, Throwable cause, Object... args) {
		super(code, cause, args);
	}

	public SimpleCodeRuntimeException(int code, Throwable cause) {
		super(code, cause);
	}

	public SimpleCodeRuntimeException(int code) {
		super(code);
	}

	@Override
	public Map<Integer, String> getMsgTemplate() {
		return Collections.emptyMap();
	}

}
