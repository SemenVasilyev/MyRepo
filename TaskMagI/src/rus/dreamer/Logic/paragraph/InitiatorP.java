package rus.dreamer.Logic.paragraph;

import rus.dreamer.Logic.Log;
import rus.dreamer.Logic.Settings;

import java.sql.*;

public class InitiatorP {
    String id;
    String fio;
    String phone1;
    String phone2;

    InitiatorP(String fio, String phone1, String phone2){
        this.fio = fio;
        this.phone1 = phone1;
        this.phone2 = phone2;
    }

    public Boolean takeId(){
        Boolean res;
        res = getFIOId();
        if (!res){
           return false;
        }
        return true;
    }

    public Boolean getFIOId(){
        ResultSet rs;
        try{
            Connection con = Settings.getDatabaseParagraph().getConnection();
            CallableStatement cst = con.prepareCall("{? = call get_FIO_id(?,?,?,?)}");
            cst.registerOutParameter("RETURN_VALUE", Types.INTEGER);
            cst.registerOutParameter("FIO_id",Types.NVARCHAR);

            cst.setString("FIO", this.fio);
            cst.setString("Tel1", this.phone1);
            cst.setString("Tel2", this.phone2);

            cst.execute();
            if (cst.getInt("RETURN_VALUE")> 0) {
                this.id = cst.getString("FIO_id");
                return true;
            }
            else {
                return false;
            }

        }catch(SQLException e){
            e.printStackTrace();
            Log.logAdd(e.getMessage(),"error");
            return false;
        }
    }

    @Deprecated
    public Boolean setInitiatorPInDB(){
        ResultSet rs;
        try{
            Connection con = Settings.getDatabaseParagraph().getConnection();
            PreparedStatement pst = con.prepareStatement("INSERT INTO  T_FIO (FIO, Tel, Tel1) VALUES (?,?,?)  RETURNING id INTO :id ");
            pst.setString(1, this.fio);
            pst.setString(2, this.phone1);
            pst.setString(3, this.phone2);

            pst.executeUpdate();

            rs = pst.getGeneratedKeys();

            if ( rs != null && rs.next() )
            {
                this.id = rs.getString(1);
            }

        }catch (SQLException e){
            e.printStackTrace();
            Log.logAdd(e.getMessage(), "error");
            return false;
        }
        return true;
    }
}
