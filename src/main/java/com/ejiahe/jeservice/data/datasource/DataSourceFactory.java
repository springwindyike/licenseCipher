package com.ejiahe.jeservice.data.datasource;

import com.ejiahe.jeservice.data.util.DataSourceConfig;

import javax.sql.DataSource;

/**
 * @author MaiLingFeng
 */
public interface DataSourceFactory {
    DataSource create(DataSourceConfig config) throws Exception;
}
