package com.tritonmon.servlet;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.tritonmon.database.ResultSetParser;

import context.MyContext;

import com.google.common.collect.Lists;

import java.util.List;

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
	
	public static String parseWhereCondition(String column, String value) {
		List<String> columnList = Lists.newArrayList(column.split(","));
		List<String> valueList = Lists.newArrayList(value.split(","));
		
		// if columnList.size() != valueList.size() throw something
		// handle case where just one or zero column/value
		String condition = "";
		for (int i = 0; i < columnList.size(); i++) {
			condition += columnList.get(i)+"="+valueList.get(i)+" AND ";
		}
		
		return condition.substring(0, condition.lastIndexOf("AND"));
		
	}
}
