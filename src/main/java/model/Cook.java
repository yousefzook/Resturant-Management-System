package model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
public class Cook {

    @Getter @Setter
    private List assignedOrders;
    @Getter @Setter
    private Integer id;
    @Getter @Setter
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
