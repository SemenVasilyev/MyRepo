package rus.dreamer.Logic;

import org.apache.commons.lang3.ObjectUtils;
import rus.dreamer.GUI.AddresChoise;
import rus.dreamer.GUI.OrganChoise;

import java.sql.*;
import java.util.ArrayList;


public class Initiator {
    public int id;
    public FIO fio;
    public Podrazd podrazd;
    public Organ organ;
    public String phone1;
    public String phone2;

    public Initiator(FIO fio, Podrazd podrazd, Organ organ, String phone1, String phone2) {
        this.fio = fio;
        this.podrazd = podrazd;
        this.organ = organ;
        this.phone1 = phone1;
        this.phone2 = phone2;

    }

    public Initiator(String fio, String podrazd, String organ, String phone1, String phone2) {
        this.fio = new FIO(fio, null);
        this.podrazd = new Podrazd(podrazd);
        this.organ = new Organ(organ);
        this.phone1 = phone1;
        this.phone2 = phone2;

    }

    public static Initiator getInitiator(String fioTitle, String podrazdTitle, String organTitle, String phone1, String phone2) {
        FIO fio = new FIO(fioTitle, null);
        Podrazd podrazd = new Podrazd(podrazdTitle);
        Organ organ = new Organ(organTitle);
        Initiator init = new Initiator(fio, podrazd, organ, phone1, phone2);
        return init;
    }

    public void setPodrazd(String podrazd) {this.podrazd = new Podrazd(podrazd);
    }
    public void setOrgan(String organ){this.organ = new Organ(organ);
    }

    /**
     * Данная функция смотрит присутсвует ли данный инициатор в БД (поиск ведется по Фамилии и Подразделению)
     *
     * @param database
     * @return
     */
    private ArrayList<Initiator> searchInDBbyFIOandOrg(DB database) {
        ArrayList<Initiator> result = new ArrayList<Initiator>();
        try {
            Connection con = database.getConnection();
            ResultSet rs;
            CallableStatement cs = con.prepareCall("{ call dbo.usp_InitiatorSearch(?,?,?,?,?,?)}");
            //cs.registerOutParameter(1, java.sql.Types.INTEGER);
            cs.setObject(1, null);
            // cs.setObject(2, this.podrazd.id);//"organ_id", this.podrazd.id
            // cs.setObject(3, null);//"podrazd_id"
            cs.setObject(2, null);//"organ_id", this.podrazd.id
            cs.setObject(3,this.podrazd.id);//"podrazd_id"
            cs.setInt(4, this.fio.id);//"fam_id", this.fio.id
            cs.setString(5, null);//"phoneA", this.phone1
            cs.setString(6, null);//"phoneB" this.phone2
            rs = cs.executeQuery();
            //int status = cs.getInt(1);
            while (rs.next()) {

                //FIO fio = FIO.searchFIO(database, rs.getInt("fam_id"));
                Podrazd podrazd = Podrazd.searchInDB(database, rs.getInt("podrazd_id"));
                String phoneA = rs.getString("phoneA");
                String phoneB = rs.getString("phoneB");
                Initiator init = new Initiator(this.fio, podrazd,organ, phoneA, phoneB);
                init.id = rs.getInt("ini_id");
                result.add(init);
            }
            return result;

        } catch (SQLException s) {
            s.printStackTrace();
            Log.logAdd(s.getMessage(), "error");
            return null;
        }
    }

    /**
     * Метод проверки на существование инициатора
     * Проверяем по фамилии и органу
     * Если вариантов несколько то берем с последним id
     * Если вариант один то берем его
     * Если вариантов нет то проверяем только по фамилии
     * если вариантов несколько то выводим пользователю список с возможностью выбора
     * если не выбрал, создаем нового, пишем в лог
     * 16.06.2016 изменение процесса добавление инициатора(учитываем Орган)
     *
     * @param database
     * @return
     */
    public Boolean checkInDB(DB database) {
        Boolean res;

        // Проверяем наличие ФИО
        if (this.fio == null || this.fio.id == 0) {      // Проверяем есть ли ФИО
        res = this.fio.searchFIO(database);     // проверяем есть ли такой ФИО в БД
        if (!res) {
            res = this.fio.addInDB(database);     // Создаем ФИО в БД
            if (!res) {
                Log.logAdd("Не удалось создать ФИО инициатара", "error");
                return false;
            }
        }
    }


        // Проверяем наличие Подразделения
        res = this.podrazd.searchInDB(database); // Ищим Орган инициатора в БД
        if (!res) {   // Если такого Подразделение нет в БД
        //Подразделение должнобыть всегда, при нормальных условиях код не выполняется
            AddresChoise addresChoise = new AddresChoise(this);
            addresChoise.create();
            res = this.podrazd.searchInDB(database);
            if (!res) {
                Log.logAdd("Не удалось добавить в БД МАГ Подразделение :" + this.podrazd.title, "error");
                return false;
                         }
            else{organ=new Organ(this.podrazd.organ_id);}
        }
        else{organ=new Organ(this.podrazd.organ_id);}



        // Ищем по ФИО
        ArrayList<Initiator> iniList = new ArrayList<Initiator>();
        iniList = searchInDBbyFIOandOrg(database);


        if (iniList.size() > 0) {  // Если  есть хоть один Инициатор с заданной ФИО

            if (iniList.size() < 2) { // Если один результат
                this.id = iniList.get(0).id;
                this.fio = iniList.get(0).fio;
                this.podrazd = iniList.get(0).podrazd;
                this.phone1 = iniList.get(0).phone1;
                this.phone2 = iniList.get(0).phone2;
                this.organ = iniList.get(0).organ;
            } else {  // Если результатов много
                this.id = iniList.get(iniList.size() - 1).id;
                this.fio = iniList.get(iniList.size() - 1).fio;
                this.podrazd = iniList.get(iniList.size() - 1).podrazd;
                this.phone1 = iniList.get(iniList.size() - 1).phone1;
                this.phone2 = iniList.get(iniList.size() - 1).phone2;

            }
        } else {

            res = addInDB(database);
            if (res) {
                return true;
            }
            Log.logAdd("Ошибка добавления инициатора в БД МАГ", "error");
            return false;
        }
        return true;
    }

    /**
     * Метод добавления инициатора в БД
     */
    public Boolean addInDB(DB database) {
        Boolean res;
        try {

             res = this.organ.searchInDB(database); // Ищим Орган инициатора в БД
             if (!res) {   // Если такого Органа нет в БД
           //Орган должен быть всегда,при нормальных условиях код не выполняется
            OrganChoise organChoise = new OrganChoise(this);
            organChoise.create();
            res = this.organ.searchInDB(database);
            if (!res) {
                Log.logAdd("Не удалось добавить Орган при добавлении инициатора :" + this.organ.title, "error");
                return false;
            }
        }


            Connection con = database.getConnection();
            ResultSet rs;
            CallableStatement cs = con.prepareCall("{? = call dbo.usp_InitiatorAdd(?,?,?,?,?,?)}");
            cs.registerOutParameter(1, java.sql.Types.INTEGER);
            cs.registerOutParameter(2, java.sql.Types.INTEGER); //"ini_id"
            cs.setInt(3, this.organ.id);//"organ_id"
            cs.setInt(4, this.podrazd.id);//"podrazd_id"
            cs.setInt(5, this.fio.id);//"fam_id"
            cs.setString(6, this.phone1);//"phoneA"
            cs.setString(7, this.phone2);//"phoneB"
            cs.execute();
            int status = cs.getInt(1);
            this.id = cs.getInt(2);
            Log.logAdd("В БД \"МАГ\" добавлен Инициатор: " + this.id + " " + this.fio.title + " " + this.podrazd.title + " " + this.phone1 + " " + this.phone2, "info");

            return true;
        } catch (SQLException s) {
            s.printStackTrace();
            Log.logAdd(s.getMessage(), "error");
            return false;
        }
    }

    /**
     * Ищем инициатора с нужным подразделениемиз всех полученных
     * Если не нашли то возвращаем Null
     * Нашли несколько возвращаем с последним ID
     *
     * @param iniList
     * @return
     */
    private Initiator searchPodrazd(ArrayList<Initiator> iniList) {
        Initiator resulIni;
        // запускаем цикл
        for (Initiator element : iniList) {
            if (element.podrazd.title.replaceAll(" ", "").equals(this.podrazd.title.replace(" ", ""))) {  // проверяем совпадает ли Подразделение
                resulIni = element;
            }
        }
        return null;
    }

}
