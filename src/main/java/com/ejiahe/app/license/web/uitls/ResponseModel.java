package com.ejiahe.app.license.web.uitls;

import com.ejiahe.app.license.web.uitls.ActionConstants;

import java.util.ArrayList;
import java.util.List;


/**
 * 返回结果bean
 * @author liaofei
 */
public class ResponseModel {

	private int status;

	private Object data;

	private PageUtil pageUtil;

	private List<String> infoMessages;
	private List<String> warningMessages;
	private List<String> errorMessages;
	
	public ResponseModel() {
		
	}
	
	public ResponseModel (final int status) {
		this.status = status;
	}
	
	public ResponseModel ( final int status, final Object data) {
		this.data = data;
		this.status = status;
	}

	public ResponseModel ( final int status, final Object data, final PageUtil pageUtil) {
		this.data = data;
		this.status = status;
		this.pageUtil = pageUtil;
	}

	public ResponseModel ( final int status, final Object data, final List<String> errorMessages) {
		this.data = data;
		this.status = status;
		this.errorMessages = errorMessages;
	}

	public static ResponseModel ok(Object data, PageUtil pageUtil){
		ResponseModel responseModel = new ResponseModel();
		responseModel.setStatus(ActionConstants.ACTION_STATUS_SUCCESS);
		responseModel.setData(data);
		responseModel.setPageUtil(pageUtil);
		return responseModel;
	}

	public static ResponseModel ok(Object data){
		ResponseModel responseModel = new ResponseModel();
		responseModel.setStatus(ActionConstants.ACTION_STATUS_SUCCESS);
		responseModel.setData(data);
		return responseModel;
	}

	public static ResponseModel failed(String error){
		ResponseModel responseModel = new ResponseModel();
		responseModel.setStatus(ActionConstants.ACTION_STATUS_FAILURE);
		List<String> errorMessages = new ArrayList<String>();
		errorMessages.add(error);
		responseModel.setErrorMessages(errorMessages);
		return responseModel;
	}

	public static ResponseModel warned(String warningMessage) {
		ResponseModel responseModel = new ResponseModel();
		responseModel.setStatus(ActionConstants.ACTION_STATUS_FAILURE);
		List<String> warningMessages = new ArrayList<String>();
		warningMessages.add(warningMessage);
		responseModel.setWarningMessages(warningMessages);
		return responseModel;
	}
	
	public Object getData() {
	
		return data;
	}

	public void setData(Object data) {
	
		this.data = data;
	}

	public int getStatus() {

		return status;
	}

	public void setStatus(int status) {

		this.status = status;
	}

	public PageUtil getPageUtil(){

		return pageUtil;
	}

	public void setPageUtil(PageUtil pageUtil){

		this.pageUtil = pageUtil;
	}

	public List<String> getInfoMessages() {
	
		return infoMessages;
	}

	public void setInfoMessages(List<String> infoMessages) {
	
		this.infoMessages = infoMessages;
	}

	public List<String> getWarningMessages() {
	
		return warningMessages;
	}

	public void setWarningMessages(List<String> warningMessages) {
	
		this.warningMessages = warningMessages;
	}

	public List<String> getErrorMessages() {
	
		return errorMessages;
	}

	public void setErrorMessages(List<String> errorMessages) {
	
		this.errorMessages = errorMessages;
	}

}
