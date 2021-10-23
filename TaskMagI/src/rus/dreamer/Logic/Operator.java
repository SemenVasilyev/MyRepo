package rus.dreamer.Logic;

import java.sql.*;
import java.util.ArrayList;

/**
 * Класс для работы с объектами типо "Исполнитель"
 */
public class Operator {
    Integer id; // ИД - исполнителя
    String fio; // ФИО - исполнителя

    Operator(Integer id, String fio){
        this.id = id;
        this.fio = fio;
    }

    public  static ArrayList<Operator> getAllOperators()
    {
        DB database  = Settings.getDB();
        ArrayList<Operator> operatorsList = new ArrayList<Operator>();
        try{
            Connection con = database.getConnection();
            ResultSet rs;
            PreparedStatement ps  = null;
            ps = con.prepareCall("SELECT * FROM  v_slOperatorsFIO ORDER BY title ASC");
            rs = ps.executeQuery();
            while(rs.next()){
                operatorsList.add(new Operator(rs.getInt(1),rs.getString(2)));
            }
            con.close();
            return operatorsList;
        }catch(SQLException s){
            s.printStackTrace();
            Log.logAdd(s.getMessage(),"error");
            return null;
        }
    }

    /**
     * Добавляет исполнителя заданию
     * @return
     */

    public  Boolean addOperatorToTask(Integer taskID, java.util.Date beginDate) {
        DB database = Settings.getDB();
        ResultSet rs;
        try{
            Connection con = database.getConnection();
            CallableStatement cst =  con.prepareCall("{? = call usp_TasksDoerAdd(?,?,?,?,?,?)}");
            cst.registerOutParameter("RETURN_VALUE", Types.INTEGER);
            cst.setInt("doer_id", 0);
            cst.setInt("task_id", taskID);
            cst.setInt("oper_id", this.id);
            cst.setDate("begin_date", new java.sql.Date(beginDate.getTime()));
            cst.setDate("end_date", null);
            cst.setInt("main", 1);
            cst.execute();

            if (cst.getInt("RETURN_VALUE")== -1)
                return  true;
            else
                return false;

        }catch (SQLException e){
            e.printStackTrace();
            Log.logAdd(e.getMessage(),"error");
            return false;

        }
    }

    public String toString(){
        return this.fio;
    }
}

