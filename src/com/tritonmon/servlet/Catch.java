package com.tritonmon.servlet;

import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.common.collect.Lists;
import com.tritonmon.util.ServletUtil;

@Path("/addpokemon")
@Produces(MediaType.TEXT_PLAIN)
public class Catch {
	
	@POST
	@Path("/starter/{username}/{pokemon_id}/{nickname}/{health}/moves={moves}/pps={pps}")
	public Response addStarter(
			@PathParam("username") String username, 
			@PathParam("pokemon_id") String pokemon_id, 
			@PathParam("nickname") String nickname,
			@PathParam("health") String health, 
			@PathParam("moves") String moves, 
			@PathParam("pps") String pps) {
		
		Map<String, String> columnsAndValues = ServletUtil.parseMovePps(columns, values);
		
		if (columnsAndValues.containsKey("error")) {
			return Response.status(404).entity(columnsAndValues.get("error").build());
		}
		
		String query = "INSERT INTO users_pokemon "
				+ "(username, slot_num, pokemon_id, nickname, level, xp, health" + columns + ") VALUES "
				
				+ "("+ServletUtil.decodeWrap(username)+", "
				+ "0, "
				+pokemon_id+", "
				+ServletUtil.decodeWrap(nickname)+", "
				+"5, "
				+"125, "
				+health
				+values+");";
		
		return ServletUtil.buildResponse(query);
	}
}