package testservlet;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import com.tritonmon.database.ResultSetParser;

import context.MyContext;

@Path("/hello")
public class TestServlet {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getJSON() throws JSONException {
		String query = "SELECT * FROM pokemon;";
		ResultSet rs = MyContext.dbConn.query(query);
		
		String result = "This string isn't being updated...";
		try {
			result = ResultSetParser.toJSONString(rs);
		} catch (SQLException e) {
			result += "SQLException<br />" + e.toString();
		}
		
		return new JSONObject(result).toString(2);
	}
	
}
