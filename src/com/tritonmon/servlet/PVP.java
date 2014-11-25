package com.tritonmon.servlet;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tritonmon.util.ServletUtil;

@Path("/pvp")
@Produces(MediaType.APPLICATION_JSON)
public class PVP {

	// username1 is ALWAYS challenger, username2 is always the challenged
	
	
	// initiates battle
	@POST
	@Path("/begin/{username1}/{username2}/{pokemonId1}/{pokemonId2}")
	public Response begin(@PathParam("username1") String username1, @PathParam("username2") String username2, 
			@PathParam("pokemonId1") String pokemonId1, @PathParam("pokemonId2") String pokemonId2) {
		String query = "INSERT INTO pvp (challenger, challenged, pokemonId1, pokemonId2) VALUES("+ServletUtil.decodeWrap(username1)+","
			+ ServletUtil.wrapInString(username2)+pokemonId1+","+pokemonId2+");";
		return ServletUtil.buildResponse(query);
	}
	
	// username does that move
//	@POST
//	@Path("/domove/{username}/{moveId}")
	
	
}
