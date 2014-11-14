package com.tritonmon.servlet;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tritonmon.util.ServletUtil;

@Path("/pokeball")
@Produces(MediaType.TEXT_PLAIN)
public class Pokeball {

	// increments #poke-balls allocated to user
	@POST
	@Path("/addpokeball/{username}")
	public Response AddPokeBall(@PathParam("username") String username) {
		String query = "UPDATE USERS SET num_pokeballs=num_pokeballs+1 WHERE username="+username+";";
		return ServletUtil.buildResponse(query);
	}
	
	// decrements #poke-balls allocated to user
	@POST
	@Path("/usedpokeball/{username}")
	public Response UsedPokeball(@PathParam("username") String username) {
		String query = "UPDATE USERS SET num_pokeballs=num_pokeballs-1 WHERE username="+username+";";
		return ServletUtil.buildResponse(query);
	}
}
