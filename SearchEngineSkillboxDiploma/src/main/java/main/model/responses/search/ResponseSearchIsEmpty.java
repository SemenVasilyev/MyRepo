package main.model.responses.search;

import lombok.Data;
import main.model.responses.Response;

@Data
public class ResponseSearchIsEmpty implements Response {

    private boolean result = false;
    private String error = "Задан пустой поисковый запрос";

}
