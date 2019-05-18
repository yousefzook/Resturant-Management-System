package model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "table_details")
@Setter
@Getter
public class Table {

    public Table() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "table_id")
    private int id;

    public Table(int id) {
        this.id = id;
    }
}
