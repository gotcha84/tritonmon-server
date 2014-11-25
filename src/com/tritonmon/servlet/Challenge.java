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
	
	// Deprecated. dont call this. will work on removing from app
//	@GET
//	@Path("/getbestpokemoninfo/{username}")
//	public String getBestPokemonInfo(@PathParam("username") String username) {
//		String query = "SELECT * FROM users_pokemon WHERE username="+ServletUtil.decodeWrap(username)+ " ORDER BY level DESC LIMIT 6;";
//		return ServletUtil.getJSON(query);
//	}
		
	// nvm this is a duplicate
//	@GET
//	@Path("/getallpokemoninfo/{username}")
//	public String getAllPokemonInfo(@PathParam("username") String username) {
//		String query = "SELECT * FROM users_pokemon WHERE username="+ServletUtil.decodeWrap(username)+";";
//		return ServletUtil.getJSON(query);
//	}
	
	// username1 is ALWAYS challenger, username2 is always the challenged
	
	
	// initiates username1 challenging username2
	@POST
	@Path("/challenge/{username1}/{username2}")
	public Response challenge(@PathParam("username1") String username1, @PathParam("username2") String username2) {
		String query = "INSERT INTO challenges (challenger, challenged) VALUES("+ServletUtil.decodeWrap(username1)+","
			+ ServletUtil.wrapInString(username2)+");";
		return ServletUtil.buildResponse(query);
	}
	
	// maybe dont allow this idk
	// username1 unchallenges username2
	@POST
	@Path("/unchallenge/{username1}/{username2}")
	public Response unchallenge(@PathParam("username1") String username1, @PathParam("username2") String username2) {
		String query = "DELETE FROM challenges WHERE challenger="+ServletUtil.decodeWrap(username1)+" AND challenged="
			+ ServletUtil.wrapInString(username2)+";";
		return ServletUtil.buildResponse(query);
	}
	
	// maybe get them even if declined?
	// get all not declined challengers
	@GET
	@Path("/getchallengers/{username2}")
	public String getChallengers(@PathParam("username2") String username2) {
		String query = "SELECT challenger FROM challenges WHERE challenged="+ServletUtil.decodeWrap(username2)+" AND declined=0;";
		return ServletUtil.parseToList(query, "challenger");
	}
	
	// maybe get them even if declined?
	// get all people you've challenged
	@GET
	@Path("/getchallengings/{username1}")
	public String getChallengings(@PathParam("username1") String username1) {
		String query = "SELECT challenged FROM challenges WHERE challenger="+ServletUtil.decodeWrap(username1)+" AND declined=0;";
		return ServletUtil.parseToList(query, "challenged");
	}
	
	// get all unseen challengers
	@GET
	@Path("/getunseenchallengers/{username2}")
	public String getUnseenChallengers(@PathParam("username2") String username2) {
		String query = "SELECT challenger FROM challenges WHERE challenged="+ServletUtil.decodeWrap(username2)+" AND seen_challenge=0;";
		return ServletUtil.parseToList(query, "challenger");
	}
	
	// get all unseen declined challenges
	@GET
	@Path("/getunseendeclinedchallengers/{username1}")
	public String getUnseenDeclinedChallengers(@PathParam("username1") String username1) {
		String query = "SELECT challenged FROM challenges WHERE challenger="+ServletUtil.decodeWrap(username1)+" AND declined=1 AND seen_decline=0;";
		return ServletUtil.parseToList(query, "challenged");
	}
	
	// set a challenge to seen (but not decided)
	@POST
	@Path("/setseenchallenge/{username1}/{username2}")
	public Response setSeenChallenge(@PathParam("username1") String username1, @PathParam("username2") String username2) {
		String query = "UPDATE challenges SET seen_challenge=1 WHERE challenger="+ServletUtil.wrapInString(username1)
				+" AND challenged=" + ServletUtil.wrapInString(username2)+";";
		return ServletUtil.buildResponse(query);
	}
	
	// set a challenge to be declined (but not seen the decline yet)
	@POST
	@Path("/setdeclinechallenge/{username1}/{username2}")
	public Response setDeclineChallenge(@PathParam("username1") String username1, @PathParam("username2") String username2) {
		String query = "UPDATE challenges SET seen_challenge=1,declined=1 WHERE challenger="+ServletUtil.wrapInString(username1)
				+" AND challenged=" + ServletUtil.wrapInString(username2)+";";
		return ServletUtil.buildResponse(query);
	}
	
	// sets all seen decline challenges to seen decline
	@POST
	@Path("/setdeclinedchallenge/{username1}")
	public Response setDeclineChallenges(@PathParam("username1") String username1) {
		String query = "UPDATE challenges SET seen_decline=1 WHERE challenger="+ServletUtil.wrapInString(username1)
				+" AND declined=1 AND seen_decline=0 ;";
		return ServletUtil.buildResponse(query);
	}
	
	@POST
	@Path("/removeseendeclinedchallenges")
	public Response removeSeenDeclinedChallenges() {
		String query = "DELETE FROM challenges WHERE declined=1 and seen_decline=1;";
		return ServletUtil.buildResponse(query);
	}
}
