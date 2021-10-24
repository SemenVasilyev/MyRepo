 class Station{
    private String line;
    private String name;

    public Station(String line, String name) {
        this.line = line;
        this.name = name;
    }

     public String getLine() {
         return line;
     }

     public String getName() {
         return name;
     }

    public boolean equals(Station s){

        if (line == s.getLine() && name == s.getName())
           return true;
       return false;
    }

     @Override
     public String toString() {
         return "Линия №" + line + ", название станции: " + name + "\n"  ;
     }
 }