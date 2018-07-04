package com.ejiahe.app.license.web.page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author MaiLingFeng
 */
@Controller
public class IndexPageController {

    @RequestMapping("/index")
    public String index(){
        return "/license/login";
    }

}
