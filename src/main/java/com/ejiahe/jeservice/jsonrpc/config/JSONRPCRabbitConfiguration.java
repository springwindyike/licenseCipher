package com.ejiahe.jeservice.jsonrpc.config;

import com.ejiahe.jeservice.config.RabbitConfiguration;
import com.ejiahe.jeservice.jsonrpc.server.rabbit.AMQPMessageHandler;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Named;

/**
 * Created by mjf on 2016/10/20.
 */
//@Configuration
public class JSONRPCRabbitConfiguration {


    public static final String JSONRPCServiceName = RabbitConfiguration.ServiceName;
    public static final String JSONRPCExchangeName = "gzbRpcBus";
    public static final String JSONRPCQueueName = "rpc." + JSONRPCServiceName;

    @Bean
    @Named("jsonrpc")
    public SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setConcurrentConsumers(3);
        container.setMaxConcurrentConsumers(10);
        container.setMessageListener(jsonrpcMessageListener());
        container.setQueues(jsonrpcQueue());
        return container;
    }


    @Bean
    public MessageListener jsonrpcMessageListener(){
        return new AMQPMessageHandler();
    }

    @Bean
    public Queue jsonrpcQueue(){
        return new Queue(JSONRPCQueueName, true, false, true);
    }


    @Bean
    public Exchange jsonrpcExchange(){
        return new TopicExchange(JSONRPCExchangeName,true,false);
    }


    @Bean
    public Binding jsonrpcBinding(){
        return BindingBuilder.bind(jsonrpcQueue()).to(jsonrpcExchange())
                .with(RabbitConfiguration.ServiceName).noargs();
    }

}
