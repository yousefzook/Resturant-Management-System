package model;

import lombok.Data;
import lombok.Getter;

@Data
public class Dish {

    @Getter
    Integer id, timeToPrepare, rateCount;
    @Getter
    String name, description, photoPath;
    @Getter
    Float rate, price;

    public Dish(Integer id, String name, String photoPath, String description, Float price, Float rate, Integer rateCount, Integer timeToPrepare) {
        this.id = id;
        this.name = name;
        this.photoPath = photoPath;
        this.price = price;
        this.description = description;
        this.rate = rate;
        this.rateCount = rateCount;
        this.timeToPrepare = timeToPrepare;
    }
}
