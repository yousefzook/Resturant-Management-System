package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface DBMethods {

    /**
     * Insert new dish to the menu
     *
     * @param dishToAdd
     * @throws Exception, if some error happens in data base
     */
    public void addDish(Dish dishToAdd) throws Exception;

    /**
     * get list of dishes
     *
     * @param DishesIDs
     * @return the list of dishes found, Null in the array if dish #i in the given
     * IDs is not exist, an empty array will be returned if no dishes
     * available
     * @throws Exception, if some error happens in data base
     */
    public Dish[] getDishes(int[] DishesIDs) throws Exception;

    /**
     * get list of dishes
     *
     * @param DishesIDs
     * @return the list of dishes found, Null in the array if dish #i in the given
     * IDs is not exist, an empty array will be returned if no dishes
     * available
     * @throws Exception, if some error happens in data base
     */
    public List<Dish> getTopDishes(int limit) throws Exception;

    /**
     * get all available dishes
     *
     * @return
     */
    public List<Dish> getAvailableDishes() throws SQLException, Exception;

    /**
     * get all unavailable dishes
     *
     * @return
     */
    public List<Dish> getUnAvailableDishes() throws SQLException, Exception;

    /**
     * get all dishes in an order
     *
     * @param orderId
     * @return list of dishes in order with ID: 'orderID'
     * @throws Exception, if some error happens in data base
     */
    public List<Dish> getDishesInOrder(int orderId) throws Exception;

    /**
     * update a dish in the database, if old dish name equals the new dish name and
     * the price is different, the old dish state becomes 'unAvailable' which remove
     * it from the menu
     *
     * @param oldDish
     * @param newDish
     * @throws Exception, if some error happens in data base
     */
    public void updateDish(Dish oldDish, Dish newDish) throws Exception;

    /**
     * set the state of dish with ID: 'dishId' to unavilable
     *
     * @param dishId
     * @throws Exception, if some error happens in data base
     */
    public void removeDish(int dishId) throws Exception;

    /**
     * reset the state of a dish to 'available' state, an exception will be thrown
     * if a dish with the same id is exist already in 'available' state, or if no
     * dish exists with this id
     *
     * @param dishId
     * @throws Exception, if some error happens in data base
     */
    public void reAddDish(int dishId) throws Exception;

    /**
     * insert new cook to the cookers database
     *
     * @param cook
     * @throws Exception, if some error happens in data base
     */
    public void addCook(Cook cook) throws Exception;

    /**
     * set cook to 'not active' state
     *
     * @param cookId
     * @throws Exception, if some error happens in data base
     */
    public void fireCook(int cookId) throws Exception;

    /**
     * reset the state of a cook to 'active' state, an exception will be thrown if a
     * cook with the same id is already in 'available' state
     *
     * @param CId
     * @throws Exception, if some error happens in data base
     */
    public void reHireCook(int CId) throws Exception;

    /**
     * get all income in a time period starting from 'startDate' till 'endDate'
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception, if some error happens in data base
     */
    public Double getTotalIncome(Date startDate, Date endDate) throws Exception;

    /**
     * execute custom query on database
     *
     * @param sqlCommand
     * @return
     * @throws Exception, if the query is not valid or a sql database exception
     */
    public ResultSet executeCustomQuery(String sqlCommand) throws Exception;

    /**
     * get all cooks in the database
     *
     * @return
     * @throws Exception
     */
    public List<Cook> getCooks() throws Exception;
}
