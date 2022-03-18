import javax.annotation.processing.SupportedSourceVersion;
import java.util.Scanner;


public class ATM {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Bank theBank = new Bank("Ruba Банк");

        User aUser = theBank.addUser("Александр", "Рубцов", "2000");

        Account newAccount = new Account("Другие счета" ,aUser,theBank);
        aUser.addAccount(newAccount);
        theBank.addAccount(newAccount);

        User curUser;
        while(true)
        {
            //удержание заявки входа до полного входа в систему
            curUser = ATM.mainMenuPrompt(theBank,sc);

            //остаемся в главном меню,пока пользователь не выйдет
            ATM.printUserMenu(curUser, sc);

        }
    }

    /**
     *
     * @param theBank
     * @param sc
     * @return
     */

    public static User mainMenuPrompt(Bank theBank, Scanner sc)
    {
        //inits
        String userID;
        String pin;
        User authUser;

        //запрашивать у пользователя комбинацию ID/pin пока не будет получен правильный
        do {
            System.out.printf("\n\nДобро пожаловать в %s\n\n", theBank.getName());
            System.out.printf("Введите ID пользователя: ");
            userID = sc.nextLine();
            System.out.printf("Введите PIN код: ");
            pin = sc.nextLine();

            // получаем и сверяем введенные данные пользователя
            authUser = theBank.userLogin(userID, pin);
            if(authUser == null)
            {
                System.out.println("Некорректный ID/pin пользователя." + "Пожалуйста, повторите попытку.");
            }

        } while (authUser == null);  // траим,пока не будут введены правильные логин и пароль

        return authUser;
    }

    public static void printUserMenu(User theUser, Scanner sc)
    {
        // Выводим сводку учетных записей пользователя
        theUser.printAccountsSummary();

        //init
        int choice;

        //меню пользователя

        do {
            System.out.printf("Добро пожаловать %s, чем бы ты хотел заняться?\n", theUser.getFirstName());
            System.out.println("  1) Показать историю транзакций");
            System.out.println("  2) Снять средства со счета");
            System.out.println("  3) Внести средства на счет");
            System.out.println("  4) Перевести средства");
            System.out.println("  5) Выход");
            System.out.println();
            System.out.println(" Выберите операцию: ");
            choice = sc.nextInt();

            if (choice < 1 || choice > 5)
            {
                System.out.println("Некорректная операция. Пожалуйста,введите цифру от 1 до 5");
            }
        }
        while (choice < 1 || choice > 5);

        // процесс выбора

        switch (choice)
        {

            case 1:
                ATM.showTransHistory(theUser, sc); //история транзакций
                break;
            case 2:
                ATM.withdrawFunds(theUser, sc); //Снятие средств
                break;
            case 3:
                ATM.depositFunds(theUser,sc); // Внесение средств
                break;
            case 4:
                ATM.transferFunds(theUser, sc); // Перевод
                break;
            case 5:
                //съедаем то,что осталось от предыдущего ввода
                sc.nextLine();
                break;
            }

        //повторное отображение меню, если пользователь не захочет выйти
        if(choice != 5)
        {
            ATM.printUserMenu(theUser, sc);
        }
    }

    /**
     * Показывает историю транзакций на аккаунте
     * @param theUser пользователь вошедший в систему
     * @param sc сканер объектов для ввода пользователя
     */

    public static void showTransHistory(User theUser, Scanner sc)
    {
        int theAcct;

        // выбор на каком аккаунте просмотреть транзакции
        do {
            System.out.printf("Введите цифру (1-%d) аккаунта\n" +
                    "на котором вы хотели бы просмотреть историю транзакций: ", theUser.numAccounts());
            theAcct = sc.nextInt()-1;
            if(theAcct < 0 || theAcct >= theUser.numAccounts())
            {
                System.out.println("Недействительный аккаунт. Пожалуйста,повторите снова.");
            }

        } while (theAcct < 0 || theAcct >= theUser.numAccounts());

        // распечатать историю транзакций
        theUser.printAcctTransHistory(theAcct);
    }

    /**
     * процесс перевода средств с одного счета на другой
     * @param theUser пользователь, вошедший в систему
     * @param sc Сканер для пользовательского ввода
     */
    public static void  transferFunds(User theUser,Scanner sc)
    {
        //inits
        int fromAcct;
        int toAcct;
        double amount;
        double acctBal; // баланс счета

        // получаем счет с которого будет производиться перевод

        do {
            System.out.printf("Введите цифру (1-%d)\n" +
                    "чтобы выбрать аккаунт с которого будет осуществлен перевод: ", theUser.numAccounts());
            fromAcct = sc.nextInt() - 1;
            if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
                System.out.println("Недействительный аккаунт. Пожалуйста,повторите снова.");
            }

        } while (fromAcct < 0 || fromAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(fromAcct);

        //получаем счет на который будет производиться перевод
        do {
            System.out.printf("Введите цифру (1-%d)\n" +
                    "чтобы выбрать аккаунт на который будет осуществлен перевод: ", theUser.numAccounts());
            toAcct = sc.nextInt() - 1;
            if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
                System.out.println("Недействительный аккаунт. Пожалуйста,повторите снова.");
            }

        } while (toAcct < 0 || toAcct >= theUser.numAccounts());

        //получаем счет для перевода

        do {
            System.out.printf("Введите сумму перевода (Максимум ₽%.02f): ₽", acctBal);
            amount = sc.nextDouble();
            if(amount < 0)
            {
                System.out.println("Сумма должна быть больше нуля.");
            }
            else if (amount > acctBal)
            {
                System.out.printf("Сумма больше,чем баланс счета ₽%.02f.\n", acctBal);
            }

        }   while (amount < 0 || amount > acctBal);

        // перевод осуществлен
        theUser.addAcctTransaction(fromAcct, -1*amount, String.format(
                "Перевод с аккаунта %s",theUser.getAcctUUID(toAcct)));
        theUser.addAcctTransaction(toAcct, amount, String.format(
                "Перевод с аккаунта %s",theUser.getAcctUUID(fromAcct)));
    }

    /**
     * процесс вывода средств со счета
     * @param theUser пользователь, вошедший в систему
     * @param sc сканер для пользовательского ввода
     */
    public static void withdrawFunds(User theUser,Scanner sc)
    {
        //inits
        int fromAcct;
        int toAcct;
        double amount;
        double acctBal; // баланс счета
        String memo;

        // получаем счет на который будет производиться перевод

        do {
            System.out.printf("Введите цифру (1-%d)\n" +
                    "чтобы выбрать аккаунт с которого вы хотите снять средства: ", theUser.numAccounts());
            fromAcct = sc.nextInt() - 1;
            if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
                System.out.println("Недействительный аккаунт. Пожалуйста,повторите снова.");
            }

        } while (fromAcct < 0 || fromAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(fromAcct);

        do {
            System.out.printf("Введите сумму перевода (Максимум ₽%.02f): ₽", acctBal);
            amount = sc.nextDouble();
            if(amount < 0)
            {
                System.out.println("Сумма должна быть больше нуля.");
            }
            else if (amount > acctBal)
            {
                System.out.printf("Сумма больше,чем баланс счета ₽%.02f.\n", acctBal);
            }

        }   while (amount < 0 || amount > acctBal);

        //съедаем то,что осталось от предыдущего ввода
        sc.nextLine();

        //выводим пямятку
        System.out.print("Памятка: ");
        memo = sc.nextLine();

        //вывод средств
        theUser.addAcctTransaction(fromAcct, -1*amount,memo);

    }

    /**
     * Процесс внесения средств на счет
     * @param theUser вошедший в систему пользователь
     * @param sc сканер для пользовательского ввода
     */
    public static void depositFunds(User theUser,Scanner sc)
    {
        //inits
        int toAcct;

        double amount;
        double acctBal; // баланс счета
        String memo;

        // получаем счет с которого будет производиться перевод

        do {
            System.out.printf("Введите цифру (1-%d)\n" +
                    "чтобы выбрать аккаунта с которого будет осуществлен перевод: ", theUser.numAccounts());
            toAcct = sc.nextInt() - 1;
            if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
                System.out.println("Недействительный аккаунт. Пожалуйста,повторите снова.");
            }

        } while (toAcct < 0 || toAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(toAcct);

        do {
            System.out.printf("Введите сумму которую хотели бы снять (Максимум ₽%.02f): ₽", acctBal);
            amount = sc.nextDouble();
            if(amount < 0)
            {
                System.out.println("Сумма должна быть больше нуля.");
            }

        }   while (amount < 0);

        //съедаем то,что осталось от предыдущего ввода
        sc.nextLine();

        //выводим пямятку
        System.out.print("Памятка: ");
        memo = sc.nextLine();

        //вывод средств
        theUser.addAcctTransaction(toAcct, amount,memo);

    }
}
