package com.tritonmon.servlet;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tritonmon.util.ServletUtil;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
public class Generic {
	
	@GET
	@Path("/hello")
	public String test() {
		return "hello world";
	}
	
	// select * from table;
	@GET
	@Path("/table={table}")
	public String getAll(@PathParam("table") String table) {
		String query = "SELECT * FROM "+table+";";
		return ServletUtil.getJSON(query);
	}
	
	// select attribute from table;
	@GET
	@Path("/table={table}/attribute={attribute}")
	public String getAttribute(@PathParam("table") String table, @PathParam("attribute") String attribute) {
		String query = "SELECT "+attribute+" FROM "+table+";";
		return ServletUtil.getJSON(query);
	}
	
	// select * from table where column = value;
	@GET
	@Path("/table={table}/column={column}/value={value}")
	public String getByColumnValue(@PathParam("table") String table, @PathParam("column") String column, 
			@PathParam("value") String value) {
		String query = "SELECT * FROM "+table+" WHERE "+ServletUtil.parseWhereCondition(column, value)+";";
		return ServletUtil.getJSON(query);
	}
	
	// select attribute from table where column = value;
	@GET
	@Path("/table={table}/attribute={attribute}/column={column}/value={value}")
	public String getAttributeByColumnValue(@PathParam("table") String table, @PathParam("attribute") String attribute, 
			@PathParam("column") String column, @PathParam("value") String value) {
		String query = "SELECT "+attribute+" FROM "+table+" WHERE "+ServletUtil.parseWhereCondition(column, value)+";";
		return ServletUtil.getJSON(query);
	}
	
	// insert into table values (value);
	@POST
	@Path("/insert/table={table}/value={value}")
	public Response insert(@PathParam("table") String table, @PathParam("value") String value) {
		String query = "INSERT INTO "+table+" VALUES("+value+");";
		return ServletUtil.buildResponse(query);
	}
	
	// update table set setcolumn = setvalue where column = value;
	@POST
	@Path("/update/table={table}/setcolumn={setcolumn}/setvalue={setvalue}/column={column}/value={value}")
	public Response update(@PathParam("table") String table, @PathParam("setcolumn") String setcolumn, 
			@PathParam("setvalue") String setvalue, @PathParam("column") String column,
			@PathParam("value") String value) {
		String query = "UPDATE "+table+" SET "+ServletUtil.parseSetCondition(setcolumn, setvalue)+" WHERE "+ 
			ServletUtil.parseWhereCondition(column, value)+";";
		return ServletUtil.buildResponse(query);
	}
	
}
