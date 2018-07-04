package com.ejiahe.jeservice.data.datasource;


import com.ejiahe.jeservice.data.annotation.TargetDataSource;
import com.ejiahe.jeservice.util.CloseEnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.inject.Named;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * EIM数据源, 对外表现为DataSource并提供获取数据库连接的能力;
 * 支持多数据源共存,通过setCurrentLookupKey来执行当前上下文的数据源;
 *
 * <p>
 * 数据源类型:
 * <ul>
 * <li>主、从数据源: 分别有多个,不进行区分,随机分配数据源;
 * <li>特殊数据源：可有多个,通过name来进行唯一区分;
 * </p>
 *
 * <p>
 * 常配合{@link TargetDataSource}使用;
 * </p>
 * @author MaiJingFeng
 */
@Named
public class JEDataSource extends AbstractDataSource {

    private static Logger LOG = LoggerFactory.getLogger(JEDataSource.class);

    private static JEDataSource datasource;

    /**
     * 缺省数据源是否启用
     */
    private boolean lenientFallback = true;

    private Map<Object, DataSource> dataSources = new ConcurrentHashMap<Object,DataSource>();

    private DataSource defaultDataSource;

    private ThreadLocal<Object> currentLookupKey = new ThreadLocal<Object>();

    /**
     * 该初始化方法由容器调用;
     * 需要直接获取EIMDataSource请使用getInstance();
     */
    public JEDataSource(){
        datasource = this;
    }

    /**
     * 获取EIM数据源的单例对象,该方法常在使用时才调用;
     * @return
     *
     */
    public static JEDataSource getInstance(){
        return datasource;
    }

    /**
     * 设置缺省数据源;
     * 若lenientFallback=true,当未能检索到指定数据源时,将使用该数据源;
     * 必配置参数;
     * @param defaultDataSource
     *
     */
    public void setDefaultDataSource(DataSource defaultDataSource) {
        this.defaultDataSource = defaultDataSource;
    }

    /**
     * 添加数据源;
     * @param lookupKey 数据源的唯一检索,若当前已存在一样key的数据源,则将覆盖;
     * @param dataSource
     *
     */
    public void addDataSource(Object lookupKey, DataSource dataSource){
        if(lookupKey != null && dataSource != null){
            try{
                DataSource tempDS = dataSources.put(lookupKey, dataSource);
                if(tempDS != null && tempDS instanceof CloseEnable){
                    ((CloseEnable)tempDS).close();
                }

                LOG.info("JEDataSource: Add New DataSource [{}], [{}]",lookupKey, dataSource);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }


    /**
     * 设置缺省数据源是否启用;
     * @param lenientFallback
     *
     */
    public void setLenientFallback(boolean lenientFallback){
        this.lenientFallback = lenientFallback;
    }

    /**
     * 设置当前上下文的数据源;
     * <p>
     * 若想设置的为主从数据源
     * @param lookupKey
     *
     */
    public void setCurrentLookupKey(Object lookupKey){
        this.currentLookupKey.set(lookupKey);
        LOG.info("Set Current Target DataSource To [{}]" , lookupKey);
    }

    protected DataSource determineTargetDataSource() {
        Assert.notNull(this.defaultDataSource, "The Default DataSource not initialized");
        Object lookupKey = determineCurrentLookupKey();
        return getTargetDataSource(lookupKey,lenientFallback);
    }

    protected DataSource getTargetDataSource(Object lookupKey, boolean lenientFallback){
        DataSource dataSource = null;

        if(lookupKey != null){
            dataSource = this.dataSources.get(lookupKey);
        }

        if (dataSource == null && lenientFallback) {
            if(lookupKey != null){
                LOG.warn("Can not found DataSource({}), Will Return Default DataSource, \n\r{}",lookupKey,Thread.currentThread().getStackTrace());
            }
            dataSource = this.defaultDataSource;
        }

        if (dataSource == null) {
            throw new IllegalStateException("Cannot determine target DataSource for lookup key [" + lookupKey + "]");
        }
        return dataSource;
    }


    protected Object determineCurrentLookupKey(){
        return this.currentLookupKey.get();
    }

    /**
     * 获取数据源的连接;
     */
    public Connection getConnection() throws SQLException {
        return determineTargetDataSource().getConnection();
    }

    /**
     * 获取指定数据源的连接, 若指定数据源不存在,则返回缺省数据源;
     * <p>
     * 数据源的lookupKey的取值:
     * <ul>
     * <li>主数据源: {@link DataSourceCategory}.master
     * <li>从数据源: {@link DataSourceCategory}.slave
     * <li>特殊数据源: 数据源名称(字符串),例如'messageArchiveDb'
     * </ul>
     * </p>
     * @param lookupKey
     * @return
     * @throws SQLException
     *
     */
    public Connection getConnection(Object lookupKey) throws SQLException {
        return getTargetDataSource(lookupKey, true).getConnection();
    }

    /**
     * 获取指定数据源的连接
     * <p>
     * 数据源的lookupKey的取值:
     * <ul>
     * <li>主数据源: {@link DataSourceCategory}.master
     * <li>从数据源: {@link DataSourceCategory}.slave
     * <li>特殊数据源: 数据源名称(字符串),例如'messageArchiveDb'
     * </ul>
     * </p>
     * @param lookupKey 数据源的lookupKey
     * @param lenientFallback 若该值为true,则指定数据源不存在时,将使用缺省数据源;
     * @return
     *
     */
    public Connection getConnection(Object lookupKey, boolean lenientFallback) throws SQLException {
        return getTargetDataSource(lookupKey,lenientFallback).getConnection();
    }


    /**
     * 该方法不支持;
     */
    public Connection getConnection(String username, String password) throws SQLException {
        return determineTargetDataSource().getConnection(username, password);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface.isInstance(this)) {
            return (T) this;
        }
        return determineTargetDataSource().unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return (iface.isInstance(this) || determineTargetDataSource().isWrapperFor(iface));
    }


    @Override
    public void close() {

        for(DataSource tempDS : dataSources.values()){
            tryCloseDataSource(tempDS);
        }
        dataSources.clear();

        if(defaultDataSource != null){
            tryCloseDataSource(defaultDataSource);
            defaultDataSource = null;
        }

    }



    /**
     * Closes a database connection (returning the connection to the connection pool). Any
     * statements associated with the connection should be closed before calling this method.
     * This method should be called within the finally section of your database logic, as
     * in the following example:
     * <pre>
     * Connection con = null;
     * try {
     *     con = JEDataSource.getInstance().getConnection();
     *     ....
     * }
     * catch (SQLException sqle) {
     *     Log.error(sqle.getMessage(), sqle);
     * }
     * finally {
     *     JEDataSource.closeConnection(con);
     * }</pre>
     *
     * @param con the connection.
     */
    public static void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            }
            catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }


    public static void tryCloseDataSource(DataSource tempDS){
        if(tempDS != null){
            if(tempDS instanceof CloseEnable){
                ((CloseEnable)tempDS).close();
            }
        }
    }

}
