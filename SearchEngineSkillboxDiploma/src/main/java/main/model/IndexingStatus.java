package main.model;

import java.util.concurrent.atomic.AtomicBoolean;

public class IndexingStatus {
    private static AtomicBoolean run = new AtomicBoolean(false);

    public static void setRun(boolean status){
        run.set(status);
    }

    public static boolean isRun() {
        return run.get() ? true : false;
    }
}
