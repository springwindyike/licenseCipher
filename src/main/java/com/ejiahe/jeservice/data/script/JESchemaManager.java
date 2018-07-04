/**
 * $Revision$
 * $Date$
 *
 * Copyright (C) 2005-2008 Jive Software. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ejiahe.jeservice.data.script;

import com.ejiahe.jeservice.data.datasource.JEDataSource;
import com.ejiahe.jeservice.data.util.TableManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class JESchemaManager {

	private static final Logger Log = LoggerFactory.getLogger(JESchemaManager.class);

    private static final String CHECK_VERSION =
            "SELECT version FROM ucVersion WHERE name=?";

    private JEDataSource dataSource;

    private TableManager tableManager;

    /**
     * Creates a new Schema manager.
     */
    public JESchemaManager(JEDataSource dataSource, TableManager tableManager) {
        this.dataSource = dataSource;
        this.tableManager = tableManager;
    }
    

    /**
     * Checks to see if the database needs to be upgraded. This method should be
     * called once every time the application starts up.
     *
     * @param schemaKey the database schema key (name).
     * @param requiredVersion the version that the schema should be at.
     * @param resourceLoader a resource loader that knows how to load schema files.
     * @throws Exception if an error occured.
     * @return True if the schema update was successful.
     */
    public boolean checkSchema(String schemaKey, int requiredVersion,
            ResourceLoader resourceLoader) throws Exception
    {
    	return checkSchema(schemaKey, requiredVersion, resourceLoader, false);
    }
    
    /**
     * Checks to see if the database needs to be upgraded. This method should be
     * called once every time the application starts up.
     *
     * @param schemaKey the database schema key (name).
     * @param requiredVersion the version that the schema should be at.
     * @param resourceLoader a resource loader that knows how to load schema files.
     * @param isForceRepair 是否强制修复,若该值为true,则将从-1版本升级到requiredVersion
     * @throws Exception if an error occured.
     * @return True if the schema update was successful.
     */
    private boolean checkSchema(String schemaKey, int requiredVersion,
            ResourceLoader resourceLoader, boolean isForceRepair) throws Exception
    {
        int currentVersion = -1;
        if(!isForceRepair){
        	PreparedStatement pstmt = null;
            ResultSet rs = null;
            Connection con = null;
            try {
            	con = dataSource.getConnection();
                pstmt = con.prepareStatement(CHECK_VERSION);
                pstmt.setString(1, schemaKey);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    currentVersion = rs.getInt(1);
                }
            }
            catch (SQLException sqle) {
                // The database schema must not be installed.
                Log.debug("SchemaManager: Error verifying "+schemaKey+" version, probably ignorable.", sqle);

            }
            finally {
                JEDataSource.closeConnection(con);
            }
        }
        
        // If already up to date, return.
        if (currentVersion >= requiredVersion) {
            return true;
        }
        // If the database schema isn't installed at all, we need to install and upgrade it;
        else{
        	if (currentVersion == -1) {
        		if(!installSchema(schemaKey, resourceLoader)){
        			return false;
        		}
        		
        		currentVersion++;
        	}
        	
        	return upgradeSchema(schemaKey, resourceLoader, 
        			currentVersion, requiredVersion,true);
        	
        }
        
    }
    
    /**
     * 初始化数据库
     * @param schemaKey
     * @param resourceLoader
     * @return
     */
    private boolean installSchema(String schemaKey, ResourceLoader resourceLoader){
    	Log.info("installSchema[{}]",schemaKey);

        // Resource will be like "/database/openfire_hsqldb.sql"
        String resourcePath = "classpath:database/" + schemaKey + ".sql";
        Resource resource = resourceLoader.getResource(resourcePath);
        if (resource == null) {
            return false;
        }

        InputStream input = null;
        try {
            input = resource.getInputStream();
            // For plugins, we will automatically convert jiveVersion to ofVersion
            executeSQLScript(input);
        }
        catch (Exception e) {
            Log.error(e.getMessage(), e);
            return false;
        }
        finally {
            try {
                if(input != null)
                 input.close();
            }
            catch (Exception e) {
                // Ignore.
            }
        }
        Log.info("install database schema success");
        System.out.println("install database schema success");
        
        return true;
    }
    
    /**
     * 升级数据库
     * @param schemaKey
     * @param resourceLoader
     * @param currentVersion
     * @param requiredVersion
     * @param ignoreAndSkipSqlError 是否忽略并跳过SQL错误,若为true,则当某一个SQL执行错误,依旧会执行next SQL
     * @return
     */
    private boolean upgradeSchema(String schemaKey, ResourceLoader resourceLoader,
    		int currentVersion, int requiredVersion, boolean ignoreAndSkipSqlError){
    	// The database is an old version that needs to be upgraded.
        Log.info("upgrade database: currentVersion[{}], schemaKey[{}], requiredVersion[{}]",
                Arrays.asList(currentVersion, schemaKey, requiredVersion));

        // Run all upgrade scripts until we're up to the latest schema.
        for (int i = currentVersion + 1; i <= requiredVersion; i++) {
            InputStream resource = getUpgradeResource(resourceLoader, i, schemaKey);
            
            if (resource == null) {
                continue;
            }
            try {
                executeSQLScript(resource);
            }
            catch (Exception e) {
                Log.error(e.getMessage(), e);
                return false;
            }
            finally {
                try {
                	resource.close();
                }
                catch (Exception e) {
                    // Ignore.
                }
            }
        }
        Log.info("upgrade database schema success");
        System.out.println("upgrade.database.schema.success");
        return true;
    }
    

    private InputStream getUpgradeResource(ResourceLoader resourceLoader, int upgradeVersion,
            String schemaKey)
    {
        Resource resource = null;
        // Resource will be like "/database/upgrade/6/openfire_hsqldb.sql"
        String path = "classpath:" + "database" + File.separator + "upgrade" + File.separator +
                upgradeVersion + File.separator + schemaKey + ".sql";

        resource = resourceLoader.getResource(path);
        if(resource == null){
            Log.error("upgrade database: can not found schema[{}]", path);
            return null;
        }else{
            try {
                return resource.getInputStream();
            } catch (IOException e) {
                Log.error(e.getMessage());
                return null;
            }
        }
    }


    /**
     * Executes a SQL script.
     *
     * @param resource an input stream for the script to execute.
     * @throws IOException if an IOException occurs.
     * @throws SQLException if an SQLException occurs.
     */
    private void executeSQLScript(InputStream resource) throws IOException,
            SQLException
    {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(resource,"UTF-8"));
            boolean done = false;
            while (!done) {
                StringBuilder command = new StringBuilder();
                while (true) {
                    String line = in.readLine();
                    if (line == null) {
                        done = true;
                        break;
                    }
                    // Ignore comments and blank lines.
                    if (isSQLCommandPart(line)) {
                        command.append(" ").append(line);
                    }
                    if (line.trim().endsWith(";")) {
                        break;
                    }
                }
                // Send command to database.
                if (!done && !command.toString().equals("")) {
                	if (isMultipleTableCommand(command.toString())){
                		//如果更新命令是针对有分表的
                		String srcTableName = getSrcTableNameFromCommand(command.toString());
                		Collection<String> tableNames = tableManager.getMultipleTablNames(srcTableName);
                		
                		//遍历所有分表，每个分表都执行一次更新命令
                		String cmdString = null;
                		for (String tableName : tableNames){
                			 PreparedStatement pstmt = null;
                			 Connection con = null;
     		                try {
     		                	con = dataSource.getConnection();
     		                    cmdString = getCommandWithNewTableName(
     		                    		command.toString(), tableName);

     		                    pstmt = con.prepareStatement(cmdString);
     		                    pstmt.execute();
     		                }
     		                catch (SQLException e) {
     		                    // Lets show what failed
     		                    Log.error("SchemaManager: Failed to execute SQL:\n"+cmdString,e);
     		                    
     		                    //DIY, when meet an SQLError, just skip and process next SQL;
     		                    //throw e;
     		                }
     		                finally {
     		                   dataSource.closeConnection(con);
     		                }
                		}
                	} else {
                	
		                PreparedStatement pstmt = null;
		                Connection con = null;
 		                try {
 		                	con = dataSource.getConnection();
		                    String cmdString = command.toString();

		                    pstmt = con.prepareStatement(cmdString);
		                    pstmt.execute();
		                }
		                catch (SQLException e) {
		                    // Lets show what failed
		                    Log.error("SchemaManager: Failed to execute SQL:\n"+command.toString(),e);
		                    
		                    //DIY, when meet an SQLError, just skip and process next SQL;
		                    //throw e;
		                }
		                finally {
		                	dataSource.closeConnection(con);
		                }
                	}
                }
            }
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (Exception e) {
                    Log.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * Returns true if a line from a SQL schema is a valid command part.
     *
     * @param line the line of the schema.
     * @return true if a valid command part.
     */
    private boolean isSQLCommandPart(String line) {
        line = line.trim();
        if (line.equals("")) {
            return false;
        }
        // Check to see if the line is a comment. Valid comment types:
        //   "//" is HSQLDB
        //   "--" is DB2 and Postgres
        //   "#" is MySQL
        //   "REM" is Oracle
        //   "/*" is SQLServer
        return !(line.startsWith("//") || line.startsWith("--") || line.startsWith("#") ||
                line.startsWith("REM") || line.startsWith("/*") || line.startsWith("*"));
    }
    
    /**
     * 升级命令是否是针对多表的
     * 如果升级命令是针对多表的，那么源表名会被”@“包围
     * 例如：UPDATE @ucUserMessageArchive@ SET messageArchiveId = messageId;
     * @param command 升级命令
     * @return
     */
    private boolean isMultipleTableCommand(String command){
		//正则表达式
    	String regExp = "@([^@]+)@";
    	
    	Pattern pattern = Pattern.compile(regExp);
    	Matcher matcher = pattern.matcher(command);
		
    	return matcher.find();
    }
    
    /**
     * 从多表升级命令中得到原表名
     * @param command 多表升级命令
     * @return 找到返回表名，未找到返回空字符串
     */
    private String getSrcTableNameFromCommand(String command){
    	String regExp = "@([^@]+)@";
    	
    	Pattern pattern = Pattern.compile(regExp);
    	Matcher matcher = pattern.matcher(command);
    	
    	if (matcher.find()){
    		return matcher.group(1);
    	} else {
    		return "";
    	}
    	
    }
    
    /**
     * 替换多表升级命令中的表名，得到适当的升级命令
     * @param command 多表升级命令
     * @param newTableName 新的表名
     * @return
     */
    private String getCommandWithNewTableName(String command, String newTableName){
    	String regExp = "@([^@]+)@";
    	
    	Pattern pattern = Pattern.compile(regExp);
    	Matcher matcher = pattern.matcher(command);
    	
    	return matcher.replaceFirst(newTableName);
    	
    }
}