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
	
	
	// initiates username1 offering a trade to username2
	@POST
	@Path("/trade/{username1}/{pokemon1UPId}/{pokemon1PId}/{pokemon1Level}/{username2}/{pokemon2UPId}/{pokemon2PId}/{pokemon2Level}")
	public Response trade(@PathParam("username1") String username1, @PathParam("pokemon1UPId") String pokemon1UPId, 
			@PathParam("pokemon1PId") String pokemon1PId, @PathParam("pokemon1Level") String pokemon1Level, 
			@PathParam("username2") String username2, @PathParam("pokemon2UPId") String pokemon2UPId,
			@PathParam("pokemon2PId") String pokemon2PId, @PathParam("pokemon2Level") String pokemon2Level) {
		String query = "INSERT INTO trades (offerer, lister, offer_users_pokemon_id, "
				+ "lister_users_pokemon_id, offer_pokemon_id, lister_pokemon_id, offer_level, lister_level) "
				+ "VALUES("+ServletUtil.decodeWrap(username1)+","+ ServletUtil.wrapInString(username2) + "," 
				+ pokemon1UPId + "," + pokemon2UPId + "," + pokemon1PId + "," + pokemon2PId + "," + pokemon1Level + "," + pokemon2Level+");";
		return ServletUtil.buildResponse(query);
	}
	
	
	// deprecated
	// maybe dont allow this idk
	// username1 untrades username2
//	@POST
//	@Path("/untrade/{username1}/{username2}")
//	public Response untrade(@PathParam("username1") String username1, @PathParam("username2") String username2) {
//		String query = "DELETE FROM trades WHERE offerer="+ServletUtil.decodeWrap(username1)+" AND lister="
//			+ ServletUtil.wrapInString(username2)+";";
//		return ServletUtil.buildResponse(query);
//	}
	
	// maybe get them even if declined?
	// get all not declined offerers
	@GET
	@Path("/getalltradingin/{username2}")
	public String getofferers(@PathParam("username2") String username2) {
		String query = "SELECT * FROM trades WHERE lister="+ServletUtil.decodeWrap(username2)+";";
		return ServletUtil.getJSON(query);
	}
	
	
	//deprecated
	// maybe get them even if declined?
	// get all not declined offerers
//	@GET
//	@Path("/gettradingin/{username2}")
//	public String getofferers(@PathParam("username2") String username2) {
//		String query = "SELECT offerer FROM trades WHERE lister="+ServletUtil.decodeWrap(username2)+" AND declined=0;";
//		return ServletUtil.parseToList(query, "offerer");
//	}
	
	// maybe get them even if declined?
	// get all people you've offered to
	@GET
	@Path("/getofferingout/{username1}")
	public String getChallengings(@PathParam("username1") String username1) {
		String query = "SELECT lister FROM trades WHERE offerer="+ServletUtil.decodeWrap(username1)+" AND declined=0;";
		return ServletUtil.parseToList(query, "lister");
	}
	
//	deprecated
	// get all unseen offerers
//	@GET
//	@Path("/getunseentradingin/{username2}")
//	public String getUnseenofferers(@PathParam("username2") String username2) {
//		String query = "SELECT offerer FROM trades WHERE lister="+ServletUtil.decodeWrap(username2)+" AND seen_offer=0;";
//		return ServletUtil.parseToList(query, "offerer");
//	}
//	
//	deprecated
//	// get all unseen declined trades
//	@GET
//	@Path("/getunseendeclinedtradingin/{username1}")
//	public String getUnseenDeclinedofferers(@PathParam("username1") String username1) {
//		String query = "SELECT lister FROM trades WHERE offerer="+ServletUtil.decodeWrap(username1)+" AND declined=1 AND seen_decline=0;";
//		return ServletUtil.parseToList(query, "lister");
//	}

	// might need to break this up
	@POST
	@Path("/setaccepttrade/{username1}/{pokemon1}/{username2}/{pokemon2}")
	public Response acceptTrade(@PathParam("username1") String username1, @PathParam("pokemon1") String pokemon1, 
			@PathParam("username2") String username2, @PathParam("pokemon2") String pokemon2) {
		String query = "SELECT slot_num FROM users_pokemon WHERE users_pokemon_id="+pokemon1;
		String firstSlotId = ServletUtil.getJSON(query);
		query = "SELECT slot_num FROM users_pokemon WHERE users_pokemon_id="+pokemon2;

		String secondSlotId = ServletUtil.getJSON(query);
		System.out.println("first slot id: " + firstSlotId);
		System.out.println("second slot id: " + secondSlotId);
		
		query = "UPDATE users_pokemon SET username="+ServletUtil.wrapInString(username2) + ",slot_num=" + secondSlotId + " WHERE users_pokemon_id="+pokemon1+";";
		Response firstResult = ServletUtil.buildResponse(query);
		if (firstResult.getStatus() != 200 && firstResult.getStatus() != 204) {
			return firstResult;
		}	
		query = "UPDATE users_pokemon SET username="+ServletUtil.wrapInString(username1) + ",slot_num=" + firstSlotId + " WHERE users_pokemon_id="+pokemon2+";";
		Response secondResult = ServletUtil.buildResponse(query);
		if (secondResult.getStatus() != 200 && firstResult.getStatus() != 204) {
			return secondResult;
		}	
		query = "UPDATE trades SET seen_offer=1,accepted=1 WHERE offerer="+ServletUtil.wrapInString(username1)+" AND lister="+ServletUtil.wrapInString(username2) 
				+ "AND offerer_users_pokemon_id="+pokemon1+" AND lister_users_pokemon_id="+pokemon2+";";
		return ServletUtil.buildResponse(query);
				
	}
	
	// set a trade to seen (but not decided)
	@POST
	@Path("/setseentrade/{username1}/{username2}/{pokemon1}/{pokemon2}")
	public Response setSeenTrade(@PathParam("username1") String username1, @PathParam("username2") String username2,
			@PathParam("pokemon1") String pokemon1, @PathParam("pokemon2") String pokemon2) {
		String query = "UPDATE trades SET seen_offer=1,declined=1 WHERE offerer="+ServletUtil.wrapInString(username1)
				+" AND lister=" + ServletUtil.wrapInString(username2)+" AND offerer_users_pokemon_id="+pokemon1
				+" AND lister_users_pokemon_id="+pokemon2+";";
		return ServletUtil.buildResponse(query);
	}
	
	// set a trade to be declined (but not seen the decline yet)
	@POST
	@Path("/setdeclinetrade/{username1}/{username2}/{pokemon1}/{pokemon2}")
	public Response setDeclineTrade(@PathParam("username1") String username1, @PathParam("username2") String username2,
			@PathParam("pokemon1") String pokemon1, @PathParam("pokemon2") String pokemon2) {
		String query = "UPDATE trades SET seen_offer=1,declined=1 WHERE offerer="+ServletUtil.wrapInString(username1)
				+" AND lister=" + ServletUtil.wrapInString(username2)+" AND offerer_users_pokemon_id="+pokemon1
				+" AND lister_users_pokemon_id="+pokemon2+";";
		return ServletUtil.buildResponse(query);
	}
	
	@POST
	@Path("/setseendecisions/{username1}")
	public Response setSeenDecisions(@PathParam("username1") String username1) {
		String query = "UPDATE trades SET seen_decline=1 WHERE lister="+ServletUtil.wrapInString(username1)
				+" AND declined=1 AND seen_decline=0;";
		Response firstResult = ServletUtil.buildResponse(query);
		if (firstResult.getStatus() != 200 && firstResult.getStatus() != 204) {
			return firstResult;
		}
		query = "UPDATE trades SET seen_acceptance=1 WHERE lister="+ServletUtil.wrapInString(username1)
				+" AND accepted=1 AND seen_decline=0;";
		return ServletUtil.buildResponse(query);
	}
	
	@POST
	@Path("/removeseendecisions")
	public Response removeSeenDecisions() {
		String query = "DELETE FROM trades WHERE declined=1 and seen_decline=1;";
		Response firstResult = ServletUtil.buildResponse(query);
		if (firstResult.getStatus() != 200 && firstResult.getStatus() != 204) {
			return firstResult;
		}
		query = "DELETE FROM trades WHERE accepted=1 and seen_acceptance=1;";
		return ServletUtil.buildResponse(query);
	}	
	
	
	// btm 4 deprecated
	
//	// sets all seen decline trades to seen decline
//	@POST
//	@Path("/setseendeclinedtrades/{username1}")
//	public Response setSeenDeclinedTrades(@PathParam("username1") String username1) {
//		String query = "UPDATE trades SET seen_decline=1 WHERE offerer="+ServletUtil.wrapInString(username1)
//				+" AND declined=1 AND seen_decline=0 ;";
//		return ServletUtil.buildResponse(query);
//	}
//	
//	@POST
//	@Path("/removeseendeclinedtrades")
//	public Response removeSeenDeclinedTrades() {
//		String query = "DELETE FROM trades WHERE declined=1 and seen_decline=1;";
//		return ServletUtil.buildResponse(query);
//	}
//	
//	// sets all seen decline trades to seen decline
//	@POST
//	@Path("/setseenacceptedtrades/{username1}")
//	public Response setSeenAcceptedTrades(@PathParam("username1") String username1) {
//		String query = "UPDATE trades SET seen_accepted=1 WHERE offerer="+ServletUtil.wrapInString(username1)
//				+" AND accepted=1 AND seen_decline=0 ;";
//		return ServletUtil.buildResponse(query);
//	}
//	
//	@POST
//	@Path("/removeseenacceptedtrades")
//	public Response removeSeenAcceptedTrades() {
//		String query = "DELETE FROM trades WHERE accepted=1 and seen_acceptance=1;";
//		return ServletUtil.buildResponse(query);
//	}
}
