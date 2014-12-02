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
public class Trading {
	
	
	// toggles availability state
	@POST
	@Path("/toggleavailable/{available}/{username}")
	public Response toggleAvailable(@PathParam("available") String available, @PathParam("username") String username) {
		String val = available.equals("true") ? "1" : "0";
		String query = "UPDATE users SET available_for_trading="+val+" WHERE username="+ServletUtil.decodeWrap(username)+";";
		
		return ServletUtil.buildResponse(query);
	}
	
	@GET
	@Path("/getavailablefortrading")
	public String getAvailableForTradingUserInfo() {
		String query = "SELECT * FROM users WHERE available_for_trading=1;";
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
	
	// username1 is ALWAYS offerer, username2 is always the lister
	
	
	// initiates username1 challenging username2
	@POST
	@Path("/trade/{username1}/{username2}")
	public Response trade(@PathParam("username1") String username1, @PathParam("username2") String username2) {
		String query = "INSERT INTO trades (offerer, lister) VALUES("+ServletUtil.decodeWrap(username1)+","
			+ ServletUtil.wrapInString(username2)+");";
		return ServletUtil.buildResponse(query);
	}
	
	// maybe dont allow this idk
	// username1 untrades username2
	@POST
	@Path("/untrade/{username1}/{username2}")
	public Response untrade(@PathParam("username1") String username1, @PathParam("username2") String username2) {
		String query = "DELETE FROM trades WHERE offerer="+ServletUtil.decodeWrap(username1)+" AND lister="
			+ ServletUtil.wrapInString(username2)+";";
		return ServletUtil.buildResponse(query);
	}
	
	// maybe get them even if declined?
	// get all not declined offerers
	@GET
	@Path("/getofferers/{username2}")
	public String getofferers(@PathParam("username2") String username2) {
		String query = "SELECT offerer FROM trades WHERE lister="+ServletUtil.decodeWrap(username2)+" AND declined=0;";
		return ServletUtil.parseToList(query, "offerer");
	}
	
	// maybe get them even if declined?
	// get all people you've lister
	@GET
	@Path("/getchallengings/{username1}")
	public String getChallengings(@PathParam("username1") String username1) {
		String query = "SELECT lister FROM trades WHERE offerer="+ServletUtil.decodeWrap(username1)+" AND declined=0;";
		return ServletUtil.parseToList(query, "lister");
	}
	
	// get all unseen offerers
	@GET
	@Path("/getunseenofferers/{username2}")
	public String getUnseenofferers(@PathParam("username2") String username2) {
		String query = "SELECT offerer FROM trades WHERE lister="+ServletUtil.decodeWrap(username2)+" AND seen_offer=0;";
		return ServletUtil.parseToList(query, "offerer");
	}
	
	// get all unseen declined trades
	@GET
	@Path("/getunseendeclinedofferers/{username1}")
	public String getUnseenDeclinedofferers(@PathParam("username1") String username1) {
		String query = "SELECT lister FROM trades WHERE offerer="+ServletUtil.decodeWrap(username1)+" AND declined=1 AND seen_decline=0;";
		return ServletUtil.parseToList(query, "lister");
	}
	
	// set a trade to seen (but not decided)
	@POST
	@Path("/setseentrade/{username1}/{username2}")
	public Response setSeenTrade(@PathParam("username1") String username1, @PathParam("username2") String username2) {
		String query = "UPDATE trades SET seen_offer=1 WHERE offerer="+ServletUtil.wrapInString(username1)
				+" AND lister=" + ServletUtil.wrapInString(username2)+";";
		return ServletUtil.buildResponse(query);
	}
	
	// set a trade to be declined (but not seen the decline yet)
	@POST
	@Path("/setdeclinetrade/{username1}/{username2}")
	public Response setDeclineTrade(@PathParam("username1") String username1, @PathParam("username2") String username2) {
		String query = "UPDATE trades SET seen_offer=1,declined=1 WHERE offerer="+ServletUtil.wrapInString(username1)
				+" AND lister=" + ServletUtil.wrapInString(username2)+";";
		return ServletUtil.buildResponse(query);
	}
	
	// sets all seen decline trades to seen decline
	@POST
	@Path("/setdeclinedtrade/{username1}")
	public Response setDeclineTrades(@PathParam("username1") String username1) {
		String query = "UPDATE trades SET seen_decline=1 WHERE offerer="+ServletUtil.wrapInString(username1)
				+" AND declined=1 AND seen_decline=0 ;";
		return ServletUtil.buildResponse(query);
	}
	
	@POST
	@Path("/removeseendeclinedtrades")
	public Response removeSeenDeclinedTrades() {
		String query = "DELETE FROM trades WHERE declined=1 and seen_decline=1;";
		return ServletUtil.buildResponse(query);
	}
}
