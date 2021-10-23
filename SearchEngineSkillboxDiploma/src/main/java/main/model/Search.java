package main.model;

import main.entitys.Index;
import main.entitys.Lemma;
import main.entitys.Page;
import main.model.FindedPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.*;
import java.util.stream.Collectors;

public class Search {

    public static Set<FindedPage> start(String input) {

        Map<String, Integer> inputLemmas = Lemmatizer.lemmatize(input);
        Set<Lemma> sortedLemmas = new TreeSet<>();

        inputLemmas.entrySet().stream().forEach(entry -> {
            Lemma lemma = (Lemma) WorkingWithDB.getFromDB(new Lemma(entry.getKey()));
            if (lemma != null) {
                sortedLemmas.add(lemma);
            }
        });

        Set<FindedPage> findedPages = searchPages(sortedLemmas);

        return findedPages;

    }

    private static Set<FindedPage> searchPages(Set<Lemma> sortedLemmas) {
        if (sortedLemmas.isEmpty()) {
            return new HashSet<>();
        }

        Iterator iterator = sortedLemmas.iterator();
        Set<Page> pagesForFirstLemma = new HashSet<>(WorkingWithDB.getPagesByLemma((Lemma) iterator.next()));

        Set<Page> remainingPages = new HashSet<>(pagesForFirstLemma);
        while (iterator.hasNext()) {
            Lemma lemma = (Lemma) iterator.next();
            for (Page page : pagesForFirstLemma) {
                if (!WorkingWithDB.isLemmaAndPage(lemma, page)) {
                    remainingPages.remove(page);
                }
            }
        }

        if (remainingPages.isEmpty()){
            return new TreeSet<>();
        }

        List<Index> indexes = new ArrayList<>();
        for (Page page : remainingPages) {
            for (Lemma lemma : sortedLemmas) {
                Index index = WorkingWithDB.getIndexByLemmaAndPage(lemma, page);
                indexes.add(index);
            }
        }

        Map<Page, Float> relevance = new HashMap<>();
        for (Index index : indexes) {
            if (relevance.containsKey(index.getPage())) {
                relevance.put(index.getPage(), relevance.get(index.getPage()) + index.getRank());
            } else {
                relevance.put(index.getPage(), index.getRank());
            }
        }

        Float maxRelevance = relevance.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).get().getValue();

        Set<FindedPage> findedPages = new TreeSet<>();
        relevance.entrySet().stream().forEach(entry -> {
            Page page = entry.getKey();
            String snippet = getSnippetWithHighlightedWords(page, sortedLemmas);
            float relevanceForPage = (entry.getValue() / maxRelevance);
            findedPages.add(new FindedPage(page, snippet, relevanceForPage));
        });

        return findedPages;
    }

    private static String getSnippetWithHighlightedWords(Page page, Set<Lemma> sortedLemmas) {
        Set<String> lemmasStringSet = sortedLemmas.stream().map(Lemma::getLemma).collect(Collectors.toSet());
        Document document = Jsoup.parse(page.getContent());
        String textBodyHTML = document.body().text();

        Map<String, String> bodyLemmas = Lemmatizer.lemmatizeWithMatching(textBodyHTML.toString());
        Set<String> wordsForHighlight = bodyLemmas.entrySet().stream().filter(entry -> lemmasStringSet.contains(entry.getValue())).map(Map.Entry::getKey).collect(Collectors.toSet());

        for (String word : wordsForHighlight) {
            textBodyHTML = textBodyHTML.replaceAll( "(?i)"  + word, "<b>" + word + "</b>");
        }

        int index = textBodyHTML.indexOf("<b>");
        int indexSart = (index - 100) < 0 ? 0 : index - 100;
        int indexEnd = (index + 100) > textBodyHTML.length() ? textBodyHTML.length() : index + 100;
        String snippet = textBodyHTML.substring(indexSart, indexEnd);

        return snippet;
    }
}
