class Line {
    private String number;
    private String name;

    public Line(String number, String name) {
        this.number = number;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Линия №" + number + ", название линии: " + name;
    }
}