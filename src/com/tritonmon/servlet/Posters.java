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
		String update = "INSERT INTO testing VALUES(1, \"hihi\");";
		int rs = ServletUtil.updateJSON(update);
		if (rs != 0) {
			return Response.status(rs).entity(update).build();
		} else {
			return null;
		}
	}
	
	@POST
	@Path("/insert/table={table}/value={value}")
	public Response insert(@PathParam("table") String table, @PathParam("value") String value) {
		String query = "INSERT INTO "+table+" VALUES("+value+");";
		int rs = ServletUtil.updateJSON(query);
		if (rs != 0) {
			return Response.status(rs).entity(query).build();
		} else {
			return null;
		}
	}
	
	@POST
	@Path("/update/table={table}/setcolumn={setcolumn}/setvalue={setvalue}/column={column}/"
			+ "value={value}")
	public Response update(@PathParam("table") String table, @PathParam("setcolumn") String setcolumn, 
			@PathParam("setvalue") String setvalue, @PathParam("column") String column,
			@PathParam("value") String value) {
		String query = "UPDATE "+table+" SET "+ServletUtil.parseSetCondition(setcolumn, setvalue)+" WHERE "+ 
			ServletUtil.parseWhereCondition(column, value)+";";
		int rs = ServletUtil.updateJSON(query);
		if (rs != 0) {
			return Response.status(rs).entity(query).build();
		} else {
			return null;
		}
	}
}
