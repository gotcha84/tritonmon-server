package testservlet;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import context.MyContext;

@Path("/hello")
public class TestServlet {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getJSON() {
		ResultSet rs = MyContext.dbConn.query("SELECT * FROM pokemon WHERE name='charmander';");
		
		String result = "result: ";
		try {
			while(rs.next()) {
				result += rs.getString("pokemon");
			}
		} catch (SQLException e) {
			result += "why the fuck isn't this changing" + e.toString();
		}
		Gson gson = new GsonBuilder().create();
		return gson.toJson(result);
	}
	
}
