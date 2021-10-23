package main.model.responses;

import main.model.FindedPage;
import main.model.Search;
import main.model.responses.indexPage.ResponseIndexPage;
import main.model.responses.indexPage.ResponseIndexPageFalse;
import main.model.responses.indexing.ResponseIndexingStart;
import main.model.responses.indexing.ResponseIndexingStartFalse;
import main.model.responses.indexing.ResponseIndexingStop;
import main.model.responses.indexing.ResponseIndexingStopFalse;
import main.model.responses.search.ResponseSearch;
import main.model.responses.search.ResponseSearchIsEmpty;
import main.model.responses.search.ResponseSearchNotFound;
import main.model.responses.sratistics.ResponseStatistics;

import java.util.Set;
import java.util.stream.Collectors;

public class ResponsesController {

    //GET /api/startIndexing
    public static Response responseIndexingStart(boolean isRun) {
        if (!isRun) {
            System.out.println("Запустить");
            return new ResponseIndexingStart();
        }
        System.out.println("Индексация уже запущена");
        return new ResponseIndexingStartFalse();
    }

    // GET /api/stopIndexing
    public static Response responseIndexingStop(boolean isRun) {
        if (isRun) {
            System.out.println("Остановить");
            return new ResponseIndexingStop();
        }
        System.out.println("Индексация не запущена");
        return new ResponseIndexingStopFalse();
    }

    //GET /api/statistics
    public static Response responseStatistics() {
        return new ResponseStatistics();
    }

    //POST /api/indexPage
    public static Response responseIndexPage(boolean isIndexed ) {
        if (isIndexed) {
            System.out.println("Страница проиндексирована");
            return new ResponseIndexPage();
        }
        System.out.println("Страница не проиндексирована");
        return new ResponseIndexPageFalse();
    }

    //GET /api/search
    public static Response responseSearch(String site, String query) {
        Set<FindedPage> findedPageSet = Search.start(query);
        if(findedPageSet.isEmpty()){
            return new ResponseSearchNotFound();
        }

        if(!site.equals("")) {
           Set<FindedPage> sdf =  findedPageSet.stream().filter(findedPage -> findedPage.getSite().equals(site)).collect(Collectors.toSet());
            System.out.println();
        }

        return new ResponseSearch(findedPageSet);
    }
}
