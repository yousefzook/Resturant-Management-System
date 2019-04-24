package model.actionresults;

import lombok.Data;
import model.entity.Cook;

import java.util.List;

@Data
public class CookResponse {
    private boolean success;
    private String message;
    private List<Cook> cooks;
}
