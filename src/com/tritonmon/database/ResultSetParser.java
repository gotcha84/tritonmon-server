package com.tritonmon.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tritonmon.context.MyContext;

public class ResultSetParser {
	
	public static String toJSONString(ResultSet rs) throws SQLException {
		return MyContext.gson.toJson(parse(rs));
	}
	
	public static String toJSONString(List<Map<String, Object>> data) {
		return MyContext.gson.toJson(data);
	}
	
	private static List<Map<String, Object>> parse(ResultSet rs) throws SQLException {
		ResultSetMetaData metadata = rs.getMetaData();
	    int numColumns = metadata.getColumnCount();
	    
	    if (numColumns == 0) {
	    	return null;
	    }
	    
	    List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
	    
	    // add data
	    while (rs.next()) {
	    	// add row
    		Map<String, Object> row = new HashMap<String, Object>();
	    	for(int i=1; i<=numColumns; i++) { // Note: 1-based counting, not 0-based
	    		row.put(metadata.getColumnLabel(i), rs.getObject(i));
	    	}
	    	rows.add(row);
	    }
		if (rows.isEmpty()) {
			return null;
		}
		return rows;
	}
	
}
