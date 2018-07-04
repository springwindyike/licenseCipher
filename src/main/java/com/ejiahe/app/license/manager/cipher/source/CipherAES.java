package com.ejiahe.app.license.manager.cipher.source;


import com.ejiahe.app.license.manager.cipher.AbstractSymmetricCipher;
import com.ejiahe.app.license.manager.cipher.ICipher;
import com.ejiahe.app.license.uitls.AesBase64Cipher;

/**
 * AES加密/解密器;
 * <p>
 * <ul>
 * <li>字符串AES加密后经base64转码输出;
 * <li>密钥为base64编码密钥;
 * 
 */
public class CipherAES implements ICipher {


	@Override
	public String encrypt(String msg, String privateKey) {
		try{
			return AesBase64Cipher.encrypt(msg, privateKey);
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}


	}

	@Override
	public String decrypt(String encode_encrypt_msg, String privateKey) {
		try {
			return AesBase64Cipher.decrypt(encode_encrypt_msg,privateKey);
		} catch (Exception e) {
			e.printStackTrace();
			return   null;
		}
	}

	@Override
	public String createPrivateKey() {
		return AesBase64Cipher.getRandomEncodingAesKey();
	}

	@Override
	public String getAlgorithm() {
		return null;
	}

	/**
     * <strong>3DES</strong> 算法<p>
     * <h2>1 概述</h2>
     *	<tt>
     *		3DES（或称为Triple DES）是三重数据加密算法（TDEA，Triple Data Encryption Algorithm）块密码的通称。
     *		它相当于是对每个数据块应用三次DES加密算法。由于计算机运算能力的增强，原版DES密码的密钥长度变得容易被暴力破解；
     *		3DES即是设计用来提供一种相对简单的方法，即通过增加DES的密钥长度来避免类似的攻击，而不是设计一种全新的块密码算法。
     *	</tt>
     *<h2>2 算法原理</h2>
     *
     *	<tt>使用3条56位的密钥对 数据进行三次加密。3DES（即Triple DES）是DES向AES过渡的加密算法（1999年，NIST将3-DES指定为过渡的加密标准）。</tt>
     *
     *	<tt>其具体实现如下：设Ek()和Dk()代表DES算法的加密和解密过程，K代表DES算法使用的密钥，P代表明文，C代表密文，这样：</tt><p>
     *
     *		<li>3DES加密过程为：C=Ek3(Dk2(Ek1(P)))
     *		<li>3DES解密过程为：P=Dk1(EK2(Dk3(C)))
     *
     * @author focus
     * @date 2015年10月13日
     * @time 下午5:39:08
     */
    public static class Cipher3DES extends AbstractSymmetricCipher {
    //
    //	public static void main(String[] args)  {
    //		Cipher3DES cipher = new Cipher3DES();
    //		String privateKey =  cipher.createPrivateKey();
    //		System.out.println(" 私钥 ：" + privateKey);
    //
    //		String str = "focus12312314";
    //		String encrypt = cipher.encrypt(str, privateKey);
    //
    //		System.out.println(" 加密前：" + str);
    //		System.out.println(" 加密后：" + encrypt);
    //
    //		System.out.println(" 解密 ....");
    //		String decrypt = cipher.decrypt(encrypt, privateKey);
    //		System.out.println(" 解密之后： " + decrypt);
    //
    //	}
        @Override
        public String getAlgorithm() {
            return "desede";
        }

    }
}
