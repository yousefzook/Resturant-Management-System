package model.actionresults;

import lombok.Data;

@Data
public class EmptyResponse {
    private boolean success;
    private String message;
}
