package com.tritonmon.servlet;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tritonmon.util.ServletUtil;

@Path("")
@Produces(MediaType.TEXT_PLAIN)
public class Posters {
	
	@POST
	@Path("/hihi")
	public Response test2() {
		String query = "INSERT INTO testing VALUES(1, \"hihi\");";
		return ServletUtil.updateJSON(query);
	}
	
	@POST
	@Path("/insert/table={table}/value={value}")
	public Response insert(@PathParam("table") String table, @PathParam("value") String value) {
		String query = "INSERT INTO "+table+" VALUES("+value+");";
		return ServletUtil.updateJSON(query);
	}
	
	@POST
	@Path("/update/table={table}/setcolumn={setcolumn}/setvalue={setvalue}/column={column}/"
			+ "value={value}")
	public Response update(@PathParam("table") String table, @PathParam("setcolumn") String setcolumn, 
			@PathParam("setvalue") String setvalue, @PathParam("column") String column,
			@PathParam("value") String value) {
		String query = "UPDATE "+table+" SET "+ServletUtil.parseSetCondition(setcolumn, setvalue)+" WHERE "+ 
			ServletUtil.parseWhereCondition(column, value)+";";
		return ServletUtil.updateJSON(query);
	}
	
	// adds new user
	@POST
	@Path("/adduser/{username}/{password}/{gender}/{hometown}")
	public Response addNewUser(@PathParam("username") String username, @PathParam("password") String password,
			@PathParam("gender") String gender, @PathParam("hometown") String hometown) {
		String query = "INSERT INTO users (username, password, gender, hometown) VALUES("+ServletUtil.wrapInString(username)+","
			+ServletUtil.wrapInString(password)+","+ServletUtil.wrapInString(gender)+","+ServletUtil.wrapInString(hometown)+");";
		return ServletUtil.updateJSON(query);
	}
	
}
