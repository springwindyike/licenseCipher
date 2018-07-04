package com.ejiahe.util;

import com.ejiahe.jeservice.util.AesBase64Cipher;
import com.ejiahe.jeservice.util.exception.AesException;
import com.ejiahe.jeservice.util.PKCS7Encoder;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

/**
 * Created by mjf on 2016/10/25.
 */
public class AESEncryption {


    @Test
    public void testEncrypted() throws AesException {

        String aesKey = "dFwewmO5F4NbDG23pfUN3Q==";

        String test = "12345";

        String test2 = "1234567890123456";

        String encrypted = AesBase64Cipher.encrypt(test,aesKey);

        System.out.println(encrypted);

        encrypted = AesBase64Cipher.encrypt(test2,aesKey);

        System.out.println(encrypted);


    }

    @Test
    public void testDecrypted() throws AesException {
        String encrypted = "WZHNo0s5MBWlELRvn38BYg==";
        String aesKey = "dFwewmO5F4NbDG23pfUN3Q==";


        String derypted = AesBase64Cipher.decrypt(encrypted,aesKey);

        System.out.println(derypted);

    }


    @Test
    public void testPC7() throws UnsupportedEncodingException {

        String test = "12345";

        byte[] source = test.getBytes("UTF-8");

        byte[] padBytes = PKCS7Encoder.encode(source.length);

        System.out.println(padBytes.length);
    }

}
