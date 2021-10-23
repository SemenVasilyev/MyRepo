package main.model.responses.sratistics;

import lombok.Data;
import main.model.Statistics;
import main.model.responses.Response;

@Data
public class ResponseStatistics implements Response {

    private boolean result = true;
    private Statistics statistics = new Statistics();
}
