package rus.dreamer.Logic;


import rus.dreamer.Logic.paragraph.Oper;

import java.sql.*;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class Task {

    public int id; // ID задания
    public String taskNumber; // Входящий номер(в последующем получать автоматически)
    public String numberBlank; // Номер бланка
    public java.util.Date startTask; // Дата начала
    public String phoneNumber; // Номер телефона
    public String imei; // Номер IMEI
    public Initiator initiator; // Инициатор
    public Operator operator; // Испонитель
    public Shifr shifr; // Шифр
    public Meropr meropr; // Мероприятие
    public String taskYear; // Год мероприятия
    public String target; // Цель
    public String orient; // Ориентировка
    public ObjectT object; // Объект
    public SanctionPerson sancPerson; //Лицо санкционировавшее мероприятие
    public String sancNumb;         // Номер санкции
    public Integer sancPeriod;      // Период
    public Date sancDate;     // Дата начала
    public int delo;        // Дело основание
    public String deloNumb; // Номер дела основания
    public Article article; // Сатья
    public int artPart; // Часть статьи
    public String addDelo;
    public Boolean OPB;

    public Task(String taskNumber, String numberBlank, Initiator initiator, Meropr meropr, String target, String orient, ObjectT object) {
        this.taskNumber = taskNumber;
        this.numberBlank = numberBlank;
        this.initiator = initiator;
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        calendar.setTime(new java.util.Date());
        this.taskYear = Integer.toString(calendar.get(Calendar.YEAR));
        this.meropr = meropr;
        this.target = target;
        this.orient = orient;
        this.object = object;
    }

    public Task() {

    }

    public Boolean taskAdd(DB database, String taskNumber, String numberBlank, java.util.Date startTask, String phoneNumber, String imei, String initTitle, String initOrgan,
                           String initPodr, String initPhoneA, String initPhoneB, Shifr shifr, Meropr meropr, String sancFam, String sancOrg, String sancPost, String sancNumb, java.util.Date sancDate, String sancPeriod,
                           String target, String orient, String family, String name, String surname, java.util.Date dateBirth, String sex,
                           String town, String street, String building, String korp, String flat, String jobName, String jobDolgn,
                           String jobTown, String jobStreet, String jobBuilding, String jobKorp, String jobFlat, int delo, String deloNumb,
                           String addDelo, Article article, int artPart, Operator operator, String addDeloType, String addDeloNum, java.util.Date addDeloDate, String addDeloPlace, Boolean OPB) {
        Boolean res;

        this.taskNumber = taskNumber;
        this.numberBlank = numberBlank;

        this.startTask = startTask;

        this.operator = operator;

        this.phoneNumber = phoneNumber;
        this.imei = imei;

        //Взаимодействие с ОПБ
        this.OPB = OPB;

        //Санкции
        this.sancNumb = sancNumb;

        try {
            this.sancDate = new java.sql.Date(sancDate.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            this.sancDate = null;
        }
        try {
            this.sancPeriod = sancPeriod.length() != 0 ? Integer.parseInt(sancPeriod) : null;
        } catch (Exception e) {
            this.sancPeriod = 180;
        }

        this.delo = delo;
        this.deloNumb = deloNumb;
        this.addDelo = addDelo;

        this.article = article;
        this.artPart = artPart;

        // Судья
        if (sancPost.equals("не требуется")) {
            this.sancPerson = new SanctionPerson();
        } else {
            this.sancPerson = new SanctionPerson(sancOrg, sancPost, sancFam);
            res = this.sancPerson.checkInDB(database);
            Log.logAdd("Добавление судьи в БД: ", res);
            if (!res) {
                this.sancPerson.id = 0;
                return false;
            }
        }

        //Инициатор
        this.initiator = new Initiator(initTitle, initPodr, initOrgan, initPhoneA, initPhoneB);
        res = this.initiator.checkInDB(database);
        Log.logAdd("Добавление инициатора в Задание", res);
        if (!res) {
            this.initiator.id = 0;
            return false;
        }

        this.shifr = shifr;

        this.meropr = meropr;

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        calendar.setTime(new java.util.Date());
        this.taskYear = Integer.toString(calendar.get(Calendar.YEAR));

        this.target = target;
        this.orient = orient;

        this.object = new ObjectT(database, name, family, surname, dateBirth, sex, town, street, building, korp, flat, jobName, jobTown, jobStreet, jobBuilding, jobKorp, jobFlat);

        res = this.addInDB(database); // Добавляем задание в Базу

        Log.logAdd("Добавление задание в БД", "info");

        if (res) {
            Log.logAdd("Добавление задание в БД прошло успешно!", "info");

            // Проверяем включен ли модуль "additionalTables"
            if (Settings.getAdditionalTables()) { // Если да то пытаемя добавить данные в таблицу УД/КУСП
                if (!addDeloType.equals("") && !addDeloNum.equals("") && !addDeloDate.equals("")) {
                    AdditionalTables additionalTables = new AdditionalTables(addDeloType, addDeloNum, addDeloDate, addDeloPlace);
                    additionalTables.initialize(this.id);
                }
            }
            // Добавляем Исполнителя к заданию
            if (operator != null) {
                Boolean op = operator.addOperatorToTask(this.id, this.startTask);
                if (op) Log.logAdd("Добавление исполнителя к заданию ID " + this.id + " прошло успешно");
            } else Log.logAdd("Добавление исполнителя к заданию ID " + this.id + " не прошло");

            boolean res2 = this.article.crimaActInDB(database, this.id, this.artPart);
            res = this.object.add(database, this.id);
            if (!res || !res2)
                return false;
            return true;
        } else {
            Log.logAdd("Добавление задание в БД завершилось ошибкой!", "error");
            return false;
        }
    }


    public boolean addInDB(DB database) {
        try {
            Connection con = database.getConnection();
            CallableStatement cst = con.prepareCall("{? = call usp_TasksAdd(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
            cst.registerOutParameter("RETURN_VALUE", Types.NVARCHAR);
            cst.registerOutParameter("task_id", Types.INTEGER);
            cst.setInt("otd_id", Integer.parseInt(Settings.getOtdId()));                // ID ОТД(всегда 1 у Хабаровска 4)
            cst.setInt("ini_id", this.initiator.id);     // ID инициатора
            cst.setObject("mer_id", this.meropr.id);         // ID мероприятия
            cst.setInt("obj_id", this.shifr.getId());            // Шифр задания из sl_Object
            cst.setString("task_number", this.taskNumber);
            cst.setString("task_year", this.taskYear);       // Год
            cst.setDate("take_date", new java.sql.Date(this.startTask.getTime()));//cst.setDate("take_date", java.sql.Date.valueOf("2003-12-12")); Дата принятия
            cst.setObject("begin_date", null);// cst.setDate("begin_date", java.sql.Date.valueOf("2003-12-12"));  Здесь либо 0 либо Дата (не null) а то не пройдет конвертацию в dt_TaskAdd 164 строка Дата начала
            cst.setObject("end_date", null);//cst.setDate("end_date", java.sql.Date.valueOf("2003-12-12"));        Здесь либо 0 либо Дата (не null) а то не пройдет конвертацию в dt_TaskAdd 165 строка Дата окончания
            cst.setString("number_blank", this.numberBlank); // Номер бланка
            cst.setInt("where_act_id", Integer.parseInt(Settings.getWhereActId())); // для хабаровска 20, для нас 4
            cst.setString("long_act", null);
            cst.setString("phone_otm", this.phoneNumber); // Номер телефона
            cst.setInt("delo_id", this.delo);
            cst.setString("delo_num", this.deloNumb);
            cst.setObject("delo_alias", this.addDelo);
            cst.setObject("alias_obj", null);
            cst.setBoolean("join_opu", this.OPB);
            cst.setString("type_control_id", "4");
            cst.setInt("task_type", 1);
            cst.setObject("task_mn", null);
            cst.setString("IMEI", this.imei);       // Номер IMEI
            cst.setString("IMSI", "");
            cst.setInt("RestrictExport", 1);
            cst.setObject("crimact_id", null);
            cst.setObject("citizenship_id", null);
            cst.setObject("orient", this.orient);
            cst.setObject("target", this.target);
            cst.setString("judicial_order", this.sancNumb);
            cst.setDate("sanc_date", this.sancDate);
            cst.setObject("sanc_long", sancPeriod);
            cst.setObject("sanction_person_id", sancPerson.id);
            cst.setInt("moment_connection", 0);
            cst.setObject("ProcessingBeginDate", null);
            cst.setObject("ProcessingEndDate", null);
            cst.setObject("Comment", null);
            cst.setInt("PrivacyLevel", 3);

            cst.execute();

            this.id = cst.getInt("task_id");

            if (this.id != 0)
                return true;
            else
                return false;

        } catch (SQLException s) {
            s.printStackTrace();
            Log.logAdd(s.getMessage(), "error");
            return false;
        }
    }


}
