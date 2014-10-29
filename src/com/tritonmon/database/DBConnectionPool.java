package com.tritonmon.database;

import java.util.ArrayList;

public class DBConnectionPool {
	private ArrayList<DBConnection> mPool;
	
	DBConnectionPool() {
		mPool = new ArrayList<DBConnection>();
	}
	
	public void addConnection(DBConnection conn) {
		mPool.add(conn);
	}
	
	public DBConnection getConnection(int i) {
		return mPool.get(i);
	}
}
