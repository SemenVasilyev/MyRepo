package rus.dreamer.Logic.paragraph;

import rus.dreamer.Logic.Log;
import rus.dreamer.Logic.Settings;

import java.sql.*;

public class Grif {
    String id;
    String caption;

    public Grif(String caption){
        this.caption = caption;
    }

    public Boolean getId(){
        ResultSet rs;
        try{
            Connection con = Settings.getDatabaseParagraph().getConnection();
            CallableStatement cst = con.prepareCall("{? = call get_Grif_id(?,?)}");
            cst.registerOutParameter("RETURN_VALUE", Types.INTEGER);
            cst.registerOutParameter("Grif_id", Types.NVARCHAR);
            cst.setString("Grif", caption);

            cst.execute();
            this.id = cst.getString("Grif_id");
            if (this.id.length() != 0){
                return true;
            }

        }catch (SQLException e){
            e.printStackTrace();
            Log.logAdd(e.getMessage(),"errro");
            return false;
        }

        return false;
    }
}
