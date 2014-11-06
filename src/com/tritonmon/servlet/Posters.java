package com.tritonmon.servlet;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tritonmon.util.ServletUtil;

@Path("/hihi")
@Consumes(MediaType.TEXT_PLAIN)
public class Posters {
	
	@POST
	public Response test2() {
		String query = "INSERT INTO testing VALUES(1, hihi)";
		ServletUtil.getJSON(query);
		return Response.status(200).entity(query).build();
	}
}
