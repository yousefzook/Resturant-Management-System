package model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Dish {

    private int id, timeToPrepare, rateCount;

    private String name, description;

    private float rate, price;

    private byte[] image;
}
