package model.actionresults;

import lombok.Data;
import model.entity.Table;

import java.util.List;

@Data
public class TableResponse {
    private boolean success;
    private String message;
    private List<Table> tables;
}
