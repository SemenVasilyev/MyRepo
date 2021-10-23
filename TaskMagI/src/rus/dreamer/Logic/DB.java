package rus.dreamer.Logic;

import javax.swing.*;
import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;


final public class DB {
    private Connection con = null;
    private ResultSet rs;
    private String driver;
    private String url; // = "jdbc:sqlserver://";
    private String serverName;//= "192.168.3.98\\SQLEXPRESS";
    private String portNumber;// = "1433";
    private String databaseName;//= "MagObjectsJournal";
    private String userName;//  = "";
    private String password;// = "";

    // Сообщает драйверу о необходимости использовать сервером побочного курсора,
    // что позволяет использовать несколько активных выражений
    // для подключения.
    private final String selectMethod = "cursor";

    public DB(String url, String driver, String serverName, String portNumber, String databaseName, String userName, String password) {

        this.url = url;
        this.driver = driver;
        this.serverName = serverName;
        this.portNumber = portNumber;
        this.databaseName = databaseName;
        this.userName = userName;
        this.password = password;

    }


    private String getConnectionUrl() {
        if (this.portNumber != "") {
            return this.url + this.serverName + ":" + this.portNumber + ";databaseName=" + this.databaseName + ";selectMethod=" + this.selectMethod + ";";
        } else {
            return this.url + this.serverName + ";databaseName=" + this.databaseName + ";selectMethod=" + this.selectMethod + ";";
        }

    }

    /**
     * Устанавливаем соединение с БД
     *
     * @return
     */
    public Connection getConnection() {
        try {
            Class.forName(this.driver);
            con = DriverManager.getConnection(getConnectionUrl(), this.userName, this.password);
            if (con != null) {
                System.out.println("Connection Successful!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            //System.out.println("Error Trace in getConnection() : " + e.getMessage());
        }
        return con;
    }

    /**
     * Тестируем подключение к базе данных
     *
     * @throws SQLException
     */
    public void testConnection() {
        try {
            Class.forName(this.driver);
            con = DriverManager.getConnection(getConnectionUrl(), this.userName, this.password);
            if (con != null) Log.logAdd("Подключение к БД " + this.serverName +" " +this.databaseName, true);
            else {
                Log.logAdd("Подключение к БД " + this.serverName + " "+this.databaseName, false);
                JOptionPane.showMessageDialog(null, "Невозможно подключится к Базе Данных МАГ! Проверте настройки в config.xml приложение будет закрыто!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                throw new SQLException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.logAdd("Подключение к БД " + this.serverName +" " +this.databaseName, false);
        }
    }


    /**
     * Метод для тестирования подключения к базе
     */
    public void displayDbProperties() {
        DatabaseMetaData dm = null;
        ResultSet rs = null;
        try {
            con = this.getConnection();
            if (con != null) {
                dm = con.getMetaData();
                System.out.println("Driver Information");
                System.out.println("\tDriver Name: " + dm.getDriverName());
                System.out.println("\tDriver Version: " + dm.getDriverVersion());
                System.out.println("\nDatabase Information ");
                System.out.println("\tDatabase Name: " + dm.getDatabaseProductName());
                System.out.println("\tDatabase Version: " + dm.getDatabaseProductVersion());
                System.out.println("Avalilable Catalogs ");
                if (con != null) {
                    if (dm.supportsNamedParameters() == true) {
                        System.out.println("NAMED PARAMETERS FOR CALLABLE"
                                + "STATEMENTS IS SUPPORTED");
                    }
                }
                rs = dm.getCatalogs();
                while (rs.next()) {
                    System.out.println("\tcatalog: " + rs.getString(1));
                }
                rs = null;
                rs = dm.getProcedures("MagObjectsJournal", null, "%");
                while (rs.next()) {
                    System.out.println("\tprodcedure: " + rs.getString(3));
                }
                rs.close();
                rs = null;
                closeConnection();
            } else System.out.println("Error: No active Connection");
        } catch (Exception e) {
            e.printStackTrace();
        }
        dm = null;
    }

    /**
     * Закрываем соединение с БД
     */
    private void closeConnection() {
        try {
            if (con != null)
                con.close();
            con = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
