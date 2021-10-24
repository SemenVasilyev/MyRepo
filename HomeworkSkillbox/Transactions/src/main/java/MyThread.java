import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class MyThread extends Thread {
    private Thread worker;
    private AtomicBoolean running = new AtomicBoolean(false);
    private Bank bank;

    MyThread(Bank bank){
        this.bank = bank;
    }

    public void start() {
        worker = new Thread(this);
        worker.start();
    }

    public void stopThread() {
        running.set(false);
    }

    @SneakyThrows
    @Override
    public void run() {
        running.set(true);
        Random random = new Random();
        while (running.get()){
            long amount = (long) ((Math.random() * (55000 - 10)) + 10);
            String randomAccount1 = bank.getRandomAccount();
            String randomAccount2 = bank.getRandomAccount();
            bank.transfer(randomAccount1,randomAccount2,amount);
        }
    }
}
