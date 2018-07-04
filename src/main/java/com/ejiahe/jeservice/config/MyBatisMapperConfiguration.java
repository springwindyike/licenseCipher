package com.ejiahe.jeservice.config;

import com.ejiahe.jeservice.data.annotation.Mapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//@MapperScan(basePackages = {"com.ejiahe"},annotationClass = Mapper.class)
//@AutoConfigureAfter(MyBatisConfiguration.class)
public class MyBatisMapperConfiguration {

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
