package com.tritonmon.servlet;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tritonmon.util.ServletUtil;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
public class Challenge {

	// adds new user
	@POST
	@Path("/challenge/{username1}/{username2}")
	public Response challenge(@PathParam("username1") String username1, @PathParam("username2") String username2) {
		String query = "INSERT INTO challenges (challenger, challenged) VALUES("+ServletUtil.decodeWrap(username1)+","
			+ ServletUtil.wrapInString(username2)+");";
		return ServletUtil.buildResponse(query);
	}
	
	// adds new user
	@POST
	@Path("/unchallenge/{username1}/{username2}")
	public Response unchallenge(@PathParam("username1") String username1, @PathParam("username2") String username2) {
		String query = "DELETE FROM challenges WHERE challenger="+ServletUtil.decodeWrap(username1)+" AND challenged="
			+ ServletUtil.wrapInString(username2)+";";
		return ServletUtil.buildResponse(query);
	}
	
	@GET
	@Path("/getchallengers/{username}")
	public String getChallengers(@PathParam("username") String username) {
		String query = "SELECT challenger FROM challenges WHERE challenged="+ServletUtil.decodeWrap(username)+";";
//		return ServletUtil.getJSON(query);
		return ServletUtil.parseToList(query, "challengers");
	}
	
	@GET
	@Path("/getchallengings/{username}")
	public String getChallengings(@PathParam("username") String username) {
		String query = "SELECT challenged FROM challenges WHERE challenger="+ServletUtil.decodeWrap(username)+";";
//		return ServletUtil.getJSON(query);
		return ServletUtil.parseToList(query, "challenged");
	}
}
