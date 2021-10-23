package main.model;

import lombok.Data;
import main.entitys.Site;

import java.util.Date;
import java.util.concurrent.ForkJoinPool;

@Data
public class IndexingFullThread extends Thread {

    private SiteFromPropertys siteFromPropertys;

    public IndexingFullThread(SiteFromPropertys siteFromPropertys) {
        this.siteFromPropertys = siteFromPropertys;
    }

    @Override
    public void run() {

        if (!IndexingStatus.isRun()) {
            return;
        }

        Site site = new Site(StatusSite.INDEXING, new Date(), null,
                siteFromPropertys.getUrl(), siteFromPropertys.getName());

        System.out.println(site.getUrl());

        site = WorkingWithDB.addAndReturnSite(site);

        ForkJoinPool pool = new ForkJoinPool();
        URLProcessor urlProcessor = new URLProcessor(site, site.getUrl());
        pool.invoke(urlProcessor);

        while (!urlProcessor.isDone()) {
        }

        pool.shutdown();

        if (!site.getStatusSite().equals(StatusSite.FAILED) && IndexingStatus.isRun()) {
            site.setStatusSite(StatusSite.INDEXED);
            WorkingWithDB.addAndReturnSite(site);
        }

        System.out.println("Сайт проиндексирован " + site.getUrl() + ", статус " + site.getStatusSite() + ", error " + site.getLastError());
    }

}
