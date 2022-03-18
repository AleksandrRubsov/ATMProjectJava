import java.util.Date;

public class Transaction {

    private  double amount;  //сумма

    private Date timestamp;  //время транзакции

    private String memo;   // памятка операций

    private Account inAccount; //аккаунт на котором произошла транзакция

    public Transaction(double amount, Account inAccount)
    {
        this.amount = amount;
        this.inAccount = inAccount;
        this.timestamp = new Date();
        this.memo = "";
    }

    /**
     * Создаем новую транзакцию
     * @param amount сумма перевода
     * @param memo памятка транзакции
     * @param inAccount аккаунт, которому принадлежит транзакция
     */

    public Transaction(double amount, String memo, Account inAccount)
    {
        //обращаемся к первому конкструктору
        this(amount, inAccount);

        //устанавливаем памятку
        this.memo = memo;
    }

    public double getAmount()
    {
        return this.amount;
    }


    /**
     * получаем строку суммирующую транзакции
     * @return просуммированные транзакции
     */
    public String getSummaryLine()
    {
        if(this.amount >= 0)
        {
            return String.format("%s : ₽%.02f : %s", this.timestamp.toString(), this.amount, this.memo);
        }
        else
        {
            return String.format("%s : ₽(%.02f) : %s", this.timestamp.toString(), -this.amount, this.memo);
        }
    }


}
