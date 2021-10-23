package main.model.responses.indexing;

import lombok.Data;
import main.model.responses.Response;

@Data
public class ResponseIndexingStart implements Response {

    private boolean result = true;

}
