import com.google.gson.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static final String SOURCE = "https://www.moscowmap.ru/metro.html#lines";
    static ArrayList<Line> linesArray = new ArrayList<>();
    static ArrayList<Stations> stationsArray = new ArrayList<>();
    static Set<Connections> connectionsSet = new HashSet<>();

    public static void main(String[] args) throws IOException, InterruptedException {
        parseMetro();
        writerJson();
        MetroMap resultMetroMap = ParseMyJson.parse("filePath.json");
        resultMetroMap.print();

    }

    public static void writerJson() throws IOException {
        MetroMap metroMap = new MetroMap(linesArray, stationsArray, connectionsSet);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileWriter fileWriter = new FileWriter(new File("filePath.json"));
        fileWriter.write(gson.toJson(metroMap));
        fileWriter.close();
    }

    public static void parseMetro() throws IOException {
        Document document = Jsoup.connect(SOURCE).get();
        Elements element = document.select("span.js-metro-line");
        element.forEach(e -> {
            linesArray.add(new Line(e.attributes().get("data-line"), e.text()));
        });

        element = document.select("div.js-metro-stations");
        element.forEach(line -> {
            Stations stations = new Stations(line.attributes().get("data-line"));
            line.select("a").forEach(station -> {
                Station newStation = new Station(line.attributes().get("data-line"), station.select("span.name").text());
                stations.addStation(newStation);
                station.select("span.t-icon-metroln").forEach(transition -> {
                    String lineTransition = transition.className().replace("t-icon-metroln ln-", "");
                    String infoTransition = transition.attributes().get("title");
                    String nameTransition = infoTransition.substring(infoTransition.indexOf("«") + 1, infoTransition.indexOf("»"));
                    Station stationTransition = new Station(lineTransition, nameTransition);
                    Connections newConnections = new Connections(newStation, stationTransition);
                    connectionsSet.add(newConnections);
                });
            });
            stationsArray.add(stations);
        });
    }
}
