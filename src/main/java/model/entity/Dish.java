package model.entity;

import lombok.*;

import javax.persistence.*;


@Data
@Entity(name = "dish_details")
@Builder(toBuilder = true)
@AllArgsConstructor
public class Dish {

    public Dish() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "dish_id")
    private Integer id;

    @Column(name = "time_to_prepare", nullable = false)
    private Integer timeToPrepare;

    @Column(name = "rate_count", nullable = false)
    @Builder.Default
    private Integer rateCount = 0;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "rate", nullable = false)
    @Builder.Default
    private Float rate = 5F;

    @Column(name = "price", nullable = false)
    private Float price;

    @Column(name = "image_path", nullable = false)
    private String imagePath;

    @Column(name = "is_active", columnDefinition = "boolean default true", nullable = false)
    private boolean active = true;
}
