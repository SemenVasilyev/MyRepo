package rus.dreamer.Logic;

import java.sql.*;
import java.util.ArrayList;

public class SanctionPerson {
    Integer id;         // ID из dt_SacntionPerson
    String org;      // Организация
    String post;     // Должность
    String fam;      // Фамилия судьи

    public SanctionPerson(String org, String post, String fam){
        this.org = org;
        this.post = post;
        this.fam = fam;
    }

    public SanctionPerson(int id,String org, String post, String fam){
        this.id = id;
        this.org = org;
        this.post = post;
        this.fam = fam;
    }
    public SanctionPerson(){
        this.id = null;

    }

    public boolean addInDB(DB database){

        try{
            ResultSet rs;
            Connection con = database.getConnection();
            CallableStatement cst = con.prepareCall("{? = call usp_SanctionPersonAdd(?,?,?,?,?)}");
            cst.registerOutParameter("RETURN_VALUE", Types.INTEGER);
            cst.registerOutParameter("sanction_person_id", Types.INTEGER);
            cst.setObject("place", null);
            cst.setString("sanc_org", this.org);
            cst.setString("sanc_post",this.post);
            cst.setString("sanc_fam", this.fam); // Фамилия И.О.
            cst.execute();

            this.id = cst.getInt("sanction_person_id");

            if (this.id != 0){
                Log.logAdd("Добавлено новое санкционирующее лицо: " +  this.fam + this.post+ this.org,"info");
                return true;
            }

            return false;

        }catch(SQLException s){
            s.printStackTrace();
            Log.logAdd(s.getMessage(),"error");
            return false;
        }
    }

    private  ArrayList<SanctionPerson> searchInDB(DB database, String fam, String sanc_org, String sanc_post){
        ArrayList<SanctionPerson> sancPerList = new ArrayList<SanctionPerson>();
        try{
            ResultSet rs;
            Connection con = database.getConnection();
            CallableStatement cst = con.prepareCall("{? = call usp_SanctionPersonSearch(?,?,?,?,?)}");
            cst.registerOutParameter("RETURN_VALUE", Types.INTEGER);
            cst.setString("sanction_person_id", null);
            cst.setString("place", null);
            cst.setString("sanc_org", sanc_org.equals("")? null: sanc_org);
            cst.setString("sanc_post", sanc_post.equals("")? null: sanc_post);
            cst.setString("sanc_fam", fam.equals("")? null: fam);
            cst.execute();
            rs = cst.getResultSet();

            while (rs.next()){
                sancPerList.add(new SanctionPerson(rs.getInt("sanction_person_id"),rs.getString("sanc_org"),rs.getString("sanc_post"), rs.getString("sanc_fam")));
            }
        }catch(SQLException s){
            s.printStackTrace();
            Log.logAdd(s.getMessage(),"error");
        }
        return sancPerList;
    }

    /**
     *Метод для выбора судьи. Выбирем есть ли судья с указанными (ФИО, местом, должностью) если нет,
     * проверяем есть ли судья с указанным ФИО и выводим список, где можно либо выбрать существующего либо создать нового.
     */
    public Boolean checkInDB(DB database){
        Boolean res;
        if (this.id == null){
            if(this.fam.equals("")){ // Если фамилии нет то нам нет нужны создавать судью
                Log.logAdd("Ошибка добавления судьи(отсутсвует фамилия)","error");
                return false;
            }

            ArrayList<SanctionPerson> sanPersList = new ArrayList<SanctionPerson>();    // создаем массив для хранения судей
            sanPersList =  searchInDB(database, this.fam, this.org, this.post);        // ищем судью по фамилии, месту и должности


            if (sanPersList.size()>0){
                if (sanPersList.size()<2){
                    this.id = sanPersList.get(0).id;
                    this.org = sanPersList.get(0).org;
                    this.post = sanPersList.get(0).post;
                    this.fam = sanPersList.get(0).fam;
                }
                else{
                    this.id = sanPersList.get(sanPersList.size()-1).id;
                    this.org = sanPersList.get(sanPersList.size()-1).org;
                    this.post = sanPersList.get(sanPersList.size()-1).post;
                    this.fam = sanPersList.get(sanPersList.size()-1).fam;
                }
                Log.logAdd(String.format("Выбран уже существующий судья: %s, %s, %s, %s", this.id, this.org, this.post, this.fam));
            }
            else{
                res = this.addInDB(database);
                if(!res){
                    Log.logAdd("Ошибка добавления судьи","error");
                }
            }
        }
        return true;
    }
}

