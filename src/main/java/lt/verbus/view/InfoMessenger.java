package lt.verbus.view;

import lt.verbus.model.BankAccount;

public class InfoMessenger {
    public static void informAboutSuccessfulTransfer(){
        System.out.println(ConsoleColor.GREEN + "INFO: Money successfully transfered." + ConsoleColor.DEFAULT);
    }

    public static void informAboutSuccessfulTopUp(){
        System.out.println(ConsoleColor.GREEN + "INFO: Money successfully added to your account." + ConsoleColor.DEFAULT);
    }

    public static void informAboutSuccessfulWithdrawal(){
        System.out.println(ConsoleColor.GREEN + "INFO: Money successfully withdrawn from your account." + ConsoleColor.DEFAULT);
    }

    public static void informAboutSuccesfulExport(){
        System.out.println(ConsoleColor.GREEN + "INFO: List successfully exported to file." + ConsoleColor.DEFAULT);
    }

    public static void informAboutSuccessfullyCreatedUser(){
        System.out.println(ConsoleColor.GREEN + "INFO: User successfully created." + ConsoleColor.DEFAULT);
    }

    public static void informAboutSuccessfullyOpenedBankAccount(BankAccount bankAccount){
        System.out.println(ConsoleColor.GREEN + "INFO: Bank account successfully created:");
        System.out.println(bankAccount + ConsoleColor.DEFAULT);
    }

}
