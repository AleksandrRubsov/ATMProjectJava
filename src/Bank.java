import java.util.ArrayList;
import java.util.Random;

public class Bank {

    private String name;

    private ArrayList<User> users;  // список клиентов

    private  ArrayList<Account> accounts;  // список аккаунтов

    public Bank(String name)
    {
        this.name = name;
        this.users = new ArrayList<User>();
        this.accounts = new ArrayList<Account>();

    }

    /**
     * Generate a new universally unigue ID for a user
     * @return the uuid
     */

    public String getNewUserUUID()
    {
        //inits
        String uuid;
        Random rng = new Random();
        int len = 6;
        boolean nonUnique;

        //continue looping until we get a unique ID
        do {

            //generate number
            uuid = "";
            for (int c = 0; c < len; c++)
            {
                uuid += ((Integer)rng.nextInt(10)).toString();
            }

            //check to make sure it's unique
            nonUnique = false;

            for(User u : this.users)
            {
                if(uuid.compareTo(u.getUUID()) == 0)
                {
                    nonUnique = true;
                    break;
                }

            }


        } while (nonUnique);

        return uuid;
    }



    public String getNewAccountUUID()
    {
        //inits
        String uuid;
        Random rng = new Random();
        int len = 10;
        boolean nonUnique;

        //continue looping until we get a unique ID
        do {

            //generate number
            uuid = "";
            for (int c = 0; c < len; c++)
            {
                uuid += ((Integer)rng.nextInt(10)).toString();
            }

            //check to make sure it's unique
            nonUnique = false;

            for(Account a : this.accounts)
            {
                if(uuid.compareTo(a.getUUID()) == 0)
                {
                    nonUnique = true;
                    break;
                }

            }


        } while (nonUnique);

        return uuid;
    }

    /**
     *
     * @param anAcct добавление аккаунта
     */
    public void addAccount(Account anAcct)
    {
        this.accounts.add(anAcct);
    }
    /**
     *
     * @param firstName имя пользователя
     * @param lastName фамилия пользователя
     * @param pin пин код юзера
     * @return возврат нового пользователя
     */
    public User addUser(String firstName, String lastName, String pin)
    {
        //создание нового юзера и добавление его в список

        User newUser = new User(firstName, lastName, pin,this);
        this.users.add(newUser);

        //создание сберегательного счета пользователя
        Account newAccount = new Account("Сбережения", newUser,this);
        // добавляем счет в список
        newUser.addAccount(newAccount); // владельцев
        this.addAccount(newAccount); // банка

        return newUser;
    }

    /**
     *
     * @param userID UUID пользователя
     * @param pin пин пользователя
     * @return возвращает пользователя,если авторизация прошла успешно и 0,если нет
     */
    public User userLogin(String userID, String pin)
    {
        //ищем ID пользователя в списке
        for(User u : this.users)
        {
            //проверяем правильность ID
            if(u.getUUID().compareTo(userID) == 0 && u.validatePin(pin))
            {
                return u;
            }
        }
        //если пользователь не найден

        return null;
    }

    /**
     * Возвращаем название банка
     * @return Название банка банка
     */

    public String getName()
    {
        return this.name;
    }


}
