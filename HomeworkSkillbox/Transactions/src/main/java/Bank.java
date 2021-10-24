import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class Bank {

    private HashMap<String, Account> accounts = new HashMap<>();
    private final Random random = new Random();

    public synchronized boolean isFraud(String fromAccountNum, String toAccountNum, long amount) throws InterruptedException {
        Thread.sleep(1000);
        return random.nextBoolean();
    }

    /**
     * TODO: реализовать метод. Метод переводит деньги между счетами. Если сумма транзакции > 50000,
     * то после совершения транзакции, она отправляется на проверку Службе Безопасности – вызывается
     * метод isFraud. Если возвращается true, то делается блокировка счетов (как – на ваше
     * усмотрение)
     */
    public void transfer(String fromAccountNum, String toAccountNum, long amount) throws InterruptedException {
        Account fromAccount = accounts.get(fromAccountNum);
        Account toAccount = accounts.get(toAccountNum);
        if(fromAccount.getMoney()>=amount && !fromAccount.getIsFraud().get() && !toAccount.getIsFraud().get()){
            if(amount > 50000){
                boolean isFraud = this.isFraud(fromAccountNum,toAccountNum, amount);
                if(isFraud){
                    setFraud(fromAccount,toAccount);
                    System.out.println("Fraud: from " + fromAccountNum + ", to " + toAccountNum + ", amount: " + amount + "\n");
                }else {
                    transferDone(fromAccount,toAccount,amount);
                }
            }else {
                transferDone(fromAccount,toAccount,amount);
            }
        }
    }

    /**
     * TODO: реализовать метод. Возвращает остаток на счёте.
     */
    public long getBalance(String accountNum) {
        Account account = accounts.get(accountNum);
        return account.getMoney();
    }

    public long getSumAllAccounts() {
        return accounts.entrySet().stream().map(Map.Entry::getValue).filter(a -> a.getIsFraud().get() != true).map(Account::getMoney).collect(Collectors.summingLong(Long::longValue));
        //return accounts.entrySet().stream().map(Map.Entry::getValue).map(Account::getMoney).collect(Collectors.summingLong(Long::longValue));
    }

    public void addAccount(Account account){
        accounts.put(account.getAccNumber(), account);
    }

    public String getRandomAccount(){
        Random random = new Random();
        Object[] values = accounts.values().toArray();
        Account randomAccount =(Account) values[random.nextInt(values.length)];
        return randomAccount.getAccNumber();
    }

    private synchronized void transferDone(Account fromAccount, Account toAccount,long amount){
        System.out.println("Transfer: from " + fromAccount.getAccNumber() + "(" +fromAccount.getMoney() + "), to " + toAccount.getAccNumber() + "(" + toAccount.getMoney() +"), amount: " + amount);
        fromAccount.setMoney(fromAccount.getMoney() - amount);
        toAccount.setMoney(toAccount.getMoney() + amount);
        System.out.println("               " + fromAccount.getAccNumber() + "(" +fromAccount.getMoney() + "), to " + toAccount.getAccNumber() + "(" + toAccount.getMoney() +")\n");
    }

    private  void setFraud(Account fromAccount,Account toAccount){
        fromAccount.setIsFraud(new AtomicBoolean(true));
        toAccount.setIsFraud(new AtomicBoolean(true));
    }
}
