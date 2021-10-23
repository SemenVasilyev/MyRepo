package rus.dreamer.GUI;


import rus.dreamer.Logic.Settings;
import rus.dreamer.Logic.XMLReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Config extends JFrame {
    public JPanel configPanel;
    private JButton bOk;
    private JButton bCancel;
    private JTextField tDriver;
    private JTextField tName;
    private JTextField tServerName;
    private JTextField tPort;
    private JTextField tDBName;
    private JTextField tUser;
    private JTextField tPassword;
    private JCheckBox chParagraph;
    private JTextField tDriverP;
    private JTextField tURLP;
    private JTextField tServerNameP;
    private JTextField tPortP;
    private JTextField tDBNameP;
    private JTextField tUserP;
    private JTextField tPasswordP;
    private JCheckBox cbCom;
    private JTextField tSpeed;
    private JTextField tBits;
    private JTextField tStopBit;
    private JTextField tParity;
    private JCheckBox cbLogging;
    private JPanel jParagraph;
    private JPanel jCom;
    private JComboBox cbPort;
    private JCheckBox cbGroupByThree;
    private JCheckBox cbUndPersInEach;
    private JPanel jStek;
    private JTextField tDriverS;
    private JTextField tURLS;
    private JTextField tServerNameS;
    private JTextField tPortS;
    private JTextField tDBNameS;
    private JTextField tUserS;
    private JTextField tPasswordS;
    private JCheckBox chStek;
    Map<String, String> config = new HashMap<String, String>();

    final JFrame frame = new JFrame("Config");

    public Config() throws IOException {
        // Ищем файлик и берем из него настройки
        final File file = new File("config.xml");
        if (!file.exists()) {
            // Зануляеем значения
            JOptionPane.showMessageDialog(null, "Не найден файл config.xml", "Ошибка", JOptionPane.ERROR_MESSAGE);
            throw new IOException("Не найден файл config.xml");
        } else {

            // Распарсиваем файлик
            //Map<String, String> config = new HashMap<String, String>();
            config = XMLReader.Reader(file);

            tDriver.setText(config.get("driver"));
            tName.setText(config.get("url"));
            tServerName.setText(config.get("serverName"));
            tPort.setText(config.get("portNumber"));
            tDBName.setText(config.get("databaseName"));
            tUser.setText(config.get("userName"));
            tPassword.setText(config.get("password"));

            chParagraph.setSelected(new Boolean(config.get("paragraph")));

            tDriverP.setText(config.get("driver2"));
            tURLP.setText(config.get("url2"));
            tServerNameP.setText(config.get("serverName2"));
            tPortP.setText(config.get("portNumber2"));
            tDBNameP.setText(config.get("databaseName2"));
            tUserP.setText(config.get("userName2"));
            tPasswordP.setText(config.get("password2"));

            if (chParagraph.isSelected()) { // Если Параграф True
                jParagraph.setVisible(true);
            } else {
                jParagraph.setVisible(false);
            }

            chStek.setSelected(new Boolean(config.get("stek")));

            tDriverS.setText(config.get("driver3"));
            tURLS.setText(config.get("url3"));
            tServerNameS.setText(config.get("serverName3"));
            tPortS.setText(config.get("portNumber3"));
            tDBNameS.setText(config.get("databaseName3"));
            tUserS.setText(config.get("userName3"));
            tPasswordS.setText(config.get("password3"));

            if (chStek.isSelected()) {   //Если Stek Trues
                jStek.setVisible(true);
            } else {
                jStek.setVisible(false);
            }


            // Разное
            cbLogging.setSelected(new Boolean(config.get("log")));
            cbGroupByThree.setSelected(new Boolean(config.get("groupByThree")));
            cbUndPersInEach.setSelected(new Boolean(config.get("undPersInEach")));


            cbCom.setSelected(new Boolean(config.get("com")));

            String[] list = Settings.findPorts();
            ComboBoxModel cbmodel = new DefaultComboBoxModel(list);
            cbmodel.setSelectedItem(config.get("port"));
            cbPort.setModel(cbmodel);

            tSpeed.setText(config.get("speed"));
            tBits.setText(config.get("bits"));
            tStopBit.setText(config.get("stopBits"));
            tParity.setText(config.get("parity"));

            if (cbCom.isSelected()) {
                jCom.setVisible(true);
            } else {
                jCom.setVisible(false);
            }
        }


        bCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();

            }
        });


        chParagraph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (chParagraph.isSelected()) {
                    jParagraph.setVisible(true);
                } else {
                    jParagraph.setVisible(false);
                }
            }
        });

        chStek.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (chStek.isSelected()) {
                    jStek.setVisible(true);
                } else {
                    jStek.setVisible(false);
                }
            }
        });

        cbCom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cbCom.isSelected()) {
                    jCom.setVisible(true);
                } else {
                    jCom.setVisible(false);
                }
            }
        });


        bOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Нажатие на кнопку "Применить"
                config.put("driver", tDriver.getText());
                config.put("url", tName.getText());
                config.put("serverName", tServerName.getText());
                config.put("portNumber", tPort.getText());
                config.put("databaseName", tDBName.getText());
                config.put("userName", tUser.getText());
                config.put("password", tPassword.getText());

                config.put("paragraph", String.valueOf(chParagraph.isSelected()));
                config.put("driver2", tDriverP.getText());
                config.put("url2", tURLP.getText());
                config.put("serverName2", tServerNameP.getText());
                config.put("portNumber2", tPortP.getText());
                config.put("databaseName2", tDBNameP.getText());
                config.put("userName2", tUserP.getText());
                config.put("password2", tPasswordP.getText());

                config.put("stek", String.valueOf(chStek.isSelected()));
                config.put("driver3", tDriverS.getText());
                config.put("url3", tURLS.getText());
                config.put("serverName3", tServerNameS.getText());
                config.put("portNumber3", tPortS.getText());
                config.put("databaseName3", tDBNameS.getText());
                config.put("userName3", tUserS.getText());
                config.put("password3", tPasswordS.getText());

                config.put("log", String.valueOf(cbLogging.isSelected()));
                config.put("groupByThree", String.valueOf(cbGroupByThree.isSelected()));
                config.put("undPersInEach", String.valueOf(cbUndPersInEach.isSelected()));

                config.put("speed", tSpeed.getText());
                config.put("bits", tBits.getText());
                config.put("stopBits", tStopBit.getText());
                config.put("parity", tParity.getText());


                Boolean res = XMLReader.writer(config);

                if (res) {
                    JOptionPane.showMessageDialog(null, "Для применения изменений перезапустите приложение!", "Внимание", JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();
                }


            }
        });

        //JFrame frame = new JFrame("Config");
        frame.setContentPane(configPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setMinimumSize(new Dimension(500, 500));
        frame.setPreferredSize(new Dimension(700, 700));
        frame.setMaximumSize(new Dimension(700, 700));
        Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setMaximumSize(sSize);
        frame.setVisible(true);
    }


}
