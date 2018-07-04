package com.ejiahe.jeservice.util;

import java.util.Collection;

/**
 * SQL拼接器;
 * 
 * <pre>
 * 	sql.select("username")
		.select("password")
		.from("ofUser")
		.where("xx3=xx")
		.where("name=111")
		.where("xxx=111")
		.and()
		.where("count=11")
		.where("count2=111")
		.or()
		.where("temp=11")
		.where("temp2=22")
		.select("name")
		.left_outer_join("ofTenement")
 * 
 * </pre>
 * 
 * <p>输出:
 * <pre>
 * SELECT username, password, name
	FROM ofUser
	LEFT OUTER JOIN ofTenement
	WHERE (xx3=xx AND name=111 AND xxx=111) 
	AND (count=11 AND count2=111) 
	OR (temp=11 AND temp2=22)
 * 
 * </pre>
 * 
 * @author MaiJingFeng
 */
public class SQL {

	private org.apache.ibatis.jdbc.SQL sql = new org.apache.ibatis.jdbc.SQL();
	
	private int offset = -1;
	private int rows = -1;
	
	public SQL update(String table) {
		sql.UPDATE(table);
		return this;
	}

	public SQL set(String sets) {
		sql.SET(sets);
		return this;
	}

	public SQL insert_into(String tableName) {
		sql.INSERT_INTO(tableName);
		return this;
	}

	public SQL values(String columns, String values) {
		sql.VALUES(columns, values);
		return this;
	}

	public SQL select(String columns) {
		sql.SELECT(columns);
		return this;
	}

	public SQL select_distinc(String columns) {
		sql.SELECT_DISTINCT(columns);
		return this;
	}

	
	public SQL delete_from(String table) {
		sql.DELETE_FROM(table);
		return this;
	}

	public SQL from(String table) {
		sql.FROM(table);
		return this;
	}

	public SQL join(String join) {
		sql.JOIN(join);
		return this;
	}

	public SQL inner_join(String join) {
		sql.INNER_JOIN(join);
		return this;
	}
	
	
	public SQL inner_join_with_select(String childSelectSql, String asTableAlias, String on){
		
		StringBuilder b = new StringBuilder();
		b.append("(")
		.append(childSelectSql)
		.append(")");
		
		if(asTableAlias != null && !asTableAlias.isEmpty()){
			b.append(" AS " + asTableAlias);
		}
		
		if(on != null && !on.isEmpty()){
			b.append(" ON " + on);
		}
		
		sql.INNER_JOIN(b.toString());
		
		return this;
	}

	public SQL left_outer_join(String join) {
		sql.LEFT_OUTER_JOIN(join);
		return this;
	}
	
	public SQL left_outer_join_with_select(String childSelectSql, String asTableAlias, String on){
		
		StringBuilder b = new StringBuilder();
		b.append("(")
		.append(childSelectSql)
		.append(")");
		
		if(asTableAlias != null && !asTableAlias.isEmpty()){
			b.append(" AS " + asTableAlias);
		}
		
		if(on != null && !on.isEmpty()){
			b.append(" ON " + on);
		}
		
		sql.LEFT_OUTER_JOIN(b.toString());
		
		return this;
	}

	public SQL right_outer_join(String join) {
		sql.RIGHT_OUTER_JOIN(join);
		return this;
	}
	
	public SQL right_outer_join_with_select(String childSelectSql, String asTableAlias, String on){
		
		StringBuilder b = new StringBuilder();
		b.append("(")
		.append(childSelectSql)
		.append(")");
		
		if(asTableAlias != null && !asTableAlias.isEmpty()){
			b.append(" AS " + asTableAlias);
		}
		
		if(on != null && !on.isEmpty()){
			b.append(" ON " + on);
		}
		
		sql.RIGHT_OUTER_JOIN(b.toString());
		
		return this;
	}

	public SQL outer_join(String join) {
		sql.OUTER_JOIN(join);
		return this;
	}
	
	public SQL outer_join_with_select(String childSelectSql, String asTableAlias, String on){
		
		StringBuilder b = new StringBuilder();
		b.append("(")
		.append(childSelectSql)
		.append(")");
		
		if(asTableAlias != null && !asTableAlias.isEmpty()){
			b.append(" AS " + asTableAlias);
		}
		
		if(on != null && !on.isEmpty()){
			b.append(" ON " + on);
		}
		
		sql.OUTER_JOIN(b.toString());
		
		return this;
	}

	public SQL or() {
		sql.OR();
		return this;
	}

	public SQL and() {
		sql.AND();
		return this;
	}

	public SQL group_by(String columns) {
		sql.GROUP_BY(columns);
		return this;
	}

	public SQL having(String conditions) {
		sql.HAVING(conditions);
		return this;
	}

	public SQL order_by(String columns) {
		sql.ORDER_BY(columns);
		return this;
	}
	
	public SQL where(String conditions) {
		sql.WHERE(conditions);
		return this;
	}
	
	public SQL where_equals(String columns, Object value){
		if(value != null){
			sql.WHERE(columns + "=" + getValue(value));
		}
		return this;
	}
	
	public SQL where_not_equals(String columns, Object value){
		if(value != null){
			sql.WHERE(columns + "!=" + getValue(value));
		}
		return this;
	}
	
	public SQL where_greater_equals(String columns, Object value){
		if(value != null){
			sql.WHERE(columns + ">=" + getValue(value));
		}
		return this;
	}
	
	public SQL where_less_equals(String columns, Object value){
		if(value != null){
			sql.WHERE(columns + "<=" + getValue(value));
		}
		return this;
	}
	
	public SQL where_greater(String columns, Object value){
		if(value != null){
			sql.WHERE(columns + ">" + getValue(value));
		}
		return this;
	}
	
	public SQL where_less(String columns, Object value){
		if(value != null){
			sql.WHERE(columns + "<" + getValue(value));
		}
		return this;
	}
	
	
	public SQL where_like(String columns, String value){
		if(value != null && !"".equals(value)){
			sql.WHERE(columns + " LIKE " + getValue(value.replace("%", "/%%") + "%"));
		}
		return this;
	}
	
	
	public SQL where_like_full(String columns, String value){
		if(value != null && !"".equals(value)){
			sql.WHERE(columns + " LIKE " + getValue("%" + value.replace("%", "/%%")+ "%"));
		}
		return this;
	}
	
	
	public SQL where_not_in(String columns, Collection<?> values){
		if(values != null && !values.isEmpty()){
			
			StringBuilder builder = new StringBuilder();
			builder.append(columns);
			builder.append(" NOT IN ");
			builder.append("(");
			int i=0;
			for(Object value: values){
				if(i != 0){
					builder.append(",");
				}
				builder.append(getValue(value));
				i++;
			}
			builder.append(")");
			
			sql.WHERE(builder.toString());
		}
		
		return this;
	}
	
	public SQL where_in(String columns, Collection<?> values){
		if(values != null && !values.isEmpty()){
			
			StringBuilder builder = new StringBuilder();
			builder.append(columns);
			builder.append(" IN ");
			builder.append("(");
			int i=0;
			for(Object value: values){
				if(i != 0){
					builder.append(",");
				}
				builder.append(getValue(value));
				i++;
			}
			builder.append(")");
			
			sql.WHERE(builder.toString());
		}
		
		return this;
	}
	
	public SQL where_between(String columns, Object leftValue, Object rightValue){
		if(leftValue != null && rightValue != null){
			StringBuilder builder = new StringBuilder();
			builder.append(columns);
			builder.append(" BETWEEN ");
			builder.append(getValue(leftValue));
			builder.append(" AND ");
			builder.append(getValue(rightValue));
			sql.WHERE(builder.toString());
		}
		
		return this;
	}
	
	
	public SQL limit(int offset, int rows){
		this.offset = offset;
		this.rows = rows;
		return this;
	}
	
	
	private String getValue(Object value){
		String realValue;
		if(value instanceof String){
			realValue = "'" + StrUtils.escapeSql((String)value) + "'";
		}else{
			realValue = value.toString();
		}
		
		return realValue;
	}
	

	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(sql.toString());
		
		if(this.rows > 0){
			b.append(" LIMIT ");
			b.append(offset);
			b.append(",");
			b.append(rows);
		}
		
		return b.toString();
	}
	
	public String toSQL(){
		return toString();
	}
	
	
	public String toSQLWithoutLimit(){
		return sql.toString();
	}
	
	
	
}
