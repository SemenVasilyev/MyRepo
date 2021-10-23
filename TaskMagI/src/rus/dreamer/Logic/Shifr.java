package rus.dreamer.Logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Shifr {
    private int id;
    protected String title;

    public Shifr(int id, String title){
        this.id = id;
        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }

    public int getId() {return  this.id;}

    public static ArrayList<Shifr> getfromDb(DB database){
        ArrayList<Shifr> shifrList = new ArrayList<Shifr>();
        try {
            Connection con = database.getConnection();
            ResultSet rs;
            Statement st = con.createStatement();
            rs = st.executeQuery("SELECT id, title, caption FROM sl_Object");
            while(rs.next()){
                shifrList.add(new Shifr(rs.getInt(1), rs.getString(2)));
            }
            con.close();
            return shifrList;
        } catch (SQLException e) {
            e.printStackTrace();
            Log.logAdd(e.getMessage(),"error");
            return null;
        }
    }

    public String toString(){
        return this.title;
    }
}


