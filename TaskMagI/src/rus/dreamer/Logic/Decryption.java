package rus.dreamer.Logic;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс для разбора и дешифровки QR-кода
 */
public class Decryption {
    String vers; // Версия Визиря
    String decryptData;
    String qrCode;
    public String merTitle; // Индекс мероприятия
    public int otdId = 1; // всегда 1
    public String iniName; // Имя инициатора
    public String phoneN; // Номер телефона
    public String imei; // Номер imei
    public String iniOrgan = ""; //Орган инициатора пустой
    public String iniPodrazd; // Подразделение Инициатора
    public String iniTel1; // Телефон инициатора 1
    public String iniTel2; // Телефон инициатора 2
    public String target; // Цель мероприятия
    public String orient; // Ориентировка(Справка об объекте)
    public String name; // Имя Объекта
    public String family; // Фамилия Объекта
    public String surname; // Отчество объекта
    public String citiz; // Гражданство
    public String dateBirth; // Дата рождения объекта
    public String sex; // Пол объекта
    public String town; // Город
    public String street;   // Улица
    public String build;    // Дом
    public String kopr;     // Корпус
    public String flat;     // Квартира
    public String jobName; // Место работы
    public String jobDolgn; // Место работы должность
    public String jobTown; // Адрес места работы город
    public String jobStreet; // Адрес места работы улица
    public String jobBuild; // Адрес места работы здание
    public String jobKopr; // Адрес места работы корпус
    public String jobFlat; // Адрес места работы квартира
    public String sancFam; // Фамилия И.О. санкционировавшего
    public String sancOrg; // Место работы санкционировавшего
    public String sancPost; // Должность санкционировавшего
    public String sancNumb; // Номер постановления
    public String sancDate; // Дата вынесения
    public String sancBeg; // Дата начала
    public String sancPeriod; // Длительсность
    public String sancEnd; // Дата окончания
    public String delo;     // Тип основания
    public String deloNumber; // Номер основания
    public String article;  //Статья
    public String articlePart;  // Номер
    public String adDeloType; // Доп. тип дела
    public String adDeloNumber; // Дополнительный номер дела
    public String adDeloDate; // Дата дела
    public String adDeloKUSPPlace; // Место заведения КУСП
    public String OPB; // Взаимодействие с ОПБ

    public String decpypt(String qrCode) throws ArithmeticException {
        // Получаем данные с поля TP_QRQode
        int num;
        String textA, textB;
        String[] massA, massB;
        String[][] code = new String[86][2];

        Log.logAdd("Дешифровка QR-кода");

        if (qrCode == null) {
            return null;
        } else {
            // Расшифровываем QR-код
            // Потом переделать и представить в виде двумерного массива, чтобы было нагляднее в исходнике что-во что кодируется
            code[0][0] = "й";
            code[0][1] = "x";
            code[1][0] = "ц";
            code[1][1] = "c";
            code[2][0] = "у";
            code[2][1] = "u";
            code[3][0] = "к";
            code[3][1] = "k";
            code[4][0] = "е";
            code[4][1] = "e";
            code[5][0] = "н";
            code[5][1] = "n";
            code[6][0] = "г";
            code[6][1] = "g";
            code[7][0] = "ш";
            code[7][1] = "w";
            code[8][0] = "щ";
            code[8][1] = "%w";
            code[9][0] = "з";
            code[9][1] = "z";
            code[10][0] = "х";
            code[10][1] = "h";
            code[11][0] = "ъ";
            code[11][1] = "%q";
            code[12][0] = "ф";
            code[12][1] = "f";
            code[13][0] = "ы";
            code[13][1] = "%b";
            code[14][0] = "в";
            code[14][1] = "v";
            code[15][0] = "а";
            code[15][1] = "a";
            code[16][0] = "п";
            code[16][1] = "p";
            code[17][0] = "р";
            code[17][1] = "r";
            code[18][0] = "о";
            code[18][1] = "o";
            code[19][0] = "л";
            code[19][1] = "l";
            code[20][0] = "д";
            code[20][1] = "d";
            code[21][0] = "ж";
            code[21][1] = "j";
            code[22][0] = "э";
            code[22][1] = "%a";
            code[23][0] = "я";
            code[23][1] = "y";
            code[24][0] = "ч";
            code[24][1] = "q";
            code[25][0] = "с";
            code[25][1] = "s";
            code[26][0] = "м";
            code[26][1] = "m";
            code[27][0] = "и";
            code[27][1] = "i";
            code[28][0] = "т";
            code[28][1] = "t";
            code[29][0] = "ь";
            code[29][1] = "$b";
            code[30][0] = "б";
            code[30][1] = "b";
            code[31][0] = "ю";
            code[31][1] = "$v";
            code[32][0] = "ё";
            code[32][1] = "%y";
            code[33][0] = "Й";
            code[33][1] = "%x";
            code[34][0] = "Ц";
            code[34][1] = "%c";
            code[35][0] = "У";
            code[35][1] = "%u";
            code[36][0] = "К";
            code[36][1] = "%k";
            code[37][0] = "Е";
            code[37][1] = "%e";
            code[38][0] = "Н";
            code[38][1] = "%n";
            code[39][0] = "Г";
            code[39][1] = "%g";
            code[40][0] = "Ш";
            code[40][1] = "$w";
            code[41][0] = "Щ";
            code[41][1] = "$z";
            code[42][0] = "З";
            code[42][1] = "%z";
            code[43][0] = "Х";
            code[43][1] = "%h";
            code[44][0] = "Ъ";
            code[44][1] = "$o";
            code[45][0] = "Ф";
            code[45][1] = "%f";
            code[46][0] = "Ы";
            code[46][1] = "$q";
            code[47][0] = "В";
            code[47][1] = "%v";
            code[48][0] = "А";
            code[48][1] = "$a";
            code[49][0] = "П";
            code[49][1] = "%p";
            code[50][0] = "Р";
            code[50][1] = "%r";
            code[51][0] = "О";
            code[51][1] = "%o";
            code[52][0] = "Л";
            code[52][1] = "%l";
            code[53][0] = "Д";
            code[53][1] = "%d";
            code[54][0] = "Ж";
            code[54][1] = "%j";
            code[55][0] = "Э";
            code[55][1] = "$j";
            code[56][0] = "Я";
            code[56][1] = "$y";
            code[57][0] = "Ч";
            code[57][1] = "$c";
            code[58][0] = "С";
            code[58][1] = "$s";
            code[59][0] = "М";
            code[59][1] = "%m";
            code[60][0] = "И";
            code[60][1] = "%i";
            code[61][0] = "Т";
            code[61][1] = "%t";
            code[62][0] = "Ь";
            code[62][1] = "$d";
            code[63][0] = "Б";
            code[63][1] = "$i";
            code[64][0] = "Ю";
            code[64][1] = "$u";
            code[65][0] = "Ё";
            code[65][1] = "$e";
            code[66][0] = "(";
            code[66][1] = "$1";
            code[67][0] = ")";
            code[67][1] = "$2";
            code[68][0] = "№";
            code[68][1] = "$3";
            code[69][0] = "0";
            code[69][1] = "$4";
            code[70][0] = "\"";
            code[70][1] = "$5";
            code[71][0] = "=";
            code[71][1] = "$6";
            code[72][0] = "/";
            code[72][1] = "$7";
            code[73][0] = ",";
            code[73][1] = "$8";
            code[74][0] = ":";
            code[74][1] = "$9";
            code[75][0] = ";";
            code[75][1] = "%1";
            code[76][0] = "-";
            code[76][1] = "%2";
            code[77][0] = "&";
            code[77][1] = "%3";
            code[78][0] = " ";
            code[78][1] = "%4";
            code[79][0] = "@";
            code[79][1] = "%5";
            code[80][0] = "!";
            code[80][1] = "%6";
            code[81][0] = "?";
            code[81][1] = "%7";
            code[82][0] = "_";
            code[82][1] = "%8";
            code[83][0] = "~";
            code[83][1] = "%9"; // если тильда значит английские символы
            code[84][0] = "%";
            code[84][1] = "%%";
            code[85][0] = "$";
            code[85][1] = "$$";

        }

        qrCode = qrCode.toLowerCase();
        num = qrCode.length();
        String result = "";

        // Алгоритм декодирования в строку нормального вида

        for (int i = 0; i < num; ) {
            String tmp = qrCode.substring(i, i + 1);
            if (tmp.equals("%") || tmp.equals("$")) {
                tmp = qrCode.substring(i, i + 2);
                for (int j = 0; j < code.length; j++) {
                    if (tmp.equals(code[j][1])) {
                        if (j == 83) {  // Если английский символ, то переносим его без изменений
                            result += qrCode.substring(i + 2, i + 3);
                            i = i + 3;
                            break;
                        } else {
                            result += code[j][0];//qrCode.equals()
                            i = i + 2;
                            break;
                        }
                    }
                    if (j == code.length - 1) {
                        Log.logAdd("Не возможно разобрать строку, не известный символ:" + tmp, "error");
                        JOptionPane.showMessageDialog(null, "Не возможно разобрать строку, не известный символ:" + tmp, "Ошибка!", JOptionPane.ERROR_MESSAGE);
                        throw new ArithmeticException();
                    }
                }

            } else {
                int k = i;
                for (int j = 0; j < code.length; j++) {
                    if (tmp.equals(code[j][1])) {
                        result += code[j][0];
                        i++;
                        break;
                    }
                }
                if (k == i) {
                    result += qrCode.substring(i, i + 1);
                    i++;
                    continue;
                }
            }
        }

        this.decryptData = result;
        graber();

        return this.decryptData;

    }

    /**
     * Функция разбора строки по отдельным переменным
     */
    public boolean graber() {

        String[] tmp = decryptData.split("\\+", 44);    // временная переменная делим нашу строку по символу +
        int len = tmp.length;

        this.vers = tmp[0];

        for (int i = 0; i < tmp.length; i++) {      // Перебераем все элементы и избавляемся от концевых пробелов
            tmp[i] = tmp[i].trim();
        }


        Integer version;
        version = Integer.parseInt(this.vers.replaceAll("\\D", ""));
        if (version < 19) {
            JOptionPane.showMessageDialog(null, "Не совместимая версия Vizier!\n Версия программы должна быть не ниже: \"1.9\"\n Текущая версия: " + this.vers, "Ошибка!", JOptionPane.WARNING_MESSAGE);
            Log.logAdd("Получен QR.Не совместимая версия Vizier! Версия программы должна быть не ниже: 1.9", false);
            return false;
        }


        switch (Integer.parseInt(tmp[1])) {
            case 0:
                this.merTitle = "ПТП";
                break;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
                this.merTitle = "СИТКС";
                break;
            case 24:
                this.merTitle = "ПТП,СИТКС(СМС),СИТКС(БС)";
                break;
            case 25:
                this.merTitle = "НАЗ,НВД";
                break;
            case 26:
                this.merTitle = "ПТП,СИТКС(СМС)";
                break;
            case 27:
                this.merTitle = "САМ,СОМ СТ";
                break;
            case 7:
                this.merTitle = "НАЗ";
                break;
            case 8:
                this.merTitle = "НВД";
                break;
            case 9:
                this.merTitle = "ОД";
                break;
            case 10 - 16:
                this.merTitle = "CОМ";
             /* case 10:
                  this.merTitle = "НП";
                  break;
              case 11:
                  this.merTitle = "ОТВ";
                  break;
              case 12:
                  this.merTitle = "СТ";
                  break;
              case 13:
                  this.merTitle = "ЭН";
                  break;
              case 14:
                  this.merTitle = "ОТО";
                  break;
              case 15:
                  this.merTitle = "ЭД";
                  break;
              case 16:
                  this.merTitle = "мониторинг";*/
                break;
            case 17:
                this.merTitle = "техническое осуществление контроля и записи переговоров";
                break;
            case 18:
                this.merTitle = "САМ";
                break;
            default:
                this.merTitle = "ПТП";
                break;
        }

        if (tmp[2].equals("Тлф №")) {
            if (tmp[3].length() >= 10) {
                // Разбиваем номер телефона по 3 цифры
                this.phoneN = tmp[3].substring(tmp[3].length() - 10); // берем 10 цифр справа
                if (Settings.getGroupByThree()) {
                    this.phoneN = groupByThree(phoneN);
                }
            } else {
                this.phoneN = tmp[3];
            }

        } else if (tmp[2].equals(("IMEI №").toLowerCase())) {

            this.imei = tmp[3];

        }

        // Проверяем есть ли значением
        // Проверяем строковое или число
        if (checkString(tmp[17])) {
            this.sancPost = getSancPost(Integer.parseInt(tmp[17]));
        } else {
            this.sancPost = tmp[17].toLowerCase();
        }
        // this.sancPost = getSancPost(Integer.parseInt(tmp[17].equals("")? tmp[17]="777": tmp[17]));

        if (checkString(tmp[18])) {
            this.sancOrg = getSancOrgID(Integer.parseInt(tmp[18]));
        } else {
            this.sancOrg = getSancOrg(tmp[18]);
        }

        //this.sancOrg = getSancOrg(Integer.parseInt(tmp[18].equals("")? tmp[18]="777": tmp[18]));

        this.sancFam = tmp[20];

        this.sancNumb = tmp[22];
        if (tmp[23].length() > 0) {
            this.sancPeriod = tmp[23];
        } else {
            this.sancPeriod = tmp[26];
        }

        this.sancDate = tmp[21];

        this.delo = tmp[14];
        this.deloNumber = tmp[15];

        this.article = tmp[12];
        this.articlePart = tmp[13];

        this.otdId = 1;
        this.family = tmp[4];
        this.name = tmp[5];
        this.surname = tmp[6];
        this.dateBirth = tmp[7];
        this.citiz = tmp[8];

        if (tmp[9].equals("Женский"))
            this.sex = "Ж";
        else if (tmp[9].equals("Мужской"))
            this.sex = "М";
        else
            this.sex = "";


        String jobAdr[];
        jobAdr = tmp[10].split(",", 9);
        if (jobAdr.length > 3) {
            if (checkString(jobAdr[6].trim())) jobAdr[6] = ""; // Проверяем чтобы в данное поле попадали только числа
            if (checkString(jobAdr[8].trim())) jobAdr[8] = "";
            this.jobName = jobAdr[0].trim();
            this.jobDolgn = jobAdr[1].trim();
            this.jobTown = jobAdr[4].trim();
            this.jobStreet = jobAdr[5].trim();
            this.jobBuild = jobAdr[6].trim();
            this.jobKopr = jobAdr[7].length() > 3 ? jobAdr[7].substring(4).trim() : "";
            this.jobFlat = jobAdr[8].length() > 3 ? jobAdr[8].substring(3).trim() : "";
        }

        String adr[];
        adr = tmp[11].split(",", 7);
        if (adr.length > 1) {
            if (!checkString(adr[4].trim())) adr[4] = "";  // Проверяем чтобы в данное поле попадали только числа
            if ("".equals(adr[6].trim())){
                adr[6] = "";
            }
            else {
                if (!checkString(adr[6].trim().substring(3))) adr[6] = "";
            }
            this.town = adr[2].trim();
            this.street = adr[3].trim();
            this.build = adr[4].trim();
            this.kopr = adr[5].length() > 3 ? adr[5].trim().substring(4).trim() : "";
            this.flat = adr[6].length() > 3 ? adr[6].substring(3).trim() : "";
        }


        this.target = tmp[31];  // Цель
        this.orient = tmp[32];  // Ориентировка
        this.iniName = tmp[36]; // ФИО Инициатора
        this.iniPodrazd = tmp[34]; // Орган где работает инициатор
        if (checkString(this.iniPodrazd)) {     // Если является числом, значит на входе ID который необходимо преобразовать
            this.iniPodrazd = getOrgFromId(this.iniPodrazd);
        }

        this.iniTel1 = tmp[37]; // Мобильный телефон инициатора
        this.iniTel2 = tmp[38]; // Рабочий телефон инициатора


        if (version >= 22) {
            try {
                if (tmp[39].length() > 0 && tmp[40].length() > 0 && tmp[41].length() > 0) {

                    adDeloType = tmp[39];
                    adDeloNumber = tmp[40];
                    adDeloDate = tmp[41];
                    adDeloKUSPPlace = tmp[42];

                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.logAdd("Ошибка разбора QR-кода, QR-код не содержит запрашиваемых данных, возможно устаревшая версия Vizier");
            }
        }
        if(tmp.length> 43){ OPB = tmp[43];}
        else{OPB="notopb";}


        return true;
    }

    private String getSancPost(int id) {
        String post;
        switch (id) {
            case 0:
                post = "Судья";
                break;
            case 1:
                post = "Заместитель председателя";
                break;
            case 2:
                post = "И.о. председателя";
                break;
            case 3:
                post = "И.о. заместителя председателя";
                break;
            case 4:
                post = "Начальник";
                break;
            case 5:
                post = "Врио начальника";
                break;
            case 6:
                post = "Заместитель начальника управления - начальник полиции";
                break;
            case 7:
                post = "Заместитель начальника полиции (по оперативной работе)";
                break;
            case 11:
                post = "Врио заместителя начальника управления - начальник полиции";
                break;
            case 13:
                post = "не требуется";
                break;
            default:
                post = "не требуется";
                break;
        }
        return post;
    }


    /**
     *  Сравниваем IDщники  и значения
     * @param place
     * @return
     */
    private String getSancOrgID(Integer place) {
        String res;
        switch (place){
            case 0:
                res = "Хабаровский краевой суд";
                break;
            case 1:
                res = "Центральный районный суд г. Хабаровска";
                break;
            case 2:
                res = "Железнодорожный районный суд г. Хабаровска";
                break;
            case 3:
                res = "Индустриальный районный суд г. Хабаровска";
                break;
            case 4:
                res = "Кировский районный суд г. Хабаровска";
                break;
            case 5:
                res = "Краснофлотский районный суд г. Хабаровска";
                break;
            case 6:
                res = "Хабаровский районный суд г. Хабаровска";
                break;
            case 7:
                res = "Центральный районный суд г. Комсомольска-на-Амуре";
                break;
            case 8:
                res = "Ленинский районный суд г. Комсомольска-на-Амуре";
                break;
            case 9:
                res = "Комсомольский районный суд";
                break;
            case 10:
                res = "Амурский городской суд";
                break;
            case 11:
                res = "Солнечный районный суд";
                break;
            case 12:
                res = "Аяно-Майский районный суд";
                break;
            case 13:
                res = "Бикинский городской суд";
                break;
            case 14:
                res = "Ванинского районного суда";
                break;
            case 15:
                res = "Верхнебуреинский районный суд";
                break;
            case 16:
                res = "Вяземский районный суд";
                break;
            case 17:
                res = "Нанайский районный суд";
                break;
            case 18:
                res = "Николаеский-на-Амуре городской суд";
                break;
            case 19:
                res = "Охотский районный суд";
                break;
            case 20:
                res = "Советско-Гаванский городской суд";
                break;
            case 21:
                res = "Суд района им. Лазо";
                break;
            case 22:
                res = "Суд района им. П. Осипенко";
                break;
            case 23:
                res = "Тугуро-Чумиканский районный суд";
                break;
            case 24:
                res = "УМВД России по г. Хабаровску";
                break;
            case 25:
                res = "УМВД России по г. Комсомольску-на-Амуре";
                break;
            case 26:
                res = "ОМВД России по Амурскому району";    // повтор
                break;
            case 27:
                res = "ОМВД России по Хабаровскому району";
                break;
            case 28:
                res = "ОМВД России по Району им. Лазо";
                break;
            case 29:
                res = "ОМВД России по Комсомольскому району";
                break;
            case 30:
                res = "ОМВД России по Амурскому району";
                break;
            case 31:
                res = "ОМВД России по Солнечному району";
                break;
            case 32:
                res = "Следственного коммитета России по Хабаровскому гарнизону";
                break;
            case 33:
                res = "НЕ ТРЕБУЕТСЯ";
                break;
            default:
                res = "НЕ ТРЕБУЕТСЯ";
                break;
        }
      return res;
    }


    private String getSancOrg(String place) {
        String post;
        place = place.replace(" ", "");

        switch (place) {
            case "Хабаровскогокраевогосуда":
                post = "Хабаровский краевой суд";
                break;
            case "Центральногорайонногосудаг.Хабаровска":
                post = "Центральный районный суд г. Хабаровска";
                break;
            case "Железнодорожногорайонногосудаг.Хабаровска":
                post = "Железнодорожный районный суд г. Хабаровска";
                break;
            case "Индустриальногорайонногосудаг.Хабаровска":
                post = "Индустриальный районный суд г. Хабаровска";
                break;
            case "Кировскогорайонногосудаг.Хабаровска":
                post = "Кировский районный суд г. Хабаровска";
                break;
            case "Краснофлотскогорайонногосудаг.Хабаровска":
                post = "Краснофлотский районный суд г. Хабаровска";
                break;
            case "Хабаровскогорайонногосудаг.Хабаровска":
                post = "Хабаровский районный суд г. Хабаровска";
                break;
            case "Центральногорайонногосудаг.Комсомольска-на-Амуре":
                post = "Центральный районный суд г. Комсомольска-на-Амуре";
                break;
            case "Ленинскогорайонногосудаг.Комсомольска-на-Амуре":
                post = "Ленинский районный суд г. Комсомольска-на-Амуре";
                break;
            case "Комсомольскогорайонногосуда":
                post = "Комсомольский районный суд";
                break;
            case "Амурскогогородскогосуда":
                post = "Амурский городской суд";
                break;
            case "Солнечногорайонногосуда":
                post = "Солнечный районный суд";
                break;
            case "Амурскогорайонногосуда":
                post = "Амурский районный суд";
                break;
            case "Солнечногогородскогосуда":
                post = "Солнечный городской суд";
                break;
            case "Аяно-Майскогорайонногосуда":
                post = "Аяно-Майский районный суд";
                break;
            case "Бикинскогогородскогосуда":
                post = "Бикинский городской суд";
                break;
            case "Ванинскогорайонногосуда":
                post = "Ванинского районного суда";
                break;
            case "Верхнебуреинскогорайонногосуда":
                post = "Верхнебуреинский районный суд";
                break;
            case "Вяземскогорайонногосуда":
                post = "Вяземский районный суд";
                break;
            case "Нанайскогорайонногосуда":
                post = "Нанайский районный суд";
                break;
            case "Николаеского-на-Амурегородскогосуда":
                post = "Николаеский-на-Амуре городской суд";
                break;
            case "Охотскогорайонногосуда":
                post = "Охотский районный суд";
                break;
            case "Советско-Гаванскогогородскогосуда":
                post = "Советско-Гаванский городской суд";
                break;
            case "Сударайонаим.Лазо":
                post = "Суд района им. Лазо";
                break;
            case "Сударайонаим.П.Осипенко":
                post = "Суд района им. П. Осипенко";
                break;
            case "Тугуро-Чумиканскогорайонногосуда":
                post = "Тугуро-Чумиканский районный суд";
                break;
            case "Ульчскогорайонногосуда":
                post = "Ульчанский районный суд";
                break;
            default:
                post = "";
                break;
        }
        return post;
    }

    /**
     * Получаем значение органа
     *
     * @param id
     * @return
     */
    private String getOrgFromId(String id) {
        Integer ident;
        ident = Integer.valueOf(id);
        return Podrazd.getPodrazdVisirID(ident);

/*
        switch (ident) {

            case 0:
                return "ОУР УМВД России по г. Комсомольску-на-Амуре";
            case 1:
                return "ОУР ОП-1 УМВД России по г. Комсомольску-на-Амуре"; // УМВД России по г. Комсомольску-на-Амуре
            case 2:
                return "ОУР ОП-2 УМВД России по г. Комсомольску-на-Амуре";
            case 3:
                return "ОУР ОП-3 УМВД России по г. Комсомольску-на-Амуре";
            case 4:
                return "ОУР ОП-4 УМВД России по г. Комсомольску-на-Амуре";
            case 5:
                return "ОЭБиПК УМВД России по г. Комсомольску-на-Амуре";
            case 6:
                return "Отд. ЦПЭ УМВД России по Хабаровскому краю";
            case 7:
                return "Комсомольский ЛО МВД России на транспорте";
            case 8:
                return "ООРЧ СБ УМВД России по Хабаровскому краю";
            case 9:
                return "ОУР ОМВД России по Комсомольскому району";
            case 10:
                return "ГБИПК ОМВД РОССИИ ПО КОМСОМОЛЬСКОМУ РАЙОНУ";
            case 11:
                return "ОУР ОМВД России по Солнечному району";
            case 12:
                return "ГБИПК ОМВД РОССИИ ПО СОЛНЕЧНОМУ РАЙОНУ";
            case 13:
                return "ОУР ОМВД России по Амурскому району";
            case 14:
                return "ГБИПК ОМВД РОССИИ ПО АМУРСКОМУ РАЙОНУ";
//////////////////////////// подразделения г.Хабаровск/////////////////
            case 15:
                return "ОУР УМВД России по Г.ХАБАРОВСКУ";
            case 16:
                return "ОЭБ И ПК УМВД РФ ПО Г.ХАБАРОВСКУ";
            case 17:
                return "ОП № 1 УМВД России ПО Г.ХАБАРОВСКУ";
            case 18:
                return "ОП № 2 УМВД РФ ПО Г.ХАБАРОВСКУ";
            case 19:
                return "ОП № 4 УМВД РФ ПО Г.ХАБАРОВСКУ";
            case 20:
                return "ОП № 5 УМВД РФ ПО Г.ХАБАРОВСКУ";
            case 21:
                return "ОП № 3 УМВД РФ ПО Г.ХАБАРОВСКУ";
            case 22:
                return "ОП № 6 УМВД РФ ПО Г.ХАБАРОВСКУ";
            case 23:
                return "ОП № 7 УМВД РФ ПО Г.ХАБАРОВСКУ";
            case 24:
                return "ОП № 8 УМВД РФ ПО Г.ХАБАРОВСКУ";
            case 25:
                return "ОП № 9 УМВД РФ ПО Г.ХАБАРОВСКУ";
            case 26:
                return "ОП № 10 УМВД РФ ПО Г.ХАБАРОВСКУ";
///////////////////////////////////////////////////////////////////////
            case 27:
                return "ОКОН УМВД России по г. Комсомольску-на-Амуре";
            case 28:
                return "ОКОН ОМВД России по Амурскому району";
            case 29:
                return "ОКОН ОМВД России по Солнечному району";
            //
            case 30:
                return "ОКОН УМВД РФ по Комсомольскому району"; // отсутствует в базе подразделений
            // подразделения отсутствующие в визере
            case 35:
                return "ОУР Комсомольского ЛО МВД России на транспорте";
            case 36:
                return "ОБППГ Комсомольского ЛО МВД России на транспорте";
            case 37:
                return "4 отд. 2 ОРЧ ГУ МВД РФ по ДФО";
            case 38:
                return "УЭБиПК УМВД России по Хабаровскому краю";
            case 39:
                return "4 отд. 1 ОРЧ полиции ГУ МВД РФ по ДФО";
            case 40:
                return "ЭБиПК Комсомольского ЛО МВД России на транспорте";
            case 41:
                return "1 ОРЧ полиции ГУ МВД РФ по ДФО";
            case 42:
                return "2 ОРЧ полиции ГУ МВД РФ по ДФО";
            case 43:
                return "ОБОП УУР УМВД России по Хабаровскому краю";
            case 44:
                return "ОЭБиПК МОМВД России Биробиджанский";
            default:
                return "НЕ УСТАНОВЛЕН";
        }*/
    }


    /**
     * Является ли строка числом или нет
     *
     * @param string
     * @return
     */
    public static boolean checkString(String string) {
        if (string == null || string.length() == 0) return false;

        int i = 0;
        if (string.charAt(0) == '-') {
            if (string.length() == 1) {
                return false;
            }
            i = 1;
        }

        char c;
        for (; i < string.length(); i++) {
            c = string.charAt(i);
            if (!(c >= '0' && c <= '9')) {
                return false;
            }
        }
        return true;
    }

    /**
     * Группируют номер группами по три числа разделенных пробелами
     * если меньше 3-х то добавляем числа к последней группе
     *
     * @param string
     * @return
     */
    public static String groupByThree(String string) {
        String text = "";
        for (int i = 0; i <= string.length() - 1; i++) {
            if (i % 3 == 0 && string.length() - i >= 3) {

                text += string.substring(i, i + 3) + " ";
                i += 2;
                continue;
            } else if (string.length() - i <= 2) {
                text = text.substring(0, text.length() - 1) + string.substring(i, string.length());
                break;
            }
        }
        text.trim();
        return text;
    }

}
