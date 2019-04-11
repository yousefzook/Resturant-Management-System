package model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

@Data
public class Order {

    @Getter @Setter
    private OrderState state;
    @Getter @Setter
    private Cook cook;
    @Getter @Setter
    private int id;
    @Getter @Setter
    private Map<Dish, Integer> map;
    @Getter @Setter
    private float price;
    @Getter @Setter
    private Table t;
    @Getter @Setter
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
