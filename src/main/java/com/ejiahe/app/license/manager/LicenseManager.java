package com.ejiahe.app.license.manager;


import com.alibaba.fastjson.JSONObject;
import com.ejiahe.app.license.bean.CipherLicense;
import com.ejiahe.app.license.bean.License;
import com.ejiahe.app.license.bean.LisenceCipherText;
import com.ejiahe.app.license.conf.CipherStrategy;
import com.ejiahe.app.license.conf.SignStrategy;
import com.ejiahe.app.license.manager.cipher.ICipher;
import com.ejiahe.app.license.manager.sign.ISign;
import com.ejiahe.app.license.manager.sign.source.SignMD5;
import com.ejiahe.app.license.uitls.LicenseUtil;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * License 许可管理
 * @author focus
 * @date 2015年10月9日
 * @time 下午1:46:07
 */
public class LicenseManager {

	// 策略的分隔符， 分割  内容加密解密策略&签名加密解密策略
	private static final String STRATEGY_SPLIT = "&";
	Logger log = LoggerFactory.getLogger(getClass());
	

	/**
	 * 创建许可
	 * @param lisenceCreateText
	 * @return
	 * @author focus
	 * @date 2015年10月10日
	 * @time 上午11:16:08
	 */
	public CipherLicense createLisence(License lisenceCreateText){
		
		// 1  对数据进行处理，合成
		// 2 通过策略对数据进行加密
		
		// 策略  格式如：内容解密策略&签名策略,秘钥
		//			签名策略,秘钥
		//			内容解密策略,秘钥
		//			内容解密策略
		//			内容签名策略
		//			内容解密策略&签名策略
		// 策略可以为空： 如果为空则表示使用默认的策略进行加密解密，和签名验证等
		// 如果只有一种则说明，内容和签名都是用的同一个加密策略
		String cipherStrategyName = CipherStrategy.DEFAULT.name();
		String signStrategyName = SignStrategy.DEFAULT.name();

		String strategy = cipherStrategyName + "&" + signStrategyName;
		// 取得策略
		CipherStrategy cipherStrategy = CipherStrategy.parse(cipherStrategyName);
		SignStrategy signStrategy = SignStrategy.parse(signStrategyName);
		
		// 通过策略取得 对应的生成逻辑
		ICipher cipher = cipherStrategy.getChiperManager();
		ISign sign = signStrategy.getSignManager();
		
 		// 秘钥 ,根据策略生成
		String privateKey = cipher.createPrivateKey();
		
		
		// 许可中的文本内容， 包括过期时间， 最大用户数量， 最大用户可登陆的数量
		Map<String,Object> lisenceTextMap = new HashMap<String,Object>();
		lisenceTextMap.put("expireDate", lisenceCreateText.getExpireDate());
		String extFieldsStr = lisenceCreateText.getExtFields();
		if(!Strings.isNullOrEmpty(extFieldsStr)){
			lisenceTextMap.put("extFields",extFieldsStr);
		}
		// 许可对已企业的唯一标识
		String nonce = lisenceCreateText.getNonce();
		// 策略
		if(privateKey != null && !privateKey.equals("")){
			strategy = strategy + "," + privateKey;
		}
		
		// 生成许可中的许可明细的内容
		String lisenceText = cipher.encrypt(LicenseUtil.parseMapToJson(lisenceTextMap), privateKey);
		String signMsg = nonce + lisenceText + strategy +nonce;

		try{
			JSONObject extFieldJson = JSONObject.parseObject(extFieldsStr);
			Object compatilityLowVersion = extFieldJson.get("compatibilityLowVersion");
			if(!Boolean.valueOf(String.valueOf(compatilityLowVersion))){
				signMsg += "_gzb";
			}
		}catch(Exception e){

		}

		String msgSign = createSign( sign,signMsg);

		CipherLicense lisence = new CipherLicense();
		lisence.setLisence(lisenceText);
		lisence.setNonce(nonce);
		lisence.setStrategy(strategy);
		lisence.setMsgSign(msgSign);

		return lisence;
	}

	/**
	 * 取得签名里面原始内容的内容： "nonce + lisence + strategy + nonce"
	 * @param sign
	 * @param signMsg
	 * @return
	 */
	private String createSign(ISign sign,String signMsg) {
		// 取得签名里面原始内容的内容： "nonce + lisence + strategy + nonce"
		String signSourceContent = new SignMD5().createMsgSign(signMsg);
		// 生成签名
		return sign.createMsgSign(signSourceContent);
	}

}
