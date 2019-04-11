package model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
public class Table {

    @Getter
    @Setter
    private List<Order> orders;

    public Table(int id) {
        orders = new ArrayList<Order>();
    }

    public void addOrder(Order o) {
        orders.add(o);
    }
}
