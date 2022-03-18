import java.util.ArrayList;

public class Account {

    private String name; // имя аккаунта

    private  String uuid; // ID аккаунта

    private User holder; // юзер пользующийся аккаунтом

    private ArrayList<Transaction> transactions;

    /**
     * @param name  name of the account
     * @param holder  user that holds this account
     * @param theBank  Bank
     */

    public Account(String name, User holder, Bank theBank)
    {
        this.name = name;
        this.holder = holder;

        // get new account UUID
        this.uuid = theBank.getNewAccountUUID();

        //инициируем транзакции в список
        this.transactions = new ArrayList<Transaction>();



    }

    /**
     * Get the account ID
     * @return the uuid
     */
    public String getUUID()
    {
        return this.uuid;
    }

    /**
     * Получаем сводную строку аккаунта
     * @return сводную строку
     */

    public String getSummaryLine() {
        //получаем баланс аккаунта
        double balance = this.getBalance();

        //форматируем баланс, если он отрицательный
        if (balance >= 0) {
            return String.format("%s : ₽%.02f : %s", this.uuid, balance, this.name);
        } else {
            return String.format("%s : ₽(%.02f) : %s", this.uuid, balance, this.name);
        }
    }
        public double getBalance()
        {
            double balance = 0;
            for (Transaction t : this.transactions)
            {
                balance += t.getAmount();
            }
            return balance;
        }

    /**
     * выводит историю транзакций
     */
    public void printTransHistory()
        {
            System.out.printf("\nИстория транзакций аккаунта %s\n", this.uuid);
            for (int t = this.transactions.size()-1;t >= 0; t--)
            {
                System.out.println(this.transactions.get(t).getSummaryLine());
            }
            System.out.println();

        }

        public void addTransaction(double amount, String memo)
        {
            //создаем новую транзакцию и добавляем её в список
            Transaction newTrans = new Transaction(amount, memo,this);
            this.transactions.add(newTrans);
        }
}
