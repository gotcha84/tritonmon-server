package com.tritonmon.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import com.google.common.collect.ImmutableMap;
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
	
	public static String buildUserResponse(String username, int isFacebook, String query) {
		int rs =  updateJSON(query);
		if (rs > 0) {
			String newQuery = "SELECT * FROM users WHERE username=" + username + " AND is_facebook=" + isFacebook + ";";
			return getJSON(newQuery);
		} else {
			return null;
		}
	}
	
	public static String buildUserResponse(int usersId, String query) {
		int rs =  updateJSON(query);
		if (rs > 0) {
			String newQuery = "SELECT * FROM users WHERE users_id=" + usersId + ";";
			return getJSON(newQuery);
		} else {
			return null;
		}
	}
	
	public static Map<String, String> parseMovesPps(String moves, String pps) {
		List<String> moveParts = Lists.newArrayList(moves.split(","));
		List<String> ppParts = Lists.newArrayList(pps.split(","));
		
		if (moveParts.size() != ppParts.size()) {
			return ImmutableMap.of("error", "moves list and PPs list are not same length.");
		}
		
		String columns = "";
		String values = "";
		switch (moveParts.size()) {
			case 0:
				columns = "";
				values = "";
				break;
			case 1:
				columns = "move1, pp1";
				values = moveParts.get(0) + ", " + ppParts.get(0);
				break;
			case 2:
				columns = "move1, move2, pp1, pp2";
				values = moveParts.get(0) + 
						", " + moveParts.get(1) + 
						
						", " + ppParts.get(0) + 
						", " + ppParts.get(1); 
				break;
			
			case 3:
				columns = "move1, move2, move3, pp1, pp2, pp3";
				values = moveParts.get(0) + 
						", " + moveParts.get(1) + 
						", " + moveParts.get(2) + 
						
						", " + ppParts.get(0) + 
						", " + ppParts.get(1) + 
						", " + ppParts.get(2); 
				break;
			
			case 4:
				columns = "move1, move2, move3, move4, pp1, pp2, pp3, pp4";
				values = moveParts.get(0) + 
						", " + moveParts.get(1) + 
						", " + moveParts.get(2) + 
						", " + moveParts.get(3) + 
						
						", " + ppParts.get(0) + 
						", " + ppParts.get(1) + 
						", " + ppParts.get(2) + 
						", " + ppParts.get(3);
				break;
			default:
				return ImmutableMap.of("error", "moves list has more than 4 moves.");
			
		}
		return ImmutableMap.of("columns", columns, "values", values);
	}
	
	public static String parseToList(String query, String tokenToStrip) {
		
		ResultSet rs = MyContext.dbConn.query(query);
		
		try {
			
			List<Map<String, Object>> parsed = ResultSetParser.parse(rs);
			
			if (parsed.isEmpty()) {
				return "";
			}
			else {
				String json = "[";
				int i=0;
				for (Map<String, Object> row : parsed) {
					json += "\""+row.get(tokenToStrip)+"\"";
					if (i != parsed.size()-1) {
						json += ",";
					}
					i++;
				}
				json += "]";
				return json;
			}
		} catch (SQLException e) {
			return null;
		}
	}
	
}
