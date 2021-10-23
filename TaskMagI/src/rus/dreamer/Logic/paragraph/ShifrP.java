package rus.dreamer.Logic.paragraph;

import rus.dreamer.Logic.Settings;
import rus.dreamer.Logic.*;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShifrP {
    public String id;
    public String title;

    ShifrP(String title ){
        this.title = title;
    }

    public String getid(){return this.id;}

    /**
     * Получаем ID шифра по названию
        */
    public Boolean getShifr(){
        Integer jurId = 0;
        try {
            Connection con = Settings.getDatabaseParagraph().getConnection();
            ResultSet rs;

            PreparedStatement ps = con.prepareCall("SELECT id FROM T_Shifr WHERE ShifrNum = ?");
            ps.setString(1, this.title);
            rs = ps.executeQuery();

            while(rs.next()){
                this.id = rs.getString(1);
            }
            con.close();

            if(!this.id.isEmpty()){
                return true;
            }

        } catch (SQLException e){
            e.printStackTrace();
            Log.logAdd(e.getMessage(), "error");

            return false;
        }
         catch (NullPointerException e){
             e.printStackTrace();
             Log.logAdd(e.getMessage(),"error");
             return false;
        }
        return false;
    }
}
