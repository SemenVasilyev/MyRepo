package main.model.responses.search;

import lombok.Data;
import main.model.responses.Response;

@Data
public class ResponseSearchNotFound implements Response {

    private boolean result = false;
    private String error = "Страниц соответсвующих запросу не найдено";

}
