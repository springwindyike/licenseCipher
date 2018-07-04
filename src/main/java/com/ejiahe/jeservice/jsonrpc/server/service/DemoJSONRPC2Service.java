package com.ejiahe.jeservice.jsonrpc.server.service;

import com.ejiahe.jeservice.jsonrpc.packet.JSONRPCError;
import com.ejiahe.jeservice.jsonrpc.packet.JSONRPCRequest;
import com.ejiahe.jeservice.jsonrpc.packet.JSONRPCResponse;
import com.ejiahe.jeservice.jsonrpc.server.JSONRPC2Service;
import com.ejiahe.jeservice.jsonrpc.server.annotation.JSONRPCClientPermission;
import com.ejiahe.jeservice.jsonrpc.server.annotation.RequirePermission;
import com.ejiahe.jeservice.jsonrpc.server.session.JSONRPC2Session;

import javax.inject.Named;

/**
 * Created by mjf on 2016/10/27.
 */
@Named
public class DemoJSONRPC2Service implements JSONRPC2Service {
    @Override
    public String getScope() {
        return "demo";
    }

    @RequirePermission(basePermission= JSONRPCClientPermission.ONE)
    public void test(JSONRPCRequest request, JSONRPC2Session session) throws JSONRPCError {

//        System.out.println("JSONRPC: " + request);

        session.write(JSONRPCResponse.createResult(request));
    }

    @Override
    public void start() {
//        System.out.println("JSONRPC: Demo Service Started");
    }

    @Override
    public void stop() {

    }
}
