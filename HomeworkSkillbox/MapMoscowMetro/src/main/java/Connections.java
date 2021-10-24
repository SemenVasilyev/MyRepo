class Connections {
    Station station1;
    Station station2;

    public Connections(Station station1, Station station2) {
        this.station1 = station1;
        this.station2 = station2;
    }

    public Station getStation1() {
        return station1;
    }

    public Station getStation2() {
        return station2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Connections that = (Connections) o;
        return (station1.equals(((Connections) o).getStation1()) && station2.equals(getStation2()) ||
                station1.equals(((Connections) o).getStation2()) && station2.equals(getStation1()));
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return "Пересечение станций:\n" + station1 + station2 ;
    }
}