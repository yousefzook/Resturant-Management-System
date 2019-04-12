package model;

import java.sql.ResultSet;
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
     *         IDs is not exist, an empty array will be returned if no dishes
     *         available
     * @throws Exception, if some error happens in data base
     */
    public Dish[] getDishes(int[] DishesIDs) throws Exception;

    /**
     * get all dishes in an order
     *
     * @param orderId
     * @return list of dishes in order with ID: 'orderID'
     * @throws Exception, if some error happens in data base
     */
    public List<Dish> getDishesInOrder(int orderId) throws Exception;

    /**
     * update a dish in the database, if old dish name or id equals the new dish
     * name or id, the old dish state becomes 'unAvailable' which remove it from the
     * menu
     *
     * @param oldDishId
     * @param newDish
     * @throws Exception, if some error happens in data base
     */
    public void updateDish(int oldDishId, Dish newDish) throws Exception;

    /**
     * set the state of dish with ID: 'dishId' to unavilable
     *
     * @param dishId
     * @throws Exception, if some error happens in data base
     */
    public void removeDish(int dishId) throws Exception;

    /**
     * reset the state of a dish to 'available' state, an exception will be thrown
     * if a dish with the same name or id is exist already in 'available' state
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
     * @param cook
     * @throws Exception, if some error happens in data base
     */
    public void fireCook(Cook cook) throws Exception;

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
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception, if some error happens in data base
     */
    public Double getTotalIncome(Date startDate, Date endDate)throws Exception;

    /**
     * execute custom query on database
     * @param sqlCommand
     * @return
     * @throws Exception, if the query is not valid or a sql database exception
     */
    public ResultSet executeCustomQuery(String sqlCommand)throws Exception;
}