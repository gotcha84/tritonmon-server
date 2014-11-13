package com.tritonmon.servlet;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tritonmon.util.ServletUtil;

@Path("/addpokemon")
@Produces(MediaType.TEXT_PLAIN)
public class CatchingPokemon {

	// increments #poke-balls allocated to user
//	@POST
//	@Path("/{username}/{pokemon_id}")
//	public Response AddPokeBall(@PathParam("username") String username) {
//		String query = "UPDATE USERS SET num_pokeballs=num_pokeballs+1 WHERE username="+username+";";
//		return ServletUtil.updateJSON(query);
//	}
	
	@POST
	@Path("/starter/{username}/{pokemon_id}")
	public Response AddPokeBall(@PathParam("username") String username, @PathParam("pokemon_id") String pokemon_id) {
		String query = "INSERT INTO users_pokemon (username, party_id, pokemon_id, nickname, level, xp, status, move1) VALUES ("
	+ServletUtil.decodeWrap(username)+",1,"+pokemon_id+","+ServletUtil.wrapInString("Inglebert")+",5,125,0,0);";
		return ServletUtil.buildResponse(query);
	}
}
