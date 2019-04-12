package model.actionresults;

import lombok.Data;
import model.Dish;

import java.util.List;

@Data
public class DishResponse {
    private boolean success;
    private String message;
    private List<Dish> dishes;
}
