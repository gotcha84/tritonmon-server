package com.tritonmon.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.core.Response;

import com.google.common.collect.Lists;
import com.tritonmon.context.MyContext;
import com.tritonmon.database.ResultSetParser;

public class ServletUtil {
	
	public static String getJSON(String query) {
		
		ResultSet rs = MyContext.dbConn.query(query);
		
		String result = "This string isn't being updated...";
		try {
			result = ResultSetParser.toJSONString(rs);
		} catch (SQLException e) {
			result += "SQLException<br />" + e.toString();
		}
		
		return result;
	}
	
	public static Response updateJSON(String query) {
		int rs =  MyContext.dbConn.update(query);
		if (rs != 0) {
			return Response.status(rs).entity(query).build();
		} else {
			return null;
		}
	}
	
	public static String wrapInString(String value) {
		return "\""+value+"\"";
	}
 
	public static String parseSetCondition(String column, String value) {
		return parseCondition(column, value, " , ");
	}
	
	public static String parseWhereCondition(String column, String value) {
		return parseCondition(column, value, " AND ");
	}
	
	private static String parseCondition(String column, String value, String joiner) {
		List<String> columnList = Lists.newArrayList(column.split(","));
		List<String> valueList = Lists.newArrayList(value.split(","));
		
		// if columnList.size() != valueList.size() throw something
		// handle case where just one or zero column/value
		String condition = "";
		for (int i = 0; i < columnList.size(); i++) {
			condition += columnList.get(i)+"="+valueList.get(i)+joiner;
		}
		
		return condition.substring(0, condition.lastIndexOf(joiner));
	}
	
}
