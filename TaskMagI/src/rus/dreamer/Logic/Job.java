package rus.dreamer.Logic;

import java.sql.*;

public class Job {
    int id;
    int objId;
    String fullName;

    Job(String fullName){
        this.fullName = fullName;
    }

    public boolean addInDB(DB database){
        Connection con = database.getConnection();
        ResultSet rs;
        try{
            CallableStatement cst =  con.prepareCall("{? = call usp_impJuridicalAdd(?,?,?,?,?,?)}");
            cst.registerOutParameter("RETURN_VALUE", Types.INTEGER);
            cst.setObject("short_name", null);
            cst.setString("full_name", this.fullName);
            cst.setObject("having", "");
            cst.setObject("sphere", "");
            cst.registerOutParameter("person_id", Types.INTEGER);
            cst.registerOutParameter("object_id", Types.INTEGER);

            cst.execute();


            this.objId = cst.getInt("person_id");

            if (this.objId != 0){
                this.id = cst.getInt("person_id");
                this.objId = cst.getInt("object_id");
                return true;
            }
         }
        catch(SQLException s){
            s.printStackTrace();
            Log.logAdd(s.getMessage(),"error");
        }

        return true;
    }



}
