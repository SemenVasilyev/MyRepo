package main.model.responses.search;

import lombok.Data;
import main.model.FindedPage;
import main.model.responses.Response;

import java.util.Set;

@Data
public class ResponseSearch implements Response {

    private boolean result = true;
    private int count;
    private Set<FindedPage> data;

    public ResponseSearch(Set<FindedPage> findedPageSet) {
        count = findedPageSet.size();
        data = findedPageSet;

    }
}
