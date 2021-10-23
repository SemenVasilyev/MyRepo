package rus.dreamer.Logic;

import java.sql.*;
import java.util.ArrayList;


public class FIO {
    int id;
    String title;
    String caption;


    /**
     *
     * @param id
     * @param title
     * @param caption
     */
    public FIO(int id, String title, String caption){
        this.id = id;
        this.title = title;
        this.caption = caption;
    }

    /**
     *
     * @param title
     * @param caption
     */
    public FIO( String title, String caption){
        this.title = title;
        this.caption = caption;
    }

    /**
     *
     * @param database
     * @param title
     * @return
     */
    public static FIO searchFIO(DB database, String title){
        FIO iniFio = null;
        try {
            Connection con = database.getConnection();
            ResultSet rs;
            PreparedStatement ps  = null;
            ps = con.prepareCall("SELECT id, title, caption FROM sl_FamIni WHERE title = ?");

            ps.setString(1, title);
            rs = ps.executeQuery();
            while(rs.next()){
                iniFio = new FIO(rs.getInt(1), rs.getString(2),rs.getString(3));
            }
            con.close();
            return iniFio;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param database
     * @param id
     * @return
     */
    public static FIO searchFIO(DB database, int id){
        FIO iniFio = null;
        try {
            Connection con = database.getConnection();
            ResultSet rs;
            PreparedStatement ps  = null;
            ps = con.prepareCall("SELECT id, title, caption FROM sl_FamIni WHERE id = ?");

            ps.setInt(1, id);
            rs = ps.executeQuery();
            while(rs.next()){
                iniFio = new FIO(rs.getInt(1), rs.getString(2),rs.getString(3));
            }
            con.close();
            return iniFio;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param database
     * @return
     */
    public  Boolean searchFIO(DB database){
        try {
            Connection con = database.getConnection();
            ResultSet rs;
            PreparedStatement ps  = null;
            ps = con.prepareCall("SELECT id, title, caption FROM sl_FamIni WHERE title = ? ORDER BY id ASC");

            ps.setString(1, this.title);
            rs = ps.executeQuery();
            while(rs.next()){
                this.id = rs.getInt(1);
                this.title = rs.getString(2);
                this.caption = rs.getString(3);
            }
            con.close();
            if (this.id == 0){
                return false;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Добавляем элемент в БД
     * @param database
     * @return
     */
    public  Boolean addInDB(DB database){
        try{
            Connection con = database.getConnection();
            ResultSet rs;
            String query = "INSERT INTO sl_FamIni (title, caption) VALUES (?,?)";
            PreparedStatement ps  = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, this.title);
            ps.setString(2, this.caption);
            Boolean result = ps.execute();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()){
                this.id = generatedKeys.getInt(1);  // получаем id значение нашего элемента
            }
            Log.logAdd("В БД \"Учёта заданий\" таблицу sl_FamIni добавлена запись:" + this.id +" "+ this.title +" "+ this.caption ,"info");
            return true;

        }catch (SQLException s){
            s.printStackTrace();
            return false;
        }
    }
}
