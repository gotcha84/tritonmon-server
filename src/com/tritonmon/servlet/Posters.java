package com.tritonmon.servlet;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tritonmon.util.ServletUtil;

@Path("/hihi")
@Produces(MediaType.TEXT_PLAIN)
public class Posters {
	
	@POST
	public Response test2() {
		String update = "INSERT INTO testing VALUES(1, hihi)";
		ServletUtil.updateJSON(update);
		return Response.status(200).entity(update).build();
	}
}
