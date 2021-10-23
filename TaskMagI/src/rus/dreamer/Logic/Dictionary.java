package rus.dreamer.Logic;

import java.sql.*;

/**
 * Класс для получения словарей с БД "МАГ"
 */
public class Dictionary {

    /**
     * Получает данные словаря
     * @param table - словарь
     */
    public static void getDictionary(String table){
        try{
            ResultSet rs;
            Connection con = Settings.getDB().getConnection();
            CallableStatement cst = con.prepareCall("{? = call usp_DictionarySlRead(?)}");
            cst.registerOutParameter("RETURN_VALUE",Types.INTEGER);
            cst.setString(1,table);
            Boolean res =  cst.execute();

        } catch (SQLException e){
            e.printStackTrace();
            Log.logAdd(e.getMessage());
        }
    }
}
