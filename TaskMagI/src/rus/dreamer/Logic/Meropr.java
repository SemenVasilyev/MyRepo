package rus.dreamer.Logic;

import java.sql.*;
import java.util.ArrayList;

public class Meropr {
    int id;
    String title;

    public Meropr(int id, String title){
        this.id = id;
        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }
    public int getID(){return this.id;};

    /**
     *
     * @param database
     * @return
     */
    public static ArrayList<Meropr> getfromDb(DB database){
        ArrayList<Meropr>  merList = new ArrayList<Meropr>();
        try {
            Connection con = database.getConnection();
            ResultSet rs;
            Statement st = con.createStatement();
            rs = st.executeQuery("SELECT id, title, caption FROM sl_Meropr");
            while(rs.next()){
                merList.add(new Meropr(rs.getInt(1), rs.getString(2)));
            }
            con.close();
            return merList;
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
