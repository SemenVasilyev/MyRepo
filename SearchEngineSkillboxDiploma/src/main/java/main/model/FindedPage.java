package main.model;

import main.entitys.Page;
import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

@Data
public class FindedPage implements Comparable<FindedPage> {

    private String site;

    private String siteName;

    private String uri;

    private String title;

    private String snippet;

    private float relevance;

    FindedPage(Page page,String snippet, float relevance){
        this.site = page.getSite().getUrl();
        this.siteName = page.getSite().getName();
        this.uri = page.getPath();
        Document document = Jsoup.parse(page.getContent());
        String title = document.title();
        this.title = title;
        this.snippet = snippet;
        this.relevance = relevance;

    }

    @Override
    public int compareTo(FindedPage p) {
        int i = (int)(p.getRelevance() * 10) - (int)(relevance * 10);
        if (i == 0)
            return uri.compareTo(p.getUri());
        return i;
    }
}
