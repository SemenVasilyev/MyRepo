package rus.dreamer.Logic;

import java.sql.*;


public class ObjectT {
    int objectId;
    Person person;
    Job job;
    Address address;
    Address jobAddress;


    ObjectT(DB database, String name, String family, String surname, java.util.Date dateBirth, String sex, String town, String street, String building, String korp, String flat , String jobName, String jobTown, String jobStreet,String jobBuilding, String jobKorp,String jobFlat) {
      //  Boolean res;
        if(family.length()<1 || family.toLowerCase().contains("устан")){
            if(Settings.getUndPersInEach()){
                this.person = new Person("НЕ УСТАНОВЛЕННО","НЕ УСТАНОВЛЕННО" , "НЕ УСТАНОВЛЕННО", null, null);
            }
            else {
                this.person = new Person("НЕ УСТАНОВЛЕННО",null , null, null, null);
            }
        }else{
            this.person = new Person(name, family, surname, dateBirth, sex);
        }
        this.address = new Address(town, street, building.length()>0? Integer.parseInt(building):null, korp, flat.length()>0?Integer.parseInt(flat):null);
        this.job = new Job(jobName);

//try-костыль, в номер дома вместо числа писали строку с улицей
        try {
            Integer.parseInt(jobBuilding);
        }catch(Exception e){jobBuilding=""; }

        this.jobAddress = new Address(jobTown, jobStreet, jobBuilding.length()>0? Integer.parseInt(jobBuilding):null, jobKorp, jobFlat.length()>0?Integer.parseInt(jobFlat):null);
    }

    public Boolean add(DB database, int taskId) {
        Boolean res;

        // Добавляем объект
        if(person.family.toLowerCase().contains("установ") && person.name == null){ //person.name.equals("")
            res = this.person.addNothing(database);
        }
        else{
            res = this.person.addInDB(database);
        }

        if (res) {
            res= addInDB(database, taskId, 1, this.person.objID);
            if (!res)
                return false;
        }
        else
            return false;

        // Добавляем адрес
        res = this.address.addInDb(database);

        if (res) {
            res = addInDB(database, taskId,3, this.address.objId);
            if (!res)
                return false;
        }

        // Добавляем  место работы
        res = this.job.addInDB(database);

        if (res) {
            res = addInDB(database, taskId,2, this.job.objId);
        }

        // Добавляем адрес места работы
        if (res) {
            res = this.jobAddress.addInDb(database);

            if (res) {
                res = addInDB(database, taskId,4, this.jobAddress.objId);
            }
        }

        return true;
    }


    public Boolean addInDB(DB database, int taskId ,int typeTask, int objTypeId) {

        try {
            Connection con = database.getConnection();
            ResultSet rs;
            CallableStatement cst = con.prepareCall("{? = call usp_TasksObjectAdd(?,?,?,?)}");
            cst.registerOutParameter("RETURN_VALUE", Types.INTEGER);
            cst.registerOutParameter("task_object_id", Types.INTEGER);
            cst.setInt("task_id", taskId);
            cst.setInt("type_task_object_id", typeTask);
            cst.setInt("object_id", objTypeId);
            cst.execute();

            this.objectId = cst.getInt("task_object_id");
            if (this.objectId != 0)
                return true;

        } catch (SQLException s) {
            s.printStackTrace();
            Log.logAdd(s.getMessage(),"error");
            return false;
        }
            return false;
    }


    public Boolean searchInDB(DB database, int taskId) {
        try {
            Connection con = database.getConnection();
            ResultSet rs;
            CallableStatement cst = con.prepareCall("{? = call usp_TasksObjectSearch(?,?,?,?)}");
            cst.registerOutParameter("RETURN_VALUE", Types.INTEGER);
            cst.setInt("task_object_id", this.objectId);
            cst.setInt("task_id", taskId);
            cst.setInt("type_task_object_id", 1);
            cst.setInt("object_id", this.person.objID);
            rs = cst.executeQuery();

           if(cst.getInt("RETURN_VALUE")==0)
               return false;
        } catch (SQLException e) {
            e.printStackTrace();
            Log.logAdd(e.getMessage(),"error");
            return false;
        }
        return false;
    }

}


