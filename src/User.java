
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.security.MessageDigest;
import java.util.function.DoubleToIntFunction;

public class User {

    private String firstName;

    private String lastName;

    private String uuid;

    private byte pinHash[];

    private ArrayList<Account> accounts;

    /**
     *
     * @param firstName the user first name
     * @param lastName  the user last name
     * @param pin       the user account pin number
     * @param theBank   the Bank object that the user is a customer of
     */


    public User(String firstName, String lastName, String pin, Bank theBank)
    {
        //set user name
        this.firstName = firstName;
        this.lastName = lastName;

        //hash the pin MD5
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            this.pinHash = md.digest(pin.getBytes());
        }
        catch (NoSuchAlgorithmException e)
        {
            System.err.println("error, caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }

        // get a new unique  universal ID for the user

        this.uuid = theBank.getNewUserUUID();

        //пустой список записей

        this.accounts = new ArrayList<Account>();

        // логи

        System.out.printf("Новый пользователь %s, %s with ID %s created. \n", lastName,firstName, this.uuid);

    }

    /**
     * Add an account for the user
     * @param anAcct the account to add
     */
    public void addAccount(Account anAcct)
    {
        this.accounts.add(anAcct);
    }

    /**
     * Return the user's UUID
     * @return the uuid
     */
    public String getUUID()
    {
        return this.uuid;
    }

    /**
     *
     * @param aPin проверка ПИНа
     * @return возврат действительного или недействительного ПИНА
     */
    public boolean validatePin(String aPin)
    {
     try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return MessageDigest.isEqual(md.digest(aPin.getBytes()),this.pinHash);
     }
     catch (NoSuchAlgorithmException e)
     {
         System.err.println("error, caught NoSuchAlgorithmException");
         e.printStackTrace();
         System.exit(1);
     }
     return false;
    }

    public String getFirstName()
    {
        return this.firstName;
    }

    public void printAccountsSummary() //вывод транзакций для определенного счета
    {
        System.out.printf("\n\n%s, информация по вашим счетам\n", this.firstName);
        for(int a = 0; a < this.accounts.size();a++)
        {
            System.out.printf("  %d) %s\n", a+1, this.accounts.get(a).getSummaryLine());
        }
        System.out.println();
    }

    public int numAccounts()
    {
        return this.accounts.size();
    }

    public void  printAcctTransHistory(int acctIdx)
    {
        this.accounts.get(acctIdx).printTransHistory();
    }

    public double getAcctBalance(int acctIdx)
    {
        return this.accounts.get(acctIdx).getBalance();
    }

    /**
     * Получение UUID для определенного аккаунта
     * @param acctIdx индекс учетной записи пользователя
     * @return UUID аккаунта
     */
    public String getAcctUUID(int acctIdx)
    {
        return this.accounts.get(acctIdx).getUUID();
    }

    public void addAcctTransaction(int acctIdx, double amount, String memo)
    {
        this.accounts.get(acctIdx).addTransaction(amount, memo);
    }

}
