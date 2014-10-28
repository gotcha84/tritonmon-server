package context;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class MyContext implements ServletContextListener {

	public void contextInitialized(ServletContextEvent arg0) {
		sshToServer();
		initDB();
		
		System.out.println("context initialized");
	}
	
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("context destroyed");
	}

	private void sshToServer() {
		final String user = "ec2-user";
		final String password = "tritonmon";
		final String host = "ec2-54-193-111-74.us-west-1.compute.amazonaws.com";
		final int port = 22;
		
		try {
			JSch jsch = new JSch();
			Session session = jsch.getSession(user, host, port);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			System.out.println("SSH connection successful : " + user + "@" + host);
		}
		catch (JSchException e) {
			e.printStackTrace();
		}
	}
	
	private void initDB() {
		// JDBC driver name and database URL
		final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
		final String DB_URL = "jdbc:mysql://localhost:3306/tritonmon";

		// database credentials
		final String username = "tritonmon";
		final String password = "tritonmon";
		
		// register JDBC driver
		try {
			Class.forName(JDBC_DRIVER);
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			// open a connection
			conn = DriverManager.getConnection(DB_URL, username, password);
			System.out.println("database connection successful");
			
			// execute a query
			stmt = conn.createStatement();
			String query = "SELECT * FROM pokemon;";
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				System.out.println(rs.getString("name"));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				// clean up
				rs.close();
				stmt.close();
				conn.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
