package com.ejiahe.jeservice.web.uitls;

import com.ejiahe.jeservice.config.WebContextConfig;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author MaiLingFeng
 */
public class DefaultHandleServlet extends HttpServlet{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String forwardURL = "/" + WebContextConfig.PAGE_NOT_FOUND_VIEW;

    public static String NAME = "default";

    public DefaultHandleServlet(){

    }

    @Override
    protected void service(HttpServletRequest arg0, HttpServletResponse arg1)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        arg0.getRequestDispatcher(forwardURL).forward(arg0, arg1);
    }

}
