package model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;


@Entity(name = "dish_details")
@Data
@Builder
@AllArgsConstructor
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "dish_id")
    private Integer id;

    @Column(name = "time_to_prepare")
    private Integer timeToPrepare;

    @Column(name = "rate_count")
    @Builder.Default
    private Integer rateCount = 1;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "rate")
    @Builder.Default
    private Float rate = 5F;

    @Column(name = "price")
    private Float price;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "is_active")
    private boolean active = true;

    public Dish() {
    }
}
