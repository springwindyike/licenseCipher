package com.ejiahe.jeservice.util.http;


import com.ejiahe.jeservice.util.LockManager;
import com.google.common.base.Strings;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;

import javax.inject.Named;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

/**
 * HTTP连接池工厂类;
 * 可通过单例或@Inject注入方式获取该类示例;
 * @author MaiLingFeng
 *
 */
@Named
public class HttpClientFactory{

	public static final String DefaultName = "default";
	private static HttpClientFactory factory = new HttpClientFactory();

	private Map<String,CloseableHttpClient> clients =
			new ConcurrentHashMap<String,CloseableHttpClient>();
	
	public static HttpClientFactory getInstance(){
		return factory;
	}
	
	/**
	 * 获取缺省的HttpClient(带连接池)
	 * 
	 */
	public HttpClient getDefaultHttpClient(){
		return createHttpClient(DefaultName, 1024, 256);
	}
	
	/**
	 * 获取或创建HttpClient连接池;
	 * @param name 连接池的唯一名称;
	 * @param maxTotal 最大连接数
	 * @param maxPerRoute 单域最大连接数
	 */
	public HttpClient createHttpClient(String name, int maxTotal,int maxPerRoute) {
		CloseableHttpClient client = clients.get(name);
		if(client == null){
			Lock lock = LockManager.getInstance().getLocalLock("HTTPClientFactory", name);
			lock.lock();
			try {
	    		SSLContext sslContext = SSLContext.getInstance("TLS");
	    		TrustManager tm = new DefaultX509TrustManager();
	    		sslContext.init(null, new TrustManager[] { tm }, null);
	    		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
	    				sslContext,
	    				SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER); 
				
				PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(
						RegistryBuilder
								.<ConnectionSocketFactory>create()
								.register(
										"http",
										PlainConnectionSocketFactory
												.getSocketFactory())
								.register("https", sslsf).build());
				poolingHttpClientConnectionManager.setMaxTotal(maxTotal);
				poolingHttpClientConnectionManager.setDefaultMaxPerRoute(maxPerRoute);
				
				Collection<Header> headers = new ArrayList<Header>();  
		        headers.add(new BasicHeader("Accept","*/*"));
				
		        client = HttpClients.custom()
										.setConnectionManager(poolingHttpClientConnectionManager)
										.setSSLSocketFactory(sslsf)
										.setDefaultHeaders(headers)
										.build();
		        clients.put(name, client);
			} catch (Throwable e) {
				e.printStackTrace();
				throw new IllegalStateException(e);
			}finally{
				lock.unlock();
			}
		}
		
		return client;
	}
	
	/**
	 * 关闭HTTP连接池
	 * @param clientName 连接池名称
	 */
	public void closeHttpClient(String clientName){
		if(!Strings.isNullOrEmpty(clientName)){
			CloseableHttpClient client = clients.remove(clientName);
			if(client != null){
				try {
					client.close();
				} catch (IOException e) {
					//ignore;
				}
			}
		}
	}

	public void start() {
		if(clients == null){
			clients = 
					new ConcurrentHashMap<String,CloseableHttpClient>(); 
		}
	}

	public void stop() {
		for(CloseableHttpClient client : clients.values()){
			try {
				client.close();
			} catch (IOException e) {
				//ignore;
				e.printStackTrace();
			}
		}
		
		clients.clear();
		clients = null;
	}
	
	
	protected class DefaultX509TrustManager implements X509TrustManager{

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
}
