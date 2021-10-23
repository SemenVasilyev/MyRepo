package main.model;

import main.entitys.Index;
import main.entitys.Lemma;
import main.entitys.Page;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Repository {
    private static Map<String, Lemma> lemmaHashMap = new ConcurrentHashMap<>();
    private static Map<String,Page>  pageHashMap = new ConcurrentHashMap<>();
    private static Map<String, Index> indexHashMap = new ConcurrentHashMap<>();

    public static void put(Lemma lemma) {
        lemmaHashMap.put(lemma.getLemma(), lemma);
    }

    public static void put(Page page) {
        pageHashMap.put(page.getPath(), page);
    }

    public static void put(Index index) {
        String key = index.getLemma().getId() + ":" + index.getPage().getId();
        indexHashMap.put(key, index);
    }

    public static boolean contains(Lemma lemma) {
        return lemmaHashMap.containsKey(lemma.getLemma());
    }

    public static boolean contains(Page page) {
        return pageHashMap.containsKey(page.getPath());
    }

    public static boolean contains(Index index) {
        String key = index.getLemma().getId() + ":" + index.getPage().getId();
        return indexHashMap.containsKey(key);
    }

    public static Lemma getLemma(Lemma lemma) {
        return lemmaHashMap.get(lemma.getLemma());
    }

    public static Page getPage(Page page) {
        return pageHashMap.get(page.getPath());
    }

    public static Index getIndex(Index index) {
        String key = index.getLemma().getId() + ":" + index.getPage().getId();
        return indexHashMap.get(key);
    }
}
