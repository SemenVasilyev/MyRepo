package main.model;

import main.entitys.*;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class WorkingWithDB {
   // private static Class classLemma = Lemma.class;

    public static void addToDB(Object entity) {
        String classObject = entity.getClass().getName();
        switch (classObject) {
            case "main.entitys.Lemma":
                addAndReturnLemma((Lemma) entity);
                break;
            case "main.entitys.Page":
                addAndReturnPage((Page) entity);
                break;
            case "main.entitys.Index":
                addAndReturnIndex((Index) entity);
                break;
            case "main.entitys.Field":
                addAndReturnField((Field)entity);
                break;
            case "main.entitys.Site":
                addAndReturnSite((Site)entity);
                break;
            default:
                /// добавить логгер
                break;
        }
    }

    private static Lemma addAndReturnLemma(Lemma lemmaForSearch) {
//        List<Lemma> lemmas = getThisObjectFromDB(lemmaForSearch);
//        Lemma lemma;
//        if (lemmas.isEmpty()) {
//            lemma = lemmaForSearch;
//            MySessionFactory.addEntityToDB(lemma);
//        } else {
//            lemma = lemmas.get(0);
//            lemma.setFrequency(lemma.getFrequency() + 1);
//            MySessionFactory.updateEntityToDB(lemma);
//        }
//        return lemma;

        Lemma lemma;
        if (!Repository.contains(lemmaForSearch)) {
            lemma = lemmaForSearch;
            MySessionFactory.addEntityToDB(lemma);
            lemma = (Lemma) getThisObjectFromDB(lemma);
            Repository.put(lemma);
        } else {
            lemma = Repository.getLemma(lemmaForSearch);
            lemma.setFrequency(lemma.getFrequency() + 1);
            MySessionFactory.updateEntityToDB(lemma);
        }
        return lemma;

    }

    private static Page addAndReturnPage(Page pageForSearch) {
//        List<Page> pages = getThisObjectFromDB(pageForSearch);
//        Page page;
//        if (pages.isEmpty()) {
//            page = pageForSearch;
//            MySessionFactory.addEntityToDB(page);
//        } else {
//            page = pages.get(0);
//        }
//        return page;


        Page page;
        if (!Repository.contains(pageForSearch)) {
            page = pageForSearch;
            MySessionFactory.addEntityToDB(page);
            page = (Page) getThisObjectFromDB(page);
            Repository.put(page);
        } else {
            page = Repository.getPage(pageForSearch);
        }
        return page;

    }


    private static Index addAndReturnIndex(Index indexForSearch) {
//        Page page = addAndReturnPage(indexForSearch.getPage());
//        Lemma lemma = addAndReturnLemma(indexForSearch.getLemma());
//
//        if(!isLemmaAndPage(lemma,page)) {
//            indexForSearch.setPage(page);
//            indexForSearch.setLemma(lemma);
//
//        MySessionFactory.addEntityToDB(indexForSearch);
//        }
//        return indexForSearch;

        Page page = addAndReturnPage(indexForSearch.getPage());
        Lemma lemma = addAndReturnLemma(indexForSearch.getLemma());

        if(!Repository.contains(indexForSearch)) {
            indexForSearch.setPage(page);
            indexForSearch.setLemma(lemma);

            MySessionFactory.addEntityToDB(indexForSearch);
            Repository.put(indexForSearch);
        }
        return indexForSearch;
    }

    private static Field addAndReturnField(Field fieldForSearch) {
        Field fields = (Field) getThisObjectFromDB(fieldForSearch);
        Field field;
        if (fields == null) {
            field = fieldForSearch;
            MySessionFactory.addEntityToDB(field);
        } else {
            field = fields;
            field.setWeight(fieldForSearch.getWeight());
            MySessionFactory.updateEntityToDB(field);
        }
        return field;
    }
    public static Site addAndReturnSite(Site siteForSearch) {
        Site sites =(Site) getThisObjectFromDB(siteForSearch);
        Site site;
        if (sites == null) {
            site = siteForSearch;
            MySessionFactory.addEntityToDB(site);
        } else {
            site = sites;
            site.setStatusSite(siteForSearch.getStatusSite());
            site.setStatusTime(siteForSearch.getStatusTime());
            site.setLastError(siteForSearch.getLastError());
            MySessionFactory.updateEntityToDB(site);
        }
        return site;
    }

    public static Object getFromDB(Object objects){
        Object object = getThisObjectFromDB(objects);
        if (object == null) {
             return null;
        }
        return object;
    }

    private static Object getThisObjectFromDB(Object entity) {
        try (Session session = MySessionFactory.getINSTANCE().openSession()) {

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Object> query = builder.createQuery(Object.class);

            switch (entity.getClass().getName()) {
                case "main.entitys.Lemma":
                    Root<Lemma> rootLemma = query.from(Lemma.class);
                    Lemma lemma = (Lemma) entity;
                    query.select(rootLemma).where(builder.equal(rootLemma.get("lemma"), lemma.getLemma()));
                    break;
                case "main.entitys.Page":
                    Root<Page> rootPage = query.from(Page.class);
                    Page page = (Page) entity;
                    query.select(rootPage).where(builder.equal(rootPage.get("path"), page.getPath()));
                    break;
                case "main.entitys.Index":
                    Root<Index> rootIndex = query.from(Index.class);
                    Index index = (Index) entity;
                    query.select(rootIndex).where(builder.equal(rootIndex.get("lemma"), index.getLemma().getId()),
                                                    builder.equal(rootIndex.get("page"), index.getPage().getId()));
                    break;
                case "main.entitys.Field":
                    Root<Field> rootField = query.from(Field.class);
                    Field field = (Field) entity;
                    query.select(rootField).where(builder.equal(rootField.get("name"), field.getName()));
                    break;
                case "main.entitys.Site":
                    Root<Site> rootSite = query.from(Site.class);
                    Site site = (Site) entity;
                    query.select(rootSite).where(builder.equal(rootSite.get("url"), site.getUrl()));
                    break;
                default:
                    return null;
            }

            List<Object> list = session.createQuery(query).setMaxResults(1).getResultList();

            if (list.isEmpty()){
                return null;
            }

            return list.get(0);
        }

    }

    public static List<Index> getIndexesByLemma(Lemma lemma){
        try(Session session = MySessionFactory.getINSTANCE().openSession()){
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Index> query = builder.createQuery(Index.class);
            Root<Index> rootIndex = query.from(Index.class);
            query.select(rootIndex).where(builder.equal(rootIndex.get("lemma"), lemma.getId()));
            return session.createQuery(query).getResultList();
        }
    }

    public static List<Page> getPagesByLemma(Lemma lemma){
        try(Session session = MySessionFactory.getINSTANCE().openSession()){
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Index> query = builder.createQuery(Index.class);
            Root<Index> rootIndex = query.from(Index.class);
            query.select(rootIndex).where(builder.equal(rootIndex.get("lemma"), lemma.getId()));

            List<Index> indexs = session.createQuery(query).getResultList();

            List<Page> pages = new ArrayList<>();
            for(Index index : indexs){
                pages.add(index.getPage());
            }
            return pages;
        }
    }

    public static boolean isLemmaAndPage(Lemma lemma, Page page){
        try(Session session = MySessionFactory.getINSTANCE().openSession()){
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Index> query = builder.createQuery(Index.class);
            Root<Index> rootIndex = query.from(Index.class);
            query.select(rootIndex).where(builder.equal(rootIndex.get("lemma"), lemma.getId()),
                                             builder.equal(rootIndex.get("page"), page.getId()));


            return !(session.createQuery(query).getResultList()).isEmpty();
        }
    }

    public static Index getIndexByLemmaAndPage(Lemma lemma, Page page){
        try(Session session = MySessionFactory.getINSTANCE().openSession()){
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Index> query = builder.createQuery(Index.class);
            Root<Index> rootIndex = query.from(Index.class);
            query.select(rootIndex).where(builder.equal(rootIndex.get("page"), page.getId()),
                                            builder.equal(rootIndex.get("lemma"), lemma.getId()));

            return session.createQuery(query).setMaxResults(1).getResultList().get(0);
        }
    }


    //------ methods for Statistic.Total
    public static Long getAmountAllLemmas(){
        try(Session session = MySessionFactory.getINSTANCE().openSession()){
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Long> query = builder.createQuery(Long.class);
            query.select(builder.count(query.from(Lemma.class)));

            return session.createQuery(query).getSingleResult();
        }
    }

    public static Long getAmountAllPages(){
        try(Session session = MySessionFactory.getINSTANCE().openSession()){
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Long> query = builder.createQuery(Long.class);
            query.select(builder.count(query.from(Page.class)));

            return session.createQuery(query).getSingleResult();
        }
    }

    public static Long getAmountAllSites(){
        try(Session session = MySessionFactory.getINSTANCE().openSession()){
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Long> query = builder.createQuery(Long.class);
            query.select(builder.count(query.from(Site.class)));

            return session.createQuery(query).getSingleResult();
        }
    }

    public static List<Site> getAllSites() {
        try (Session session = MySessionFactory.getINSTANCE().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Site> query = builder.createQuery(Site.class);
            Root<Site> rootSite = query.from(Site.class);
            query.select(rootSite);

            return session.createQuery(query).getResultList();
        }
    }

    //------ methods for Statistic.DetailedAboutSite
    public static long getAmountPagesForSite(int id){
        try (Session session = MySessionFactory.getINSTANCE().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Page> query = builder.createQuery(Page.class);
            Root<Page> rootPages = query.from(Page.class);
            query.select(rootPages).where(builder.equal(rootPages.get("site"), id));

            return session.createQuery(query).stream().count();
        }
    }

    public static long getAmountLemmasForSite(int id){
        try (Session session = MySessionFactory.getINSTANCE().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Lemma> query = builder.createQuery(Lemma.class);
            Root<Lemma> rootLemma = query.from(Lemma.class);
            query.select(rootLemma).where(builder.equal(rootLemma.get("site"), id));

            return session.createQuery(query).stream().count();
        }
    }
    //----------------------------------------------


}
