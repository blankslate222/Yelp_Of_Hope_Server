package com.nikhil.connection;

import java.sql.*;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Pool {

	private static Connection[] conPool = null;

	private static boolean poolCreated = false;
	private static int stackCtr = -1;
	private static final int poolSize = 10;

	private static String USER = "root";
	private static String PASS = "stvp";
	private static String DB_URL = "jdbc:mysql://localhost:3306/yelp";

	public static Connection getConn() {
		Connection conn = null;
		if (!poolCreated)
			createPool();
		conn = popFromPool();
		System.out.println("Sent from getConn");
		return conn;
	}

	public static void returnConn(Connection conn) {
		// TODO Auto-generated method stub
		if (stackCtr == poolSize - 1) {
			System.out.println("Stack overflow");
			return;
		}
		stackCtr++;
		conPool[stackCtr] = conn;
		System.out.println("returned to pool");
		return;
	}

	private static Connection popFromPool() {
		Connection conn = null;
		if (stackCtr == -1) {
			System.out.println("all connections are busy");

		} else {
			conn = conPool[stackCtr];
			stackCtr--;
			System.out.println("popped from pool");
			return conn;
		}
		return null;

	}

	private static void createPool() {

		try {

			conPool = new Connection[poolSize];
			for (int i = 0; i < poolSize; i++) {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				stackCtr++;
				conPool[stackCtr] = DriverManager.getConnection(DB_URL, USER,
						PASS);

			}
			poolCreated = true;
			System.out.println("Pool created");
			return;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
