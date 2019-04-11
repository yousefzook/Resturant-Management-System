package model;

import lombok.Data;
import lombok.Getter;

@Data
public class Dish {

    @Getter
    int id, timeToPrepare, rateCount;
    @Getter
    String name, description, photoPath;
    @Getter
    float rate, price;

    public Dish(int id, String name, String photoPath, String description, float price, float rate, int rateCount, int timeToPrepare) {
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
