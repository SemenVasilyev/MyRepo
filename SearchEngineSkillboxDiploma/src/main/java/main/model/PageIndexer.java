package main.model;

import main.entitys.Index;
import main.entitys.Lemma;
import main.entitys.Page;
import main.entitys.Site;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PageIndexer {

    private static final float TITLE_SELECTOR = 1;
    private static final float BODY_SELECTOR = 0.8F;

    public static void parsePage(Site site, String url, int code, String html) {

        Map<String, Float> lemmasRank = getLemmasRank(html);

        Page page = new Page(url, code, html, site);

        if (code >= 400) {
            WorkingWithDB.addToDB(page);
            site.setLastError("error " + code + ", " + page.getPath() );
            site.setStatusTime(new Date());
            site.setStatusSite(StatusSite.FAILED);
            return;
        }
        lemmasRank.entrySet().stream().forEach(entry -> {
           Lemma lemma = new Lemma(entry.getKey(), site);
           Index index = new Index(page,lemma,entry.getValue());
           WorkingWithDB.addToDB(index);
        });

    }

    private static Map<String, Float> getLemmasRank(String html) {
        Document document = Jsoup.parse(html);
        String title = document.title();
        String bodyCleanTags = document.body().text();

        Map<String, Integer> titleLemmas = Lemmatizer.lemmatize(title);
        Map<String, Integer> bodyLemmas = Lemmatizer.lemmatize(bodyCleanTags);

        Map<String, Float> lemmasRank = new HashMap<>();

        titleLemmas.entrySet().stream().forEach(entry -> {
            float rankLemmaAtTitle = entry.getValue() * TITLE_SELECTOR;
            if (lemmasRank.containsKey(entry.getKey())) {
                lemmasRank.replace(entry.getKey(), lemmasRank.get(entry.getKey()) + rankLemmaAtTitle);
            } else {
                lemmasRank.put(entry.getKey(), rankLemmaAtTitle);
            }
        });

        bodyLemmas.entrySet().stream().forEach(entry -> {
            float rankLemmaAtTitle = entry.getValue() * BODY_SELECTOR;
            if (lemmasRank.containsKey(entry.getKey())) {
                lemmasRank.replace(entry.getKey(), (lemmasRank.get(entry.getKey()) + rankLemmaAtTitle));
            } else {
                lemmasRank.put(entry.getKey(), rankLemmaAtTitle);
            }
        });

        return lemmasRank;
    }
}
