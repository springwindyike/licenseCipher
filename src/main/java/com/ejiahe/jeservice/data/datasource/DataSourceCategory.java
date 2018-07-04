package com.ejiahe.jeservice.data.datasource;

/**
 * 数据源类型;
 * 暂定划分为: 主数据源(可多个), 从数据源(可多个), 特殊数据源(可多个);
 *
 *
 * @author MaiJingFeng
 */
public enum DataSourceCategory {

    /**
     * 主数据源
     */
    master,

    /**
     * 从数据源
     */
    slave,

    /**
     * 特殊数据源
     */
    special;

}
