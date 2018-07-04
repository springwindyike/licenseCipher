package com.ejiahe.jeservice.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.Alias;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.lang.annotation.Annotation;
import java.util.Map;

//@Configuration
//@Order(3)
public class MyBatisConfiguration implements ApplicationContextAware, TransactionManagementConfigurer {

    private ApplicationContext applicationContext;

    public static final String MapperXMLResourcePath = "classpath:mapper/**/*.xml";

    private static MyBatisConfiguration instance;
    @Inject
    private DataSource dataSource;


    public MyBatisConfiguration(){
        instance = this;
    }

    public static MyBatisConfiguration getInstance(){
        return instance;
    }
    @Bean
    public SqlSessionFactory buildSqlSessionFactory(DataSource dataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);

        //do some DIY configuration
        Class<?>[] aliases = getClassesWithAnnotation(Alias.class);
        if(aliases != null){
            sessionFactory.setTypeAliases(aliases);
        }

        sessionFactory.setTypeHandlers(applicationContext.getBeansOfType(TypeHandler.class).values().toArray(new TypeHandler[]{}));

        //添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            sessionFactory.setMapperLocations(resolver.getResources(MapperXMLResourcePath));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return sessionFactory.getObject();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 根据Annotation类型检索Class
     * @param annotationType
     * @return 若不存在,则返回null
     *
     */
    public Class<?>[] getClassesWithAnnotation(Class<? extends Annotation> annotationType){
        Map<String,Object> dbBeans = getApplicationContext().getBeansWithAnnotation(annotationType);

        if(dbBeans != null && !dbBeans.isEmpty()){
            int size = dbBeans.size();
            Class<?>[] aliasesClass = new Class[size];

            int i=0;
            for(Object aliasClass : dbBeans.values()){
                aliasesClass[i] = aliasClass.getClass();
                i++;
            }

            return aliasesClass;
        }

        return null;
    }

    /**
     * 根据SuperClass类型检索Class
     * @param superType
     * @return 若不存在,则返回null
     *
     */
    public Class<?>[] getClassesWithType(Class<?> superType){
        Map<String,?> dbBeans = getApplicationContext().getBeansOfType(superType);

        if(dbBeans != null && !dbBeans.isEmpty()){
            int size = dbBeans.size();
            Class<?>[] aliasesClass = new Class[size];

            int i=0;
            for(Object aliasClass : dbBeans.values()){
                aliasesClass[i] = aliasClass.getClass();
                i++;
            }

            return aliasesClass;
        }

        return null;
    }


    public ApplicationContext getApplicationContext(){
        return this.applicationContext;
    }

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
}
