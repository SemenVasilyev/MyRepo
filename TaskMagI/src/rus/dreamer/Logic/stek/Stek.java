package rus.dreamer.Logic.stek;

import rus.dreamer.Logic.DB;
import rus.dreamer.Logic.Log;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;



public class Stek {

    DB database;        // БД стэка
    String imei;        // IMEI телефона
    String phoneNumber; // Номер телефона
    String meropr;      // Тип мероприятия
    SimpleDateFormat sdf;
    Calendar startDate;
    String result;      // Результат проверки
    Date sancEnd;   // Срок санкции
    Date sancStart; // дата начал санкции


    String checkDate;   // Дата проверки
    String status;      // Статус проверки
    Calendar date;      // Дата проверки
    String initiator;   // Инициатор

    /**
     *
     * @param database
     * @param number
     * @param imei
     * @param date
     * @param meropr
     */
    public Stek(DB database, String number, String imei, Date date, String meropr, Date sancDate, String sancLong){
        this.database = database;
        this.phoneNumber = number;
        this.imei = imei.replaceAll(" ","");
        this.sdf = new SimpleDateFormat("yyyyMMdd");  // Формат времени для вставки в запрос
        this.date = Calendar.getInstance();
        this.date.setTime(date);
        this.startDate = Calendar.getInstance();
        this.startDate.setTime(date);
        this.startDate.add(Calendar.DAY_OF_MONTH, -7);  // поменяй на  - 7
        this.meropr = meropr;

        this.sancStart = sancDate;
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.setTime(sancStart);
        try {
            tempCalendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(sancLong));
            this.sancEnd = new Date(tempCalendar.getTimeInMillis());
        } catch (Exception s){
            s.printStackTrace();
        }

    }

    /** Определеяем тип задания и в зависимости от этого выводим результат
     *
     */
    public void getInformation(){
        //Определяем тип задания
        if(this.meropr.equals("ПТП")) {
            if (!this.imei.isEmpty()) // Проверяем
                getFromDBPTP(this.imei);
            else {
                getFromDBPTP(this.phoneNumber);
            }
        }
        else{
            List<ObjectHolder> list = new ArrayList<ObjectHolder>();
            if (!this.imei.isEmpty()) // Проверяем
                list =  getFromDBSITKS(this.imei);
            else {
                list = getFromDBSITKS(this.phoneNumber);
            }
            Boolean res = checkSITKS(list);
        }

        showResult();
    }

    /**
     * Getting information from DB
     * @param number
     */
    public void getFromDBPTP(String number){
        try {
            ResultSet rs;
            Connection con = this.database.getConnection();
            Statement st = con.createStatement();
            rs = st.executeQuery("SELECT TOP 1\n" +
                    "SMS_IN.Status,\n" +
                    "SMS_IN.LastCountSMS,\n" +
                    "Oper_Phone.Dolgnost,\n" +
                    "Oper_Phone.Slujba,\n" +
                    "Oper_Phone.zvanie,\n" +
                    "Oper_Phone.FIO\n" +
                    "FROM SMS_IN\n" +
                    "INNER JOIN Oper_Phone ON SMS_IN.Sender = Oper_Phone.Phone\n" +
                    "WHERE SMS_IN.TextSMS LIKE \'%"+ number +"%\' AND \n" +
                    "dbo.SMS_IN.TimeSMS BETWEEN \'" + sdf.format(startDate.getTime()) +"\'  AND  \'" + sdf.format(date.getTime())+ "\'" +
                    "order by LastCountSMS DESC");
            while (rs.next()){
                this.status = Integer.toString(rs.getInt(1));
                this.checkDate =  rs.getDate(2).toString();
                this.initiator =  rs.getString(3) + " "+ rs.getString(4) +" "+ rs.getString(5) +" " +  rs.getString(6);
            }
        }catch(SQLException s){
            s.printStackTrace();
            Log.logAdd(s.getMessage(),"error");
        }
    }

    public  List<ObjectHolder> getFromDBSITKS(String number){
        try {
            ResultSet rs;
            List<ObjectHolder> list = new ArrayList<ObjectHolder>();
            Connection con = this.database.getConnection();
            Statement st = con.createStatement();
            rs = st.executeQuery("SELECT\n" +
                    "SMS_IN.Status,\n" +
                    "SMS_IN.LastCountSMS,\n" +
                    "Oper_Phone.Dolgnost,\n" +
                    "Oper_Phone.Slujba,\n" +
                    "Oper_Phone.zvanie,\n" +
                    "Oper_Phone.FIO,\n" +
                    "SMS_IN.TextSMS\n" +
                    "FROM SMS_IN\n" +
                    "INNER JOIN Oper_Phone ON SMS_IN.Sender = Oper_Phone.Phone\n" +
                    "WHERE SMS_IN.TextSMS LIKE \'%"+ number +"%\'" +
                    "order by LastCountSMS DESC");
            while (rs.next()){
                list.add(new ObjectHolder(Integer.toString(rs.getInt(1)), rs.getDate(2).toString(), new String(rs.getString(3) + " "+ rs.getString(4) +" "+ rs.getString(5) +" " +  rs.getString(6)), rs.getString(7)));
            }
            return  list;
        }catch (SQLException s){
            s.printStackTrace();
            Log.logAdd(s.getMessage(),"error");
        }
        return null;
    }

    /**
     * Метод для проверки вхождения даты санкции в дату проверки СИТКС
     *
     * @param list
     */
    private Boolean checkSITKS(List<ObjectHolder> list){
        for(ObjectHolder item:list){
            // достаем срок из строки
            String[] tempString =  item.textSMS.split("\\*");
            DateFormat format =  new SimpleDateFormat("dd.MM.yyyy");
            Date dateFirst = null, dateSecond = null;

           try {
               dateFirst = format.parse(tempString[2]);
               dateSecond = format.parse(tempString[3]);
           } catch (ParseException s){
               s.printStackTrace();
               Log.logAdd(s.getMessage(), "error");
               return false;
           }

            // Процесс сравнения результатов
            if (this.sancStart.before(dateFirst) && this.sancEnd.after(dateFirst)) {
                this.status = item.status;
                this.checkDate = item.checkDate;
                this.initiator = item.initiatior;
                return true;
            }
            else {
                if (this.sancStart.before(dateSecond) && this.sancEnd.after(dateSecond)){
                    this.status = item.status;
                    this.checkDate = item.checkDate;
                     this.initiator = item.initiatior;
                    return true;
                }
                else
                    continue;
            }
        }
        return false;
    }


    /**
     * For Showing information Window about Phone activation
     */
    public void  showResult(){
        if (this.status != null)
        {
            switch(this.status) {
                case "1":
                    this.status = "Активен";
                    break;
                case "2":
                    this.status = "Не активен";
                    break;
                case "3":
                    this.status = "В обработке";
                    break;
            }

            this.result = "Статус: " + this.status +"\n" +
                    "Дата проверки: " +  this.checkDate + "\n" +
                    "Инициатор: " + this.initiator;
        }
        else {
            this.result = "Проверка не проводилась!";
        }
        JOptionPane.showMessageDialog(null ,this.result, "Данные об активации!", JOptionPane.INFORMATION_MESSAGE);
    }


    /**
     * Класс для хранения результатов Result Set
     *
     */
    class ObjectHolder {
        private String status;
        private String checkDate;
        private String initiatior;
        private String textSMS;

        public ObjectHolder(String status, String checkDate, String initiatior, String textSMS){
            this.status = status;
            this.checkDate = checkDate;
            this.initiatior = initiatior;
            this.textSMS =  textSMS;
        }
    }
}
