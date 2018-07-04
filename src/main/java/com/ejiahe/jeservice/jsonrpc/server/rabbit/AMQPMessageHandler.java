package com.ejiahe.jeservice.jsonrpc.server.rabbit;

import com.ejiahe.jeservice.jsonrpc.packet.JSONRPCError;
import com.ejiahe.jeservice.jsonrpc.packet.JSONRPCParseException;
import com.ejiahe.jeservice.jsonrpc.packet.JSONRPCRequest;
import com.ejiahe.jeservice.jsonrpc.server.JSONRPCServiceDispatcher;
import com.ejiahe.jeservice.jsonrpc.server.session.JSONRPC2AMQPSession;
import com.ejiahe.jeservice.jsonrpc.server.session.JSONRPC2Session;
import com.google.common.base.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import javax.inject.Inject;

/**
 * Created by mjf on 2016/10/28.
 */
public class AMQPMessageHandler implements MessageListener{

    private static final Logger LOG = LoggerFactory.getLogger(AMQPMessageHandler.class);

    @Inject
    private JSONRPCServiceDispatcher serviceDispatcher;

    @Inject
    private RabbitTemplate template;

    public AMQPMessageHandler(){

    }

    @Override
    public void onMessage(Message message) {
        JSONRPC2Session session = new JSONRPC2AMQPSession(template,message);

        String body = new String(message.getBody(), Charsets.UTF_8);
        try {
            if (body == null) {
                throw new JSONRPCParseException("Null argument", JSONRPCParseException.JSON, (String)null);
            } else if (body.trim().length() == 0) {
                throw new JSONRPCParseException("Invalid JSON: Empty string", JSONRPCParseException.JSON, body);
            } else {
                JSONRPCRequest msg = JSONRPCRequest.parse(body);
                serviceDispatcher.handleJSONRPC2Request(session, msg);
            }
        } catch (JSONRPCParseException e) {
            LOG.warn(e.getMessage());
            session.write(JSONRPCError.PARSE_ERROR,null);
        }
    }
}
