import java.util.ArrayList;

class Stations {
    private String number;
    private ArrayList<Station> stations = new ArrayList<>();

    public Stations(String number) {
        this.number = number;
    }



    public void addStation(Station station) {
        stations.add(station);
    }

    @Override
    public String toString() {
        StringBuilder string  = new StringBuilder();
        stations.forEach(station -> {
            string.append(station);
        });
        return "Линия №" + number + "\n" + string;
    }
}