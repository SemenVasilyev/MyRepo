package rus.dreamer.Logic.paragraph;

import rus.dreamer.Logic.Log;
import rus.dreamer.Logic.Settings;

import java.sql.*;

public class Document {
    String id;
    String caption;
    String printV;

    public Document(String merName){
        this.caption =  merName;
        this.printV = "Задание на проведение ОТМ " + merName;

    }
    public boolean getID(){
        ResultSet rs;
        try{
            Connection con = Settings.getDatabaseParagraph().getConnection();
            CallableStatement cst = con.prepareCall("{? = call get_VidDoc_id(?,?,?,?)}");
            cst.registerOutParameter("RETURN_VALUE", Types.INTEGER);
            cst.registerOutParameter("VidDoc_id", Types.NVARCHAR);
            cst.setString("VidDoc", caption);
            cst.setString("PrintVidDoc", printV);
            cst.setObject("OperName",null);

            cst.execute();
            this.id = cst.getString("VidDoc_id");
            if (this.id.length() != 0){
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
            Log.logAdd(e.getMessage());
            return false;
        }

        return false;
    }
}
