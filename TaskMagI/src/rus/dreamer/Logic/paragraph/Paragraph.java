package rus.dreamer.Logic.paragraph;


//import jdk.nashorn.internal.codegen.CompilerConstants;
import org.apache.log4j.Logger;
import rus.dreamer.GUI.AddresChoise;
import rus.dreamer.Logic.Importer;
import rus.dreamer.Logic.Initiator;
import rus.dreamer.Logic.Log;
import rus.dreamer.Logic.Settings;
import rus.dreamer.GUI.MainWindow;

import java.sql.*;
import java.util.Calendar;
import java.util.Date;


public class Paragraph {

    Boolean asPril;
    Integer inID;           //Номер входящего
    Integer number;         // Номер СТМ
    public Integer prilId;         // Номер приложения
    Grif grif;
    String blunkNumber;     // Номер бланка
    Date inDate;            // Дата принятия
    Document document;      // Вид документа
    Integer jurId;          // ID -журнала
    Oper oper;              // Исполнитель
    ShifrP shifr;
    InitiatorP initiator;   // Инициатор
    AddresP address;
    Integer iteration;
    Integer inID2;
    Integer prilId2;

    public Paragraph(Date date, Oper oper, String blunkNumber, String merType, String shifrTitle , Boolean asPril, String inNum, String fio, String phone1, String phone2, String address, Integer i,Integer inID2, Integer prilID2){
        this.inDate = date;
        this.prilId = 0;
        this.oper = oper;
        if(merType.equals("КТКС")) merType = "СИТКС";
        this.grif = new Grif("с");
        this.blunkNumber = blunkNumber;
        this.document = new Document(merType);
       // this.jurId = getJurId(merType);
        this.shifr = new ShifrP(shifrTitle);
        this.asPril = asPril;
        this.inID = !inNum.equals("")?Integer.parseInt(inNum):0;
        this.initiator = new InitiatorP(fio, phone1, phone2);
        this.address = new AddresP(address);
        //this.getInID();
        this.iteration = i;
        this.prilId2=prilID2;

        if(merType.equals("ПТП,СИТКС(СМС),СИТКС(БС)")||merType.equals("ПТП,СИТКС(СМС)")){
            if(i==1)this.jurId = getJurId("ПТП");
            if(i!=1)this.jurId = getJurId("СИТКС");
        }
        else if(merType.equals("НАЗ,НВД")){
            if(i==1)this.jurId = getJurId("НАЗ");
            if(i!=1)this.jurId = getJurId("НВД");
        }
        else if(merType.equals("САМ,СОМ СТ")) {
            if (i == 1) this.jurId = getJurId("САМ");
            if (i != 1) this.jurId = getJurId("СОМ");
        }
        else{this.jurId = getJurId(merType);}

        this.inID2=inID2; // если не первое ОТМ из комплексного задания то inID2 != null
            }

    public Integer getNum(){return this.number;}

    public Integer getInId(){return this.inID;}

    public void setAddress(String addr){
        this.address = new AddresP(addr);
    }

    /**
     * Управляющий процесс регистрации заданий в БД Paragraph
      */
    public Boolean process(){
        Boolean res;

        res = this.grif.getId();      // Получаем ID грифа "секретно"
        if (!res){
            Log.logAdd("Ошибка получения GrifID.", "error");
            return false;
        }
        res = this.document.getID();  // Получаем ID типа документа "Задание"
        if (!res){
            if (!res){
                Log.logAdd("Ошибка получения ID типа документа.", "error");
                return false;
            }
        }
        res = this.shifr.getShifr();  // Получаем ID шифра
        if (!res){
            Log.logAdd("Ошибка получения ID шифра.", "error");
            return false;
        }

        res = this.address.checkAddresP();
        if (!res){
            // Если адрес не найден, то выполняем выбор адреса в ручную
            AddresChoise addresChoise = new AddresChoise(this);
            addresChoise.create();
            res = this.address.checkAddresP();
            if (!res){
                Log.logAdd("Ошибка получения ID адреса.", "error");
                return false;
            }

        }

        // Проверяем как зарегистрировано задание как приложение или нет
        if(this.iteration==null)this.iteration=1; //вслучае одиночного задания, iteration будет null
        if(this.asPril && this.iteration<2){
           res = this.addInPril(); // Регистрируем в журнале приложений

            if (!res){
                Log.logAdd("Ошибка регистрации задания как приложения.", "error");
                return false;
            }
        }
        else{ // Получаем входящий номер
            if (inID2!=null){this.inID=inID2;} //если номер уже получен
            else {// если только собираемся получить входящий номер
                res = this.getInID(); // Получаем входящий номер
                if (!res) {
                    Log.logAdd("Ошибка регистрации задания в журнале входящих.", "error");
                }
            }
        }

        if(this.prilId2!=-1)this.prilId=this.prilId2;

        //this.shifr.getShifr();
        res = this.initiator.takeId(); // Получение ID инициатора(создание или выбор готового)
        if (!res){
            Log.logAdd("Ошибка регистрации инициатора в журнале заданий .", "error");
            return false;
        }

        // Самое главное что нам нужно Номер задания
        res = this.get_OtmNumber(this.prilId);
        if (!res){
            Log.logAdd("Ошибка регистрации задания в журнале заданий и получения \"Номера задания\".", "error");
            return false;
        }
        // Получаем номер задания

    return true;
    }

    /**
     * Получаем номер входящего в БД
      */
    private Boolean getInID(){
        try{
            ResultSet rs;
            Connection con = Settings.getDatabaseParagraph().getConnection();
            CallableStatement cst = con.prepareCall("{? = call get_InNum(?,?,?,?,?,?,?,?,?,?,?,?)}");
            cst.registerOutParameter("RETURN_VALUE",Types.NVARCHAR);
            cst.registerOutParameter("In_id", Types.INTEGER);
            cst.setString("Grif_id",this.grif.id);
            cst.setString("BlankNum", this.blunkNumber);
            cst.setTimestamp("InDate", new Timestamp(new Date().getTime()));
            cst.setString("Adress_id", this.address.id); //Получить Adress_id позже 0FE1BD4B-0115-4C1B-91D4-E178D7C52D09
            cst.setString("VidDoc_id", this.document.id);
            cst.setInt("CountCopy", 0);
            cst.setInt("CountList", 1);
            cst.setObject("OperName", null);
            cst.setString("Oper_id", this.oper.id);
            cst.setObject("DeloNum", null);
            cst.setObject("DeloPrim", null);

            cst.execute();

            this.inID = cst.getInt("In_id");
            if (this.inID > 0){
                return true;
            }


        }catch(SQLException e){
            e.printStackTrace();
            Log.logAdd(e.getMessage(), "error");
            return false;
        }

        return false;
    }


    /**
     * Получаем шифр ОТМ
        */
    public Boolean get_OtmNumber(Integer prilId){
        try{
            ResultSet rs;
            Connection con = Settings.getDatabaseParagraph().getConnection();
            CallableStatement cst = con.prepareCall("{? = call get_OtmNumber(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            cst.registerOutParameter("RETURN_VALUE",Types.NVARCHAR);
            cst.registerOutParameter("OTMNumber", Types.INTEGER);
            cst.setInt("Jur_id", this.jurId);
            cst.setInt("In_id", this.inID);
            cst.setInt("Pril_id", prilId);
            cst.setString("Shifr_id", this.shifr.getid());
           // cst.setString("Grif_id",this.grif.id);
            cst.setString("FIO_id",this.initiator.id);
           // cst.setString("Adress_id",  this.address.id); //Получить Adress_id позже
           //cst.setTimestamp("InDate", new Timestamp(new Date().getTime()));
           // cst.setString("BlankNum", this.blunkNumber);
           // cst.setString("Oper_id", this.oper.id);
            cst.setObject("StartDate", null);
            cst.setObject("InvNum_id", null);
            cst.setObject("EndDate", null);
            cst.setString("CarierRegNum", null);
            cst.setObject("OutNum", null);
            //cst.setObject("InNum", this.inID);
            cst.setObject("Target", null);
            cst.setObject("PlaceZad", null);
            cst.setObject("PlaceCarier", null);
            cst.setObject("PlaceOther", null);

            cst.execute();

            this.number = cst.getInt("OTMNumber");

            if (number > 0) {
                return true;
            }



        }catch(SQLException e){
            e.printStackTrace();
            Log.logAdd(e.getMessage(), "error");
            return false;
        }


        return false;
    }

    /**
     * Получаем ID-журнала с нашим типом мероприятий
     * @param name
        */
    public Integer getJurId(String name){
        Integer jurId = 0;
        try {
            Connection con = Settings.getDatabaseParagraph().getConnection();
            ResultSet rs;

            PreparedStatement ps = con.prepareCall("SELECT id FROM OTMTableList WHERE Name LIKE ?");
            ps.setString(1, "%"+name+"%");
            rs = ps.executeQuery();

            while(rs.next()){
                jurId = rs.getInt(1);
            }
            con.close();

        } catch (SQLException e){
            e.printStackTrace();
        }
        return jurId;
    }

    /**
     *Регистрирует приложение, если задание по сопроводительному письму
        */
    public Boolean addInPril(){
        ResultSet rs;
        try{
            Connection con = Settings.getDatabaseParagraph().getConnection();
            CallableStatement cst = con.prepareCall("{?=call add_InPril(?,?,?,?,?,?,?,?,?,?,?,?)}");
            cst.registerOutParameter("RETURN_VALUE",Types.INTEGER);
            cst.registerOutParameter("Pril_id", Types.INTEGER);
            cst.setInt("In_id", this.inID);
            cst.setString("Grif_id", this.grif.id);
            cst.setString("Adress_id", this.address.id);
            cst.setTimestamp("InDate", new Timestamp(new Date().getTime()));
            cst.setString("BlankNum", this.blunkNumber);
            cst.setString("VidDoc_id", this.document.id);
            cst.setInt("CountList", 1);
            cst.setString("OperName", null);
            cst.setString("Oper_id",this.oper.id );
            cst.setString("DeloNum", null);
            cst.setString("DeloPrim", null);

            Boolean ret = cst.execute();

                Integer res = cst.getInt("RETURN_VALUE");
                if (res == 0) {
                this.prilId = cst.getInt("Pril_id");
                return true;
            }
            if (res == -3) {
                System.out.println("Зарегистрировано максимальное количество документов в приложении!");
                Log.logAdd("Максимальное количество документов в приложении!", "error");
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }



        return false;
    }

}
