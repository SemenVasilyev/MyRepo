import lombok.Data;

import java.util.concurrent.atomic.AtomicBoolean;

@Data
public class Account {
    private long money;
    private String accNumber;
    private AtomicBoolean isFraud = new AtomicBoolean(false);

    static Account generateAccount(){
//        int max = 70000;
//        int min = 20000;
        Account account = new Account();
//        account.setMoney((long) ((Math.random() * (max - min)) + min));
        account.setMoney(60000);
        account.setAccNumber("" +(int) ((Math.random() * (99999999 - 1)) + 1));
        return account;
    }


}
