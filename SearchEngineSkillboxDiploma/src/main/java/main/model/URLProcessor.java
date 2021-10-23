package main.model;

import lombok.Data;
import main.entitys.Site;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.RecursiveAction;


@Data
public class URLProcessor extends RecursiveAction {

    private static CopyOnWriteArraySet<String> repitSetURL = new CopyOnWriteArraySet<>();

    private Connection.Response response = null;
    private String mainURL;
    private String domain;
    private Site site;
    private boolean flagOnePage = false;
    private boolean flagOnePageReady = false;

    public URLProcessor(String url) {
        parsePage(url);
        if (response == null) {
            return;
        }
        flagOnePage = true;
        mainURL = url;
        domain = getDomainName(url);

        List<SiteFromPropertys> sites = MyProperties.getSiteFromPropertys();

        for (SiteFromPropertys siteFromPropertys : sites) {
            String domainSiteFromPropertys = getDomainName(siteFromPropertys.getUrl());
            if (domain.equals(domainSiteFromPropertys)) {
                Site site = new Site(siteFromPropertys.getUrl());
                site = (Site) WorkingWithDB.getFromDB(site);
                if (site != null) {
                    this.site = site;
                    flagOnePageReady = true;
                }
                break;
            }
        }
    }

    public URLProcessor(Site site, String mainURL) {
        this.site = site;
        this.mainURL = mainURL;
        this.domain = getDomainName(mainURL);
    }

    @Override
    protected void compute() {

        if (flagOnePage) {
            onePageIndexing();
            return;
        }

        if (!IndexingStatus.isRun()) {
            return;
        }

        Map<String, URLProcessor> tasks = new HashMap<>();

        parsePage(mainURL);
        if (response == null) {
            site.setLastError(" ответ " + mainURL + " не получен ");
            site.setStatusTime(new Date());
            site.setStatusSite(StatusSite.FAILED);
            WorkingWithDB.addAndReturnSite(site);
            return;
        }

        String partWithoutDomain = cleanFromDomane();
        PageIndexer.parsePage(site, partWithoutDomain, getStatusCode(), getHtmlBody());

        try {
            Document document = response.parse();

            Elements links = document.select("a[href]");

            for (Element link : links) {
                String childURL = link.attr("abs:href");
                if (!childURL.isEmpty()) {
                    String domainURL = getDomainName(childURL);
                    if (domain.equals(domainURL) && !repitSetURL.contains(childURL) && !tasks.containsKey(childURL)) {
                        repitSetURL.add(childURL);
                        URLProcessor task = new URLProcessor(site, childURL);
                        tasks.put(childURL, task);
                    } else {
                        //   Log.error("",e);
                        repitSetURL.add(childURL);
                    }
                }
            }

            if (tasks.size() > 0) {
                for (URLProcessor task : tasks.values()) {
                    task.fork();
                }
                for (URLProcessor task : tasks.values()) {
                    task.join();
                }
            }
        } catch (IOException e) {
            // Log.error("",e);
            // e.printStackTrace();
        }
    }

    private void parsePage(String url) {
        try {
            Thread.sleep(150);
            response = Jsoup.connect(url)
                    .userAgent(MyProperties.getUserAgent())
                    .referrer("http://www.google.com")
                    .execute();
        } catch (Exception e) {
            //Log.error("",e);
//            e.printStackTrace();
        }
    }

    private int getStatusCode() {
        return response.statusCode();
    }

    private String getHtmlBody() {
        return response.body();
    }

    public static String getDomainName(String url) {
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        String domain = uri.getHost();
        if(domain == null) {
            return "домен сайта не определен " + url;
        }
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

    public static boolean indexingOnePage(String url) {
        URLProcessor urlProcessor = new URLProcessor(url);
        if (urlProcessor.isFlagOnePageReady()) {
            urlProcessor.compute();
            return true;
        }
        return false;
    }

    public static void cleanRepitSetURL(){
        repitSetURL = new CopyOnWriteArraySet<>();
    }

    private void onePageIndexing() {
        PageIndexer.parsePage(site, mainURL, getStatusCode(), getHtmlBody());
    }

    private String cleanFromDomane(){
        return mainURL.substring(mainURL.indexOf(domain) + domain.length());
    }
}
