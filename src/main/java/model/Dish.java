package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Dish {

    private int id, timeToPrepare, rateCount;

    private String name, description;

    private float rate, price;

    private byte[] image;

    private String imagePath;

    public Dish() {
    }

}
