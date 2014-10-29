package com.tritonmon.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
	// JDBC driver name and database URL
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	private static final String DB_URL = "jdbc:mysql://localhost:3306/tritonmon";
	
	// database credentials
	private static final String USERNAME = "tritonmon";
	private static final String PASSWORD = "tritonmon";
	
	private Connection mConnection;
	private Statement mStatement;
	
	public void init() {
		// register JDBC driver
		try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		// open a connection
		try {
			mConnection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
			mStatement = mConnection.createStatement();
			System.out.println("database connection successful");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void shutdown() {
		try {
			// clean up
			mStatement.close();
			mConnection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ResultSet query(String query){
		try {
			return mStatement.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
