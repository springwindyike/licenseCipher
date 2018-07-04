package com.ejiahe.jeservice.jsonrpc.server.session;

import com.google.common.base.Charsets;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.InputStream;

/**
 * Created by mjf on 2016/10/27.
 */
public class JSONRPC2AMQPSession extends JSONRPC2AbstractSession {

    private RabbitTemplate template;
    private MessageProperties requestProperties;

    public JSONRPC2AMQPSession(RabbitTemplate template, Message msg){
        this.template = template;
        this.requestProperties = msg.getMessageProperties();
    }

    @Override
    public void write(Object o) {

        MessageProperties properties = MessagePropertiesBuilder.newInstance()
                .setClusterIdIfAbsent(requestProperties.getClusterId())
                .setCorrelationIdStringIfAbsent(requestProperties.getCorrelationIdString())
                .setCorrelationIdIfAbsent(requestProperties.getCorrelationId())
                .build();
        Message reply = MessageBuilder.withBody(o.toString().getBytes(Charsets.UTF_8))
                    .andProperties(properties).build();

        template.send("",requestProperties.getReplyTo(),reply);
    }

    @Override
    public void write(InputStream input) {
        throw new UnsupportedOperationException("the jsonrpc amqp session unsupport this operation");
    }


    @Override
    public void close() {
        //ignore;
    }

    @Override
    public String getHostAddress() {
        return "";
    }

    @Override
    public String getHostName() {
        return "";
    }

    @Override
    public String getClientId() {
        return "";
    }




}
