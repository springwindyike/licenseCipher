package com.ejiahe.app.license.uitls;

/**
 * 加密算法工具类
 *
 *
 * @author MaiJingFeng
 */
public class Encryptions {
    /**
     * 临时密钥
     */
    private static final String KEY = "PJudHYUx23JJH";

    /**
     * 解密(使用系统缺省算法)
     * @param encryptedString 已加密的字符串
     * @return
     *
     */
    public static String decrypt(String encryptedString){
        return decrypt(KEY,encryptedString);
    }

    /**
     * 加密(使用系统缺省算法)
     * @param sourceString 原始字符串
     * @return
     *
     */
    public static String encrypt(String sourceString){
        return encrypt(KEY, sourceString);
    }

    /**
     * 加密(使用系统缺省算法)
     * @param secretKey
     * @param sourceStrint
     * @return
     */
    public static String encrypt(String secretKey, String sourceStrint){
        Blowfish cipher = new Blowfish(secretKey);
        return cipher.encrypt(sourceStrint);
    }

    /**
     * 解密(使用系统缺省算法)
     * @param secretKey
     * @param encryptedString
     * @return
     */
    public static String decrypt(String secretKey, String encryptedString){
        Blowfish cipher = new Blowfish(secretKey);
        return cipher.decrypt(encryptedString);
    }


}

