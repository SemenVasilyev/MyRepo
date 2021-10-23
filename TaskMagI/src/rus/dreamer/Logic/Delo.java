package rus.dreamer.Logic;

import java.sql.*;
import java.util.ArrayList;

public class Delo {
    public int id;
    String title;
    String caption;

    public Delo(int id, String title, String caption){
        this.id = id;
        this.title = title;
        this.caption = caption;
    }

    public Delo(String title){
        this.title = title;
        this.caption = "ИНОЕ";
    }

    public String getDelo(){
        return this.title;
    }

    public int getId(){ return this.id; }

    public static ArrayList<Delo> getfromDb(DB database){
        ArrayList<Delo> deloList = new ArrayList<Delo>();
        try {
            Connection con = database.getConnection();
            ResultSet rs;
            Statement st = con.createStatement();
            rs = st.executeQuery("SELECT id, title, caption FROM sl_Delo");
            while(rs.next()){
                deloList.add(new Delo(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
            con.close();
            return deloList;
        } catch (SQLException e) {
            e.printStackTrace();
            Log.logAdd(e.getMessage(),"error");
            return null;
        }
    }


    public  boolean addInDB(DB database){
        Connection con = database.getConnection();
        ResultSet rs;

        try{
            CallableStatement cst =  con.prepareCall("{? = call usp_DictionaryInsert(?,?,?,?)}");
            cst.registerOutParameter("RETURN_VALUE", Types.INTEGER);
            cst.setString("table_name", "sl_Delo");
            cst.registerOutParameter("id", Types.INTEGER);
            cst.setString("title", this.title);
            cst.setString("caption", this.caption);

            cst.execute();

            this.id = cst.getInt("id");

            if (id != 0){
                return true;
            }
        }catch(SQLException e){
            e.printStackTrace();
            Log.logAdd(e.getMessage(),"error");
            return false;
        }
        return false;
    }

    public String toString(){
        return this.title;
    }
}
