package model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import model.OrderState;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
public class Cook {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "cook_id")
    private Integer id;

    @Column(name = "f_name")
    private String firstName;

    @Column(name = "l_name")
    private String lastName;

    @Column(name = "is_hired", columnDefinition = "boolean default true", nullable = false)
    private Boolean hired;

    @ElementCollection(targetClass = Order.class)
    @MapKey(name = "order_id")
    private Set<Order> assignedOrders;

    public Cook(Integer id, String firstName, String lastName, boolean hired) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.hired = hired;
        assignedOrders = new HashSet<>();
    }

    public boolean acceptOrder(Order o) {
        return true;
    }

    public boolean setOrderState(Order o, OrderState s) {
        return true;
    }
}
