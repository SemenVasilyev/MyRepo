package rus.dreamer.Logic;

import java.sql.*;
import java.util.ArrayList;

public class Podrazd {
    int id;
    String title;
    int organ_id;

    public Podrazd(int id, String name){
        this.id = id;
        this.title = name;
    }

    public Podrazd(String name){
        this.title = name;
    }


     public Boolean searchInDB(DB database){
        try{
            Connection con = database.getConnection();
            ResultSet rs;
            PreparedStatement ps  = null;
                        ps = con.prepareCall("SELECT id, title, organ_id FROM sl_Podrazd WHERE Replace(title, ' ','') = ?");

            ps.setString(1, this.title.replace(" ", ""));
            rs = ps.executeQuery();
            while(rs.next()){
                this.id = rs.getInt(1);
                this.title = rs.getString(2);
                this.organ_id = rs.getInt(3);
            }
            con.close();
            if (this.id != 0){
                //если подразделение существует то осуществляем поиск и подстановку органа по ID
                return true;
            }
            else return false;

        }catch(SQLException s){
            s.printStackTrace();
            return false;
        }
    }


    public static Podrazd searchInDB(DB database, int id){
        Podrazd podrazd = null;
        try{
            Connection con = database.getConnection();
            ResultSet rs;
            PreparedStatement ps  = null;
            ps = con.prepareCall("SELECT id, title FROM sl_Podrazd WHERE id = ?");

            ps.setInt(1, id);
            rs = ps.executeQuery();
            while(rs.next()){
                podrazd = new Podrazd(rs.getInt(1),rs.getString(2));
            }
            con.close();
            return podrazd;

        }catch(SQLException s){
            s.printStackTrace();
            Log.logAdd(s.getMessage(),"error");
            return null;
        }
    }

    public static String getPodrazdVisirID(int id){
        DB database  = Settings.getDB();
        try{
            Connection con = database.getConnection();
            ResultSet rs;
            PreparedStatement ps  = null;
            ps = con.prepareCall("SELECT title FROM sl_Podrazd WHERE visir_podrazd_id = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while(rs.next()){
                return rs.getString(1);
            }
            con.close();
            return "НЕ УСТАНОВЛЕН";

        }catch(SQLException s){
            s.printStackTrace();
            Log.logAdd(s.getMessage(),"error");
            return null;
        }
    }

    //Добавление подразделения(использоваться не должно)
    public Boolean addInDB(DB database) {
        try{
            database = Settings.getDB();
            Connection con = database.getConnection();
            ResultSet rs;
            //String query = "INSERT INTO sl_Organ (title) VALUES (?)";
            String query = "INSERT INTO sl_Podrazd (title) VALUES (?)";
            PreparedStatement ps  = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, this.title);
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()){
                this.id = generatedKeys.getInt(1);  // получаем id значение нашего элемента
            }
            return true;
        } catch (SQLException s){
            s.printStackTrace();
            Log.logAdd(s.getMessage(),"error");
            return false;
        }
    }

    //Список подразделений(использоваться не должно)
    public  static ArrayList<Podrazd> getPodrazdList(){
        DB database  = Settings.getDB();
        ArrayList<Podrazd> podrazdList = new ArrayList<Podrazd>();
        try{
            Connection con = database.getConnection();
            ResultSet rs;
            PreparedStatement ps  = null;
            //ps = con.prepareCall("SELECT id, title FROM sl_Organ WHERE caption ='*' ORDER BY title  ASC "); //изначальное значение
            ps = con.prepareCall("SELECT id, title FROM sl_Podrazd  ORDER BY title  ASC ");
            rs = ps.executeQuery();
            while(rs.next()){
                podrazdList.add(new Podrazd(rs.getInt(1),rs.getString(2)));
            }
            con.close();
            return podrazdList;

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
