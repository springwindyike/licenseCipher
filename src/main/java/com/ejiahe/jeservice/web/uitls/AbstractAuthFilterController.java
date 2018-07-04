package com.ejiahe.jeservice.web.uitls;

import com.ejiahe.Application;
import com.ejiahe.jeservice.util.StatusCode;
import com.ejiahe.jeservice.util.exception.SimpleCodeException;
import com.ejiahe.jeservice.web.auth.WebAuthToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author MaiLingFeng
 */
public class AbstractAuthFilterController extends AbstractJSONExceptionHandlerController {

    private static final String BODY_ATTR = "body";
    private static Logger LOG = LoggerFactory.getLogger(AbstractAuthFilterController.class);

    @ModelAttribute
    public void debugPrintln(HttpSession session) throws IOException, SimpleCodeException {
        if(Application.isDebugModel()){
            return;
        }
        //后端token
        WebAuthToken userToken = (WebAuthToken)session.getAttribute(WebAuthToken.NAME);
        if(userToken == null){
//            //前端token
//            UserLoginInfo userInfo = (UserLoginInfo)session.getAttribute(DemoConstant.APP_AUTH_TOKEN);
//            if(userInfo == null){
                throw new SimpleCodeException(StatusCode.UNAUTHORIZED, "未认证，请重新登录");
//            }
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
