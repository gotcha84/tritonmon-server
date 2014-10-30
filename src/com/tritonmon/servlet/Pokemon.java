package com.tritonmon.servlet;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
public class Pokemon {

	@GET
	@Path("/hello")
	public String test() {
		return "test";
	}
	
	@GET
	@Path("/table={table}")
	public String getAll(@PathParam("table") String table) {
		String query = "SELECT * FROM "+table+";";
		return ServletUtil.getJSON(query);
	}
	
	@GET
	@Path("/table={table}/attribute={attribute}")
	public String getAttribute(@PathParam("table") String table, @PathParam("attribute") String attribute) {
		String query = "SELECT "+attribute+" FROM "+table+";";
		return ServletUtil.getJSON(query);
	}
	
	@GET
	@Path("/table={table}/column={column}/value={value}")
	public String getByColumnValue(@PathParam("table") String table, @PathParam("column") String column, 
			@PathParam("value") String value) {
		String query = "SELECT * FROM "+table+" WHERE "+ServletUtil.parseWhereCondition(column, value);
		return ServletUtil.getJSON(query);
	}
	
	@GET
	@Path("/table={table}/attribute={attribute}/column={column}/value={value}")
	public String getAttributeByColumnValue(@PathParam("table") String table, @PathParam("attribute") String attribute, 
			@PathParam("column") String column, @PathParam("value") String value) {
		String query = "SELECT "+attribute+" FROM "+table+" WHERE "+ServletUtil.parseWhereCondition(column, value);
		return ServletUtil.getJSON(query);
	}
	
}
