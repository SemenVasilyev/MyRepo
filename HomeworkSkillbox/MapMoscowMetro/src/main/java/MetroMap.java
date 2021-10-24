import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

class MetroMap{
    private ArrayList<Line> lines;
    private ArrayList<Stations> stations;
    private Set<Connections> connections;

    public MetroMap(ArrayList<Line> linesArray, ArrayList<Stations> stationsArray, Set<Connections> connectionsSet) {
        this.lines = linesArray;
        this.stations = stationsArray;
        this.connections = connectionsSet;
    }

    public MetroMap() {
        lines = new ArrayList<>();
        stations = new ArrayList<>();
        connections = new HashSet<>();
    }

    public void addLines(Line lines) {
        this.lines.add(lines);
    }

    public void addStations(Stations stations) {
        this.stations.add(stations);
    }

    public void addConnections(Connections connections) {
        this.connections.add(connections);
    }

    public void print(){
        System.out.println("Линии:");
        lines.forEach(System.out::println);
        System.out.println("Станции:");
        stations.forEach(System.out::println);
        System.out.println("Переходы:");
        connections.forEach(System.out::println);
    }
}