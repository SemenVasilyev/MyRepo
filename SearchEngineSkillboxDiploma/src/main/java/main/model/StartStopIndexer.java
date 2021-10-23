package main.model;

import java.util.List;

public class StartStopIndexer {

    public static void start() {
        if(!IndexingStatus.isRun()){
            IndexingStatus.setRun(true);
            List<SiteFromPropertys> listSites = MyProperties.getSiteFromPropertys();
            for (SiteFromPropertys siteFromPropertys : listSites) {
                URLProcessor.cleanRepitSetURL();
                IndexingFullThread indexingThread = new IndexingFullThread(siteFromPropertys);
                indexingThread.start();
            }
        }
    }

    public static void stop() {
        if(IndexingStatus.isRun()){
            IndexingStatus.setRun(false);
        }
    }

}
