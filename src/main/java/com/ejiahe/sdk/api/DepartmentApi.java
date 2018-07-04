package com.ejiahe.sdk.api;

import com.ejiahe.jeservice.util.StatusCode;
import com.ejiahe.jeservice.util.StrUtils;
import com.ejiahe.jeservice.util.common.GZBApiException;
import com.ejiahe.jeservice.util.common.GZBResultParseUtil;
import com.ejiahe.jeservice.util.exception.HttpException;
import com.ejiahe.jeservice.util.http.HttpSender;
import com.ejiahe.sdk.config.GZBApiConfig;
import com.ejiahe.sdk.openbean.DepartmentResult;

public class DepartmentApi {

	/**
	 * 获取部门成员
	 * @author focus
	 * @date 2016年3月2日
	 * @time 下午1:50:00
	 */
	public static DepartmentResult getDepartmentMembers(String access_token,String department_id) throws GZBApiException {
		String url = StrUtils.arrayFormat(GZBApiConfig.getInstance().getEimBaseUrl()
				+ "/department/get?access_token={}&department_id={}&fetch_user={}", access_token, department_id, true);
		
		try {
			return GZBResultParseUtil.parseObject(
					HttpSender.getHttpSender().httpGet(url),
					DepartmentResult.class);
		} catch (HttpException e) {
			throw new GZBApiException(StatusCode.GATEWAY_TIMEOUT,e);
		}
	}
}
