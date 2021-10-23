package rus.dreamer.Logic.paragraph;


import rus.dreamer.Logic.*;
import rus.dreamer.Logic.Settings;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Dreamer on 13.03.15.
 */
public class Oper {
    String id;
    String name;
    String code;

    Oper(String id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public static ArrayList<Oper> getFromDB() {
        ArrayList<Oper> operL = new ArrayList<Oper>();
        try {
            Connection con = Settings.getDatabaseParagraph().getConnection();
            ResultSet rs;
            Statement st = con.createStatement();
            rs = st.executeQuery("SELECT id, OperName, OperCode FROM T_Oper ORDER BY OperName ASC ");
            while (rs.next()) {
                operL.add(new Oper(rs.getString(1), rs.getString(2), rs.getString(3)));
            }
            con.close();
            return operL;
        } catch (SQLException e) {
            e.printStackTrace();
            Log.logAdd(e.getMessage(),"error");
            return null;
        }
    }

    public String toString(){
        return this.name + "("+this.code+")";
    }
}

