package com.ejiahe.jeservice.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 业务响应结果,可承载简单的错误码和错误信息;
 * 若有其他定制需求,可以继承该类;
 * 
 * 备注: 200表示成功
 * @author MaiLingFeng
 *
 */
@ApiModel(value="CodeResult", description = "业务响应结果")
public class CodeResult {

	@ApiModelProperty("响应状态码")
	protected int resp_code = StatusCode.SUCCESS;

	@ApiModelProperty("响应信息")
	protected String resp_msg;
	
	public CodeResult(){
		
	}
	
	public CodeResult(int code, String msg) {
		super();
		this.resp_code = code;
		this.resp_msg = msg;
	}

	
	public int getResp_code() {
		return resp_code;
	}

	public void setResp_code(int resp_code) {
		this.resp_code = resp_code;
	}

	public String getResp_msg() {
		return resp_msg;
	}

	public void setResp_msg(String resp_msg) {
		this.resp_msg = resp_msg;
	}

	public boolean isSuccess(){
		return this.resp_code == StatusCode.SUCCESS;
	}

//	public static CodeResult failed(String msg){
//		return new CodeResult(StatusCode.COMMON_ERROR, msg);
//	}
//	public static CodeResult failed(int code, String msg){
//		return new CodeResult(code, msg);
//	}
}
