package com.ejiahe.jeservice.config;

import com.ejiahe.jeservice.data.datasource.JEDataSource;
import com.ejiahe.jeservice.data.datasource.TomcatJDBCDatasourceFactory;
import com.ejiahe.jeservice.data.script.JESchemaManager;
import com.ejiahe.jeservice.data.util.DataSourceConfig;
import com.ejiahe.jeservice.data.util.TableManager;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

//@Configuration
//@EnableTransactionManagement(proxyTargetClass = true)
//@Order(2)
public class DataSourceConfiguration implements ApplicationContextAware{

    private ApplicationContext context;

    private Environment env;

    /**
     * DataSource
     */
    @Bean(name = "dataSource")
    public JEDataSource buildDataSource(){
        JEDataSource ds = new JEDataSource();

        DataSourceConfig cfg = new DataSourceConfig();

        cfg.setUrl(env.getProperty("spring.datasource.url"));
        cfg.setUsername(env.getProperty("spring.datasource.username"));
        cfg.setPassword(env.getProperty("spring.datasource.password"));
        cfg.setDriverClassName(env.getProperty("spring.datasource.driverClassName"));

        DataSource defultDS = null;
        try {
            defultDS = new TomcatJDBCDatasourceFactory().create(cfg);
        } catch (Exception e) {
            throw new IllegalStateException("DataSource Initialize Faild",e);
        }
//        DataSource defultDS = new DefaultDataSource(cfg);
        ds.setDefaultDataSource(defultDS);
        return ds;
    }

    @Bean
    public TableManager tableManager(){
        return new TableManager(buildDataSource(), context);
    }

    @Bean
    public JESchemaManager schemaManager(){
        JESchemaManager schemaManager = new JESchemaManager(buildDataSource(),tableManager());
        try {
            schemaManager.checkSchema(env.getProperty("application.databaseKey"),
                    Integer.parseInt(env.getProperty("application.databaseVersion","-1")),context);

            return schemaManager;
        } catch (Exception e) {
            throw new IllegalStateException("DataSource Schema Execute Faild",e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
        env = context.getEnvironment();
    }

}
