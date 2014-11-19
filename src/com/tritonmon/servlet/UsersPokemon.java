package com.tritonmon.servlet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tritonmon.context.MyContext;
import com.tritonmon.database.ResultSetParser;
import com.tritonmon.util.ServletUtil;

@Path("/userspokemon")
@Produces(MediaType.APPLICATION_JSON)
public class UsersPokemon {
	
	// get users_pokemon currently in party
	@GET
	@Path("/party/{username}")
	public String getParty(@PathParam("username") String username) {
		String query = "SELECT * FROM users_pokemon WHERE" 
				+ " username=" + ServletUtil.decodeWrap(username)
				+ " slot_num >= 0;";
		return getUsersPokemonJson(query);
	}
	
	// get all users_pokemon
	@GET
	@Path("/{username}")
	public String getAll(@PathParam("username") String username) {
		String query = "SELECT * FROM users_pokemon WHERE username=" + ServletUtil.decodeWrap(username);
		return getUsersPokemonJson(query);
	}
	
	private String getUsersPokemonJson(String query) {
		ResultSet rs = MyContext.dbConn.query(query);
		
		List<Map<String, Object>> parsed = new ArrayList<Map<String,Object>>();
		try {
			parsed = ResultSetParser.parse(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (parsed.isEmpty()) {
			return null;
		} else {
			for (Map<String, Object> row : parsed) {
				List<Object> moves = new ArrayList<Object>();
				if (row.containsKey("move1")) {
					moves.add(0, row.get("move1"));
					row.remove("move1");
				}
				if (row.containsKey("move2")) {
					moves.add(1, row.get("move2"));
					row.remove("move2");
				}
				if (row.containsKey("move3")) {
					moves.add(2, row.get("move3"));
					row.remove("move3");
				}
				if (row.containsKey("move4")) {
					moves.add(3, row.get("move4"));
					row.remove("move4");
				}
				row.put("moves", moves);
				
				List<Object> pps = new ArrayList<Object>();
				if (row.containsKey("pp1")) {
					pps.add(0, row.get("pp1"));
					row.remove("pp1");
				}
				if (row.containsKey("pp2")) {
					pps.add(1, row.get("pp2"));
					row.remove("pp2");
				}
				if (row.containsKey("pp3")) {
					pps.add(2, row.get("pp3"));
					row.remove("pp3");
				}
				if (row.containsKey("pp4")) {
					pps.add(3, row.get("pp4"));
					row.remove("pp4");
				}
				row.put("pps", pps);
			}
			return MyContext.gson.toJson(parsed);
		}
	}
	
	@POST
	@Path("/afterbattle/{users_pokemon_id}/{pokemon_id}/{level}/{xp}/{health}/moves={moves}/pps={pps}")
	public Response addStarter(
			@PathParam("users_pokemon_id") String users_pokemon_id, 
			@PathParam("pokemon_id") String pokemon_id, 
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
		
		String query = "INSERT INTO users_pokemon "
				+ "(users_pokemon_id, pokemon_id, level, xp, health" + columns + ") VALUES "
				
				+ "("+ServletUtil.decodeWrap(users_pokemon_id)+", "
				+pokemon_id+", "
				+ServletUtil.decodeWrap(level)+", "
				+health
				+values+");";
		
		return ServletUtil.buildResponse(query);
		
		
	}
	
}
