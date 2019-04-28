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

    @ManyToOne
    @JoinColumn(name = "table_id", nullable = false)
    private Table table;

    @Column(name = "order_state", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderState state = OrderState.inQueue;

    @ElementCollection
    @CollectionTable(name = "order_contents")
    @MapKeyClass(Dish.class)
    @Column(name = "amount")
    @MapKeyJoinColumn(name = "dish_id")
    private Map<Dish, Integer> details;


    public Order(Map<Dish, Integer> details) {
        this.details = details;
    }
}
