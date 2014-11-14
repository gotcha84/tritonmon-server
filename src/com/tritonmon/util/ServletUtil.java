package com.tritonmon.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.core.Response;

import com.google.common.collect.Lists;
import com.tritonmon.context.MyContext;
import com.tritonmon.database.ResultSetParser;

public class ServletUtil {
	
	private static final String encoding = "UTF-8";
	
	public static String getJSON(String query) {
		
		ResultSet rs = MyContext.dbConn.query(query);
		
		try {
			return ResultSetParser.toJSONString(rs);
		} catch (SQLException e) {
			return null;
		}
	}
	
	public static int updateJSON(String query) {
		return MyContext.dbConn.update(query);
	}
	
	public static String wrapInString(String value) {
		return "\""+value+"\"";
	}
	
	public static String decodeString(String value) {
		try {
			return URLDecoder.decode(value, encoding);
		} catch (UnsupportedEncodingException e) {
			return "Server: Unsupported encoding " + encoding;
		}
	}
	
	public static String decodeWrap(String value) {
		return wrapInString(decodeString(value));
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
			condition += decodeString(columnList.get(i))+"="+decodeString(valueList.get(i))+joiner;
		}
		
		return condition.substring(0, condition.lastIndexOf(joiner));
	}
	
	public static Response buildResponse(String query) {
		int rs =  updateJSON(query);
		if (rs > 0) {
			return Response.status(200).entity(query).build();
		} else if (rs == 0) {
			return Response.status(204).entity(query).build();
		} else {
			return Response.status(500).entity(query).build();
		}
	}
	
	public static String buildUserResponse(String username, String query) {
		int rs =  updateJSON(query);
		if (rs > 0) {
			String newQuery = "SELECT * FROM users WHERE username="+username+";";
			return getJSON(newQuery);
		} else {
			return null;
		}
	}
	
}
