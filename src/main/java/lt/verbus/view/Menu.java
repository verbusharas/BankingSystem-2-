package lt.verbus.view;

import lt.verbus.model.User;

import java.util.List;

public class Menu {

    public static class Home {

        public static void displayFullMenu() {
            System.out.println("       Welcome to");
            System.out.println("- BANKING SYSTEM v1.0 -");
            printSeparator();
            System.out.println("Make your choice: ");
            System.out.println("[1] Login (existing user)");
            System.out.println("[2] Sign up (new user)");
            System.out.println("[0] EXIT");
            printSeparator();
        }

    }

    public static class UserRegistration {

        public static void displayTitle() {
            System.out.println("-- REGISTER --");
            printSeparator();
            System.out.println("[0] Return to main menu");
        }

        public static void displayUsernameRequest() {
            System.out.println("Create your username:");
        }

        public static void displayFullNameRequest() {
            System.out.println("Enter your full name:");
        }

        public static void displayPhoneNumberRequest() {
            System.out.println("Enter your phone number:");
        }

    }

    public static class UserLoginRequest {

        public static void displayFullMenu() {
            System.out.println("-- LOGIN --");
            printSeparator();
            System.out.println("[0] Return to main menu");
            System.out.println("Enter your username: ");
            printSeparator();
        }

    }

    public static class UserProfile {

        public static void displayFullMenu(User user){
            System.out.println("--- WELCOME, "
                    + user.getUsername().toUpperCase() +"! ---");
            System.out.println("- Name: " + user.getFullName());
            System.out.println("- Phone Number: " + user.getPhoneNumber());
            System.out.println("-----------------------");
            System.out.println("Make your choice: ");
            System.out.println("[1] View my bank accounts");
            System.out.println("[2] View transaction history");
            System.out.println("[3] Export transaction history to file");
            System.out.println("[4] Top up account");
            System.out.println("[5] Withdraw money");
            System.out.println("[6] Transfer money");
            System.out.println("[7] Open new bank account");
            System.out.println("[0] Log out");
            System.out.println("-----------------------");
        }

    }

    public static class WithdrawFunds {

        public static void displayTitle(){
            System.out.println("---- WITHDRAW ----");
        }

    }

    public static class TopUpFunds {

        public static void displayTitle(){
            System.out.println("---- TOP UP ----");
        }

    }

    public static class TransferFunds {

        public static void displayTitle() {
            System.out.println("---- TRANSFER MONEY ----");
        }

        public static void displayReceiverRequest() {
            System.out.println("Enter username of the receiver:");
        }
    }

    public static class OpenNewAccount {

        public static void displayTitle(){
            System.out.println("-- OPEN NEW ACCOUNT --");
            printSeparator();
        }

        public static void displayCardTypeSelectionRequest() {
            System.out.println("Select card type:");
            System.out.println("1. Credit Card");
            System.out.println("2. Debit Card");
        }

    }

    public static class ExportToFile {

        public static void displayTitle() {
            System.out.println("-- EXPORT TO FILE --");
            printSeparator();
        }

        public static void displayFileNameRequest() {
            System.out.println("The list will be exported to project parent folder. " +
                    "Specify the preferred file name: ");
        }

    }

    public static void DisplayAmountRequest(){
        System.out.println("Enter the amount (EUR):");
    }

    public static void DisplayChooseFromListRequest(String nameOfListEntity) {
        System.out.println("Choose from " + nameOfListEntity + " list:");
    }

    public static void HintUserList(List<User> list) {
        ListPrinter.printUsernameCheatList(list);
    }

    private static void printSeparator(){
        System.out.println("-----------------------");
    }

}
