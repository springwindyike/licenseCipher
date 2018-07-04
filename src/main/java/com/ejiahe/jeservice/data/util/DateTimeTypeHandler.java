package com.ejiahe.jeservice.data.util;

import org.apache.ibatis.type.Alias;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import javax.inject.Named;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;


/**
 * 日期(java.util.Date)与毫秒数(bigInt)的类型转换器;
 * 若日期或毫秒数不存在或为null,则以当前时间处理;
 *
 * @author MaiJingFeng
 */
@Named
@Alias("DateTimeTypeHandler")
public class DateTimeTypeHandler implements TypeHandler<Date>{

    @Override
    public void setParameter(PreparedStatement ps, int i, Date parameter, JdbcType jdbcType)
            throws SQLException {
        // TODO Auto-generated method stub
        if(parameter != null){
            ps.setLong(i, parameter.getTime());
        }else{
            ps.setLong(i, new Date().getTime());
        }
    }

    @Override
    public Date getResult(ResultSet rs, String columnName) throws SQLException {
        // TODO Auto-generated method stub
        long datatime = rs.getLong(columnName);

        Date date = null;
        try{
            date = new Date(datatime);
        }catch(Exception e){
            date = new Date();
        }

        return date;
    }

    @Override
    public Date getResult(ResultSet rs, int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        long datatime = rs.getLong(columnIndex);

        Date date = null;
        try{
            date = new Date(datatime);
        }catch(Exception e){
            date = new Date();
        }

        return date;
    }

    @Override
    public Date getResult(CallableStatement cs, int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        long datatime = cs.getLong(columnIndex);

        Date date = null;
        try{
            date = new Date(datatime);
        }catch(Exception e){
            date = new Date();
        }

        return date;
    }

}
