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

import jersey.repackaged.com.google.common.collect.Lists;

import com.tritonmon.context.MyContext;
import com.tritonmon.database.ResultSetParser;
import com.tritonmon.util.ServletUtil;

@Path("/userspokemon")
@Produces(MediaType.APPLICATION_JSON)
public class UsersPokemon {
	
	/**
	 * get users_pokemon currently in party given username
	 * 
	 * @param username
	 * @return
	 */
	@GET
	@Path("/users_pokemon_id={users_pokemon_id}")
	public String getUsersPokemonByUsersPokemonId(@PathParam("users_pokemon_id") String users_pokemon_id) {
		String query = "SELECT * FROM users_pokemon WHERE" 
				+ " users_pokemon_id=" + users_pokemon_id
				+ ";";
		return getUsersPokemonJson(query);
	}
	
	/**
	 * get users_pokemon currently in party given username
	 * 
	 * @deprecated use getPartyFromUsersId
	 * @param username
	 * @return
	 */
	@GET
	@Path("/party/username={username}")
	public String getPartyFromUsername(@PathParam("username") String username) {
		String query = "SELECT * FROM users_pokemon WHERE" 
				+ " username=" + ServletUtil.decodeWrap(username)
				+ " AND slot_num >= 0"
				+ ";";
		return getUsersPokemonJson(query);
	}
	
	/**
	 * get users_pokemon currently in party given users_id
	 * 
	 * @param usersId
	 * @return
	 */
	@GET
	@Path("/party/users_id={users_id}")
	public String getPartyFromUsersId(@PathParam("users_id") String usersId) {
		String query = "SELECT * FROM users_pokemon WHERE" 
				+ " users_id=" + usersId
				+ " AND slot_num >= 0"
				+ ";";
		return getUsersPokemonJson(query);
	}
	
	/**
	 * get all users_pokemon given username
	 * 
	 * @deprecated use getAllFromUsersId
	 * @param username
	 * @return
	 */
	@GET
	@Path("/username={username}")
	public String getAllFromUsername(@PathParam("username") String username) {
		String query = "SELECT * FROM users_pokemon WHERE username=" + ServletUtil.decodeWrap(username) + ";";
		return getUsersPokemonJson(query);
	}
	
	/**
	 * get all users_pokemon given users_id
	 * 
	 * @param username
	 * @return
	 */
	@GET
	@Path("/users_id={users_id}")
	public String getAllFromUsersId(@PathParam("users_id") String usersId) {
		String query = "SELECT * FROM users_pokemon WHERE users_id=" + usersId + ";";
		return getUsersPokemonJson(query);
	}
	
	@POST
	@Path("/afterbattle/{users_pokemon_id}/{pokemon_id}/{level}/{xp}/{health}/moves={moves}/pps={pps}/{username}/{numPokeballs}")
	public Response addStarter(
			@PathParam("users_pokemon_id") String users_pokemon_id, 
			@PathParam("pokemon_id") String pokemon_id, 
			@PathParam("level") String level,
			@PathParam("xp") String xp,
			@PathParam("health") String health,
			@PathParam("moves") String moves, 
			@PathParam("pps") String pps,
			@PathParam("username") String username,
			@PathParam("numPokeballs") String numPokeballs) {
		
		Map<String, String> columnsAndValues = ServletUtil.parseMovesPps(moves, pps);
		
		if (columnsAndValues.containsKey("error")) {
			return Response.status(404).entity(columnsAndValues.get("error")).build();
		}
		
		String[] moveArr = moves.split(",");
		String[] ppArr = pps.split(",");
		
		if (moveArr.length != ppArr.length) {
			return Response.status(404).entity("moves list and PPs list are not same length.").build();
		}
		if (moveArr.length > 4) {
			return Response.status(404).entity("moves list has more than 4 moves.").build();
		}
		
		String moveString = "";
		String ppString = "";
		for (int i = 0; i < moveArr.length; i++) {
			moveString+="move"+(i+1)+"="+moveArr[i]+", ";
			ppString+="pp"+(i+1)+"="+ppArr[i]+", ";
		}
		ppString = ppString.substring(0, ppString.lastIndexOf(",")) + " ";
		
		String firstQuery = "UPDATE users_pokemon SET "
				+ "pokemon_id="+pokemon_id+", "
				+ "level="+level+", "
				+ "xp="+xp+", "
				+ "health="+health+", "
				+ moveString
				+ ppString
				+ "WHERE users_pokemon_id="+users_pokemon_id
				+";";
		
		Response firstResult = ServletUtil.buildResponse(firstQuery);
		if (firstResult.getStatus() != 200) {
			return firstResult;
		}
		else {
			String secondQuery = "UPDATE users SET "
				+ "num_pokeballs="+numPokeballs+" "
				+ "WHERE username="+ServletUtil.decodeWrap(username)
				+";";
			Response secondResult = ServletUtil.buildResponse(secondQuery);
			return secondResult;
		}
	}
	
	@POST
	@Path("/updateparty/users_pokemon_id={users_pokemon_id}/slot_num={slot_num}")
	public Response updateParty(
			@PathParam("users_pokemon_id") String usersPokemonId, 
			@PathParam("slot_num") String slotNum) {
		
		List<String> idParts = Lists.newArrayList(usersPokemonId.split(","));
		List<String> slotNumParts = Lists.newArrayList(slotNum.split(","));
		
		if (idParts.size() != slotNumParts.size()) {
			String message = "users_pokemon_id has " + idParts.size() + " elements but slot_num has " + slotNumParts.size() + " elements.";
			return Response.status(404).entity(message).build();
		}
		
		for (int i=0; i<idParts.size(); i++) {
			String query = "UPDATE users_pokemon SET slot_num=" + slotNumParts.get(i) + " WHERE users_pokemon_id=" + idParts.get(i) + ";";
			Response response = ServletUtil.buildResponse(query);
			if (response.getStatus() != 200) {
				return response;
			}
		}
		
		return Response.status(200).build();
	}
	
	@POST
	@Path("/heal/users_pokemon_id={users_pokemon_id}/health={health}")
	public Response heal(
			@PathParam("users_pokemon_id") String usersPokemonId,
			@PathParam("health") String health) {
	
		List<String> idParts = Lists.newArrayList(usersPokemonId.split(","));
		List<String> healthParts = Lists.newArrayList(health.split(","));
		
		if (idParts.size() != healthParts.size()) {
			String message = "users_pokemon_id has " + idParts.size() + " elements but health has " + healthParts.size() + " elements.";
			return Response.status(404).entity(message).build();
		}
		
		for (int i=0; i<idParts.size(); i++) {
			String query = "UPDATE users_pokemon SET health = " + healthParts.get(i) + " WHERE users_pokemon_id = " + idParts.get(i) + ";";
			Response response = ServletUtil.buildResponse(query);
			if (response.getStatus() != 200) {
				return response;
			}
		}
		
		return Response.status(200).build();
	}
	
	@POST
	@Path("/evolve_pokemon/users_pokemon_id={users_pokemon_id}/pokemon_id={pokemon_id}")
	public Response evolvePokemon(
			@PathParam("users_pokemon_id") String usersPokemonId,
			@PathParam("pokemon_id") String pokemon_id) {
	
		String query = "UPDATE users_pokemon SET pokemon_id = "+pokemon_id+" WHERE users_pokemon_id = " + usersPokemonId + ";";
		return ServletUtil.buildResponse(query);
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
	
}
