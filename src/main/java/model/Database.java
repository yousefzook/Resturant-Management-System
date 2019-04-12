package model;

import java.sql.*;

public class Database {

	private Connection conn;

	private static Database db;

	public static Database getInstance() {
		if (db == null)
			db = new Database();
		return db;
	}

	private Database() {
	}

	/**
	 * connect to a database, if not exists, create one.
	 * 
	 * @param dbName
	 * @throws Exception if a connection to a database is already exist
	 */
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

	/**
	 * create resturant tables, must be connected to a database first or will throw
	 * an exception
	 * 
	 * @throws Exception
	 */
	public void createResturantTables() throws Exception {
		if (conn == null) {
			System.out.println("Error, you are connected already to a database");
			throw new Exception("No connection, Connect to a database first");
		}
		String[] commands = new String[5];
		commands[0] = "CREATE TABLE IF NOT EXISTS dish (did INTEGER AUTO_INCREMENT PRIMARY KEY,"
				+ " name VARCHAR NOT NULL, description VARCHAR, price DOUBLE, rate INTEGER,"
				+ " rate_count INTEGER, time_to_prepare_in_minutes integer," + " is_available BOOLEAN, image BLOB);";
		commands[1] = "CREATE TABLE IF NOT EXISTS cook (cid INTEGER AUTO_INCREMENT PRIMARY KEY,"
				+ " first_name VARCHAR NOT NULL, last_name VARCHAR NOT NULL,  is_active BOOLEAN);";
		commands[2] = "CREATE TABLE IF NOT EXISTS tables (tid INTEGER PRIMARY KEY);";
		commands[3] = "CREATE TABLE IF NOT EXISTS orders (oid INTEGER AUTO_INCREMENT PRIMARY KEY ,"
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

	/**
	 * close connection to a database
	 */
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

	/**
	 * execute a non-query command,
	 * 
	 * @param sqlCommand, command to be executed
	 * @throws Exception if select command is passed or if syntax error
	 */
	public void execute(String sqlCommand) throws SQLException, Exception {
		if (conn == null)
			throw new Exception("No connection to a database had been established!");
		if (sqlCommand.split(" ")[0].equalsIgnoreCase("select"))
			throw new SQLException("select commands should'nt be executed with 'execute' method");
		Statement stmt = conn.createStatement();
		stmt.execute(sqlCommand);
	}

	/**
	 * execute a query command,
	 * 
	 * @return ResultSet, the result of the executed query
	 * @param sqlQuery, query to be executed
	 * @throws SQLException if the command wasn't a query or if syntax error
	 * @throws Exception    if no connection established before
	 */
	public ResultSet executeQuery(String sqlQuery) throws SQLException, Exception {
		if (conn == null)
			throw new Exception("No connection to a database had been established!");
		Statement stmt = conn.createStatement();
		return stmt.executeQuery(sqlQuery);
	}

	public static void main(String[] args) {
		Database db = new Database();
		try {
			db.connectToDB("RESTURANT.db");
			db.createResturantTables();
			db.execute("select * from tables;");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
	}
}
