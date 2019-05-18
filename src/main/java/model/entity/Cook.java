package model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@Setter
@Getter
public class Cook {

    public Cook() {
    }

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

    @ElementCollection(targetClass = Order.class, fetch = FetchType.EAGER)
    @MapKey(name = "order_id")
    @Fetch(FetchMode.SUBSELECT)
    private List<Order> assignedOrders;

    public Cook(Integer id, String firstName, String lastName, boolean hired) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.hired = hired;
        assignedOrders = new ArrayList<>();
    }
}
