package com.ejiahe.sdk.api;

import com.ejiahe.jeservice.util.CodeResult;
import com.ejiahe.jeservice.util.StatusCode;
import com.ejiahe.jeservice.util.StrUtils;
import com.ejiahe.jeservice.util.common.GZBApiException;
import com.ejiahe.jeservice.util.common.GZBResultParseUtil;
import com.ejiahe.jeservice.util.exception.HttpException;
import com.ejiahe.jeservice.util.http.HttpSender;
import com.ejiahe.sdk.config.GZBApiConfig;
import com.ejiahe.sdk.openbean.FileQueryResult;
import com.ejiahe.sdk.openbean.FileUploadResult;
import com.google.common.base.Strings;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件服务相关API
 * @author MaiLingFeng
 *
 */
public class FileApi {

	public static FileUploadResult upload(String access_token, String type, InputStream input, String filename) throws GZBApiException {
		String url = StrUtils.arrayFormat(GZBApiConfig.getInstance().getEimFileBaseUrl()
				+ "/upload?access_token={}&type={}", access_token, type);
		
		Map<String,ContentBody> parts = new HashMap<String,ContentBody>();
		parts.put("data", new InputStreamBody(input, filename));
		
		try {
			return GZBResultParseUtil.parseObject(
					HttpSender.getHttpSender().httpMultipart(url, parts),
					FileUploadResult.class);
		} catch (HttpException e) {
			throw new GZBApiException(StatusCode.GATEWAY_TIMEOUT,e);
		}
	}
	
	
	public static byte[] get(String access_token, String file_id) throws GZBApiException {
		String url = StrUtils.arrayFormat(GZBApiConfig.getInstance().getEimFileBaseUrl()
				+ "/get?access_token={}&file_id={}", access_token, file_id);
		
		try {
			return HttpSender.getHttpSender().download(url);
		} catch (HttpException e) {
			throw new GZBApiException(StatusCode.GATEWAY_TIMEOUT,e);
		}
	}


	public static void get(String access_token,String file_id,OutputStream out) throws GZBApiException {
		String url = StrUtils.arrayFormat(GZBApiConfig.getInstance().getEimFileBaseUrl()
				+ "/get?access_token={}&file_id={}", access_token, file_id);

		try {
			HttpSender.getHttpSender().download(url,out);
		} catch (HttpException e) {
			throw new GZBApiException(StatusCode.GATEWAY_TIMEOUT,e);
		}

	}

	public static void get(String access_token,String file_id,String thumbnail,HttpServletResponse resp) throws GZBApiException {
		String url = "";
		if(Strings.isNullOrEmpty(thumbnail)){
			url = StrUtils.arrayFormat(GZBApiConfig.getInstance().getEimFileBaseUrl()
					+ "/get?access_token={}&file_id={}", access_token, file_id);
		}else{
			url = StrUtils.arrayFormat(GZBApiConfig.getInstance().getEimFileBaseUrl()
					+ "/get?access_token={}&file_id={}&thumbnail={}", access_token, file_id, thumbnail);
		}

		try {
			HttpSender.getHttpSender().download(url,resp);
		} catch (Exception e) {
			throw new GZBApiException(StatusCode.GATEWAY_TIMEOUT,e);
		}

	}
	public static void get(String access_token,String file_id,String thumbnail,OutputStream out) throws GZBApiException {
		String url = StrUtils.arrayFormat(GZBApiConfig.getInstance().getEimFileBaseUrl()
				+ "/get?access_token={}&file_id={}&thumbnail={}", access_token, file_id, thumbnail);

		try {
			HttpSender.getHttpSender().download(url,out);
		} catch (HttpException e) {
			throw new GZBApiException(StatusCode.GATEWAY_TIMEOUT,e);
		}

	}

	/**
	 * 文件查询
	 * @param access_token
	 * @param file_id
	 * @return
	 * @throws GZBApiException
	 */
	public static FileQueryResult query(String access_token,String file_id) throws GZBApiException {
		String url = StrUtils.arrayFormat(GZBApiConfig.getInstance().getEimFileBaseUrl()
				+ "/query?access_token={}&file_id={}", access_token, file_id);

		try {
			return GZBResultParseUtil.parseObject(
					HttpSender.getHttpSender().httpGet(url),
					FileQueryResult.class);
		} catch (HttpException e) {
			throw new GZBApiException(StatusCode.GATEWAY_TIMEOUT,e);
		}
	}


	/**
	 * 文件标记
	 * @param access_token
	 * @param file_id
	 * @return
	 * @throws GZBApiException
	 */

	public static boolean markFile(String access_token,String file_id) throws GZBApiException {

		String url = StrUtils.arrayFormat(GZBApiConfig.getInstance().getEimFileBaseUrl()
				+ "/mark?access_token={}&file_id={}", access_token, file_id);

		try {
			CodeResult result =  GZBResultParseUtil.parseObject(
					HttpSender.getHttpSender().httpGet(url),
					CodeResult.class);
			if(result != null){
				return result.isSuccess();
			}
		} catch (HttpException e) {
			throw new GZBApiException(StatusCode.GATEWAY_TIMEOUT,e);
		}

		return false;
	}


	/**
	 * 文件取消标记
	 * @param access_token
	 * @param file_id
	 * @return
	 * @throws GZBApiException
	 */
	public static boolean unMarkFile(String access_token,String file_id) throws GZBApiException {
		String url = StrUtils.arrayFormat(GZBApiConfig.getInstance().getEimFileBaseUrl()
				+ "/unmark?access_token={}&file_id={}", access_token, file_id);

		try {
			CodeResult result =  GZBResultParseUtil.parseObject(
					HttpSender.getHttpSender().httpGet(url),
					CodeResult.class);
			if(result != null){
				return result.isSuccess();
			}
		} catch (HttpException e) {
			throw new GZBApiException(StatusCode.GATEWAY_TIMEOUT,e);
		}

		return false;
	}
	
	
	public static void main(String[] args) {
//		String fileName = "%E6%B5%8B%E8%AF%95%E9%87%8D%E5%A4%8D%E6%B7%BB%E5%8A%A02-2016-03-15";
//		String fileName = "测试重复+添@#$加3-2016-03-15";
		String fileName = "1";
//		fileName = fileName.replaceAll("%", "");
		try {
			fileName = URLDecoder.decode(fileName, "UTF-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(fileName);
	}
	
}
