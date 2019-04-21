package model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import model.OrderState;

import javax.persistence.*;
import java.util.Map;

@Data
@Entity(name = "order_details")
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "order_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cook_id")
    private Cook cook;

    @Column(name = "order_state")
    private OrderState state;

    @ElementCollection
    @MapKeyColumn(name = "dish_id")
    @MapKeyClass(value = Dish.class)
    @Column(name = "amount")
    @CollectionTable(name = "order_contents")
    private Map<Dish, Integer> details;


    public Order(Map<Dish, Integer> details) {
        this.details = details;
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
