package com.ejiahe.app.license;

import com.ejiahe.app.license.conf.LicenseConfig;
import com.ejiahe.app.license.manager.cipher.source.CipherAES;
import com.ejiahe.app.license.uitls.EmailService;
import com.ejiahe.app.license.uitls.JEConstants;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * 授权码
 * Created by focus on 2017/11/15.
 */
@Service
public class AuthorizedCode {

    public static final Integer SUCCESS = 0; // 授权码验证成功
    public static final Integer FAIL = -1; // 授权码验证失败
    public static final Integer ERROR_EXPIRE = 1; // 授权码过期
    public static final Integer ERROR_OTHER = 2; // 授权码其他错误
    //3DES(startTime&duration)
    // 加密之后生成的授权码
    private String authorizedCode = "";
    private String secretKey = ""; // 密钥
    private static String SPLIT = "&";

    private static AuthorizedCode instance;
    public AuthorizedCode(){
        instance = this;
    }


    Logger logger = LoggerFactory.getLogger(getClass());
    public static  AuthorizedCode getInstance(){
        return instance;
    }

    /**
     * 授权码生成
     * @return
     */
    public String createAuthorizeCode(){
        long startTime = getStartTime();
        long duration = JEConstants.DAY * 7;
        String code = startTime + SPLIT + duration;

        CipherAES cipher = new CipherAES();

        this.secretKey = cipher.createPrivateKey();
        logger.info("key:{}",secretKey);

        return cipher.encrypt(code, secretKey);
    }


    public int verify(){
        return verify(this.authorizedCode);
    }

    /**
     * 授权码验证
     * @param authorizedCode
     * @return 0 验证成功，1 验证失败 2 授权码过期，3 其他错误
     */
    public int verify(String authorizedCode){

        try{
            if(Strings.isNullOrEmpty(authorizedCode)) return ERROR_OTHER;

            CipherAES cipher = new CipherAES();
            String code = cipher.decrypt(authorizedCode,secretKey);
            if(code != null){

                String[] data = code.split(SPLIT);
                if(data.length == 2){

                    boolean isSuccess =  getStartTime() -
                            Long.valueOf(data[0]) < Long.valueOf(data[1]);

                    if(!isSuccess){
                       logger.info("授权码已经过期，请重新申请");
                        return ERROR_EXPIRE;
                    }
                    return SUCCESS;
                }
            }
        }catch (Exception e){
            return FAIL;
        }
        return FAIL;
    }

    private long getStartTime(){
        return System.nanoTime() / JEConstants.NANOTIME;
    }

    /**
     * 存储在内存
     * @param authorizedCode
     */
    public void setAuthorizedCode(String authorizedCode){
        this.authorizedCode = authorizedCode;
    }

    /**
     * 发送邮件
     * @return
     */
    public boolean sendMail(String authorizedCode,String serviceName,String operator,String version){

        LicenseConfig config = LicenseConfig.getInstance();
        String ip = config.getIp();
        String macAddress = config.getMacAddress();
        String name = serviceName;
        String toEmail = config.toEmail();
        String fromEmail = config.fromEmail();
        String id = config.getServiceId();

        String subject = name + "的License 授权码";
        StringBuilder content = new StringBuilder();
        content.append("服务器名: <strong>").append(name).append("</strong><p>");
        content.append("申请人:").append(operator).append("<br/>");
        content.append("服务器ID: ").append(id).append("<br/>");
        content.append("IP地址: ").append(ip).append("<br/>");
        content.append("MAC地址: ").append(macAddress).append("<p>");
        content.append("版本号: ").append(version).append("<br>");
        content.append("<strong>授权码</strong>:  <span style='color:red'>").append(authorizedCode).append("</span>");

        return EmailService.getInstance().send(fromEmail,new String[]{toEmail},subject,content.toString());
    }

}
