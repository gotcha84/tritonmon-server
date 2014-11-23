package com.tritonmon.servlet;

import java.sql.ResultSet;
import java.sql.SQLException;
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


// should change e path to users imo
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public class Users {
	
	// get user
	@GET
	@Path("/getuser/{username}")
	public String login(@PathParam("username") String username) {
		String query = "SELECT * FROM users WHERE username="+ServletUtil.decodeWrap(username)+";";
		return ServletUtil.getJSON(query);
	}
	
	// adds new user
	@POST
	@Path("/adduser/{username}/{password}/{hometown}")
	public String addNewUser(@PathParam("username") String username, @PathParam("password") String password, 
			@PathParam("hometown") String hometown) {
		String query = "INSERT INTO users (username, password, hometown) VALUES("+ServletUtil.decodeWrap(username)+","
			+ ServletUtil.wrapInString(password)+","+ServletUtil.decodeWrap(hometown)+");";
		return ServletUtil.buildUserResponse(ServletUtil.decodeWrap(username), query);
	}
	
	// toggles availability state
	@POST
	@Path("/toggleavailable/{available}/{username}")
	public Response toggleAvailable(@PathParam("available") String available, @PathParam("username") String username) {
		String val = available.equals("true") ? "1" : "0";
		String query = "UPDATE users SET available_for_pvp="+val+" WHERE username="+ServletUtil.decodeWrap(username)+";";
		
		return ServletUtil.buildResponse(query);
	}
	
	@GET
	@Path("/getavailableforpvp")
	public String getAvailableForPvpUserInfo() {
		String query = "SELECT * FROM users WHERE available_for_pvp=1;";
		return ServletUtil.getJSON(query);
//		return parseAvailableForPvp(query);
	}
	
	@GET
	@Path("/getbestpokemoninfo/{username}")
	public String getBestPokemonInfo(@PathParam("username") String username) {
		String query = "SELECT * FROM users_pokemon WHERE username="+ServletUtil.decodeWrap(username)+ " ORDER BY level DESC LIMIT 6;";
		return ServletUtil.getJSON(query);
	}
	
	
}
