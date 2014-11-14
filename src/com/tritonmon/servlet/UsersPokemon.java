package com.tritonmon.servlet;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.tritonmon.context.MyContext;
import com.tritonmon.database.ResultSetParser;
import com.tritonmon.util.ServletUtil;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
public class UsersPokemon {
	
	@GET
	@Path("/userspokemon/party/{username}")
	public String getParty(@PathParam("username") String username) {
		String query = "SELECT * FROM users_pokemon WHERE" 
				+ " username=" + ServletUtil.decodeWrap(username)
				+ " slot_num >= 0;";
		return getUsersPokemonJson(query);
	}
	
	@GET
	@Path("/userspokemon/{username}")
	public String getAll(@PathParam("username") String username) {
		String query = "SELECT * FROM users_pokemon WHERE username=" + ServletUtil.decodeWrap(username);
		return getUsersPokemonJson(query);
	}
	
	private String getUsersPokemonJson(String query) {
		ResultSet rs = MyContext.dbConn.query(query);
		
		try {
			return ResultSetParser.toJSONString(rs);
		} catch (SQLException e) {
			return null;
		}
	}
	
}
