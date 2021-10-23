package main.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.UtilityClass;
import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.english.EnglishLuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;

import java.util.*;

@UtilityClass
public class Lemmatizer {

    private static LuceneMorphology luceneMorphRussian;
    private static LuceneMorphology luceneMorphEnglish;

    static {
        try {
            luceneMorphRussian = new RussianLuceneMorphology();
            luceneMorphEnglish = new EnglishLuceneMorphology();
        } catch (Exception e) {
           // Log.error("Model.Lemmatizer: not created LuceneMorphology");
        }
    }

    public static Map<String, Integer> lemmatize(String string) {
        String[] words = cleanFromPunctuation(string).toLowerCase().split("\\s+");

        Map<String, Integer> result = new HashMap<>();

        Arrays.stream(words).map(word -> getBaseForm(word)).
                filter(wordBaseForm -> isWord(wordBaseForm)).
                forEach(wordBaseForm -> {
                    String normalWord = wordBaseForm.substring(0, wordBaseForm.indexOf("|"));
                    if (result.containsKey(normalWord)) {
                        result.put(normalWord, result.get(normalWord) + 1);
                    } else {
                        result.put(normalWord, 1);
                    }
                });

        return result;
    }

    public static Map<String, String> lemmatizeWithMatching(String string) {
        String[] words = cleanFromPunctuation(string).toLowerCase().split("\\s+");

        Map<String, String> result = new HashMap<>();

        Arrays.stream(words).map(word -> new wordAndNormalForm(word, getBaseForm(word))).
                filter(wordAndNormalForm -> isWord(wordAndNormalForm.getNormalFormm())).
                forEach(wordAndNormalForm -> {
                    String word = wordAndNormalForm.getWord();
                    String nomalForm = wordAndNormalForm.getNormalFormm();
                    String normalWord = nomalForm.substring(0, nomalForm.indexOf("|"));
                    if (!result.containsKey(normalWord)) {
                        result.put(word, normalWord);
                    }
                });

        return result;
    }


    private static boolean isWord(String wordBaseForms) {
        return !wordBaseForms.contains("МЕЖД")
                && !wordBaseForms.contains("ПРЕДЛ")
                && !wordBaseForms.contains("СОЮЗ")
                && !wordBaseForms.equals("");
    }

    public static String getBaseForm(String word) {
        try {
            return luceneMorphRussian.getMorphInfo(word).get(0);
        } catch (Exception e) {
           // Model.Log.error("Model.Lemmatizer: not recognized as a Russian word \"" + word + "\"");
        }
        try {
            return luceneMorphEnglish.getMorphInfo(word).get(0);
        } catch (Exception e) {
            //Model.Log.error("Model.Lemmatizer: not recognized as a English word \"" + word + "\"");
        }
     //   Log.error("Model.Lemmatizer: not recognized as a word \"" + word + "\"");
        return "";
    }

    public static String cleanFromPunctuation(String string) {
        return string.replaceAll("[^a-zA-Zа-яА-Я ]", " ");
    }

    @Data
    @AllArgsConstructor
    private static class wordAndNormalForm {
         private String word;
         private String normalFormm;
    }
}