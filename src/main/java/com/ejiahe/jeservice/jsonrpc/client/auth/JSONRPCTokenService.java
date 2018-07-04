package com.ejiahe.jeservice.jsonrpc.client.auth;

import com.alibaba.fastjson.JSONObject;
import com.ejiahe.jeservice.jsonrpc.client.JSONRPCClientConfig;
import com.ejiahe.jeservice.jsonrpc.packet.JSONRPCRequest;
import com.ejiahe.jeservice.jsonrpc.packet.JSONRPCResponse;
import com.ejiahe.jeservice.util.XMPPDateTimeFormat;
import com.ejiahe.jeservice.util.http.HttpSender;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Named
public class JSONRPCTokenService {
	private static Logger LOG = LoggerFactory.getLogger(JSONRPCTokenService.class);

	private String token;
	private long deadline;

	private long lastReqTime;
	private Object reqLocker = new Object();
	private ScheduledFuture<?> updateTokenTask;

	private ScheduledExecutorService scheduleService;

	@Inject
	JSONRPCClientConfig config;

	public JSONRPCTokenService() {
		scheduleService = Executors.newScheduledThreadPool(4);
	}

	/**
	 * 检查jsonrpc结果是否是401鉴权失败
	 * @param result
	 * @return 若是401错误,则返回false;
	 */
	public boolean checkJsonRpcResult(JSONRPCResponse result) {
		if (!result.isSuccess()) {
			Integer jsonCode = result.getError().getCode();
			if (jsonCode != null && jsonCode == 401) {
				invalidateToken();
				updateToken(true);

				return false;
			}
		}

		return true;
	}

	public String getToken() {
		if (isIllegal()) {
			if (!updateToken(false)) {
				return null;
			}
		}
		return token;
	}

	public boolean updateToken(boolean force) {
		synchronized (reqLocker) {
			boolean illegal = isIllegal();
			if (force || illegal) {
				long now = System.currentTimeMillis();
				if (now - lastReqTime < 3000) {
					LOG.info("Skip UpdateToken, Because now - lasReqRTime < {}", 3000);
					return true;
				} else {
					lastReqTime = System.currentTimeMillis();
					if (updateTokenTask != null) {
						updateTokenTask.cancel(true);
						updateTokenTask = null;
					}

					boolean success = reqToken();

					if (success) {
						long refreshTime = deadline - 120 * 1000;
						long delay = refreshTime - System.currentTimeMillis();
						LOG.info("next refresh time:{} delay:{}", XMPPDateTimeFormat.format(new Date(refreshTime)), delay);
						delay = delay < 5000 ? 5000 : delay;
						updateTokenTask = scheduleService.schedule(new Runnable() {
							@Override
							public void run() {
								updateToken(true);
							}
						}, delay, TimeUnit.MILLISECONDS);
					}

					return success;
				}
			}
		}
		return true;
	}

	/**
	 * 当前token是否非法
	 * @return
	 */
	public boolean isIllegal() {
		if (Strings.isNullOrEmpty(token)) {
			return true;
		}

		if (System.currentTimeMillis() > deadline) {
			return true;
		}

		return false;
	}

	private void invalidateToken() {
		long now = System.currentTimeMillis();
		synchronized (reqLocker) {
			if (now - lastReqTime > 3000) {
				token = null;
				deadline = 0;
			}
		}
	}

	/**
	 * 获取token
	 * @return
	 */
	public boolean reqToken() {
		JSONObject params = new JSONObject();
		params.put("username", config.getAppId());
		params.put("password", config.getAppSecret());

		JSONRPCRequest request = new JSONRPCRequest("auth.http.login", params);

		HttpSender sender = HttpSender.getHttpSender();
		try {
			String response = sender.httpPost(config.getServiceUrl(), request);
			JSONRPCResponse resp = JSONRPCResponse.parse(response);

			if(resp.isSuccess()){
				JSONRPCAccessToken temp = resp.getResultAsObject(JSONRPCAccessToken.class);
				this.token = temp.getAccessToken();
				this.deadline = temp.getDeadline();
				LOG.info("update token: {} deadline:{}", token, XMPPDateTimeFormat.format(new Date(deadline)));
				return true;
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage(),e);
		}

		this.token = null;
		this.deadline = 0;
		return false;
	}

	/**
	 * 检查链接状态;
	 * 直至链接成功;
	 */
	public void checkConnect() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					if(!updateToken(true)){
						LOG.error("the jsonrpc service not available... : {}",config.getServiceUrl());
					}else{
						break;
					}

					try {
						Thread.sleep(3 * 1000);
					} catch (InterruptedException e) {
						//ignore;
					}
				}
			}
		}).start();
	}

}
