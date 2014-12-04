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
	@Path("/toggleavailable/{available}/{users_id}")
	public Response toggleAvailable(@PathParam("available") String available,
			@PathParam("users_id") String users_id) {
		String val = available.equals("true") ? "1" : "0";
		String query = "UPDATE users SET available_for_trading=" + val
				+ " WHERE users_id=" + ServletUtil.decodeWrap(users_id) + ";";

		return ServletUtil.buildResponse(query);
	}

	@GET
	@Path("/getavailablefortrading")
	public String getAvailableForTradingUserInfo() {
		String query = "SELECT * FROM users WHERE available_for_trading=1;";
		return ServletUtil.getJSON(query);
		// return parseAvailableForPvp(query);
	}

	// users_id1 is ALWAYS offerer_users_id, users_id2 is always the lister_users_id

	// initiates users_id1 offering a trade to users_id2
	@POST
	@Path("/trade/{users_id1}/{pokemon1UPId}/{pokemon1PId}/{pokemon1Level}/{users_id2}/{pokemon2UPId}/{pokemon2PId}/{pokemon2Level}")
	public Response trade(@PathParam("users_id1") String users_id1,
			@PathParam("pokemon1UPId") String pokemon1UPId,
			@PathParam("pokemon1PId") String pokemon1PId,
			@PathParam("pokemon1Level") String pokemon1Level,
			@PathParam("users_id2") String users_id2,
			@PathParam("pokemon2UPId") String pokemon2UPId,
			@PathParam("pokemon2PId") String pokemon2PId,
			@PathParam("pokemon2Level") String pokemon2Level) {
		String query = "INSERT INTO trades (offerer_users_id, lister_users_id, offer_users_pokemon_id, "
				+ "lister_users_pokemon_id, offer_pokemon_id, lister_pokemon_id, offer_level, lister_level) "
				+ "VALUES("
				+ ServletUtil.decodeWrap(users_id1)
				+ ","
				+ users_id2
				+ ","
				+ pokemon1UPId
				+ ","
				+ pokemon2UPId
				+ ","
				+ pokemon1PId
				+ ","
				+ pokemon2PId
				+ "," + pokemon1Level + "," + pokemon2Level + ");";
		return ServletUtil.buildResponse(query);
	}

	// maybe get them even if declined?
	@GET
	@Path("/getalltradingin/{users_id2}")
	public String getAllTradingIn(@PathParam("users_id2") String users_id2) {
		String query = "SELECT * FROM trades WHERE lister_users_id="
				+ ServletUtil.decodeWrap(users_id2) + ";";
		return ServletUtil.getJSON(query);
	}

	// maybe get them even if declined?
	// get all people you've offered to
	@GET
	@Path("/getalloffersout/{users_id1}")
	public String getAllOffersOut(@PathParam("users_id1") String users_id1) {
		String query = "SELECT * FROM trades WHERE offerer_users_id="
				+ ServletUtil.decodeWrap(users_id1) + ";";
		return ServletUtil.getJSON(query);
	}

	// might need to break this up
	@POST
	@Path("/setaccepttrade/{users_id1}/{pokemon1}/{users_id2}/{pokemon2}")
	public Response acceptTrade(@PathParam("users_id1") String users_id1,
			@PathParam("pokemon1") String pokemon1,
			@PathParam("users_id2") String users_id2,
			@PathParam("pokemon2") String pokemon2) {

		// shitty hacky way but too bad our demo is in a week
		String query = "SELECT slot_num FROM users_pokemon WHERE users_pokemon_id="
				+ pokemon1;
		String result = ServletUtil.getJSON(query);
		String firstSlotId = result.substring(result.indexOf(":") + 1,
				result.indexOf("}"));
		query = "SELECT slot_num FROM users_pokemon WHERE users_pokemon_id="
				+ pokemon2;
		result = ServletUtil.getJSON(query);
		String secondSlotId = result.substring(result.indexOf(":") + 1,
				result.indexOf("}"));

		query = "UPDATE users_pokemon SET users_id="
				+ users_id2 + ",slot_num="
				+ secondSlotId + " WHERE users_pokemon_id=" + pokemon1 + ";";
		Response firstResult = ServletUtil.buildResponse(query);
		if (firstResult.getStatus() != 200 && firstResult.getStatus() != 204) {
			return firstResult;
		}
		query = "UPDATE users_pokemon SET users_id="
				+ users_id1 + ",slot_num="
				+ firstSlotId + " WHERE users_pokemon_id=" + pokemon2 + ";";
		Response secondResult = ServletUtil.buildResponse(query);
		if (secondResult.getStatus() != 200 && firstResult.getStatus() != 204) {
			return secondResult;
		}
		query = "UPDATE trades SET seen_offer=1,accepted=1 WHERE offerer_users_id="
				+ users_id1 + " AND lister_users_id="
				+ users_id2
				+ "AND offer_users_pokemon_id=" + pokemon1
				+ " AND lister_users_pokemon_id=" + pokemon2 + ";";
		return ServletUtil.buildResponse(query);

	}

	// set a trade to seen (but not decided)
	@POST
	@Path("/setseentrade/{users_id1}/{users_id2}/{pokemon1}/{pokemon2}")
	public Response setSeenTrade(@PathParam("users_id1") String users_id1,
			@PathParam("users_id2") String users_id2,
			@PathParam("pokemon1") String pokemon1,
			@PathParam("pokemon2") String pokemon2) {
		String query = "UPDATE trades SET seen_offer=1,declined=1 WHERE offerer_users_id="
				+ users_id1
				+ " AND lister_users_id="
				+ users_id2
				+ " AND offer_users_pokemon_id="
				+ pokemon1
				+ " AND lister_users_pokemon_id=" + pokemon2 + ";";
		return ServletUtil.buildResponse(query);
	}

	// set a trade to be declined (but not seen the decline yet)
	@POST
	@Path("/setdeclinetrade/{users_id1}/{users_id2}/{pokemon1}/{pokemon2}")
	public Response setDeclineTrade(@PathParam("users_id1") String users_id1,
			@PathParam("users_id2") String users_id2,
			@PathParam("pokemon1") String pokemon1,
			@PathParam("pokemon2") String pokemon2) {
		String query = "UPDATE trades SET seen_offer=1,declined=1 WHERE offerer_users_id="
				+ users_id1
				+ " AND lister_users_id="
				+ users_id2
				+ " AND offer_users_pokemon_id="
				+ pokemon1
				+ " AND lister_users_pokemon_id=" + pokemon2 + ";";
		return ServletUtil.buildResponse(query);
	}

	@POST
	@Path("/setseendecisions/{users_id1}")
	public Response setSeenDecisions(@PathParam("users_id1") String users_id1) {
		String query = "UPDATE trades SET seen_decline=1 WHERE offerer_users_id="
				+ users_id1
				+ " AND declined=1 AND seen_decline=0;";
		Response firstResult = ServletUtil.buildResponse(query);
		if (firstResult.getStatus() != 200 && firstResult.getStatus() != 204) {
			return firstResult;
		}
		query = "UPDATE trades SET seen_acceptance=1 WHERE offerer_users_id="
				+ users_id1
				+ " AND accepted=1 AND seen_acceptance=0;";
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

}
