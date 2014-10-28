package testservlet;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Path("/hello")
public class TestServlet {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getJSON() {
		Gson gson = new GsonBuilder().create();
		return gson.toJson("this is a string");
	}
	
}
