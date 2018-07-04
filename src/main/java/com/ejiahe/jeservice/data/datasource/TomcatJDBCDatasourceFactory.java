package com.ejiahe.jeservice.data.datasource;

import com.ejiahe.jeservice.data.util.DataSourceConfig;
import com.ejiahe.jeservice.util.JEConstants;

import javax.sql.DataSource;

/**
 * Created by mjf on 2016/10/18.
 */
public class TomcatJDBCDatasourceFactory implements DataSourceFactory {
    @Override
    public DataSource create(DataSourceConfig config) throws Exception {

        org.apache.tomcat.jdbc.pool.PoolProperties p = new org.apache.tomcat.jdbc.pool.PoolProperties();
        p.setUrl(config.getUrl());
        p.setDriverClassName(config.getDriverClassName());
        p.setUsername(config.getUsername());
        p.setPassword(config.getDecryptedPassword());
        p.setJmxEnabled(true);
        p.setTestWhileIdle(false);
        p.setTestOnBorrow(config.isTestBeforeUse());
        p.setValidationQuery(config.getTestSQL());
        p.setTestOnReturn(config.isTestAfterUse());
        p.setValidationInterval(config.getIdleConnectionTestPeriod() * JEConstants.SECOND);
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMaxActive(config.getMaxConnections());
        p.setInitialSize(config.getMinConnections());
        p.setMaxWait(config.getConnectionTimeout());
        p.setRemoveAbandonedTimeout(60);
        p.setMinEvictableIdleTimeMillis((int)(config.getMaxIdleTime() * JEConstants.SECOND));
        p.setMinIdle(10);
        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
        p.setJdbcInterceptors(
                "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
                        "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        org.apache.tomcat.jdbc.pool.DataSource datasource = new org.apache.tomcat.jdbc.pool.DataSource();
        datasource.setPoolProperties(p);

        return datasource;
    }
}
