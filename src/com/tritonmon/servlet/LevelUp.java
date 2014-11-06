package com.tritonmon.servlet;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tritonmon.util.ServletUtil;

@Path("/LevelUp")
@Produces(MediaType.TEXT_PLAIN)
public class LevelUp {
	
	// standard level up case (not necessarily by 1 though!)
	@POST
	@Path("/{id}/{level}/{xp}")
	public Response StandardLevelUp(@PathParam("id") String id, @PathParam("level") String level, 
			@PathParam("xp") String xp) {
		String query = "UPDATE users_pokemon SET level="+level+",xp="+xp+" WHERE id="+id+";";
		return ServletUtil.updateJSON(query);
	}

	// level up with new moves. move_id = 1,2,3,4
	@POST
	@Path("/{id}/{level}/{xp}/{move_id}/{move}")
	public Response LevelUpNewMoves(@PathParam("id") String id, @PathParam("level") String level, 
			@PathParam("xp") String xp, @PathParam("move_id") String move_id, @PathParam("move") String move) {
		String query = "UPDATE users_pokemon SET level="+level+",xp="+xp+",move"+move_id+"="+move+" WHERE id="+id+";";
		return ServletUtil.updateJSON(query);
	}
	
	// level up with evolution. 
	@POST
	@Path("/{id}/{level}/{xp}/{pokemon_id}")
	public Response LevelUpEvolution(@PathParam("id") String id, @PathParam("level") String level, 
			@PathParam("xp") String xp, @PathParam("pokemon_id") String pokemon_id) {
		String query = "UPDATE users_pokemon SET level="+level+",xp="+xp+",pokemon_id="+pokemon_id+" WHERE id="+id+";";
		return ServletUtil.updateJSON(query);
	}
	
	@Path("/{id}/{level}/{xp}/{pokemon_id}/{move_id}/{move}")
	public Response LevelUpEvolutionNewMoves(@PathParam("id") String id, @PathParam("level") String level, 
			@PathParam("xp") String xp, @PathParam("pokemon_id") String pokemon_id, @PathParam("move_id") String move_id, 
			@PathParam("move") String move) {
		String query = "UPDATE users_pokemon SET level="+level+",xp="+xp+",pokemon_id="+pokemon_id
				+",move"+move_id+"="+move+" WHERE id="+id+";";
		return ServletUtil.updateJSON(query);
	}
	
}
