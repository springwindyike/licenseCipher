package com.ejiahe.app.license.uitls;

import com.google.common.io.BaseEncoding;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;

/**
 * 基础加密组件
 * 
 * @author focus
 * @date 2015年10月13日
 * @time 下午3:24:35
 */
public class BaseCoder {
    public static final String KEY_SHA = "SHA";
    public static final String KEY_MD5 = "MD5";
 
    /**
     * MAC算法可选以下多种算法
     * 
     * <pre>
     * HmacMD5 
     * HmacSHA1 
     * HmacSHA256 
     * HmacSHA384 
     * HmacSHA512
     * </pre>
     */
    public static final String KEY_MAC = "HmacMD5";
 
    /**
     * BASE64解密
     * 
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptBASE64(String key) throws Exception {
        return BaseEncoding.base64()
				.decode(key);
    }
 
    /**
     * BASE64加密
     * 
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] key) throws Exception {
        return BaseEncoding.base64().encode(key);
    }
 
    /**
     * MD5加密
     * 
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptMD5(byte[] data) throws Exception {
 
        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
        md5.update(data);
 
        return md5.digest();
 
    }

    /**
     * 先MD5加密，再BASE64加密
     * @param key
     * @return
     */
    public static String encodeMD5AndBase64(String key){
        try {
            return encryptBASE64(encryptMD5(key.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {
        String password = BaseCoder.encodeMD5AndBase64("123456");
//        try {
//            System.out.println(encryptMD5("123456abcdef78uj".getBytes()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        System.out.println(password);
    }

    /**
     * SHA加密
     * 
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptSHA(byte[] data) throws Exception {
 
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        sha.update(data);
 
        return sha.digest();
 
    }
 
    /**
     * 初始化HMAC密钥
     * 
     * @return
     * @throws Exception
     */
    public static String initMacKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_MAC);
 
        SecretKey secretKey = keyGenerator.generateKey();
        return encryptBASE64(secretKey.getEncoded());
    }
 
    /**
     * HMAC加密
     * 
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptHMAC(byte[] data, String key) throws Exception {
 
        SecretKey secretKey = new SecretKeySpec(decryptBASE64(key), KEY_MAC);
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
 
        return mac.doFinal(data);
 
    }
}
