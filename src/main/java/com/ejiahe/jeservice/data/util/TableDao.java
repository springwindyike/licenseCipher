package com.ejiahe.jeservice.data.util;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;


public class TableDao {
	private static final String TABLE = "ucTable";
	
	private static final String COLUMN_TABLE_NAME = "TableName";
	private static final String COLUMN_TENEMENT_ID = "tenementId";
	private static final String COLUMN_CREATIONDATE = "creationDate";
	
	//INSERT
	private static final String INSERT = "INSERT INTO " + TABLE + "(" + COLUMN_TABLE_NAME
			+ ", " + COLUMN_TENEMENT_ID + ", " + COLUMN_CREATIONDATE + ") VALUES (?, ?, ?)";
	
	//SELECT
	private static final String SELECTALL = "SELECT " + COLUMN_TABLE_NAME + " FROM " + TABLE;
	
	private QueryRunner runner;
	
	public TableDao(){
		runner = new QueryRunner();
	}
	
	/**
	 * 查询所有表名
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public Collection<String> loadAll(Connection conn) throws SQLException{
		return runner.query(conn, SELECTALL, new ResultSetHandler<Collection<String>>(){

			@Override
			public Collection<String> handle(ResultSet result)
					throws SQLException {
				// TODO Auto-generated method stub
				Collection<String> tableNames = new ArrayList<String>();
				while (result.next()){
					tableNames.add(result.getString(COLUMN_TABLE_NAME));
				}
				
				return tableNames;
			}});
	}
	
	/**
	 * 增加一条记录
	 * @param tableName 表名
	 * @param tenementId 租户id
	 * @param conn
	 * @throws SQLException
	 */
	public void insert(String tableName, String tenementId, Connection conn) throws SQLException{
		runner.update(conn, INSERT, tableName, tenementId, System.currentTimeMillis());
	}
	
}
