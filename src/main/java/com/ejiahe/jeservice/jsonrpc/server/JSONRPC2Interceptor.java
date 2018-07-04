package com.ejiahe.jeservice.jsonrpc.server;

import com.ejiahe.jeservice.jsonrpc.packet.JSONRPCPacket;
import com.ejiahe.jeservice.jsonrpc.server.session.JSONRPC2Session;

public interface JSONRPC2Interceptor {

	public void intercept(JSONRPC2Session session, JSONRPCPacket message, boolean incoming) throws JSONRPC2MessageRejectException;
	
	public String getName();
}
