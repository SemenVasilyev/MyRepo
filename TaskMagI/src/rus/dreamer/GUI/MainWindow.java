package rus.dreamer.GUI;

import com.toedter.calendar.JDateChooser;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import rus.dreamer.Logic.*;
import rus.dreamer.Logic.paragraph.*;
import rus.dreamer.Logic.stek.Stek;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainWindow extends JFrame {
    public DB database;
    public JFrame frame;
    public JButton But_Decrypt;
    public JButton But_Import;
    public JPanel mainPanel;
    public JTextArea TpQRCode;
    public JTextArea TpDcText;
    public JTextField taskNumber;
    public JTextField blankNumber;
    public JComboBox cbMerType;
    public JTextArea taOrient;

    public JMenuBar menuBar;
    public JMenu menu;
    public JMenuItem menuItem1;
    public JMenuItem menuItem2;
    public JMenuItem menuItem3;

    public JTextArea taTarget;
    public JTable tbInitiator;
    public JTextField tObjSurname;
    public JTextField tObjFam;
    public JComboBox tObjSex;
    public JTextField tObjName;
    public JComboBox cbShifr;
    public JTextField tObjAdrTown;
    public JTextField tObjAdrStreet;
    public JTextField tObjAdrBuild;
    public JTextField tObjAdrFlat;
    public JTextField tObjAdrKorp;
    public JTextField tObjJobTown;
    public JTextField tObjJobStreet;
    public JTextField tObjJobBuild;
    public JTextField tObjJobFlat;
    public JTextField tObjJobKorp;
    public JTextField tObjJobName;
    public JTextField tObjJobDolgn;
    public JTextField tPhoneNumber;
    public JDateChooser dStartDate;
    public JDateChooser dDateBirth;
    public JTextField tSancFam;
    public JTextField tSancOrg;
    public JTextField tSancPost;
    public JTextField tSancLong;
    public JTextField tSancNumb;
    public JDateChooser dSancBeg;
    public JTextField tDeloNumber;
    public JComboBox cbDelo;
    public JComboBox cbArticles;
    public JTextField tArticlePart;
    public JComboBox cbOper;
    public JLabel lOper;
    public JRadioButton rTypeInMN1;
    public JRadioButton rTypeInMN2;
    public JTextField tInMN;
    public JLabel lInMn;
    public JPanel jpParagraph;
    public JTextField tAddDeloNumber;
    private JScrollPane jspTask;
    public JFormattedTextField tIMEI;
    private JButton But_CheckActive;
    public JList lOperators;
    public JTextField tAddDeloNum;
    public JTextField tAddDeloPlace;
    public JDateChooser dAddDelo;
    public JTextField tAddDelo;
    public JCheckBox OPB_checkBox;
    public JLabel ShifrText;

    private SerialPort serialPort;

    public MainWindow(DB database) throws IOException, SQLException {
        this.database = database;


        MaskFormatter accFomat = null;
        try {
            accFomat = new MaskFormatter("*** *** *** *** ***");
            accFomat.setValidCharacters("0123456789");
            accFomat.setOverwriteMode(true);
        }catch (ParseException e){
            Log.logAdd(e.getMessage());
        }
        DefaultFormatterFactory formatterFactory = new DefaultFormatterFactory(accFomat);

        tIMEI.setFormatterFactory(formatterFactory);

       // Привязываем COM-порт
        if(Settings.getCom()){

            serialPort= Settings.getSerialPort();
            try {
                serialPort.addEventListener(new SerialPortEventListener() {
                    @Override
                    public void serialEvent(SerialPortEvent event) {
                        switch (event.getEventType()) {
                            case SerialPortEvent.BI:
                            case SerialPortEvent.OE:
                            case SerialPortEvent.FE:
                            case SerialPortEvent.PE:
                            case SerialPortEvent.CD:
                            case SerialPortEvent.CTS:
                            case SerialPortEvent.DSR:
                            case SerialPortEvent.RI:
                            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                                break;
                            case SerialPortEvent.DATA_AVAILABLE:
                                byte[] readBuffer = new byte[64];
                                try {
                                    if (Settings.flag == true){
                                        TpQRCode.setText("");
                                        Settings.flag = false;
                                    }
                                    // read data
                                    InputStream inputStream = Settings.getInputStream();
                                    int numBytes = inputStream.read(readBuffer);
                                    if (inputStream.available() == 0) {
                                        Settings.flag = true;}
                                    inputStream.close();

                                    //send the received data to the GUI
                                    String result = new String(readBuffer, 0, numBytes);

                                    TpQRCode.append(result);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }
                });
            }catch (Exception s){
                Log.logAdd(s.getMessage());
                s.printStackTrace();
                JOptionPane.showMessageDialog(mainPanel, "QR-scanner not found! Check settings.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }

        //Create the menu bar.
        menuBar = new JMenuBar();

        //Cоздаем меню
        menu = new JMenu("Меню");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription(
                "The only menu in this program that has menu items");
        menuBar.add(menu);

        //a group of JMenuItems
        menuItem1 = new JMenuItem("Настройки",
                KeyEvent.VK_S);
        menuItem1.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_1, ActionEvent.ALT_MASK));
        menuItem1.getAccessibleContext().setAccessibleDescription(
                "This doesn't really do anything");
        menu.add(menuItem1);

        menuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(mainPanel, "This part will work soon", "Information", JOptionPane.INFORMATION_MESSAGE);
                try {
                    Config config = new Config();  // Создаем форму Настроек

                }catch (IOException s){
                    s.printStackTrace();
                    Log.logAdd("Ошибка открытия окна \"Настройки\"");
                }

            }
        });

        menuItem2 = new JMenuItem("Экспорт словарей", KeyEvent.VK_2);
        menuItem2.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_2, ActionEvent.ALT_MASK));
        menuItem2.setMnemonic(KeyEvent.VK_B);
        menu.add(menuItem2);
        menuItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainPanel, "This part will work soon", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        menuItem3 = new JMenuItem("Выход");
        menuItem3.setMnemonic(KeyEvent.VK_E);
        menu.add(menuItem3);

        menuItem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


        JScrollBar jsp = jspTask.getVerticalScrollBar();
        jsp.setUnitIncrement(10);



        //Fill in ComboBox Shifr
        ArrayList<Shifr>shifrList = new ArrayList<Shifr>();
        shifrList = Shifr.getfromDb(database);
        for(Shifr item: shifrList){
            cbShifr.addItem(item);
        }


        //Fill in ListBox Operators
        ArrayList<Operator>oreratorsList = new ArrayList<Operator>();
        oreratorsList = Operator.getAllOperators();
        lOperators.setListData(oreratorsList.toArray());


        // Fill in ComboBox Meropr default list
        ArrayList<Meropr> meroprL  = new ArrayList<Meropr>();
        meroprL = Meropr.getfromDb(database);
        meroprL.add(new Meropr(meroprL.size()+1,"ПТП,СИТКС(СМС),СИТКС(БС)"));
        meroprL.add(new Meropr(meroprL.size()+1,"НАЗ,НВД"));
        meroprL.add(new Meropr(meroprL.size()+1,"ПТП,СИТКС(СМС)"));
        meroprL.add(new Meropr(meroprL.size()+1,"САМ,СОМ СТ"));

        for(Meropr item : meroprL){
            cbMerType.addItem(item);
        }

        // Дело
        ArrayList<Delo> deloL = new ArrayList<Delo>();
        deloL = Delo.getfromDb(database);

        for (Delo item: deloL){
            cbDelo.addItem(item);
        }

        // Fill in ComboBox Article from DB
        ArrayList<Article> articleL = new ArrayList<Article>();
        articleL = Article.getFromDB(database);

        for (Article item:articleL){
            cbArticles.addItem(item);
        }

        // Заполняем дату принятия, по умолчанию сегодняшним числом
        dStartDate.setCalendar(Calendar.getInstance());

        tbInitiator.setDefaultRenderer(TableColumn.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                // value - значение, кот. хранится в ячейке
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (isSelected) {
                    component.setBackground(Color.red); // e.g.
                }
                return component;
            }
        });


        // If Paragraph's mode is active
        if (Settings.getParagraph()){
            taskNumber.setEnabled(false);
            tInMN.setEnabled(false);
            ArrayList<Oper> operL = new ArrayList<Oper>();
            operL = Oper.getFromDB();

            for(Oper item:operL){
                cbOper.addItem(item);
            }


        }
        else {
            this.jpParagraph.setVisible(false);
        }

        // If Stek mode is active
        if (Settings.getStek()){
            But_CheckActive.setVisible(true);
            But_CheckActive.setEnabled(true);
        }
        else {
            But_CheckActive.setEnabled(false);
            But_CheckActive.setVisible(false);
        }


        // Create listeners
        But_Decrypt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (TpQRCode.getText().length() > 50 ){
                    Decryption decrypt = new Decryption();
                    But_Import.setEnabled(true);
                    try {
                        String text = decrypt.decpypt(TpQRCode.getText().trim());

                    TpDcText.setText(text);
                    compare(decrypt);
                    } catch (ArithmeticException s){
                        s.printStackTrace();
                        Log.logAdd(s.getMessage(),"error");
                    }
                }
                else{
                    JOptionPane.showMessageDialog(mainPanel, "Считайте QR код в соответствующее поле", "Предупреждение!", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        But_CheckActive.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if((tPhoneNumber.getText().length()>0||tIMEI.getText().trim().length()>0)&&(cbMerType.getSelectedItem().toString().equals("ПТП")||cbMerType.getSelectedItem().toString().equals("СИТКС"))
                        && !dSancBeg.toString().isEmpty() && !tSancLong.getText().isEmpty())
                {
                    Stek stek = new Stek(Settings.getDatabaseStek(),tPhoneNumber.getText(), tIMEI.getText(), dStartDate.getDate(), cbMerType.getSelectedItem().toString(), dSancBeg.getDate(), tSancLong.getText());
                    stek.getInformation();
                }
                else
                {
                    if(!(tPhoneNumber.getText().length()>0||tIMEI.getText().trim().length()>0)){
                        JOptionPane.showMessageDialog(mainPanel, "Должен быть указан номер телефона или IMEI!", "Ошибка", JOptionPane.WARNING_MESSAGE);
                    }

                    if(!(cbMerType.getSelectedItem().toString().equals("ПТП")||cbMerType.getSelectedItem().toString().equals("СИТКС"))){
                        JOptionPane.showMessageDialog(mainPanel, "Тип мероприяния должен быть: \"ПТП\" или \"СИТКС\"!", "Ошибка", JOptionPane.WARNING_MESSAGE);
                    }
                    if(dSancBeg.toString().isEmpty() || tSancLong.getText().isEmpty()){
                        JOptionPane.showMessageDialog(mainPanel, "Поля: \"Дата вынесения постановления\" и \"Срок постановления\" должны быть заполнены!", "Ошибка", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });



        Importer importer = new Importer(this, database);
        But_Import.addActionListener(importer);

       TpQRCode.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                String os = System.getProperty("os.name").toLowerCase(); // Берем версию ОС
                if (os.contains("win")){
                    String osVersion = System.getProperty("os.version");
                    Locale loc = new Locale("en","US"); // Устанавливаем язык на английский
                    TpQRCode.setLocale(loc);
                    TpQRCode.getInputContext().selectInputMethod(loc);
                }
            }
        });



        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (rTypeInMN2.isSelected()){
                    //lInMn.setVisible(true);
                    tInMN.setEnabled(true);
                }
                else{
                    //lInMn.setVisible(false);
                    tInMN.setEnabled(false);
                }
            }
        };
        rTypeInMN1.addActionListener(listener);
        rTypeInMN2.addActionListener(listener);

        tPhoneNumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
            }
        });
    }



    private void createUIComponents() {
        // TODO: place custom component creation code here
        // Определяем таблицу
        String[] tmp = {
                "ФИО",
                "Орган",
                "Подразделение",
                "Телефон1",
                "Телефон2"
        };
        Object[][] data = {
                {new String(), new String(), new String(), new String(), new String()}};

        tbInitiator = new JTable(data, tmp);

    }

    /** Метод для присвоения всех значений в блок "Задания"
     * и их частичной обработки
     *
     * @param decrypt
     */
    private void compare(Decryption decrypt){
        this.clear();

        DateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH); // Формат для полей даты


        //Вид мероприятия
        ComboBoxModel<Meropr> merL  = cbMerType.getModel();
        for(int i = 0; i < merL.getSize(); i++){
            if (decrypt.merTitle.equals(merL.getElementAt(i).getTitle())){
                merL.setSelectedItem(merL.getElementAt(i));
                this.cbMerType.setModel(merL);
            }
        }

        if(this.cbMerType.getSelectedItem().toString().equals("ПТП,СИТКС(СМС),СИТКС(БС)")||this.cbMerType.getSelectedItem().toString().equals("ПТП,СИТКС(СМС)")||this.cbMerType.getSelectedItem().toString().equals("САМ,СОМ СТ")){
            this.cbShifr.enable(false);
            this.cbShifr.setVisible(false);
            this.ShifrText.setVisible(false);
        }
        else{
            this.cbShifr.enable(true);
            this.cbShifr.setVisible(true);
            this.ShifrText.setVisible(true);
        }


        //Взаимодействие с ОПБ
        if(decrypt.OPB.equals("opb")){this.OPB_checkBox.setSelected(true);}
        if(decrypt.OPB.equals("notopb")){this.OPB_checkBox.setSelected(false);}

        //Телефон
        this.tPhoneNumber.setText(decrypt.phoneN);

        //IMEI
        this.tIMEI.setText(decrypt.imei);

        // Цель
        this.taTarget.setText(decrypt.target);

        // Ориентировка
        this.taOrient.setText(decrypt.orient);

        // Инициатор
        this.tbInitiator.getModel().setValueAt(decrypt.iniName.toUpperCase(),0,0);
        this.tbInitiator.getModel().setValueAt(decrypt.iniOrgan,0,1);
        this.tbInitiator.getModel().setValueAt(decrypt.iniPodrazd,0,2);
        this.tbInitiator.getModel().setValueAt(decrypt.iniTel1,0,3);
        this.tbInitiator.getModel().setValueAt(decrypt.iniTel2,0,4);

        // Судья
        this.tSancFam.setText(decrypt.sancFam);
        this.tSancOrg.setText(decrypt.sancOrg);
        this.tSancPost.setText(decrypt.sancPost);

        // Санкции
        this.tSancNumb.setText(decrypt.sancNumb);
        this.tSancLong.setText(decrypt.sancPeriod);
        try{
            this.dSancBeg.setDate((java.util.Date) format.parse(decrypt.sancDate));
        }catch(Exception e){
            e.printStackTrace();
        }

        // Статья (надо отсортировать по увеличению для удобства)
        this.tArticlePart.setText(decrypt.articlePart);
        ComboBoxModel<Article> articleL  = cbArticles.getModel();
        int FlagArticle = 0;
        for(int i = 0; i < articleL.getSize(); i++){
          //  if (decrypt.article.substring(0,3).equals(articleL.getElementAt(i).title)){
          //  if (decrypt.article.equals(articleL.getElementAt(i).title)){
         //       articleL.setSelectedItem(articleL.getElementAt(i));
         //       this.cbArticles.setModel(articleL);

                if (decrypt.article.equals(articleL.getElementAt(i).title)){
                    articleL.setSelectedItem(articleL.getElementAt(i));
                    this.cbArticles.setModel(articleL);
                    FlagArticle = 1;
                  //  this.tArticles.setText(decrypt.article);
            }
        }

////////////// Если статья не найдена в ComboBox тогда добовляем ее в базу, перезаполняем ComboBox и ищем занова
        if (FlagArticle != 1){
            try {
                Connection con = database.getConnection();
                ResultSet rs;
                Statement st = con.createStatement();
                st.executeUpdate("INSERT INTO sl_ItemsCrimCode(title, caption) VALUES ("+decrypt.article+" , "+decrypt.article+")");
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
                Log.logAdd(e.getMessage(),"error");
            }

            cbArticles.removeAllItems();


            ArrayList<Article> arti = new ArrayList<Article>();
            arti = Article.getFromDB(database);

            for (Article item:arti){
                cbArticles.addItem(item);
            }


            ComboBoxModel<Article> articleLL  = cbArticles.getModel();
            for(int i = 0; i < articleLL.getSize(); i++){
                if (decrypt.article.equals(articleLL.getElementAt(i).title)) {
                    articleL.setSelectedItem(articleLL.getElementAt(i));
                    this.cbArticles.setModel(articleLL);
                }
            }
        }


///////////////////////////////////////////////////////////////////

        // Дело

        /*adDeloType = tmp[39];
        adDeloNumber = tmp[40];
        adDeloDate = tmp[41];
        adDeloKUSPPlace = tmp[42];*/

        if(decrypt.delo != null && decrypt.deloNumber != null){
            compareCB(decrypt.delo);
            this.tDeloNumber.setText(decrypt.deloNumber);
        }
        if (decrypt.adDeloNumber != null && decrypt.adDeloType != null) {
            this.tAddDelo.setText(decrypt.adDeloType);
            this.tAddDeloNum.setText(decrypt.adDeloNumber);
            if (!decrypt.adDeloKUSPPlace.equals(""))
                this.tAddDeloPlace.setText(decrypt.adDeloKUSPPlace);
            if (!decrypt.adDeloDate.equals(""))
                try {
                    this.dAddDelo.setDate((java.util.Date) format.parse(decrypt.adDeloDate));
                }catch (ParseException e){
                    e.printStackTrace();
                }

        }

       //Объект
        this.tObjFam.setText(decrypt.family);
        this.tObjName.setText(decrypt.name);
        this.tObjSurname.setText(decrypt.surname);

        try {
            if(decrypt.dateBirth.length()>0) {
                java.util.Date date = format.parse(decrypt.dateBirth);
                this.dDateBirth.setDate(date);
            }
        }
        catch(ParseException e){
            e.printStackTrace();
        }
        //this.tObjSex.setSelectedIndex(decrypt.sex);
        this.tObjSex.setSelectedItem(decrypt.sex);

        // Адресс
        this.tObjAdrTown.setText(decrypt.town);
        this.tObjAdrStreet.setText(decrypt.street);
        this.tObjAdrBuild.setText(decrypt.build);
        this.tObjAdrKorp.setText(decrypt.kopr);
        this.tObjAdrFlat.setText(decrypt.flat);

        // Место работы
        this.tObjJobName.setText(decrypt.jobName);
        this.tObjJobDolgn.setText(decrypt.jobDolgn);
        this.tObjJobTown.setText(decrypt.jobTown);
        this.tObjJobStreet.setText(decrypt.jobStreet);
        this.tObjJobBuild.setText(decrypt.jobBuild);
        this.tObjJobKorp.setText(decrypt.jobKopr);
        this.tObjJobFlat.setText(decrypt.jobFlat);

    }


    /**
     * Сравнение значений в Комбо боксе
     * @param name
     */
        public void compareCB(String name){
        ComboBoxModel<Delo> model = this.cbDelo.getModel();
        Boolean flag = false;
        for(int i = 0; i < model.getSize(); i++){
            if (name.toUpperCase().equals(model.getElementAt(i).getDelo().toUpperCase())){
                model.setSelectedItem(model.getElementAt(i));
                this.cbDelo.setModel(model);
                flag = true;
            }
        }

        if (!flag){
           // Спрашиваем
            int n = JOptionPane.showConfirmDialog(frame, "Невозможно найти в БД дело: \"" + name + "\".\n Добавить его в БД?", "Предупреждение", JOptionPane.YES_NO_OPTION);
            if (n == 0){
                Delo delo  = new Delo(name);
                boolean res = delo.addInDB(database);
                if(res){
                    JOptionPane.showMessageDialog(frame, "Данные успешно внесены в БД", "Сообщение", JOptionPane.INFORMATION_MESSAGE);
                    // Дело
                    ArrayList<Delo> deloL = new ArrayList<Delo>();
                    deloL = Delo.getfromDb(database);
                    for (Delo item: deloL){
                        cbDelo.addItem(item);
                    }

                    compareCB(name);

                }
                else {
                    JOptionPane.showMessageDialog(null, "Во время добавления данных произошла ошибка", "Сообщение", JOptionPane.INFORMATION_MESSAGE);
                }

            }
        }
    }


    public void clear(){
        taskNumber.setText("");
        blankNumber.setText("");
        cbMerType.setSelectedIndex(0);
        taOrient.setText("");
        taTarget.setText("");

        this.tbInitiator.getModel().setValueAt("",0,0);
        this.tbInitiator.getModel().setValueAt("",0,1);
        this.tbInitiator.getModel().setValueAt("",0,2);
        this.tbInitiator.getModel().setValueAt("",0,3);
        this.tbInitiator.getModel().setValueAt("",0,4);

        tObjSurname.setText("");
        tObjFam.setText("");

        tObjSex.setSelectedIndex(0);
        tObjName.setText("");
        if (!Settings.getSaveShifr()){
            cbShifr.setSelectedIndex(0);
        }
        OPB_checkBox.setSelected(false);
        tObjAdrTown.setText("");
        tObjAdrStreet.setText("");
        tObjAdrBuild.setText("");
        tObjAdrFlat.setText("");
        tObjAdrKorp.setText("");
        tObjJobTown.setText("");
        tObjJobStreet.setText("");
        tObjJobBuild.setText("");
        tObjJobFlat.setText("");
        tObjJobKorp.setText("");
        tObjJobName.setText("");
        tObjJobDolgn.setText("");
        tPhoneNumber.setText("");
        tIMEI.setText("");
        dStartDate.setCalendar(Calendar.getInstance());
        dDateBirth.setDate(null);
        tSancFam.setText(null);
        tSancOrg.setText("");
        tSancPost.setText("");
        tSancLong.setText("");
        tSancNumb.setText("");
        dSancBeg.setDate(null);
        tDeloNumber.setText("");
        tAddDeloNumber.setText("");
        tAddDelo.setText("");
        tAddDeloNum.setText("");
        dAddDelo.setDate(null);
        tAddDeloPlace.setText("");
        cbDelo.setSelectedIndex(0);
        cbArticles.setSelectedIndex(0);
        tArticlePart.setText("");
        if(!Settings.getSaveInNum()) {
            tInMN.setText("");
        }

        if (Settings.getParagraph()){

        }
    }

    private class SerialEventHandler implements SerialPortEventListener {
        public void serialEvent(SerialPortEvent event) {
            switch (event.getEventType()) {
                case SerialPortEvent.DATA_AVAILABLE:
                    readSerial();
                    break;
            }
        }
    }

    private byte[] readBuffer = new byte[400];

    private void readSerial() {
        try {
            InputStream inStream = Settings.getInputStream();
            int availableBytes = inStream.available();
            if (availableBytes > 0) {
                // Read the serial port
                inStream.read(readBuffer, 0, availableBytes);

                // Print it out
                System.out.println(
                        new String(readBuffer, 0, availableBytes));
            }
        } catch (IOException e) {
        }
    }

    private class ReadThread implements Runnable {
        public void run() {
            while(true) {
                readSerial();
            }
        }
    }

    /**
     * Set the serial event handler
     */
    private void setSerialEventHandler(SerialPort serialPort) {
        try {
            // Add the serial port event listener
            serialPort.addEventListener(new SerialEventHandler());
            serialPort.notifyOnDataAvailable(true);
        } catch (TooManyListenersException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
