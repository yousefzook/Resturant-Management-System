package model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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

    public Table(int id) {
        this.id = id;
    }
}
