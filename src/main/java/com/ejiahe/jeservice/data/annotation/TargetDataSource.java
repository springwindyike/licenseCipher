package com.ejiahe.jeservice.data.annotation;

import com.ejiahe.jeservice.data.datasource.DataSourceCategory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于指定数据源;
 * 该注解需同时在Class和Method上添加后才生效;
 * 其中Class级别的注解暂时只是标识target的作用,其实际数据源以Method注解指定的为主,
 * 默认为Master数据源;
 * <p>示例代码:
 * <pre>
 * @TargetDataSource
 * public class Provider{
 * 		//当前方法使用从数据源
 *     @TargetDataSource(category=DataSourceCategory.slave)
 *     public void add(Object o){
 *     		Connection conn = null;
 *     		try{
 *      		conn = JEDataSource.getInstance().getConnection();
 *     			...
 *
 *     		}finally{
 *     			if(conn != null){
 *     				try{
 *     					conn.close();
 *     				}catch(Exception e){
 *						//ignore
 *     				}
 *     			}
 *
 *     		}
 *
 *     }
 *
 * }
 * </pre>
 * </p>
 *
 * @author MaiJingFeng
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface TargetDataSource {

    /**
     * 目标数据库类型,缺省值为master
     * @return
     *
     */
    DataSourceCategory category() default DataSourceCategory.master;

    /**
     * 数据库名,常配合DataSourceCategory=special使用
     * @return
     *
     */
    String name() default "";
}