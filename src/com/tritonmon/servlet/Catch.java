package com.tritonmon.servlet;

import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tritonmon.util.ServletUtil;

@Path("/addpokemon")
@Produces(MediaType.TEXT_PLAIN)
public class Catch {
	
	@POST
	@Path("/starter/{users_id}/{pokemon_id}/{nickname}/{health}/moves={moves}/pps={pps}")
	public Response addStarter(
			@PathParam("users_id") String users_id, 
			@PathParam("pokemon_id") String pokemon_id, 
			@PathParam("nickname") String nickname,
			@PathParam("health") String health, 
			@PathParam("moves") String moves, 
			@PathParam("pps") String pps) {
		
		Map<String, String> columnsAndValues = ServletUtil.parseMovesPps(moves, pps);
		
		if (columnsAndValues.containsKey("error")) {
			return Response.status(404).entity(columnsAndValues.get("error")).build();
		}
		
		String columns = columnsAndValues.get("columns");
		String values = columnsAndValues.get("values");
		
		String query = "INSERT INTO users_pokemon "
				+ "(users_id, pokemon_id, slot_num, nickname, level, xp, health, " + columns + ") VALUES ("
				
				+users_id+", "
				+pokemon_id+", "
				+"0, "
				+ServletUtil.decodeWrap(nickname)+", "
				+"5, "
				+"125, "
				+health+", "
				+values+");";
		
		return ServletUtil.buildResponse(query);
	}
	
	@POST
	@Path("/caught/{users_id}/{pokemon_id}/{slot_num}/{nickname}/{level}/{xp}/{health}/moves={moves}/pps={pps}")
	public Response caughtPokemon(
			@PathParam("users_id") String users_id, 
			@PathParam("pokemon_id") String pokemon_id, 
			@PathParam("slot_num") String slot_num,
			@PathParam("nickname") String nickname,
			@PathParam("level") String level,
			@PathParam("xp") String xp,
			@PathParam("health") String health, 
			@PathParam("moves") String moves, 
			@PathParam("pps") String pps) {
		
		Map<String, String> columnsAndValues = ServletUtil.parseMovesPps(moves, pps);
		
		if (columnsAndValues.containsKey("error")) {
			return Response.status(404).entity(columnsAndValues.get("error")).build();
		}
		
		String columns = columnsAndValues.get("columns");
		String values = columnsAndValues.get("values");
		
//		String xp = new Integer((int) Math.pow(new Integer(level), 3)).toString();
		
		String query = "INSERT INTO users_pokemon "
				+ "(users_id, pokemon_id, slot_num, nickname, level, xp, health, " + columns + ") VALUES ("
				
				+users_id+", "
				+pokemon_id+", "
				+slot_num+", "
				+ServletUtil.decodeWrap(nickname)+", "
				+level+", "
				+xp+", "
				+health+", "
				+values+");";
		
		return ServletUtil.buildResponse(query);
	}
	
}