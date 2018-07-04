package com.ejiahe.jeservice.config;

import com.ejiahe.jeservice.util.JEConstants;
import com.ejiahe.jeservice.util.RandomStrUtils;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * Created by mjf on 2016/10/20.
 */
//@Configuration
public class RabbitConfiguration implements ApplicationContextAware{

    public static final String ServiceName = "gzbconnect";

    public static final String ReplyQueueName = "reply." + ServiceName + "." + RandomStrUtils.getRandomStr();

    public static final long DEFAULT_REPLY_TIMEOUT = 10 * JEConstants.SECOND;


    private ApplicationContext context;

    @Bean
    public ConnectionFactory connectionFactory() {

        Environment env = context.getEnvironment();

        CachingConnectionFactory factory =  new CachingConnectionFactory(env
                .getProperty("spring.rabbitmq.host","192.168.76.142"));

        factory.setUsername(env.getProperty("spring.rabbitmq.username","guest"));
        factory.setPassword(env.getProperty("spring.rabbitmq.password","guest"));

        return factory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {

        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setReplyTimeout(DEFAULT_REPLY_TIMEOUT);

        return template;
    }


//    @Bean
//    public Queue replyQueue(){
//        return new Queue(ReplyQueueName, false, true, true);
//    }
//
//
//    @Bean
//    @Named("reply")
//    public SimpleMessageListenerContainer rpcReplyListenerContainer(ConnectionFactory connectionFactory, RabbitTemplate amqpTemplate) {
//
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.setConcurrentConsumers(3);
//        container.setMaxConcurrentConsumers(10);
//        container.setMessageListener(amqpTemplate);
//        container.setQueues(replyQueue());
//        return container;
//    }



    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

}
