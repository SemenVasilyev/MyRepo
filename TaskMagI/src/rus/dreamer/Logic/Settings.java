package rus.dreamer.Logic;


import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class Settings {
    private static DB database;
    private static Boolean paragraph;
    private static Boolean log;
    private static Integer capacity;
    private static Boolean groupByThree;
    private static Boolean undPersInEach;
    private static Boolean saveInNum;
    private static Boolean saveShifr;
    private static String otdId;
    private static String whereActId;
    private static DB databaseParagraph;
    private static Boolean additionalTables;

    private static Boolean stek;
    private static DB databaseStek;

    private static Boolean com;
    private static String portName;
    private static Integer speed;
    private static Integer bits;
    private static Integer stopBites;
    private static Integer parity;

    public static Boolean flag = false;
    private static SerialPort serialPort;
    private static InputStream inputStream;
    private static OutputStream outputStream;
    private static CommPortIdentifier portIdentifier;
    private static ArrayList<String> portNames = new ArrayList<>();
    static boolean portOpen;


    /**
     * Настройки программы
     */
    public Settings() throws IOException {

        // Ищем файлик и берем из него настройки
        File file = new File("config.xml");
        if (!file.exists()) {
            // Зануляеем значения
            JOptionPane.showMessageDialog(null, "Не найден файл config.xml", "Ошибка", JOptionPane.ERROR_MESSAGE);
            throw new IOException("Не найден файл config.xml");
        } else {
            // Распарсиваем файлик

            Map<String, String> config = new HashMap<String, String>();
            config = rus.dreamer.Logic.XMLReader.Reader(file);

            //Лог
            log = new Boolean(config.get("log"));

            capacity = Integer.parseInt(config.get("capacity"));
            undPersInEach = new Boolean(config.get("undPersInEach"));
            groupByThree = new Boolean(config.get("groupByThree"));
            saveInNum = new Boolean(config.get("saveInNum"));
            saveShifr = new Boolean(config.get("saveShifr"));
            otdId = config.get("otd_id");
            whereActId = config.get("where_act_id");
            additionalTables = new Boolean(config.get("additionalTables"));


            database = new DB(config.get("url"), config.get("driver"), config.get("serverName"), config.get("portNumber"), config.get("databaseName"), config.get("userName"), config.get("password"));
            database.testConnection();
            paragraph = new Boolean(config.get("paragraph"));
            if (paragraph) {
                databaseParagraph = new DB(config.get("url2"), config.get("driver2"), config.get("serverName2"), config.get("portNumber2"), config.get("databaseName2"), config.get("userName2"), config.get("password2"));
                databaseParagraph.testConnection();
            }

            stek = new Boolean(config.get("stek"));
            if (stek) {
                databaseStek = new DB(config.get("url3"), config.get("driver3"), config.get("serverName3"), config.get("portNumber3"), config.get("databaseName3"), config.get("userName3"), config.get("password3"));
                databaseStek.testConnection();
            }

            // Настройки COM-порта
            com = new Boolean(config.get("com"));
            if (com) {
                try {
                    portName = config.get("port");
                    speed = Integer.parseInt(config.get("speed"));
                    parity = Integer.parseInt(config.get("parity"));
                    bits = Integer.parseInt(config.get("bits"));
                    stopBites = Integer.parseInt(config.get("stopBits"));
                } catch (Exception e) {
                    Log.logAdd("Ошибка получения параметров, для подключения Com-порта");
                    com = false;
                }
                if (com) {
                    Enumeration<CommPortIdentifier> portEnum =
                            CommPortIdentifier.getPortIdentifiers();
                    if (portOpen) {                          //close any currently open port
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.logAdd(e.getMessage());
                        }
                        serialPort.notifyOnDataAvailable(false);
                        serialPort.close();
                        serialPort.removeEventListener();
                    }
                    portOpen = false;                   //clear the portOpen flag

                    while (portEnum.hasMoreElements()) {
                        portIdentifier = portEnum.nextElement();

                        if (portIdentifier.getName().equals(portName)
                                && portIdentifier.getPortType() == portIdentifier.PORT_SERIAL) {
                            //gui.setjlStatus(portName + " found.");
                            try {    //try to open the port
                                serialPort = (SerialPort) portIdentifier.open("CommUtil", 2000);
                                portOpen = true;        //if successful, update the portOpen flag
                                break;
                            } catch (PortInUseException e) {
                                //gui.setjlStatus("Port "  + portIdentifier.getName() + " is in use.");
                            } catch (Exception e) {
                                // gui.setjlStatus("Failed to open port " +  portIdentifier.getName());
                            }
                        }
                    }

                    if (portOpen) {
                        try {
                            inputStream = serialPort.getInputStream();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        serialPort.notifyOnDataAvailable(true);

                        try {
                            serialPort.setSerialPortParams(speed, bits, stopBites, parity);
                        } catch (UnsupportedCommOperationException e) {
                            e.printStackTrace();
                        }
                        initwritetoport();
                    }
                }
            }
        }
    }

    public static DB getDB() {
        return database;
    }

    public static DB getDatabaseParagraph() {
        return databaseParagraph;
    }

    public static DB getDatabaseStek() {
        return databaseStek;
    }

    public static Boolean getStek() {
        return stek;
    }

    public static Boolean getLog() {
        return log;
    }

    public static Boolean getParagraph() {
        return paragraph;
    }

    public static SerialPort getSerialPort() {
        return serialPort;
    }

    public static InputStream getInputStream() {
        return inputStream;
    }

    public static OutputStream getOutStream() {
        return outputStream;
    }

    public static Boolean getCom() {
        return com;
    }

    public static Boolean getGroupByThree() {
        return groupByThree;
    }

    public static String getOtdId() {
        return otdId;
    }

    public static String getWhereActId() {
        return whereActId;
    }

    public static Boolean getUndPersInEach() {
        return undPersInEach;
    }

    public static Boolean getSaveInNum() {
        return saveInNum;
    }

    public static Boolean getSaveShifr() {
        return saveShifr;
    }

    public static Integer getCapacity() {
        return capacity;
    }

    public static Boolean getAdditionalTables() {
        return additionalTables;
    }


    public static String[] findPorts() {
        Enumeration<CommPortIdentifier> portEnum =
                CommPortIdentifier.getPortIdentifiers();
        int portCount = 0;

        while (portEnum.hasMoreElements()) {
            portIdentifier = portEnum.nextElement();

                   if (
                    portIdentifier.getPortType() == portIdentifier.PORT_SERIAL) {
                //put it in the array
                portNames.add(portIdentifier.getName());
                portCount++;

            }
        }

        String[] stockArr = new String[portNames.size()];
        stockArr = portNames.toArray(stockArr);
        return stockArr;
    }

    public void initwritetoport() {

        if (portOpen) {
            try {
               outputStream = serialPort.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                serialPort.notifyOnOutputEmpty(true);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }
}
