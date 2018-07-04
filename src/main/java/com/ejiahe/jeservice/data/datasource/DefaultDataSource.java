package com.ejiahe.jeservice.data.datasource;

import com.ejiahe.jeservice.data.util.DataSourceConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by MaiLingFeng on 2015/10/16.
 */
public class DefaultDataSource extends AbstractDataSource{

    private String username;
    private String password;
    private String url;
    private String driver;

    public DefaultDataSource(DataSourceConfig config){
        this.username = config.getUsername();
        this.password = config.getPassword();
        this.url = config.getUrl();
        this.driver = config.getDriverClassName();
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {

    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
