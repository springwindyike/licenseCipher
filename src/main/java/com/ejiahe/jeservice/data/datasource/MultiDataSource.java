package com.ejiahe.jeservice.data.datasource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author MaiLingFeng
 */
public class MultiDataSource extends AbstractDataSource{

    private List<DataSource> dataSources = new ArrayList<DataSource>();

    private AtomicInteger currentIdx = new AtomicInteger(0);

    private int count = 0;

    public MultiDataSource(List<DataSource> dataSources){
        this.dataSources.addAll(dataSources);
        count = dataSources.size();
    }

    @Override
    public Connection getConnection() throws SQLException {
        if(dataSources.isEmpty()){
            throw new SQLException("The DataSource is not initialized...");
        }

        int index = currentIdx.incrementAndGet();
        if(index > count){
            currentIdx.set(0);
            return dataSources.get(count -1).getConnection();
        }else{
            return dataSources.get(index-1).getConnection();
        }
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        throw new UnsupportedOperationException("getConnection(String username, String password)");
    }

    @Override
    public void close() {
        for(DataSource tempDS : dataSources){
            JEDataSource.tryCloseDataSource(tempDS);
        }
        dataSources.clear();
    }

}
