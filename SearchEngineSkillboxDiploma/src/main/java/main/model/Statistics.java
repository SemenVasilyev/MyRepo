package main.model;

import lombok.Data;
import main.entitys.Site;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class Statistics {

    private Total total;
    private List<DetailedAboutSite> detailed;

    public Statistics() {
        this.total = new Total();
        this.detailed = new ArrayList<>();

        List<Site> sites = WorkingWithDB.getAllSites();
        for (Site site : sites) {
            DetailedAboutSite detail = new DetailedAboutSite(site);

            detailed.add(detail);
        }
    }

    @Data
    public class Total {

        private long sites;
        private long pages;
        private long lemmas;
        private boolean isIndexing;

        public Total() {
            this.sites = getAmountAllSites();
            this.pages = getAmountAllPages();
            this.lemmas = getAmountAllLemmas();
            this.isIndexing = IndexingStatus.isRun();
        }

        private long getAmountAllSites() {
            return WorkingWithDB.getAmountAllSites();
        }

        private long getAmountAllPages() {
            return WorkingWithDB.getAmountAllPages();
        }

        private long getAmountAllLemmas() {
            return WorkingWithDB.getAmountAllLemmas();
        }

    }

    @Data
     public class DetailedAboutSite {
        private String url;
        private String name;
        private StatusSite status;
        private long statusTime;
        private String error;
        private long pages;
        private long lemmas;

        public DetailedAboutSite(Site site) {
            this.url = site.getUrl();
            this.name = site.getName();
            this.status = site.getStatusSite();
            this.statusTime = site.getStatusTime().getTime();
            this.error = site.getLastError();
            this.pages = WorkingWithDB.getAmountPagesForSite(site.getId());
            this.lemmas = WorkingWithDB.getAmountLemmasForSite(site.getId());
        }


    }
}
