package com.tritonmon.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.tritonmon.database.DBConnection;

public class MyContext implements ServletContextListener {

	public static Gson gson;
	
	public static DBConnection dbConn;
	
	public void contextInitialized(ServletContextEvent arg0) {
		gson = new GsonBuilder().create();
		
//		sshToServer();
		
		dbConn = new DBConnection();
		dbConn.init();
		System.out.println("database connection opened");
	}
	
	public void contextDestroyed(ServletContextEvent arg0) {
		dbConn.shutdown();
		System.out.println("database connection closed");
	}

	@SuppressWarnings("unused")
	private void sshToServer() {
		final String user = "ec2-user";
		final String password = "tritonmon";
		final String host = "ec2-54-193-111-74.us-west-1.compute.amazonaws.com";
		final int lport = 3306;
		final int rport = 5656;
		
		try {
			JSch jsch = new JSch();
			Session session = jsch.getSession(user, host, 22);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			System.out.println("Connected");
			
			int assigned_port=session.setPortForwardingL(lport, host, rport);
			System.out.println("localhost:"+assigned_port+" -> "+host+":"+rport);
			System.out.println("Port Forwarded");
		}
		catch (JSchException e) {
			e.printStackTrace();
		}
	}
}
