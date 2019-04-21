package model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity(name = "table_details")
public class Table {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "table_id")
    private int id;

    @OneToMany
    @JoinColumn(name = "order_id")
    private List<Order> orders;

    public Table(int id) {
        orders = new ArrayList<Order>();
    }

    public void addOrder(Order o) {
        orders.add(o);
    }
}
