package rus.dreamer.Logic;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import rus.dreamer.GUI.MainWindow;
import rus.dreamer.Logic.paragraph.Oper;
import rus.dreamer.Logic.paragraph.Paragraph;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils.*;


/**
 * Created by Dreamer on 27.11.14.
 */
public class Importer implements ActionListener {
    MainWindow window;
    DB database;

    private static final Logger log = Logger.getLogger(Importer.class);

    public Importer(MainWindow window, DB database) {
        this.window = window;
        this.database = database;
    }


    /**
     * Pushing the button "Импорт"
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        // Проверяем если поля изменяющиеся в данный момент в табличке
        if (window.tbInitiator.isEditing()) {
            window.tbInitiator.getCellEditor().stopCellEditing();
        }

        String numberBlank, taskNumber, inNum;
        taskNumber = window.taskNumber.getText();
        numberBlank = window.blankNumber.getText();
        inNum = window.tInMN.getText();
        if (Settings.getParagraph()) {
            if (window.rTypeInMN1.isSelected()) {
                if (numberBlank.length() >= 1) {

                    importInDB(window);
                } else {
                    JOptionPane.showMessageDialog(window.mainPanel, "Заполните \"Номер бланка\"", "Предупреждение!", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                if (numberBlank.length() >= 1 && inNum.length() >= 1) {

                    importInDB(window);
                } else {
                    JOptionPane.showMessageDialog(window.mainPanel, "Заполните \"Номер бланка\" и \"Номер входящего\"", "Предупреждение!", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else {
            if (taskNumber.length() >= 1 && numberBlank.length() >= 1) {
                importInDB(window);
            } else {
                JOptionPane.showMessageDialog(window.mainPanel, "Заполните \"Номер задания\" и \"Номер бланка\"", "Предупреждение!", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * Функция предобработки нашей формы и запуска Импорта в базу данных
     *
     * @param window
     */
    public void importInDB(MainWindow window) {
        Boolean rs;
        trimAllFields(window);
        Task task = new Task();
        String taskNumber = window.taskNumber.getText();
        String numberBlank = window.blankNumber.getText();
        Date startTask = window.dStartDate.getDate();
        String iniFio = StringUtils.capitalize(window.tbInitiator.getModel().getValueAt(0, 0).toString());
        // String iniPodrazd = window.tbInitiator.getModel().getValueAt(0,1).toString();
        String iniOrgan = window.tbInitiator.getModel().getValueAt(0, 1).toString();
        String iniPodr = window.tbInitiator.getModel().getValueAt(0, 2).toString();
        String iniPhoneA = window.tbInitiator.getModel().getValueAt(0, 3).toString();
        String iniPhoneB = window.tbInitiator.getModel().getValueAt(0, 4).toString();
        Operator operator = (Operator) window.lOperators.getSelectedValue();
        Shifr shifr = (Shifr) window.cbShifr.getSelectedItem();
        Meropr meropr = (Meropr) window.cbMerType.getSelectedItem();
        String target = window.taTarget.getText();
        String orient = window.taOrient.getText();
        String name = StringUtils.capitalize(window.tObjName.getText());
        String family = StringUtils.capitalize(window.tObjFam.getText());
        String surname = StringUtils.capitalize(window.tObjSurname.getText());
        Date dateBirth = window.dDateBirth.getDate();
        String sex = window.tObjSex.getSelectedItem().toString();
        String town = window.tObjAdrTown.getText();
        String street = window.tObjAdrStreet.getText();
        String building = window.tObjAdrBuild.getText();
        String korp = window.tObjAdrKorp.getText();
        String flat = window.tObjAdrFlat.getText();
        String jobName = window.tObjJobName.getText();
        String jobDolgn = window.tObjJobDolgn.getText();
        String jobTown = window.tObjJobTown.getText();
        String jobStreet = window.tObjJobStreet.getText();
        String jobBuilding = window.tObjJobBuild.getText();
        String jobKorp = window.tObjJobKorp.getText();
        String jobFlat = window.tObjJobFlat.getText();
        Boolean OPB = null;
        if (window.OPB_checkBox.isSelected() == true) {
            OPB = true;
        }
        if (window.OPB_checkBox.isSelected() == false) {
            OPB = false;
        }

        // Обрабатываем номер телефона
        String phoneNumber = window.tPhoneNumber.getText();
        phoneNumber = phoneNumber.replaceAll(" ", "");


        // Обрабатываем номер IMEI
        String imei = window.tIMEI.getText();
        if (imei != "") imei = imei.replaceAll(" ", "");

        String sancFam = window.tSancFam.getText();
        String sancOrg = window.tSancOrg.getText();
        String sancPost = window.tSancPost.getText().toLowerCase();
        String sancNumb = window.tSancNumb.getText();
        String sancPeriod = window.tSancLong.getText();
        String deloAdd = window.tAddDeloNumber.getText();
        Date sancDate = window.dSancBeg.getDate();
        Delo deloR = (Delo) window.cbDelo.getSelectedItem();
        Integer delo = deloR.id;
        String deloNumb = window.tDeloNumber.getText();

        String addDeloType = window.tAddDelo.getText();
        String addDeloNum = window.tAddDeloNum.getText();
        Date addDeloDate = window.dAddDelo.getDate();
        String addDeloPlace = window.tAddDeloPlace.getText();

        Article article = (Article) window.cbArticles.getSelectedItem();
        //Integer article = art.id;
        Integer artPart = window.tArticlePart.getText().length() != 0 ? Integer.parseInt(window.tArticlePart.getText()) : 0;
        window.But_Import.setEnabled(false);

        Boolean res;

        Integer inID = null; //Получаем при первой регистрации в Параграф, нужен для комплексного задания
        Meropr meropr2 = null;
        Integer prilID2 = -1;

        // Проверка Шифра-Мероприятию

        if (meropr.title.equals("ПТП") && !shifr.title.equals("70")) {
            Log.logAdd("Шифр \"" + shifr.title + "\" не соответсвует типу мероприятия \"ПТП\"!", "error");
            JOptionPane.showMessageDialog(window.mainPanel, "Шифр  \"" + shifr.title + "\" не соответсвует типу мероприятия \"ПТП\"! Импорт прерван.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (meropr.title.equals("СИТКС") && !shifr.title.contains("75")) {
            Log.logAdd("Шифр \"" + shifr.title + "\" не соответсвует типу мероприятия \"СИТКС\"!", "error");
            JOptionPane.showMessageDialog(window.mainPanel, "Шифр  \"" + shifr.title + "\" не соответсвует типу мероприятия \"СИТКС\"! Импорт прерван.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

/////////////////////////////////ПТП,2СИТКС/НАЗ,НВД///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        if (((Meropr) window.cbMerType.getSelectedItem()).getTitle().equals("ПТП,СИТКС(СМС),СИТКС(БС)") || ((Meropr) window.cbMerType.getSelectedItem()).getTitle().equals("НАЗ,НВД") || ((Meropr) window.cbMerType.getSelectedItem()).getTitle().equals("САМ,СОМ СТ")) {
            String massage = "";
            Integer sizefor = 0;
            if (((Meropr) window.cbMerType.getSelectedItem()).getTitle().equals("ПТП,СИТКС(СМС),СИТКС(БС)")) {
                sizefor = 3;
            }

            if (((Meropr) window.cbMerType.getSelectedItem()).getTitle().equals("НАЗ,НВД") || ((Meropr) window.cbMerType.getSelectedItem()).getTitle().equals("ПТП,СИТКС(СМС)") || ((Meropr) window.cbMerType.getSelectedItem()).getTitle().equals("САМ,СОМ СТ")) {
                sizefor = 2;
            }

            for (int x = 1; x <= sizefor; x++) {
///////////ПТП,2СИТКС/////////////////
                if (((Meropr) window.cbMerType.getSelectedItem()).getTitle().equals("ПТП,СИТКС(СМС),СИТКС(БС)") || ((Meropr) window.cbMerType.getSelectedItem()).getTitle().equals("ПТП,СИТКС(СМС)")) {
                    if (x == 1) {
                        ComboBoxModel<Shifr> merL = window.cbShifr.getModel();
                        for (int y = 0; y < merL.getSize(); y++) {
                            if (merL.getElementAt(y).getTitle().equals("70")) {
                                merL.setSelectedItem(merL.getElementAt(y));
                                window.cbShifr.setModel(merL);
                                shifr = (Shifr) window.cbShifr.getSelectedItem();
                            }
                        }
                    }
                    if (x > 1) {
                        ComboBoxModel<Shifr> merL = window.cbShifr.getModel();
                        for (int y = 0; y < merL.getSize(); y++) {
                            if (merL.getElementAt(y).getTitle().equals("75")) {
                                merL.setSelectedItem(merL.getElementAt(y));
                                window.cbShifr.setModel(merL);
                                shifr = (Shifr) window.cbShifr.getSelectedItem();
                            }
                        }
                    }
                    if (x == 1) {
                        ComboBoxModel<Meropr> merL2 = window.cbMerType.getModel();
                        for (int y = 0; y < merL2.getSize(); y++) {
                            if (merL2.getElementAt(y).getTitle().equals("ПТП")) {
                                //merL2.setSelectedItem(merL2.getElementAt(y));
                                meropr2 = merL2.getElementAt(y);
                            }
                        }
                    }

                    if (x > 1) {
                        ComboBoxModel<Meropr> merL2 = window.cbMerType.getModel();
                        for (int y = 0; y < merL2.getSize(); y++) {
                            if (merL2.getElementAt(y).getTitle().equals("СИТКС")) {
                                //  merL2.setSelectedItem(merL2.getElementAt(y));
                                meropr2 = merL2.getElementAt(y);
                            }
                        }
                    }
                    if (x == 1) {
                        target = "Причастность лиц пользующихся телефоном к совершению данного преступления. Их образ жизни, связи, адреса нахождения и проживания. Места хранения и сбыта похищенного, наркотических средств, оружия и боеприпасов. Обстоятельства совершения преступления.";
                    }
                    if (x == 2) {
                        target = "Фиксация коротких текстовых и иных сообщений.";
                    }
                    if (x == 3) {
                        target = "Определение местоположения мобильного терминала по базовым станциям.";
                    }
                }
///////////НАЗ.НАВД/////////////////

                if (((Meropr) window.cbMerType.getSelectedItem()).getTitle().equals("НАЗ,НВД")) {
                    if (x == 1) {
                        ComboBoxModel<Meropr> merL2 = window.cbMerType.getModel();
                        for (int y = 0; y < merL2.getSize(); y++) {
                            if (merL2.getElementAt(y).getTitle().equals("НАЗ")) {
                                //merL2.setSelectedItem(merL2.getElementAt(y));
                                meropr2 = merL2.getElementAt(y);
                            }
                        }
                    }

                    if (x > 1) {
                        ComboBoxModel<Meropr> merL2 = window.cbMerType.getModel();
                        for (int y = 0; y < merL2.getSize(); y++) {
                            if (merL2.getElementAt(y).getTitle().equals("НВД")) {
                                //  merL2.setSelectedItem(merL2.getElementAt(y));
                                meropr2 = merL2.getElementAt(y);
                            }
                        }
                    }

                }
///////////САМ,СТ/////////////////

                if (((Meropr) window.cbMerType.getSelectedItem()).getTitle().equals("САМ,СОМ СТ")) {
                    if (x == 1) {
                        ComboBoxModel<Shifr> merL = window.cbShifr.getModel();
                        for (int y = 0; y < merL.getSize(); y++) {
                            if (merL.getElementAt(y).getTitle().equals("12")) {
                                merL.setSelectedItem(merL.getElementAt(y));
                                window.cbShifr.setModel(merL);
                                shifr = (Shifr) window.cbShifr.getSelectedItem();
                            }
                        }
                    }
                    if (x > 1) {
                        ComboBoxModel<Shifr> merL = window.cbShifr.getModel();
                        for (int y = 0; y < merL.getSize(); y++) {
                            if (merL.getElementAt(y).getTitle().equals("СТ")) {
                                merL.setSelectedItem(merL.getElementAt(y));
                                window.cbShifr.setModel(merL);
                                shifr = (Shifr) window.cbShifr.getSelectedItem();
                            }
                        }
                    }
                    if (x == 1) {
                        ComboBoxModel<Meropr> merL2 = window.cbMerType.getModel();
                        for (int y = 0; y < merL2.getSize(); y++) {
                            if (merL2.getElementAt(y).getTitle().equals("САМ")) {
                                //merL2.setSelectedItem(merL2.getElementAt(y));
                                meropr2 = merL2.getElementAt(y);
                            }
                        }
                    }

                    if (x > 1) {
                        ComboBoxModel<Meropr> merL2 = window.cbMerType.getModel();
                        for (int y = 0; y < merL2.getSize(); y++) {
                            if (merL2.getElementAt(y).getTitle().equals("СОМ")) {
                                //  merL2.setSelectedItem(merL2.getElementAt(y));
                                meropr2 = merL2.getElementAt(y);
                            }
                        }
                    }

                }
////////////////////////////////////

                if (Settings.getParagraph()) {
                    Oper oper = (Oper) window.cbOper.getSelectedItem();

                    Paragraph paragraph = new Paragraph(startTask, oper, numberBlank, ((Meropr) window.cbMerType.getSelectedItem()).getTitle(), shifr.getTitle(),
                            window.rTypeInMN2.isSelected(), window.tInMN.getText(), iniFio, iniPhoneA, iniPhoneB, iniPodr, x, inID, prilID2);
                    //inID = paragraph.getInId();

                    // Регистрация задания в Параграфе
                    res = paragraph.process();
                    inID = paragraph.getInId();
                    prilID2 = paragraph.prilId;
                    if (!res) {
                        Log.logAdd("Не удалось зарегистрировать задание в БД Параграф.", "error");
                        //log.error("Не удалось зарегистрировать задание в БД Параграф.");
                        JOptionPane.showMessageDialog(window.mainPanel, "Не удалось зарегистрировать заадние в БД Параграф! Импорт прерван.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                        return;
                    } else {
                        Log.logAdd("Задание c входящим номером: " + paragraph.getInId() + ", шифр" + paragraph.getNum() + " успешно зарегистрировано в БД Параграф!");

                        window.taskNumber.setText(String.valueOf(paragraph.getNum()));

                        taskNumber = window.taskNumber.getText();
                        if (window.rTypeInMN1.isSelected()) {
                            window.tInMN.setText(String.valueOf(paragraph.getInId()));
                        }
                    }
                }

                // Разрядность номера задания

                String tmpNum = String.valueOf(window.taskNumber.getText());
                int size = tmpNum.length();

                if (size < Settings.getCapacity()) {
                    for (int i = 0; i < Settings.getCapacity() - size; i++) {
                        tmpNum = "0" + tmpNum;
                    }
                    window.taskNumber.setText(tmpNum);
                    taskNumber = tmpNum;
                }

//выбор правильного ОТМ вместо комплексного задания

                Log.logAdd("Этап регистрации задания в БД МАГ.", "info");
                rs = task.taskAdd(database, taskNumber, numberBlank, startTask, phoneNumber, imei, iniFio, iniOrgan, iniPodr, iniPhoneA,
                        iniPhoneB, shifr, meropr2, sancFam, sancOrg, sancPost, sancNumb, sancDate, sancPeriod, target, orient, name, family, surname, dateBirth, sex,
                        town, street, building, korp, flat, jobName, jobDolgn, jobTown, jobStreet, jobBuilding, jobKorp, jobFlat, delo, deloNumb, deloAdd, article, artPart, operator, addDeloType, addDeloNum, addDeloDate, addDeloPlace, OPB);
                if (rs) {
                    Log.logAdd("Задание успешно зарегистрировано в БД МАГ.", "info");
                    massage = massage + "\nШифр задания: " + meropr2 + " " + window.cbShifr.getSelectedItem() + "-" + window.taskNumber.getText() + "-" + Calendar.getInstance().get(Calendar.YEAR) + " ";
                } else {
                    massage = massage + "\n Задание не внесено в базу!!!";
                }
            }


            if (Settings.getParagraph()) {
                massage = "\nНомер входящего: " + window.tInMN.getText() + massage;
            }

            JOptionPane.showMessageDialog(window.mainPanel, "Данные успешно внесены в Базу Данных!" + massage, "Сообщение", JOptionPane.INFORMATION_MESSAGE);
            window.TpQRCode.setText("");
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Если одиночное задание
        else {
            // Если включена регистрация в Параграфе
            if (Settings.getParagraph()) {
                Oper oper = (Oper) window.cbOper.getSelectedItem();

                Paragraph paragraph = new Paragraph(startTask, oper, numberBlank, ((Meropr) window.cbMerType.getSelectedItem()).getTitle(), shifr.getTitle(),
                        window.rTypeInMN2.isSelected(), window.tInMN.getText(), iniFio, iniPhoneA, iniPhoneB, iniPodr, null, null, prilID2);

                // Регистрация задания в Параграфе
                res = paragraph.process();
                if (!res) {
                    Log.logAdd("Не удалось зарегистрировать задание в БД Параграф.", "error");
                    //log.error("Не удалось зарегистрировать задание в БД Параграф.");
                    JOptionPane.showMessageDialog(window.mainPanel, "Не удалось зарегистрировать заадние в БД Параграф! Импорт прерван.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    Log.logAdd("Задание c входящим номером: " + paragraph.getInId() + ", шифр" + paragraph.getNum() + " успешно зарегистрировано в БД Параграф!");

                    window.taskNumber.setText(String.valueOf(paragraph.getNum()));

                    taskNumber = window.taskNumber.getText();
                    if (window.rTypeInMN1.isSelected()) {
                        window.tInMN.setText(String.valueOf(paragraph.getInId()));
                    }

                }
            }


            // Разрядность номера задания

            String tmpNum = String.valueOf(window.taskNumber.getText());
            int size = tmpNum.length();

            if (size < Settings.getCapacity()) {
                for (int i = 0; i < Settings.getCapacity() - size; i++) {
                    tmpNum = "0" + tmpNum;
                }
                window.taskNumber.setText(tmpNum);
                taskNumber = tmpNum;
            }


            Log.logAdd("Этап регистрации задания в БД МАГ.", "info");
            rs = task.taskAdd(database, taskNumber, numberBlank, startTask, phoneNumber, imei, iniFio, iniOrgan, iniPodr, iniPhoneA,
                    iniPhoneB, shifr, meropr, sancFam, sancOrg, sancPost, sancNumb, sancDate, sancPeriod, target, orient, name, family, surname, dateBirth, sex,
                    town, street, building, korp, flat, jobName, jobDolgn, jobTown, jobStreet, jobBuilding, jobKorp, jobFlat, delo, deloNumb, deloAdd, article, artPart, operator, addDeloType, addDeloNum, addDeloDate, addDeloPlace, OPB);
            if (rs) {
                Log.logAdd("Задание успешно зарегистрировано в БД МАГ.", "info");
                window.TpQRCode.setText("");
                if (Settings.getParagraph()) {
                    String msg = "";
                    if (window.tInMN.getText().length() > 0) {
                        msg = msg + "\nНомер входящего: " + window.tInMN.getText();
                    }
                    if (window.taskNumber.getText().length() > 0) {
                        msg = msg + "\nШифр задания: " + window.cbShifr.getSelectedItem() + "-" + window.taskNumber.getText() + "-" + Calendar.getInstance().get(Calendar.YEAR);
                    }
                    JOptionPane.showMessageDialog(window.mainPanel, "Данные успешно внесены в Базу Данных!" + msg, "Сообщение", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(window.mainPanel, "Данные успешно внесены в Базу Данных!", "Сообщение", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                Log.logAdd("Не удалось зарегистрировать задание в БД МАГ.", "info");
                JOptionPane.showMessageDialog(window.mainPanel, "Упс произошла ошибка!", "Сообщение", JOptionPane.ERROR_MESSAGE);
            }
        }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    private void trimAllFields(MainWindow window) {

    }

}

