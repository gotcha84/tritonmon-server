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
		
		List<String> moveParts = Lists.newArrayList(moves.split(","));
		List<String> ppParts = Lists.newArrayList(pps.split(","));
		
		if (moveParts.size() != ppParts.size()) {
			return Response.status(404).entity("moves list and PPs list are not same length.").build();
		}
		
		String columns = "";
		String values = "";
		switch (moveParts.size()) {
			case 0:
				columns = "";
				values = "";
				break;
			case 1:
				columns = ", move1, pp1";
				values = ", " + moveParts.get(0) + ", " + ppParts.get(0);
				break;
			case 2:
				columns = ", move1, move2, pp1, pp2";
				values = ", " + moveParts.get(0) + 
						", " + moveParts.get(1) + 
						
						", " + ppParts.get(0) + 
						", " + ppParts.get(1); 
				break;
			
			case 3:
				columns = ", move1, move2, move3, pp1, pp2, pp3";
				values = ", " + moveParts.get(0) + 
						", " + moveParts.get(1) + 
						", " + moveParts.get(2) + 
						
						", " + ppParts.get(0) + 
						", " + ppParts.get(1) + 
						", " + ppParts.get(2); 
				break;
			
			case 4:
				columns = ", move1, move2, move3, move4, pp1, pp2, pp3, pp4";
				values = ", " + moveParts.get(0) + 
						", " + moveParts.get(1) + 
						", " + moveParts.get(2) + 
						", " + moveParts.get(3) + 
						
						", " + ppParts.get(0) + 
						", " + ppParts.get(1) + 
						", " + ppParts.get(2) + 
						", " + ppParts.get(3);
				break;
			default:
				return Response.status(404).entity("moves list has more than 4 moves.").build();
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