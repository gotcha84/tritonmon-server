package com.tritonmon.servlet;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tritonmon.util.ServletUtil;


// should change e path to users imo
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public class Users {
	
	// get user
	@GET
	@Path("/getuser/{username}")
	public String getUser(@PathParam("username") String username) {
		String query = "SELECT * FROM users WHERE username="+ServletUtil.decodeWrap(username)+" AND is_facebook=0;";
		return ServletUtil.getJSON(query);
	}
	
	// get facebook user
	@GET
	@Path("/getfacebookuser/{facebook_id}")
	public String getFacebookUser(@PathParam("facebook_id") String facebookId) {
		String query = "SELECT * FROM users WHERE username="+ServletUtil.wrapInString(facebookId)+" AND is_facebook=1;";
		return ServletUtil.getJSON(query);
	}
	
	// adds new user
	@POST
	@Path("/adduser/{username}/{password}/{hometown}")
	public String addUser(
			@PathParam("username") String username, 
			@PathParam("password") String password, 
			@PathParam("hometown") String hometown) {
		String query = "INSERT INTO users ("
				+ "username"
				+ ", password"
				+ ", hometown"
				
				+ ") VALUES("
				
				+ ServletUtil.decodeWrap(username)
				+ ", " + ServletUtil.wrapInString(password) 
				+ ", " + ServletUtil.decodeWrap(hometown) 
				
				+ ");";
		
		return ServletUtil.buildUserResponse(ServletUtil.decodeWrap(username), 0, query);
	}
	
	// adds new facebook user
	@POST
	@Path("/addfacebookuser/{facebook_id}/{facebook_name}")
	public String addFacebookUser(
			@PathParam("facebook_id") String facebookId,
			@PathParam("facebook_name") String facebookName) {
		String query = "INSERT INTO users ("
				+ "username"
				+ ", is_facebook"
				+ ", facebook_name"
				
				+ ") VALUES("
				
				+ ServletUtil.wrapInString(facebookId)
				+ ", " + ServletUtil.decodeWrap(facebookName)
				+ ", 1"  
				
				+ ");";
		
		return ServletUtil.buildUserResponse(ServletUtil.wrapInString(facebookId), 1, query);
	}
	
	// get all users
	@GET
	@Path("/getallusers")
	public String getAllUsers() {
		String query = "SELECT * FROM users WHERE 1=1;";
		return ServletUtil.getJSON(query);
	}
	
	@POST
	@Path("/pokeballs/users_id={users_id}")
	public Response restockPokeballs(
			@PathParam("users_id") String usersId) {
		String query = "UPDATE users SET num_pokeballs = 10 WHERE users_id = " + usersId + ";";
		return ServletUtil.buildResponse(query);
	}
	
}
