package main.model.responses.indexing;

import lombok.Data;
import main.model.responses.Response;

@Data
public class ResponseIndexingStartFalse implements Response {

    private boolean result = false;
    private String error = "Индексация уже запущена";

}
