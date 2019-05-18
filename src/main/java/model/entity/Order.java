package model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import model.OrderState;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Map;

@Entity(name = "order_details")
@Setter
@Getter
@AllArgsConstructor
public class Order {

    public Order() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "order_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cook_id")
    private Cook cook;

    @ManyToOne(fetch = FetchType.EAGER)
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
    @Fetch(value = FetchMode.JOIN)
    private Map<Dish, Integer> details;


    public Order(Map<Dish, Integer> details) {
        this.details = details;
    }
}
