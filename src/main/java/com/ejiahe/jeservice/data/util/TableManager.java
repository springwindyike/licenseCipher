package com.ejiahe.jeservice.data.util;

import com.ejiahe.jeservice.data.datasource.JEDataSource;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.inject.Named;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Named
public class TableManager {
	private static final Logger Log = LoggerFactory.getLogger(TableManager.class);
	
	/**
	 * 分表情况下，表名和分表因子之间的分隔符
	 */
	private static final String tableNameSeparator = "_";
	
	private TableDao tableDao;
	
	/**
	 * 表名缓存是否已经被加载
	 */
	private boolean isCacheLoaded = false;
	
	/**
	 * 系统中所有表名缓存
	 */
	private Set<String> tableNames;

	private JEDataSource dataSource;

	private ResourceLoader resourceLoader;
	
	public TableManager(JEDataSource dataSource, ResourceLoader resourceLoader){
		this.dataSource = dataSource;
		this.resourceLoader = resourceLoader;
		tableDao = new TableDao();
	}
	

	/**
	 * 得到所有表名，不会返回Null
	 * @return 系统内所有的分表名
	 */
	private Collection<String> getAllTableName(){
		if (!this.isCacheLoaded){
			//如果还没有加载缓存，先从数据库加载缓存
			
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				this.tableNames = new CopyOnWriteArraySet<String>(tableDao.loadAll(conn));
				this.isCacheLoaded = true;
			} catch (SQLException e) {
				Log.error(e.getMessage(), e);
				this.tableNames = Collections.emptySet();
				
			} finally {
				dataSource.closeConnection(conn);
			}
			
		}
		
		return this.tableNames;
	}
	
	/**
	 * 某个分表是否存在
	 * @param tableName
	 * @return 存在返回true 否则返回false
	 */
	public boolean isTableExisted(String tableName){
		return getAllTableName().contains(tableName);
	}
	
	/**
	 * 在分表的存储的情况下，根据源表名和分表因子，得到分表名
	 * @param factor 分表因子
	 * @param srcTableName 源表名
	 * @return
	 */
	public String getTableName(String factor, String srcTableName, String tenementId){
		if (factor == null){
			factor = "";
		}
		
		if (tenementId == null){
			tenementId = "";
		}
		
		//得到分表名
//		String tableName = srcTableName + tableNameSeparator + factor;
		String tableName = srcTableName;
		if(!Strings.isNullOrEmpty(factor)){
			tableName = tableName + tableNameSeparator + factor;
		}
		
		
		if (!isTableExisted(tableName)){
			//如果数据库表上不存在，创建表
			createTable(srcTableName, tableName, tenementId);
			
		}
		
		return tableName;
		
	}
	
	
	/**
	 * 根据源表名得到所有的分表
	 * @param srcTableName
	 * @return
	 */
	public Collection<String> getMultipleTablNames(String srcTableName){
		//得到系统中所有表名
		Collection<String> allTableNames = getAllTableName();
		Collection<String> multipleTableNames = new ArrayList<String>();
		
		//遍历系统中所有表名
		for (String tableName : allTableNames){
			if (isMultipleTableName(srcTableName, tableName)){
				//如果是当前源表的分表，则加入
				multipleTableNames.add(tableName);
			}
		}
		
		//返回所有分表名集合
		return multipleTableNames;
	}
	
	/**
	 * 在系统中创建指定表名的表
	 * @param fileName 存放创建脚本的文件名
	 * @param tableName 需要新建的表名
	 * @param tenementId 租户id
	 */
	private void createTable(String fileName, String tableName, String tenementId){
		
		String databasePath = "classpath:database/create/" + fileName + ".sql";

		//得到脚本文件
		Resource resource = resourceLoader.getResource(databasePath);

		if (resource == null || !resource.exists()){
			Log.info("fail to create table {}. script does not exist.", tableName);
			return ;
		}
		
		Connection conn = null;
		try {
			BufferedReader in = new BufferedReader(new FileReader(resource.getFile()));
			StringBuilder srcSql = new StringBuilder();
			String temp;
			//从脚本中读取创建表的sql语句
			while ((temp = in.readLine()) != null){
				srcSql.append(temp);
				srcSql.append(" ");
			}
			in.close();
			
			//将?替换成我们需要创建的分表名
			String sql = srcSql.toString().replaceFirst("\\?", tableName);
			
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.execute();
			
			//创建成功，存入数据库表
			tableDao.insert(tableName, tenementId, conn);

			//更新表名的缓存
			this.tableNames.add(tableName);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.warn("fail to create table " + tableName + ". " + e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.warn("fail to create table " + tableName + ". " + e.getMessage());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.warn("fail to create table " + tableName + ". " + e.getMessage());
		} finally {
			if (conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 判断一个表是否是指定源表名的分表
	 * @param srcTableName 源表名
	 * @param tableName 表名
	 * @return
	 */
	private boolean isMultipleTableName(String srcTableName, String tableName){
		int index = tableName.indexOf(tableNameSeparator);
		if (index != -1){
			return tableName.substring(0, index).equals(srcTableName);
		}
		
		return false;
		
	}
}
