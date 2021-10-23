package rus.dreamer.Logic;

import java.sql.*;

public class Person {
    int id;         // ID из dt_Phiz (phiz_id)
    int objID;      // ID из таблицы dt_ObjectCode
    String family;
    String name;
    String surname;
    java.util.Date dateBirth;
    String sex;

    Person(String family, String name, String surname, java.util.Date dateBirth, String sex ){
        this.family = family;
        this.name = name;
        this.surname = surname;
        this.dateBirth = dateBirth;
        this.sex = sex;
    }


    public  Boolean addInDB(DB database ){
        try{
            ResultSet rs;
            Connection con = database.getConnection();
            CallableStatement cst = con.prepareCall("{? = call usp_impPhysicsAdd(?,?,?,?,?,?,?)}");
            cst.registerOutParameter(1, Types.INTEGER);
            cst.setString(2, this.family);               // @family
            cst.setString(3, this.name);                 //	@name		nvarchar(50),
            cst.setString(4, this.surname);              // @surname
            cst.setDate(5,this.dateBirth!= null? new java.sql.Date(this.dateBirth.getTime()): null);              //this.dateBirth
            cst.setString(6, this.sex!=null?this.sex.toString():"");      // @sex_str
            cst.registerOutParameter(7, Types.INTEGER); //@person_id
            cst.registerOutParameter(8, Types.INTEGER); //@object_id
            cst.execute();

            this.id = cst.getInt(7);
            this.objID = cst.getInt(8);

            if (cst.getInt(1) == 0){
                return true;
            }

                return false;

        }catch(SQLException s){
            s.printStackTrace();
            Log.logAdd(s.getMessage(),"error");
            return false;
        }
    }


    public  Boolean addNothing(DB database ){
        try{
           // ArrayList<Person> personL = new ArrayList<Person>();
            ResultSet rs;
            Connection con = database.getConnection();
            Statement st = con.createStatement();
            rs = st.executeQuery("SELECT TOP(1)\n" +
                    "dbo.dt_Phiz.phiz_id,\n" +
                    "dbo.dt_ObjectCode.object_id\n" +
                    "FROM dbo.dt_Phiz\n" +
                    "INNER JOIN dbo.dt_ObjectCode ON dbo.dt_Phiz.phiz_id = dbo.dt_ObjectCode.person_id\n" +
                    "WHERE\n" +
                    "dbo.dt_Phiz.fio LIKE 'НЕУСТАНОВЛЕННОЕ ЛИЦО'\n" +
                    "ORDER BY\n" +
                    "dbo.dt_Phiz.phiz_id DESC");
                while (rs.next()){
                    this.id = rs.getInt(1);
                    this.objID =rs.getInt(2);
                }

            if(this.id != 0) {
                return true;
            }
            return false;

        }catch(SQLException s){
            s.printStackTrace();
            Log.logAdd(s.getMessage(),"error");
            return false;
        }
    }
}

