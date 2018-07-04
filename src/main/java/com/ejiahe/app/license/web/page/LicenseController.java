package com.ejiahe.app.license.web.page;

import com.ejiahe.app.license.bean.CipherLicense;
import com.ejiahe.app.license.bean.License;
import com.ejiahe.app.license.manager.LisenceManager;
import com.ejiahe.app.license.manager.sign.source.SignMD5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value={"/license"}, headers={"Accept=application/json"})
public class LicenseController {
    private static final Logger logger = LoggerFactory.getLogger(LicenseController.class);

    private static final String localIp = "127.0.0.1";
    @RequestMapping(value={"/create"}, method={RequestMethod.POST})
    public CipherLicense create(@RequestBody License license, HttpSession session,HttpServletRequest request) {

        LisenceManager lisenceManager = new LisenceManager();

        // 请求签名
        String reqSign = license.getReqSign();

        // 请求ip 限制
        String ip = request.getRemoteAddr();

        if(checkSign(reqSign,license) && localIp.equals(ip)){
            return lisenceManager.createLisence(license);

        }

       throw new RuntimeException("加密失败");
    }

    private boolean checkSign(String sign,License license){

        Long expireDate = license.getExpireDate();
        String nonce = license.getNonce();
        String  extFields = license.getExtFields();

        String source = expireDate + nonce + extFields+expireDate;

        return sign.equals(new SignMD5().createMsgSign(source));
    }

}