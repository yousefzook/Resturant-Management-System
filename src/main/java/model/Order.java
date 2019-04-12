package model;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class Order {

    private OrderState state;

    private Cook cook;

    private int id;

    private Map<Dish, Integer> map;

    private float price;

    private Table t;

    private Date date;

    public Order(int id, Map<Dish, Integer> map, float price, Table t, Date date) {
        this.id = id;
        this.map = map;
        this.price = price;
        this.t = t;
        this.date = date;
    }

    public boolean AssignCook(Cook cook) {
        this.cook = cook;
        return true;
    }

    public boolean setOrderState(OrderState s) {
        this.state = s;
        return true;
    }
}
