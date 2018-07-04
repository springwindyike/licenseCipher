package com.ejiahe;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author MaiLingFeng
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application extends SpringBootServletInitializer implements ApplicationContextAware {

    private static final String VERSION = "GZB_License_V 2.1.1";

    private static Logger LOG = LoggerFactory.getLogger(Application.class);

    private static boolean isDebugModel;

    private static ApplicationContext context;

    private Environment env;

    public Environment getEnv() {
        return env;
    }

    public void setEnv(Environment env) {
        this.env = env;
    }

    public static boolean isDebugModel(){
        return isDebugModel;
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

//    @Override
//    public void customize(ConfigurableEmbeddedServletContainer container) {
//        String contextPath = "/" + getGzbAppContextPath();
//        Integer port = env.getProperty("gzbapp.ssl.port", Integer.class);
//        String keyStore = env.getProperty("gzbapp.ssl.key-store");
//        String keyPassword = env.getProperty("gzbapp.ssl.key-password");
//
//        boolean isSsl = env.getProperty("gzbapp.ssl.is-ssl", Boolean.class);
//        if(isSsl){
//            LOG.info("config https server, contextPath: {}, port: {}", contextPath, port);
//            container.setContextPath(contextPath);
//            Ssl ssl = new Ssl();
//            ssl.setKeyStore(keyStore);
//            ssl.setKeyStorePassword(keyPassword);
//            container.setSsl(ssl);
//            container.setPort(port);
//            LOG.info("key-store:{},keyPassword:{}:", keyStore, keyPassword);
//        }
//
//    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
        env = context.getEnvironment();
    }

    public static <T>T getBean(Class<T> clzz){
        return context.getBean(clzz);
    }

    public static ApplicationContext getContext(){
        return context;
    }

    public static String getVersion(){
        return VERSION;
    }


    public static void main(String[] args) {
        isDebugModel = Boolean.parseBoolean(System.getProperty("debugModel","true"));

        System.out.println(System.getProperties().stringPropertyNames());

        SpringApplication.run(Application.class,args);

    }

}
