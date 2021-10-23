package main.model.responses.indexPage;

import lombok.Data;
import main.model.responses.Response;

@Data
public class ResponseIndexPageFalse implements Response {

    private boolean result = false;
    private String error = "Данная страница находится за пределами сайтов," +
            "указанных в конфигурационном файле";
}
