package main.conrollers;

import main.model.IndexingStatus;
import main.model.StartStopIndexer;
import main.model.URLProcessor;
import main.model.responses.*;
import main.model.responses.search.ResponseSearchIsEmpty;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class MyRestController {

    @GetMapping(value = "api/startIndexing")
    public Response startIndexing() {
        boolean isRun = IndexingStatus.isRun();
        if(!isRun){
            StartStopIndexer.start();
        }
        return ResponsesController.responseIndexingStart(isRun);
    }

    @GetMapping(value = "api/stopIndexing")
    public Response stopIndexing() {
        boolean isRun = IndexingStatus.isRun();
        if(isRun){
            StartStopIndexer.stop();
        }
        return ResponsesController.responseIndexingStop(isRun) ;
    }

    @PostMapping(value = "api/indexPage")
    public Response indexPage(@RequestParam("url") String url) {
        boolean isIndexed = URLProcessor.indexingOnePage(url);
        return ResponsesController.responseIndexPage(isIndexed);
    }

    @GetMapping(value = "api/statistics")
    public Response statistics() {
        return ResponsesController.responseStatistics();
    }

    @GetMapping(value = "api/search")
    public Response search(@RequestParam(value = "site", defaultValue = "") String site, @RequestParam("query") String query) {
        if(query.equals("")){
            return new ResponseSearchIsEmpty();
        }

        return ResponsesController.responseSearch(site, query);
    }

}