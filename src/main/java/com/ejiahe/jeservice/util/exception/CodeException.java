package com.ejiahe.jeservice.util.exception;

import com.ejiahe.jeservice.util.StringUtils;

import java.util.Map;

/**
 * 带错误码的异常;
 *
 *
 * @author MaiJingFeng
 */
public abstract class CodeException extends Exception{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    protected int code;

    protected Object[] args;

    public CodeException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public CodeException(int code){
        super();
        this.code = code;
    }

    public CodeException(int code, String msg){
        super(msg);
        this.code = code;
    }

    public CodeException(int code, String msg, Throwable cause){
        super(msg, cause);
        this.code = code;
    }

    public CodeException(int code, Object ...args){
        this(code);
        this.args = args;
    }

    public CodeException(int code,Throwable cause, Object ...args){
        this(code,cause);
        this.args = args;
    }


    public abstract Map<Integer,String> getMsgTemplate();


    protected String getMsgTemplate(int code){
        Map<Integer,String> templates = getMsgTemplate();
        if(templates != null){
            return templates.get(code);
        }

        return null;
    }

    @Override
    public String getMessage() {
        String msg = super.getMessage();
        if(msg == null){
            msg = getMsgTemplate(this.code);
        }

        if(msg != null && args != null){
            msg = StringUtils.arrayFormat(msg, args);
        }

        StringBuilder b = new StringBuilder()
                .append(msg)
                .append("#")
                .append(this.code);

        return b.toString();
    }


    public int getCode(){
        return this.code;
    }

}
