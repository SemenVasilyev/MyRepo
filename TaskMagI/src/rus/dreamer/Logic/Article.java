package rus.dreamer.Logic;

import rus.dreamer.GUI.MainWindow;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class Article {
    int crimactItemID;
    int id;
    public String title;
    String caption;

    public Article(int id, String title, String caption){
        this.id = id;
        this.caption = caption;
        this.title = title;
    }

    public static ArrayList<Article> getFromDB(DB database){
        ArrayList<Article> articlesL = new ArrayList<Article>();
        try {
            Connection con = database.getConnection();
            ResultSet rs;
            Statement st = con.createStatement();
            rs = st.executeQuery("SELECT id, title, caption FROM sl_ItemsCrimCode ORDER BY title ASC");
            while(rs.next()){
                articlesL.add(new Article(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
            con.close();
            return articlesL;
        } catch (SQLException e) {
            e.printStackTrace();
            Log.logAdd(e.getMessage(),"error");
            return null;
        }
    }

    public String toString(){
        return this.title + ". " + this.caption;
    }

    public boolean crimaActInDB(DB database, int taskID,  int artPart){
        ResultSet rs;
        try{
            Connection con = database.getConnection();
            CallableStatement cst =  con.prepareCall("{? = call usp_TasksCrimItemAdd(?,?,?,?,?)}");
            cst.registerOutParameter("RETURN_VALUE", Types.INTEGER);
            cst.registerOutParameter("crim_item_id", Types.INTEGER);
            cst.setInt("task_id", taskID);
            cst.setInt("crim_item", this.id);
            cst.setInt("part_item", artPart);
            cst.setObject("report", null);
            cst.execute();

            this.crimactItemID = cst.getInt("crim_item_id");
            if ( this.crimactItemID != 0){
                return  true;
            }

        }catch (SQLException e){
            e.printStackTrace();
            Log.logAdd(e.getMessage(),"error");
            return false;

        }
        return false;
    }
}
