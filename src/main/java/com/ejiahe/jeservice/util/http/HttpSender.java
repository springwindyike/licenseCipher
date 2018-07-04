package com.ejiahe.jeservice.util.http;


import com.ejiahe.jeservice.util.StatusCode;
import com.ejiahe.jeservice.util.exception.HttpException;
import com.google.common.base.Strings;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * HTTP简易发送器;
 * 
 * 
 * @author MaiJingFeng
 */
public class HttpSender {
	
	public static final String HTTP_METHOD_GET = HttpGet.METHOD_NAME;
	public static final String HTTP_METHOD_POST = HttpPost.METHOD_NAME;
	public static final String HTTP_METHOD_PUT = HttpPut.METHOD_NAME;
	
	/*
	 * 字符集 
	 */
	public static final Charset GBK =  Charset.forName("GBK");
	public static final Charset GBK2312 =  Charset.forName("GB2312");
	public static final Charset UTF8 =  Charset.forName("UTF-8");
	public static final Charset ISO_8859_1 =  Charset.forName("ISO-8859-1");
	
	private  Charset default_charset = UTF8;
	
	public static int DEFAULT_TIME_OUT = (int)(60 * 1000);
	
	private static ConcurrentMap<String, HttpSender> senders =
			new ConcurrentHashMap<String, HttpSender>();
	
	private CloseableHttpClient client;
	
	private String name;
	
	private int timeout;
	
	private HttpSender(String name, int maxTotal, int maxPerHost, int timeout){
		this.name = name;
		this.timeout = timeout;
		this.client = (CloseableHttpClient)HttpClientFactory.getInstance().createHttpClient("HttpSender", 
				maxTotal, maxPerHost);
	}
	
	/**
	 * 获取缺省的HttpSender;
	 * 若不存在,则创建
	 * @return
	 */
	public static HttpSender getHttpSender(){
		return getHttpSender(HttpClientFactory.DefaultName,1024,512);
	}
	
	/**
	 * 获取指定的HttpSender;
	 * 若不存在,则自动创建
	 * @param name Sender名,全局唯一
	 * @param maxTotal HTTP连接池最大数
	 * @param maxPerHost  HTTP单域名最大连接数
	 * @return
	 */
	public static HttpSender getHttpSender(String name, int maxTotal, int maxPerHost){
		return getHttpSender(name, maxTotal, maxPerHost, DEFAULT_TIME_OUT);
	}
	
	/**
	 * 获取指定的HttpSender;
	 * 若不存在,则自动创建
	 * @param name Sender名,全局唯一
	 * @param maxTotal HTTP连接池最大数
	 * @param maxPerHost  HTTP单域名最大连接数
	 * @param timeout Sender的请求缺省超时时间,单位毫秒;
	 * @return
	 */
	public static HttpSender getHttpSender(String name, int maxTotal, int maxPerHost, int timeout){
		if(Strings.isNullOrEmpty(name)){
			throw new IllegalArgumentException("The HttpSender Name can not empty");
		}
		
		HttpSender sender = senders.get(name);
		if(sender == null){
			synchronized(HttpSender.class){
				sender = senders.get(name);
				if(sender == null){
					sender = new HttpSender(name, maxTotal, maxPerHost, timeout);
					senders.put(name, sender);
				}
			}
		}
		
		return sender;
	}
	
	
	public String getName(){
		return this.name;
	}
	
	
	/**
	 * HTTP GET请求
	 * @param url
	 * @return 返回响应的字符串
	 * @throws HttpException
	 *
	 */
	public String httpGet(String url) throws HttpException{
		return send(new HttpGet(url), -1, null);
	}

	/**
	 * HTTP GET请求
	 * 提供header请求头设置
	 * @param url
	 * @param headers 请求头
	 * @return 返回响应的字符串
	 * @throws HttpException
	 *
	 */
	public String httpGet(String url, Collection<Header> headers) throws HttpException{
		HttpRequestBase request = new HttpGet(url);
		if(headers != null && !headers.isEmpty()){
			Header[] tempHeaders = new BasicHeader[headers.size()];
			headers.toArray(tempHeaders);
			request.setHeaders(tempHeaders);
		}
		return send(request, -1, null);
	}

	/**
	 * 
	* HTTP GET请求
	 * @param url
	 * @param timeout 请求超时时间,单位毫秒,缺省为-1,即使用Sender的缺省值
	 * @return 返回响应的字符串
	 * @throws HttpException
	 */
	public String httpGet(String url, int timeout) throws HttpException{
		return send(new HttpGet(url), timeout, null);
	}
	
	
	/**
	 * HTTP POST请求
	 * @param url
	 * @param msg POST数据(可直接传入JSONObject)
	 * @return 返回响应的字符串
	 * @throws HttpException
	 *
	 */
	public String httpPost(String url, Object msg) throws HttpException{
		return httpPost(url, msg, -1);
	}

	/**
	 * HTTP PUT请求
	 * @param url
	 * @param msg POST数据(可直接传入JSONObject)
	 * @return 返回响应的字符串
	 * @throws HttpException
	 *
	 */
	public String httpPut(String url, Object msg) throws HttpException{
		return httpPut(url, msg, -1);
	}

	/**
	 * HTTP POST请求
	 * @param url
	 * @param msg POST数据(可直接传入JSONObject)
	 * @param timeout 请求超时时间,单位毫秒,缺省为-1,即使用Sender的缺省值
	 * @return 返回响应的字符串
	 * @throws HttpException
	 *
	 */
	public String httpPost(String url, Object msg, int timeout) throws HttpException{
		HttpPost httpPost = new HttpPost(url);
		HttpEntity entity = null;
		
		if(msg instanceof InputStream){
			InputStreamBody inputStreamBody = new InputStreamBody((InputStream)msg,ContentType.DEFAULT_BINARY);
			entity = MultipartEntityBuilder.create()
	        		 .addPart("data",inputStreamBody)
	                 .build();
		}else{
//			entity = new StringEntity(msg.toString(), default_charset);
			entity = new StringEntity(msg.toString(), ContentType.APPLICATION_JSON);
		}

		httpPost.setEntity(entity);
		
		return send(httpPost, timeout, default_charset);
	}
	/**
	 * HTTP POST请求
	 * 提供header请求头设置
	 * @param url
	 * @param msg POST数据(可直接传入JSONObject)
	 * @param headers 请求头
	 * @return 返回响应的字符串
	 * @throws HttpException
	 *
	 */
	public String httpPost(String url, Object msg, Collection<Header> headers) throws HttpException{
		HttpPost httpPost = new HttpPost(url);
		HttpEntity entity = null;

		if(msg instanceof InputStream){
			InputStreamBody inputStreamBody = new InputStreamBody((InputStream)msg,ContentType.DEFAULT_BINARY);
			entity = MultipartEntityBuilder.create()
	        		 .addPart("data",inputStreamBody)
	                 .build();
		}else{
//			entity = new StringEntity(msg.toString(), default_charset);
			entity = new StringEntity(msg.toString(), ContentType.APPLICATION_JSON);
		}

		if(headers != null && !headers.isEmpty()){
			Header[] tempHeaders = new BasicHeader[headers.size()];
			headers.toArray(tempHeaders);
			httpPost.setHeaders(tempHeaders);
		}
		httpPost.setEntity(entity);

		return send(httpPost, -1, default_charset);
	}

	/**
	 * HTTP PUT请求
	 * @param url
	 * @param msg PUT数据(可直接传入JSONObject)
	 * @param timeout 请求超时时间,单位毫秒,缺省为-1,即使用Sender的缺省值
	 * @return 返回响应的字符串
	 * @throws HttpException
	 *
	 */
	public String httpPut(String url, Object msg, int timeout) throws HttpException{
		HttpPut httpPut = new HttpPut(url);
		HttpEntity entity = null;

		if(msg != null){
			if(msg instanceof InputStream){
				InputStreamBody inputStreamBody = new InputStreamBody((InputStream)msg,ContentType.DEFAULT_BINARY);
				entity = MultipartEntityBuilder.create()
						.addPart("data",inputStreamBody)
						.build();
			}else{
				entity = new StringEntity(msg.toString(), default_charset);
			}
			httpPut.setEntity(entity);
		}

		return send(httpPut, timeout, default_charset);
	}

	
	/**
	 * HTTP POST请求
	 * @param url
	 * @param params POST数据(map格式)
	 * @return 返回响应的字符串
	 * @throws HttpException
	 *
	 */
	public String httpPost(String url, Map<String, String> params) throws HttpException{
		return httpPost(url, params, -1);
	}
	
	/**
	 * HTTP POST请求
	 * @param url
	 * @param params POST数据(map格式)
	 * @param timeout 请求超时时间,单位毫秒,缺省为-1,即使用Sender的缺省值
	 * @return 返回响应的字符串
	 * @throws HttpException
	 *
	 */
	public String httpPost(String url, Map<String, String> params, int timeout) throws HttpException{
		HttpPost httpPost = new HttpPost(url);
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		for (String name : params.keySet()){
			nvps.add(new BasicNameValuePair(name, params.get(name)));
		}
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, default_charset));
		
		return send(httpPost, timeout, default_charset);
	}
	
	
	/**
	 * HTTP Multipart请求
	 * @param url
	 * @param parts
	 * @return
	 * @throws HttpException
	 */
	public String httpMultipart(String url, Map<String,ContentBody> parts) throws HttpException{
		HttpPost httpPost = new HttpPost(url);
		HttpEntity entity = null;
		
		//BROWSER_COMPATIBLE自定义charset，RFC6532=utf-8，STRICT=iso-8859-1
		MultipartEntityBuilder build = MultipartEntityBuilder.create().setMode(HttpMultipartMode.RFC6532);
		for(Map.Entry<String, ContentBody> entry : parts.entrySet()){
			build.addPart(entry.getKey(), entry.getValue());
		}
		
		entity = build.build();
		httpPost.setEntity(entity);
		
		return send(httpPost, -1, default_charset);
	}
	
	
	private String send(HttpRequestBase request,int timeout,
			Charset charset) throws HttpException{
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse resp = null;
		if(charset == null){
			charset = default_charset;
		}
		
		try {
			httpclient = createHttpClient();
			
			RequestConfig requestConfig = null;
			if(timeout > 0){
				requestConfig = RequestConfig.custom()
						.setSocketTimeout(timeout)
						.build();
			}else{
				requestConfig = RequestConfig.custom()
						.setSocketTimeout(this.timeout)
						.build();
			}
			
			request.setConfig(requestConfig);
			
			resp = httpclient.execute(request);
			int respCode = resp.getStatusLine().getStatusCode();
			
			HttpEntity tempE = resp.getEntity();
			BufferedReader r = new BufferedReader(new InputStreamReader(tempE.getContent(),charset));
			StringBuilder b = new StringBuilder();
			String line = r.readLine();
			while(line != null){
				b.append(line);
				line = r.readLine();
			}
			
			//System.out.println(b.toString());
			
			int statusCode = resp.getStatusLine().getStatusCode();
			
			if (statusCode < 200 || statusCode >= 300){
				throw new HttpException("HTTP Error Code(" + respCode + "): " + b.toString());
			}
			
			return b.toString();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw new HttpException("ClientProtocolException", e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new HttpException("IO Exception", e);
		} catch (HttpException e) {
			e.printStackTrace();
			throw e;
		} finally{
			if(resp != null){
				try {
					resp.close();
				} catch (IOException e) {
					//ignore;
				}
			}
			
			if(request != null){
				request.releaseConnection();
			}
		}
	}
	
//	/**
//	 * 下载
//	 * @param url
//	 * @return
//	 * @throws HttpException
//	 */
//	@SuppressWarnings("unused")
//	public byte[] download(String url) throws HttpException{
//		CloseableHttpClient httpclient = null;
//		CloseableHttpResponse resp = null;
//		HttpRequestBase request = null;
//
//		try {
//			httpclient = createHttpClient();
//
//			request = new HttpGet(url);
//
//			RequestConfig requestConfig = RequestConfig.custom()
//	        .setSocketTimeout(this.timeout)
//	        .build();
//			request.setConfig(requestConfig);
//
//			resp = httpclient.execute(request);
//			int respCode = resp.getStatusLine().getStatusCode();
//
//			HttpEntity tempE = resp.getEntity();
//			ByteArrayOutputStream output = new ByteArrayOutputStream();
//			byte[] buf = new byte[2048];
//			BufferedInputStream r = new BufferedInputStream(tempE.getContent());
//
//			int len = r.read(buf);
//			while(len != -1){
//				output.write(buf, 0, len);
//				len = r.read(buf);
//			}
//
//			return output.toByteArray();
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//			throw new HttpException("ClientProtocolException", e);
//		} catch (IOException e) {
//			e.printStackTrace();
//			throw new HttpException("IO Exception", e);
//		} finally{
//			if(resp != null){
//				try {
//					resp.close();
//				} catch (IOException e) {
//					//ignore;
//				}
//			}
//
//			if(request != null){
//				request.releaseConnection();
//			}
//		}
//	}

	/**
	 * 下载
	 * @return
	 * @throws HttpException
	 */
	public byte[] download(String url, Collection<Header> headers) throws HttpException{
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse resp = null;
		HttpRequestBase request = null;

		try {
			httpclient = createHttpClient();

			request = new HttpGet(url);
			if(headers != null && !headers.isEmpty()){
				Header[] tempHeaders = new BasicHeader[headers.size()];
				headers.toArray(tempHeaders);
				request.setHeaders(tempHeaders);
			}

			RequestConfig requestConfig = RequestConfig.custom()
	        .setSocketTimeout(this.timeout)
	        .build();
			request.setConfig(requestConfig);

			resp = httpclient.execute(request);
			int respCode = resp.getStatusLine().getStatusCode();

			HttpEntity tempE = resp.getEntity();
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] buf = new byte[2048];
			BufferedInputStream r = new BufferedInputStream(tempE.getContent());

			int len = r.read(buf);
			while(len != -1){
				output.write(buf, 0, len);
				len = r.read(buf);
			}

			return output.toByteArray();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw new HttpException("ClientProtocolException", e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new HttpException("IO Exception", e);
		} finally{
			if(resp != null){
				try {
					resp.close();
				} catch (IOException e) {
					//ignore;
				}
			}

			if(request != null){
				request.releaseConnection();
			}
		}
	}

	/**
	 * 下载
	 * @param url
	 * @return
	 * @throws HttpException
	 */
	public byte[] download(String url) throws HttpException{
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		download(url, output);
		return output.toByteArray();
	}


	public void download(String url,HttpServletResponse resp1) throws Exception {

		OutputStream output = resp1.getOutputStream();

		CloseableHttpClient httpclient = null;
		CloseableHttpResponse resp = null;
		HttpRequestBase request = null;

		try {
			httpclient = createHttpClient();

			request = new HttpGet(url);

			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(this.timeout)
					.build();
			request.setConfig(requestConfig);

			resp = httpclient.execute(request);
			int respCode = resp.getStatusLine().getStatusCode();
			if(respCode == StatusCode.SUCCESS){
				Header[] headers = resp.getAllHeaders();
				for(Header header : headers){
					resp1.addHeader(header.getName(),header.getValue());
				}

			}
			HttpEntity tempE = resp.getEntity();
			byte[] buf = new byte[2048];
			BufferedInputStream r = new BufferedInputStream(tempE.getContent());

			int len = r.read(buf);
			while(len != -1){
				output.write(buf, 0, len);
				len = r.read(buf);
			}


		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw new HttpException("ClientProtocolException", e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new HttpException("IO Exception", e);
		} finally{
			if(resp != null){
				try {
					resp.close();
				} catch (IOException e) {
					//ignore;
				}
			}

			if(request != null){
				request.releaseConnection();
			}
		}
	}
	public void download(String url,OutputStream output) throws HttpException {

		CloseableHttpClient httpclient = null;
		CloseableHttpResponse resp = null;
		HttpRequestBase request = null;

		try {
			httpclient = createHttpClient();

			request = new HttpGet(url);

			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(this.timeout)
					.build();
			request.setConfig(requestConfig);

			resp = httpclient.execute(request);
			int respCode = resp.getStatusLine().getStatusCode();

			HttpEntity tempE = resp.getEntity();
			byte[] buf = new byte[2048];
			BufferedInputStream r = new BufferedInputStream(tempE.getContent());

			int len = r.read(buf);
			while(len != -1){
				output.write(buf, 0, len);
				len = r.read(buf);
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw new HttpException("ClientProtocolException", e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new HttpException("IO Exception", e);
		} finally{
			if(resp != null){
				try {
					resp.close();
				} catch (IOException e) {
					//ignore;
				}
			}

			if(request != null){
				request.releaseConnection();
			}
		}
	}


	private CloseableHttpClient createHttpClient(){
		return client;
	}
	

	public void setCharset(Charset charset){
		this.default_charset = charset;
		
	}
}
