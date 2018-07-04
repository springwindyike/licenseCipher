package com.ejiahe.jeservice;

import com.ejiahe.jeservice.util.JEGlobals;
import org.eclipse.jetty.http.HttpScheme;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
import org.springframework.context.annotation.Bean;

import javax.inject.Inject;
import java.io.File;

/**
 * spring boot 配置支持http和https 双协议
 * Created by liaofei on 2017/11/20.
 */
//@Configuration
public class HttpsConfiguration {
    private static Logger LOG = LoggerFactory.getLogger(HttpsConfiguration.class);

//    @Value("${server.port}")
//    private int httpPort;
    @Value("${server.context-path}")
    private String contextPath;
    @Value("${server.https.port}")
    private int httpsPort;
    @Value("${server.https.keystore}")
    private String keystore;
    @Value("${server.https.keystore-file}")
    private String keystoreFile;
    @Value("${server.https.keystore-password}")
    private String keystorePassword;
//    @Value("${server.https.keystore-alias}")
//    private String keystoreAlias;
    @Value("${server.https.keystore-type}")
    private String keystoreType;

    @Inject
    private JEGlobals jeGlobals;

    @Bean
    public EmbeddedServletContainerCustomizer servletContainerCustomizer() {
        return new EmbeddedServletContainerCustomizer() {

            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                LOG.info("EmbeddedServletContainerCustomizer customize...");
                if (container instanceof JettyEmbeddedServletContainerFactory) {
//                    customizeJetty((JettyEmbeddedServletContainerFactory) container);
                    JettyEmbeddedServletContainerFactory jettyContainer = (JettyEmbeddedServletContainerFactory) container;
                    jettyContainer.addServerCustomizers(new HttpJettyServerCustomizer());
                }
            }

//            private void customizeJetty(JettyEmbeddedServletContainerFactory container) {
//                container.addServerCustomizers(new HttpJettyServerCustomizer());
//            }
        };
    }

    public class HttpJettyServerCustomizer implements JettyServerCustomizer {
        @Override
        public void customize(Server server) {
            String fullKeystoreFile = jeGlobals.getConfigDirectory() + File.separator + keystore;
            LOG.info("httpPort:{}, contextPath:{}", "无", contextPath);
            LOG.info("httpsPort:{}, keystore:{}, fullKeystoreFile:{}, keystorePassword:{}, keystoreType:{}",
                    httpsPort, keystore, fullKeystoreFile, keystorePassword, keystoreType);
//            // HTTP
//            ServerConnector connector = new ServerConnector(server);
//            connector.setPort(httpPort);
//            LOG.info("\n*** Jetty http[{}] setting successful.{}", httpPort);

            // HTTPS
            SslContextFactory sslContextFactory = new SslContextFactory();
            //Windows下启动证书时用 keystoreFile，Linex下用fullKeystoreFile
            sslContextFactory.setKeyStorePath(fullKeystoreFile);
            sslContextFactory.setKeyStorePassword(keystorePassword);
//            sslContextFactory.setCertAlias(keystoreAlias);
//            sslContextFactory.setKeyStoreType(keystoreType);

            HttpConfiguration config = new HttpConfiguration();
            config.setSecureScheme(HttpScheme.HTTPS.asString());
            config.addCustomizer(new SecureRequestCustomizer());

            ServerConnector sslConnector = new ServerConnector(
                    server,
                    new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()),
                    new HttpConnectionFactory(config));
            sslConnector.setPort(httpsPort);
//            sslConnector.setName(contextPath);

            server.setConnectors(new Connector[]{sslConnector});//
            LOG.info("\n*** Jetty SSL[{}] setting successful.", httpsPort);
        }
    }
}
