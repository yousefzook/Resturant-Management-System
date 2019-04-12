package model.actionresults;

import lombok.Data;

@Data
public class NumericResponse {
    private boolean success;
    private String message;
    private Number number;
}
