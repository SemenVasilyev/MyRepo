import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ParseMyJson {
    static MetroMap metroMap = new MetroMap();

    public static MetroMap parse(String file) throws IOException {
        JsonParser jsonParser = new JsonParser();

        try (FileReader fileReader = new FileReader(file)) {
            Object obj = jsonParser.parse(fileReader);
            JsonObject jsonData = (JsonObject) obj;

            JsonArray linesArray = (JsonArray) jsonData.get("lines");
            parseLines(linesArray);

            JsonArray stationsArray = (JsonArray) jsonData.get("stations");
            parseStations(stationsArray);

            JsonArray connectionsArray = (JsonArray) jsonData.get("connections");
            parseConnections(connectionsArray);
            System.out.println("");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return metroMap;
    }

    private static void parseLines(JsonArray linesArray) {
        linesArray.forEach(lineObject -> {
            JsonObject lineJsonObject = (JsonObject) lineObject;
            Line line = new Line(lineJsonObject.get("number").getAsString(), lineJsonObject.get("name").getAsString());
            metroMap.addLines(line);
        });
    }

    private static void parseStations(JsonArray stationsArray) {
        stationsArray.forEach(lineObject -> {
            JsonObject lineJsonObject = (JsonObject) lineObject;
            Stations stations = new Stations(lineJsonObject.get("number").getAsString());
            JsonArray stationsLine = lineJsonObject.getAsJsonArray("stations");
            stationsLine.forEach(station -> {
                JsonObject stationlineJsonObject = (JsonObject) station;
                stations.addStation(new Station(stationlineJsonObject.get("line").getAsString(), stationlineJsonObject.get("name").getAsString()));
            });
            metroMap.addStations(stations);
        });
    }

    private static void parseConnections(JsonArray connectionsArray) {
        connectionsArray.forEach(connectionObject -> {
            JsonObject connection = (JsonObject) connectionObject;
            JsonObject station1JO = (JsonObject) connection.get("station1");
            JsonObject station2JO = (JsonObject) connection.get("station2");
            Station station1 = new Station(station1JO.get("line").getAsString(),station1JO.get("name").getAsString());
            Station station2 = new Station(station2JO.get("line").getAsString(),station2JO.get("name").getAsString());
            metroMap.addConnections(new Connections(station1,station2));
        });
    }





}
