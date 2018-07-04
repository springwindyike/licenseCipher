package com.ejiahe.jeservice.data.annotation;

import com.ejiahe.jeservice.data.datasource.DataSourceCategory;
import com.ejiahe.jeservice.data.datasource.JEDataSource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * 数据源切面;
 * 该切面拦截@TargetDataSource标注的方法,并将上下文数据源设置为指定数据源;
 *
 *
 * @author MaiJingFeng
 */
@Component
@Aspect
public class DataSourceAspect {

    private static Logger LOG = LoggerFactory.getLogger(DataSourceAspect.class);

    @Inject
    JEDataSource eimDS;

    @Around("targetDS(classTargetDS,targetDS)")
    public Object around(ProceedingJoinPoint jp, TargetDataSource classTargetDS, TargetDataSource targetDS) throws Throwable{
        setCurrentDataSource(targetDS);

        LOG.info("DataSourceAspect: " + targetDS + " - "+ jp.toShortString());

        try{
            return jp.proceed();
        } finally {
            setCurrentDataSource(null);
        }
    }

    @Pointcut("@target(classTargetDS) && @annotation(targetDS)")
    private void targetDS(TargetDataSource classTargetDS, TargetDataSource targetDS){
        //ignore;
    }

    public void setCurrentDataSource(TargetDataSource targetDS){
        if(targetDS != null){
            DataSourceCategory category = targetDS.category();
            if(category == DataSourceCategory.special){
                eimDS.setCurrentLookupKey(targetDS.name());
            }else{
                eimDS.setCurrentLookupKey(category);
            }
        }else{
            eimDS.setCurrentLookupKey(null);
        }
    }

}

