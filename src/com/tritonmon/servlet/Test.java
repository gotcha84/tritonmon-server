package com.tritonmon.servlet;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public class Test {
	
	@GET
	@Path("/hello")
	@Produces(MediaType.APPLICATION_JSON)
	public String test() {
		return "{test: test}";
	}
	
}
