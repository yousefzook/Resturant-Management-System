package model.actionresults;

import lombok.Data;
import model.Order;

import java.util.List;

@Data
public class OrderResponse {
    private boolean success;
    private String message;
    private List<Order> orders;
}
