package model;

import java.sql.*;

public class Database {

	Connection conn;

	public void createResturantTables() throws Exception {
		if (conn == null) {
			System.out.println("Error, you are connected already to a database");
			throw new Exception("No connection, Connect to a database first");
		}
		String[] commands = new String[5];
		commands[0] = "CREATE TABLE IF NOT EXISTS dish (did INTEGER PRIMARY KEY,"
				+ " name VARCHAR NOT NULL, discription VARCHAR, price DOUBLE, rate INTEGER,"
				+ " rate_count INTEGER, time_to_prepare_in_minutes integer," + " is_available BOOLEAN, image BLOB);";
		commands[1] = "CREATE TABLE IF NOT EXISTS cook (cid INTEGER PRIMARY KEY,"
				+ " first_name VARCHAR NOT NULL, last_name VARCHAR NOT NULL,  is_active BOOLEAN);";
		commands[2] = "CREATE TABLE IF NOT EXISTS tables (tid INTEGER PRIMARY KEY);";
		commands[3] = "CREATE TABLE IF NOT EXISTS orders (oid INTEGER PRIMARY KEY,"
				+ "did INTEGER, cid INTEGER, tid INTEGER, total_price DOUBLE, "
				+ "state VARCHAR, order_timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, "
				+ "CONSTRAINT fk_dish FOREIGN KEY (did) REFERENCES dish(did),"
				+ "CONSTRAINT fk_cook FOREIGN KEY (cid) REFERENCES cook(cid),"
				+ "CONSTRAINT fk_dish FOREIGN KEY (tid) REFERENCES tables(tid));";

		commands[4] = "CREATE TABLE IF NOT EXISTS order_dishes (oid INTEGER, did INTEGER,"
				+ "CONSTRAINT fk_order FOREIGN KEY (oid) REFERENCES orders(oid),"
				+ "CONSTRAINT fk_dish FOREIGN KEY (did) REFERENCES dish(did)," + "PRIMARY KEY (oid, did));";

		Statement stmt;
		try {
			stmt = conn.createStatement();
			for (String command : commands) {
				System.out.println(command);
				stmt.execute(command);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public void connectToDB(String dbName) throws Exception {

		if (conn != null) {
			System.out.println("Error, you are connected already to a database");
			throw new Exception("A connection is already stablished, close connection first before starting new one");
		}
		String path = "jdbc:sqlite:" + dbName;

		try {
			conn = DriverManager.getConnection(path);
			System.out.println("connected to database: " + dbName);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public void closeConnection() {
		if (conn == null) {
			System.out.println("Connection is already closed");
			return;
		}
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conn = null;
		System.out.println("connection closed");
	}

	public static void main(String[] args) {
		Database db = new Database();
		try {
			db.connectToDB("RESTURANT.db");
			db.createResturantTables();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
	}
}
