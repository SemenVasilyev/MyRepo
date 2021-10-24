public class Main {
    public static void main(String[] args) throws InterruptedException {
        Bank bank = new Bank();

        for (int i = 0; i < 1000; i++)
            bank.addAccount(Account.generateAccount());

        System.out.println(bank.getSumAllAccounts());

        MyThread thread1 = new MyThread(bank);
        MyThread thread2 = new MyThread(bank);

        thread1.start();
        thread2.start();

        Thread.sleep(10000);

        thread1.stopThread();
        thread2.stopThread();

        Thread.sleep(100);

        System.out.println(bank.getSumAllAccounts());

    }
}
