package lt.verbus.view;

import lt.verbus.model.BankAccount;

public class Messenger {

    public static class Info{

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

    public static class Error{

        public static void warnAboutFalseMenuEntry(){
            System.out.println(ConsoleColor.RED + "ERROR: Please choose from listed menu items" + ConsoleColor.DEFAULT);
        }

        public static void warnAboutUserNotFound(String username){
            System.out.println(ConsoleColor.RED + "ERROR: User with username \'" + username + "\' not found. " + ConsoleColor.DEFAULT);
        }

        public static void warnAboutUserExists(){
            System.out.println(ConsoleColor.RED + "ERROR: User with such username already exists. Please choose different username." + ConsoleColor.DEFAULT);
        }

        public static void warnAboutInsufficientFunds(){
            System.out.println(ConsoleColor.RED + "ERROR: Could not complete operation. Insufficient funds." + ConsoleColor.DEFAULT);
        }

    }

}
