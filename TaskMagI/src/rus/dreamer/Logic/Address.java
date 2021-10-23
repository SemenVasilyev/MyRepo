package rus.dreamer.Logic;

import java.sql.*;

public class Address {
    int id;
    int objId;
    //String region;
    //String section;
    String town;
    String street;
    Integer building;
    String korp;
    Integer flat;

    Address(String town,String street, Integer building, String korp, Integer flat){
        this.town = town;
        this.street = street;
        this.building = building;
        this.korp = korp;
        this.flat = flat;
    }

    public Boolean addInDb(DB database){
        try{
            Connection con = database.getConnection();
            ResultSet rs;
            CallableStatement cst =  con.prepareCall("{? = call usp_impAdressAdd(?,?,?,?,?,?,?,?,?)}");
            cst.registerOutParameter("RETURN_VALUE", Types.INTEGER);
            cst.setString("region","");
            cst.setString("section","");
            cst.setString("town",this.town);
            cst.setString("street", this.street);
            cst.setObject("building", this.building);
            cst.setString("korp", this.korp);
            cst.setObject("flat", this.flat);
            cst.registerOutParameter("person_id",Types.INTEGER);
            cst.registerOutParameter("object_id", Types.INTEGER);

            cst.execute();
            this.id = cst.getInt("person_id");
            this.objId = cst.getInt("object_id");

            if (this.id != 0){
                return true;
            }

        }catch(SQLException e){
            e.printStackTrace();
            Log.logAdd(e.getMessage(), "error");
        }
        return false;
    }

}
