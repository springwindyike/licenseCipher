package com.ejiahe.app.license.web.uitls;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ejiahe.Application;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author MaiLingFeng
 */
public class AbstractOpenApiController {

    private static final String BODY_ATTR = "body";
    private static Logger LOG = LoggerFactory.getLogger(AbstractOpenApiController.class);
//    @Autowired
//    MyBatisConfiguration configuration;
    @ModelAttribute
    public void debugPrintln(HttpServletRequest req, Model model) throws IOException{
        String bodyStr = loadInputStream(req.getInputStream());
        if(Application.isDebugModel()){
            LOG.info("req({}): {}\n{}" ,req.getRemoteAddr(),req.getRequestURI(),bodyStr);
        }

        try{
            if(!Strings.isNullOrEmpty(bodyStr)){
                JSONObject jsonReq = JSON.parseObject(bodyStr);
                model.addAttribute(BODY_ATTR, jsonReq);
            }
        }catch(Exception e){
            LOG.error(e.getMessage());
            throw new IllegalArgumentException("invalid JSON:" + e.getMessage() , e);
        }
    }

    private String loadInputStream(InputStream input) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(input,"UTF-8"));
        StringBuilder b = new StringBuilder();
        String line = r.readLine();
        while(line != null){
            b.append(line);
            line = r.readLine();
        }

        return b.toString();
    }
}
