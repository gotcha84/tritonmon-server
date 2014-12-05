package com.tritonmon.servlet;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
	@Path("/getfacebookuser/{username}")
	public String getFacebookUser(@PathParam("username") String username) {
		String query = "SELECT * FROM users WHERE username="+ServletUtil.wrapInString(username)+" AND is_facebook=1;";
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
	@Path("/addfacebookuser/{facebook_id}")
	public String addFacebookUser(
			@PathParam("facebook_id") String facebookId) {
		String query = "INSERT INTO users ("
				+ "username"
				+ ", is_facebook"
				
				+ ") VALUES("
				
				+ ServletUtil.wrapInString(facebookId)
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
	
}
