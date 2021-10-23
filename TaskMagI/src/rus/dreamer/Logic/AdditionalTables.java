package rus.dreamer.Logic;

import java.sql.*;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by vsi on 19.04.2016.
 */
public class AdditionalTables {
    String addType;
    String addNum;
    Date addDate;
    String addPlace;

    AdditionalTables(String addType, String addNum, Date addDate, String addPlace){
        this.addType = addType;
        this.addNum = addNum;
        this.addDate = addDate;
        this.addPlace = addPlace;
    }

    public Boolean initialize(int id){
        Boolean res;

        // Проверяем существуют ли интересующие нас таблицы
        if (!isExist("zzz_places")) {
            res = createPlaces();  // создаем таблицу
            if (res) {
                insertPlaces();
            }
            else {
                return false;
            }
        }

        if (!isExist("zzz_ud_kusp_list"))
            createUDKUSPList();
        else{
            // Добавляем модуль на проверку и модернизацию таблицы паразитный код
            if(!checkTable()){      // Если не прошла проверку
                createUDKUSPListNew(); // Создаем таблицу с другим именем
                // Переносим данные в другую таблицу
                migrateTable();
            }
        }


        // Переходим к заполнению данными

        if (this.addPlace.length()!= 0){
            if (!checkPlace(this.addPlace)){
                Log.logAdd("В БД \"МАГ\" таблице zzz_places отсутсвует значение:" + this.addPlace);
                addPlace = checkPlace2(this.addPlace);
                if(addPlace.equals("")){
                    Log.logAdd("В БД \"МАГ\" таблице zzz_places отсутсвует значение:" + this.addPlace);
                    return false;
                }
            }

        }

        res = addUDKUSPList(id);  // добавляем данные в таблицу
        if(!res){
            Log.logAdd("Не удалось занести данные в таблицу zzz_ud_kusp_list", "warn");
            return false;
        }
        Log.logAdd("В таблицу \"zzz_ud_kusp_list\" занесены следующие значения:" + id +", " +this.addType +", " + this.addNum+ ", " +this.addDate +", " +this.addPlace, "info");
        return  true;
    }


    private  Boolean addUDKUSPList(int id){
        try{
            Connection con = Settings.getDB().getConnection();
            Statement stmt = con.createStatement();
            String sql;

            // Переделать 16.08.2016 (изменился алгоритм заполнения)
            if (!this.addType.equals(1))
                 sql = "INSERT INTO [zzz_ud_kusp_list] VALUES ("+ id +","+ this.addNum+",'"+ new java.sql.Date(this.addDate.getTime()) +"',NULL ,NULL, NULL)";
            else
                 sql = "INSERT INTO [zzz_ud_kusp_list] VALUES ("+ id +",NULL, NULL,"+ this.addNum+",'"+ new java.sql.Date(this.addDate.getTime()) +"',"+ this.addPlace+")";
            stmt.execute(sql);
            return true;

        }catch(SQLException e){
            e.printStackTrace();
            Log.logAdd(e.getMessage(),"error");
            return false;
        }
    }

    /**
     * Проверяем таблицы на существование
     * @return
     */
    private static Boolean isExist(String name){
        try{
            Connection con = Settings.getDB().getConnection();
            DatabaseMetaData metaData = con.getMetaData();
            ResultSet rs = metaData.getTables(null,null, name, new String[]{"Table"});
            if (rs.next()){
                Log.logAdd("В БД МАГ добавлена таблица" + name);
                return true;
            }
        }catch (Exception e){
            Log.logAdd(e.getMessage());
            return false;
        }
        return false;
    }

    /**
     * Создаем таблицу если её не существует
     * @return
     */
    private static Boolean createPlaces(){
        try{
            Connection con = Settings.getDB().getConnection();
            Statement stmp = con.createStatement();

            String command = "USE [MagObjectsJournal]\n" +
                    "SET ANSI_NULLS ON\n" +
                    "SET QUOTED_IDENTIFIER ON\n" +
                    "CREATE TABLE [dbo].[zzz_places](\n" +
                    "\t[id] [int] NOT NULL,\n" +
                    "\t[title] [nvarchar](40) NULL,\n" +
                    "PRIMARY KEY CLUSTERED \n" +
                    "(\n" +
                    "\t[id] ASC\n" +
                    ")WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]\n" +
                    ") ON [PRIMARY]\n" +
                    "EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Название' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'zzz_places', @level2type=N'COLUMN',@level2name=N'title'\n" +
                    "ALTER TABLE [dbo].[zzz_places2] ADD  DEFAULT ('') FOR [title]\n" +
                    "\n";

            stmp.executeUpdate(command);
            return  true;


        }catch (Exception e){
            e.printStackTrace();
            Log.logAdd(e.getMessage());
            return false;
        }
    }

    private static Boolean checkPlace(String number){
        try{
            Connection con = Settings.getDB().getConnection();
            Statement stmp = con.createStatement();
            String sql = "SELECT id FROM zzz_places WHERE id = " + number;
            ResultSet rs = stmp.executeQuery(sql);
            if(rs.next()){
                return true;
            }
            return false;

        }catch (SQLException e){
            e.printStackTrace();
            Log.logAdd(e.getMessage(), "error");
            return false;
        }
    }

    private static String checkPlace2(String title){
        try{
            Connection con = Settings.getDB().getConnection();
            Statement stmp = con.createStatement();
            String sql = "SELECT id FROM zzz_places WHERE title LIKE('" + title+"')";
            ResultSet rs = stmp.executeQuery(sql);
            if(rs.next()){
                return rs.getString(1);
            }
            return "";

        }catch (SQLException e){
            e.printStackTrace();
            Log.logAdd(e.getMessage(), "error");
            return "";
        }
    }

    /**
     * Заполняем таблицу
     * @return
     */
    private static Boolean insertPlaces(){
        try{
            Connection con = Settings.getDB().getConnection();
            Statement stmp = con.createStatement();

            String command = "INSERT INTO [zzz_places] VALUES (12001, '1 ОП Комс');\n" +
                    "INSERT INTO [zzz_places] VALUES (12002, '2 ОП Комс');\n" +
                    "INSERT INTO [zzz_places] VALUES (12003, '3 ОП Комс');\n" +
                    "INSERT INTO [zzz_places] VALUES (12004, '4 ОП Комс');\n" +
                    "INSERT INTO [zzz_places] VALUES (16001, 'Хабаровский район');\n" +
                    "INSERT INTO [zzz_places] VALUES (17001, 'Комсомольский район');\n" +
                    "INSERT INTO [zzz_places] VALUES (18001, 'Солнечный район');\n" +
                    "INSERT INTO [zzz_places] VALUES (19001, 'Амурский район');\n" +
                    "INSERT INTO [zzz_places] VALUES (30000, 'УМВД по Хабаровскому краю');\n" +
                    "INSERT INTO [zzz_places] VALUES (31000, '1 ОП');\n" +
                    "INSERT INTO [zzz_places] VALUES (32000, '2 ОП');\n" +
                    "INSERT INTO [zzz_places] VALUES (33000, '3 ОП');\n" +
                    "INSERT INTO [zzz_places] VALUES (34000, '4 ОП');\n" +
                    "INSERT INTO [zzz_places] VALUES (35000, '5 ОП');\n" +
                    "INSERT INTO [zzz_places] VALUES (36000, '6 ОП');\n" +
                    "INSERT INTO [zzz_places] VALUES (37000, '7 ОП');\n" +
                    "INSERT INTO [zzz_places] VALUES (38000, '8 ОП');\n" +
                    "INSERT INTO [zzz_places] VALUES (39000, '9 ОП');\n" +
                    "INSERT INTO [zzz_places] VALUES (40000, '10 ОП');\n" +
                    "INSERT INTO [zzz_places] VALUES (51000, '11 ОП');\n";

            stmp.executeUpdate(command);
            Log.logAdd("Таблица БД МАГ zzz_places, заполнена стартовыми значениями");
            return  true;

        }catch (Exception e){
            e.printStackTrace();
            Log.logAdd(e.getMessage());
            return false;
        }
    }

    /**
     * Создание таблицы zzz_ud_kusp_list
     * @return
     */
    private static Boolean createUDKUSPList(){
        try{
            Connection con = Settings.getDB().getConnection();
            Statement stmp = con.createStatement();

            String command = "USE [MagObjectsJournal]\n" +
                    "SET ANSI_NULLS ON\n" +
                    "SET QUOTED_IDENTIFIER ON\n" +
                    "CREATE TABLE [dbo].[zzz_ud_kusp_list](\n" +
                    "\t[task_id] [int] NOT NULL,\n" +
                    "\t[UD_num] [nvarchar](20) NULL,\n" +
                    "\t[UD_date] [date] NULL,\n" +
                    "\t[KUSP_num] [nvarchar](20) NULL,\n" +
                    "\t[KUSP_date] [date] NULL,\n" +
                    "\t[KUSP_place] [int] NULL,\n" +
                    "PRIMARY KEY CLUSTERED \n" +
                    "(\n" +
                    "\t[task_id] ASC\n" +
                    ")WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]\n" +
                    ") ON [PRIMARY]\n" +
                    "EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Номер УД' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'zzz_ud_kusp_list', @level2type=N'COLUMN',@level2name=N'UD_num'\n" +
                    "EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Дата заведения УД' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'zzz_ud_kusp_list', @level2type=N'COLUMN',@level2name=N'UD_date'\n" +
                    "EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Номер КУСП' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'zzz_ud_kusp_list', @level2type=N'COLUMN',@level2name=N'KUSP_num'\n" +
                    "EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Дата заведения КУСП' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'zzz_ud_kusp_list', @level2type=N'COLUMN',@level2name=N'KUSP_date'\n" +
                    "EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Место заведения КУСП' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'zzz_ud_kusp_list', @level2type=N'COLUMN',@level2name=N'KUSP_place'\n" +
                    "ALTER TABLE [dbo].[zzz_ud_kusp_list_new]  WITH CHECK ADD FOREIGN KEY([task_id])\n" +
                    "REFERENCES [dbo].[dt_Tasks] ([task_id])\n" +
                    "ALTER TABLE [dbo].[zzz_ud_kusp_list_new]  WITH CHECK ADD  CONSTRAINT [place] FOREIGN KEY([KUSP_place])\n" +
                    "REFERENCES [dbo].[zzz_places] ([id])\n" +
                    "ALTER TABLE [dbo].[zzz_ud_kusp_list] CHECK CONSTRAINT [place]\n" +
                    "ALTER TABLE [dbo].[zzz_ud_kusp_list] ADD  DEFAULT (NULL) FOR [UD_num]\n" +
                    "ALTER TABLE [dbo].[zzz_ud_kusp_list] ADD  DEFAULT (NULL) FOR [UD_date]\n" +
                    "ALTER TABLE [dbo].[zzz_ud_kusp_list] ADD  DEFAULT (NULL) FOR [KUSP_num]\n" +
                    "ALTER TABLE [dbo].[zzz_ud_kusp_list] ADD  DEFAULT (NULL) FOR [KUSP_date]\n" +
                    "ALTER TABLE [dbo].[zzz_ud_kusp_list] ADD  DEFAULT (NULL) FOR [KUSP_place]\n" +
                    "\n";

            stmp.executeUpdate(command);
            Log.logAdd("Таблица БД МАГ zzz_ud_kusp_list, создана");
            return  true;

        }catch (Exception e){
            e.printStackTrace();
            Log.logAdd(e.getMessage());
            return false;
        }
    }

    /**
     * Проверяем таблицу  zzz_ud_kusp_list на необходимость модернизации
     *  06.08.2016
     */
    private static Boolean checkTable(){
        try{
            Connection con = Settings.getDB().getConnection();
            Statement st =  con.createStatement();
            String query  = "IF NOT EXISTS(\n" +
                    "    SELECT 1 \n" +
                    "    FROM [information_schema].[columns]\n" +
                    "    WHERE table_name ='zzz_ud_kusp_list' AND column_name ='UD_num'\n" +
                    "    )\n" +
                    "SELECT 0\n" +
                    "ELSE\n" +
                    "SELECT 1";
           ResultSet rs = st.executeQuery(query);
            int res =  rs.getInt(1);
            if (res == 1) return true;

        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private static Boolean createUDKUSPListNew(){
        try{
            Connection con = Settings.getDB().getConnection();
            Statement stmp = con.createStatement();

            String command = "USE [MagObjectsJournal]\n" +
                    "SET ANSI_NULLS ON\n" +
                    "SET QUOTED_IDENTIFIER ON\n" +
                    "CREATE TABLE [dbo].[zzz_ud_kusp_list](\n" +
                    "\t[task_id] [int] NOT NULL,\n" +
                    "\t[UD_num] [nvarchar](20) NULL,\n" +
                    "\t[UD_date] [date] NULL,\n" +
                    "\t[KUSP_num] [nvarchar](20) NULL,\n" +
                    "\t[KUSP_date] [date] NULL,\n" +
                    "\t[KUSP_place] [int] NULL,\n" +
                    "PRIMARY KEY CLUSTERED \n" +
                    "(\n" +
                    "\t[task_id] ASC\n" +
                    ")WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]\n" +
                    ") ON [PRIMARY]\n" +
                    "EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Номер УД' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'zzz_ud_kusp_list_new', @level2type=N'COLUMN',@level2name=N'UD_num'\n" +
                    "EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Дата заведения УД' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'zzz_ud_kusp_list_new', @level2type=N'COLUMN',@level2name=N'UD_date'\n" +
                    "EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Номер КУСП' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'zzz_ud_kusp_list_new', @level2type=N'COLUMN',@level2name=N'KUSP_num'\n" +
                    "EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Дата заведения КУСП' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'zzz_ud_kusp_list_new', @level2type=N'COLUMN',@level2name=N'KUSP_date'\n" +
                    "EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Место заведения КУСП' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'zzz_ud_kusp_list_new', @level2type=N'COLUMN',@level2name=N'KUSP_place'\n" +
                    "ALTER TABLE [dbo].[zzz_ud_kusp_list_new]  WITH CHECK ADD FOREIGN KEY([task_id])\n" +
                    "REFERENCES [dbo].[dt_Tasks] ([task_id])\n" +
                    "ALTER TABLE [dbo].[zzz_ud_kusp_list_new]  WITH CHECK ADD  CONSTRAINT [place] FOREIGN KEY([KUSP_place])\n" +
                    "REFERENCES [dbo].[zzz_places] ([id])\n" +
                    "ALTER TABLE [dbo].[zzz_ud_kusp_list_new] CHECK CONSTRAINT [place]\n" +
                    "ALTER TABLE [dbo].[zzz_ud_kusp_list_new] ADD  DEFAULT (NULL) FOR [UD_num]\n" +
                    "ALTER TABLE [dbo].[zzz_ud_kusp_list_new] ADD  DEFAULT (NULL) FOR [UD_date]\n" +
                    "ALTER TABLE [dbo].[zzz_ud_kusp_list_new] ADD  DEFAULT (NULL) FOR [KUSP_num]\n" +
                    "ALTER TABLE [dbo].[zzz_ud_kusp_list_new] ADD  DEFAULT (NULL) FOR [KUSP_date]\n" +
                    "ALTER TABLE [dbo].[zzz_ud_kusp_list_new] ADD  DEFAULT (NULL) FOR [KUSP_place]\n" +
                    "\n";

            stmp.executeUpdate(command);
            Log.logAdd("Таблица БД МАГ zzz_ud_kusp_list, создана");
            return  true;

        }catch (Exception e){
            e.printStackTrace();
            Log.logAdd(e.getMessage());
            return false;
        }
    }

    /**
     * Метод для переноса данных в измененную таблицу
     * берем данные из старой таблицы,
     * формируем из них временную таблицу
     * копируем данные из временной таблицы в новую таблицу
     *
     */
    private static void migrateTable(){
        try{

            Connection con = Settings.getDB().getConnection();
            Statement stmp = con.createStatement();
            String querry = "CREATE TABLE #tmp_tbl (task_id INT PRIMARY KEY, ud_num NVARCHAR(55), ud_date DATE,  kusp_num NVARCHAR(55), kusp_date DATE, kusp_plase INTEGER );\n" +
                    "INSERT  #tmp_tbl (task_id)\n" +
                    "(\n" +
                    "	(SELECT DISTINCT zzz_ud_kusp_list.task_id\n" +
                    "	FROM zzz_ud_kusp_list)\n" +
                    "	)\n" +
                    "UPDATE #tmp_tbl\n" +
                    "SET ud_num = \n" +
                    "	(\n" +
                    "	SELECT\n" +
                    "	t1.number\n" +
                    "	FROM zzz_ud_kusp_list AS t1 \n" +
                    "	WHERE t1.type = 1 AND t1.task_id = #tmp_tbl.task_id\n" +
                    "	) ,\n" +
                    "	ud_date = \n" +
                    "	(\n" +
                    "	SELECT\n" +
                    "	t2.data\n" +
                    "	FROM zzz_ud_kusp_list AS t2 \n" +
                    "	WHERE t2.type = 1 AND t2.task_id = #tmp_tbl.task_id\n" +
                    "	),\n" +
                    "	kusp_num = \n" +
                    "	(\n" +
                    "	SELECT\n" +
                    "	t3.number\n" +
                    "	FROM zzz_ud_kusp_list AS t3 \n" +
                    "	WHERE t3.type = 2 AND t3.task_id = #tmp_tbl.task_id\n" +
                    "	),\n" +
                    "	kusp_date = \n" +
                    "	(\n" +
                    "	SELECT\n" +
                    "	t3.data\n" +
                    "	FROM zzz_ud_kusp_list AS t3 \n" +
                    "	WHERE t3.type = 2 AND t3.task_id = #tmp_tbl.task_id\n" +
                    "	),\n" +
                    "	kusp_plase = \n" +
                    "	(\n" +
                    "	SELECT\n" +
                    "	t4.place\n" +
                    "	FROM zzz_ud_kusp_list AS t4 \n" +
                    "	WHERE t4.type = 2 AND t4.task_id = #tmp_tbl.task_id\n" +
                    "	)\n" +
                    "DELETE FROM #tmp_tbl\n" +
                    "WHERE #tmp_tbl.task_id NOT IN (SELECT task_id FROM dt_Tasks)\n" +
                    "INSERT zzz_ud_kusp_list_new\n" +
                    "SELECT * FROM #tmp_tbl\n" +
                    "DROP TABLE zzz_ud_kusp_list;\n" +
                    "EXEC sp_rename 'zzz_ud_kusp_list_new', 'zzz_ud_kusp_list';\n" +
                    "DROP TABLE #tmp_tbl;";
            stmp.executeUpdate(querry);
        } catch (Exception e){
            e.printStackTrace();
            Log.logAdd(e.getMessage());
        }

    }

}
