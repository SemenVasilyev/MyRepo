package rus.dreamer.Logic;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;
import rus.dreamer.Logic.Settings;


public class Log {
    public final  Boolean status;
    public Log(Boolean status){
        this.status = status;
    }

    /**
     * Отчистка файла log.txt
     */
    public static boolean logClear(){
        return true;
    }

    /**
     * Добавления записи
     * @return
     */
    public static void logAdd(String event, Boolean result){
        Logger log = Logger.getLogger(Thread.currentThread().getStackTrace()[2].getClassName());
        log.info(event + " " + result);
            }

    /**
     * Добавления записи
     * @return
     */
    public static void logAdd(String event){
        if (Settings.getLog()){
            Logger log = Logger.getLogger(Thread.currentThread().getStackTrace()[2].getClassName());
            log.info(event);
        }
    }

    /**
     *
     * @param event
     * @param level
     */
    public static void logAdd(String event, String level){
        if (Settings.getLog()){
            Logger log = Logger.getLogger(Thread.currentThread().getStackTrace()[2].getClassName());
            if (level.equals("trace")){
                log.trace(event);
            }
            else if (level.equals("debug")){
                log.debug(event);
            }
            else if (level.equals("info")){
                log.info(event);
            }
            else if (level.equals("warn")){
                log.warn(event);
            }
            else if (level.equals("error")){
                log.error(event);
            }
            else if (level.equals("fatal")){
                log.fatal(event);
            }
            else{
                log.info(event);
            }

        }
    }

}
