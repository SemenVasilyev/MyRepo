package rus.dreamer.Logic.paragraph;

//import jdk.nashorn.internal.codegen.CompilerConstants;
import rus.dreamer.Logic.Log;

import rus.dreamer.Logic.Settings;

import java.net.ConnectException;
import java.sql.*;
import java.util.ArrayList;


public class AddresP {
    String id;
    String title;
    String mask;

    public AddresP(String title){this.title = title;}

    public AddresP(String title, String id){this.title = title; this.id = id;}

    public String getTitle(){
        return this.title;
    };

    public Boolean getId(){
        ResultSet rs;
        try {
            Connection con = Settings.getDatabaseParagraph().getConnection();
            CallableStatement cst = con.prepareCall("{?=call get_Adress_id](?,?,?,?,?)}");
            cst.registerOutParameter("RETURN_VALUE", Types.INTEGER);
            cst.registerOutParameter("Adress_id", Types.NVARCHAR);
            cst.setString("Adress", null);
            cst.setString("A_Mask", null);
            cst.setString("A_Group", null);
            cst.setInt("Group_id",0);

            cst.execute();

            if (cst.getInt("RETURN_VALUE") == 1){
               this.id =  cst.getString("Adress_id");
                return true;
            }

            return true;
        } catch (SQLException e){
            e.printStackTrace();
            Log.logAdd(e.getMessage(), "error");
            return false;
        }
    }

    public Boolean checkAddresP(){
        try{
            ResultSet rs;
            Connection con = Settings.getDatabaseParagraph().getConnection();
            CallableStatement cst = con.prepareCall("{?=call check_Adress(?,?)}");
            cst.registerOutParameter("RETURN_VALUE",Types.INTEGER);
            cst.registerOutParameter("Adress", Types.NVARCHAR);
            cst.registerOutParameter("Adress_id", Types.NVARCHAR);
            cst.setString("Adress",this.title);
            cst.setString("Adress_id",null);

            Boolean ret = cst.execute();
            Integer res = -1;

            ArrayList<AddresP> addrL = new ArrayList<AddresP>();
            while (true) {
                int rowCount = cst.getUpdateCount();
                if (rowCount > 0) {
                    //  это счетчик обновлений
                    System.out.println("Изменилось строк: " + rowCount);
                    cst.getMoreResults();
                    continue;
                }
                if (rowCount == 0) {
                    // команда DDL или 0 обновлений
                    System.out.println("Строки не менялись; либо выражение было DDL-командой");
                    cst.getMoreResults();
                    continue;
                }
                // если мы до сюда дошли,
                // то у нас либо набор данных (result set),
                // либо результатов больше нет
                rs = cst.getResultSet();
                if (rs != null) {
                    // используем метаданные для получения
                    //информации о колонках набора данных
                    while (rs.next()) {
                        // обрабатываем результаты
                        addrL.add(new AddresP(rs.getString("id"),rs.getString("Result")));
                                               continue;
                    }
                    break;// больше результатов нет
                }
                res = cst.getInt("RETURN_VALUE");
                break;
            }

            if (res== 1){ // Если по запросу найдено одно значение
                this.title = cst.getString("Adress");
                this.id =  cst.getString("Adress_id");
                return true;
            }

            if (res== -1){ // Если по запросу не найдено нечего
                Log.logAdd("По запросу не найдено значений","error");
                return false;
            }

            if (res== 0){
                this.id = addrL.get(addrL.size()-1).id;
                this.title = addrL.get(addrL.size()-1).title;
                return true;
            }

        } catch (SQLException e ){
            e.printStackTrace();
            Log.logAdd(e.getMessage(),"error");
            return false;
        }
        return false;
    }




}
