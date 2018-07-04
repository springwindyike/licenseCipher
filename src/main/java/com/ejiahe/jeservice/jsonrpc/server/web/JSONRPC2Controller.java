package com.ejiahe.jeservice.jsonrpc.server.web;

import com.ejiahe.jeservice.jsonrpc.packet.JSONRPCError;
import com.ejiahe.jeservice.jsonrpc.packet.JSONRPCParseException;
import com.ejiahe.jeservice.jsonrpc.packet.JSONRPCRequest;
import com.ejiahe.jeservice.jsonrpc.server.JSONRPCServiceDispatcher;
import com.ejiahe.jeservice.jsonrpc.server.session.JSONRPC2HttpSession;
import com.ejiahe.jeservice.web.uitls.AbstractJSONRPCExceptionHandlerController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by mjf on 2016/11/1.
 */
@Controller()
public class JSONRPC2Controller extends AbstractJSONRPCExceptionHandlerController {

    private static final Logger LOG = LoggerFactory.getLogger(JSONRPC2Controller.class);

    @Inject
    private JSONRPCServiceDispatcher serviceDispatcher;

    @RequestMapping("/jsonrpc")
    public void jsonrpc(HttpServletRequest req , HttpServletResponse resp) throws JSONRPCError, IOException {

        String bodyStr = loadInputStream(req.getInputStream());

        JSONRPC2HttpSession session = new JSONRPC2HttpSession(req, resp);
        try {
            if (bodyStr == null) {
                throw new JSONRPCParseException("Null argument", JSONRPCParseException.JSON, (String)null);
            } else if (bodyStr.trim().length() == 0) {
                throw new JSONRPCParseException("Invalid JSON: Empty string", JSONRPCParseException.JSON, bodyStr);
            } else {
                JSONRPCRequest msg = JSONRPCRequest.parse(bodyStr);
                serviceDispatcher.handleJSONRPC2Request(session, msg);
            }
        } catch (JSONRPCParseException e) {
            LOG.warn(e.getMessage());
            session.write(JSONRPCError.PARSE_ERROR, null);
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
