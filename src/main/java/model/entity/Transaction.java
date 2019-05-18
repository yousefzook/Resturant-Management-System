package model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Builder
@AllArgsConstructor
@Setter
@Getter
public class Transaction {

    public Transaction() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "trans_id", nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private Table table;

    @Column(name = "trans_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    private Date date;

    @Column(name = "amount_phy", nullable = false)
    private int amountPhy;

    @Column(name = "amount_fin", nullable = false)
    private float amountFin;
}
