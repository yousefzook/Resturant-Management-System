package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javafx.util.Pair;

public class ResturantDBLayer implements DBMethods {

	private Database db;
	private String dbName;

	public ResturantDBLayer(String dbName) {
		db = Database.getInstance();
		this.dbName = dbName;
	}

	public void createTables() throws Exception {
		db.createResturantTables();
	}

	public void addDish(Dish dishToAdd) throws Exception {
		db.connectToDB(dbName);

		String values = "";
		ArrayList<Pair<String, String>> attrs = dishToAdd.getDBAttributes();
		for (int i = 0; i < attrs.size() - 1; i++)
			values += "'" + attrs.get(i).getValue() + "',";
		values += "'TRUE',";
		values += "'" + attrs.get(attrs.size() - 1).getValue() + "'";

//		values += "'" + dishToAdd.id + "',";
//		values += "'" + dishToAdd.name + "',";
//		values += "'" + dishToAdd.description + "',";
//		values += "'" + dishToAdd.price + "',";
//		values += "'" + dishToAdd.rate + "',";
//		values += "'" + dishToAdd.rateCount + "',";
//		values += "'" + dishToAdd.timeToPrepare + "',";

		db.execute("insert into dish values(" + values + ");");
		db.closeConnection();
	}

	private List<Dish> parseDishSet(ResultSet resultSet) {
		List<Dish> dishes = new ArrayList<Dish>();
		try {
			while (resultSet.next()) {
				int id = resultSet.getInt("did");
				String name = resultSet.getString("name");
				String desc = resultSet.getString("description");
				float price = resultSet.getFloat("price");
				int rate = resultSet.getInt("rate");
				int rateCount = resultSet.getInt("rate_count");
				int timeToPrepare = resultSet.getInt("time_to_prepare_in_minutes");
				byte[] image = resultSet.getBytes("image");
				dishes.add(new Dish(id, name, desc, price, rate, rateCount, timeToPrepare, image));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dishes;
	}

	public Dish[] getDishes(int[] dishesIDs) throws Exception {
		db.connectToDB(dbName);
		String ids = "";
		for (int i = 0; i < dishesIDs.length - 1; i++)
			ids += dishesIDs[i] + ",";
		ids += dishesIDs[dishesIDs.length - 1];
		ResultSet resultSet = db.executeQuery("select * from dish where did in (" + ");");
		List<Dish> dishes = parseDishSet(resultSet);
		db.closeConnection();

		return (Dish[]) dishes.toArray();
	}

	public List<Dish> getDishesInOrder(int orderId) throws Exception {

		db.connectToDB(dbName);
		ResultSet resultSet = db.executeQuery(
				"select * from dish where did in (select did from order_dishes where" + " oid=" + orderId + ");");
		List<Dish> dishes = parseDishSet(resultSet);
		db.closeConnection();
		return dishes;
	}

	public void updateDish(Dish oldDish, Dish newDish) throws Exception {
		db.connectToDB(dbName);
		// if same name but different price, add the new as new dish in database
		if (oldDish.name.equals(newDish.name) && oldDish.price != newDish.price) {
			db.execute("update dish set is_available = FALSE where did=" + oldDish.id + ";");
			addDish(newDish);
			return;
		}
		ArrayList<Pair<String, String>> attrs = newDish.getDBAttributes();
		String updateValues = "";
		for (Pair<String, String> attr : attrs)
			updateValues += attr.getKey() + " = " + attr.getValue() + ",";
		updateValues.substring(0, updateValues.length() - 1); // remove last comma
		db.execute("update dish set " + updateValues + " where did=" + oldDish.id + ";");
		db.closeConnection();
	}

	public void removeDish(int dishId) throws Exception {
		// TODO Auto-generated method stub
		db.connectToDB(dbName);
		ResultSet resultSet = db.executeQuery("select * from dish where did=" + dishId + ";");
		if (!resultSet.next())
			throw new Exception("The dish is not exist");
		db.execute("update dish set is_available=FALSE where did=" + dishId + ";");
		db.closeConnection();
	}

	public void reAddDish(int dishId) throws Exception {
		db.connectToDB(dbName);
		ResultSet resultSet = db.executeQuery("select * from dish where did=" + dishId + ";");
		if (!resultSet.next())
			throw new Exception("The dish is not exist");
		if (resultSet.getBoolean("is_available"))
			throw new Exception("The dish is already available");
		db.execute("update dish set is_available=TRUE where did=" + dishId + ";");
		db.closeConnection();
	}

	public void addCook(Cook cook) throws Exception {
		db.connectToDB(dbName);
		String values = "";
		values += cook.id + "," + cook.firstName + "," + cook.lastName + ", TRUE";
		db.execute("insert into cook values(" + values + ");");
		db.closeConnection();
	}

	public void fireCook(int cookId) throws Exception {
		db.connectToDB(dbName);
		ResultSet resultSet = db.executeQuery("select * from cook where cid=" + cookId + ";");
		if (!resultSet.next())
			throw new Exception("The cook is not exist");
		db.execute("update cook set is_active=FALSE where cid=" + cookId + ";");
		db.closeConnection();
	}

	public void reHireCook(int cookId) throws Exception {
		db.connectToDB(dbName);
		ResultSet resultSet = db.executeQuery("select * from cook where cid=" + cookId + ";");
		if (!resultSet.next())
			throw new Exception("The cook is not exist");
		if (resultSet.getBoolean("is_active"))
			throw new Exception("The cook is already active");
		db.execute("update cook set is_active=TRUE where cid=" + cookId + ";");
		db.closeConnection();

	}

	public Double getTotalIncome(Date startDate, Date endDate) throws Exception {
		db.connectToDB(dbName);

		ResultSet resultSet = db.executeQuery("select sum(total_price) from order where order_timestamp > "
				+ startDate.toString() + "and order_timestamp < " + endDate.toString() + ";");

		double totalIncome = resultSet.getDouble(0);
		db.closeConnection();
		return totalIncome;
	}

	public ResultSet executeCustomQuery(String sqlQuery) throws Exception {
		db.connectToDB(dbName);
		ResultSet resultSet = db.executeQuery(sqlQuery);
		db.closeConnection();
		return resultSet;
	}

	public List<Dish> getAvailableDishes() throws SQLException, Exception {
		ResultSet resultSet = executeCustomQuery("select * from dishe where is_available=FALSE;");
		List<Dish> dishes = parseDishSet(resultSet);
		return dishes;
	}

	public List<Dish> getUnAvailableDishes() throws Exception {
		ResultSet resultSet = executeCustomQuery("select * from dishe where is_available=TRUE;");
		List<Dish> dishes = parseDishSet(resultSet);
		return dishes;
	}

	public List<Cook> getCooks() throws Exception {
		ResultSet resultSet = executeCustomQuery("select * from cook;");
		List<Cook> cooks = new ArrayList<Cook>();
		while (resultSet.next()) {
			cooks.add(new Cook(resultSet.getInt("cid"), resultSet.getString("first_name"),
					resultSet.getString("last_name")));
		}
		return null;
	}

}
