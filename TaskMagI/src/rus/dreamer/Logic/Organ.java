package rus.dreamer.Logic;

import java.sql.*;
import java.util.ArrayList;

public class Organ {
    int id;
    String title;

    public Organ(int id, String name){
        this.id = id;
        this.title = name;
    }

    public Organ(int id){
        this.id = id;
    }

    public Organ(String name){
        this.title = name;
    }

    //Поиск органа
    public Boolean searchInDB(DB database){
        try{
            Connection con = database.getConnection();
            ResultSet rs;
            PreparedStatement ps  = null;
            ps = con.prepareCall("SELECT id, title FROM sl_Organ WHERE Replace(id, ' ','') = ?");   

            ps.setInt(1,id);   // Изменения от 21.10.2016 by KVV
            rs = ps.executeQuery();
            while(rs.next()){
                this.id = rs.getInt(1);
                this.title = rs.getString(2);
            }
            con.close();
            if (this.id != 0){
                return true;
            }
            else return false;

        }catch(SQLException s){
            s.printStackTrace();
            return false;
        }
    }

    //Список органов(использоваться не должно)
    public  static ArrayList<Organ> getOrganList(){
        DB database  = Settings.getDB();
        ArrayList<Organ> organList = new ArrayList<Organ>();
        try{
            Connection con = database.getConnection();
            ResultSet rs;
            PreparedStatement ps  = null;
            ps = con.prepareCall("SELECT id, title FROM sl_Organ  ORDER BY title  ASC"); //изначальное значение
            rs = ps.executeQuery();
            while(rs.next()){
                organList.add(new Organ(rs.getInt(1),rs.getString(2)));
            }
            con.close();
            return organList;

        }catch(SQLException s){
            s.printStackTrace();
            Log.logAdd(s.getMessage(),"error");
            return null;
        }

    }

    public String toString(){
        return this.title;
    }
}
