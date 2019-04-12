package model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Cook {


    private List assignedOrders;

    private Integer id;

    private String firstName, lastName;

    public Cook(Integer id, String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        assignedOrders = new ArrayList<Order>();
    }

    public boolean acceptOrder(Order o) {
        return true;
    }

    public boolean setIrderState(Order o, OrderState s) {
        return true;
    }
}
