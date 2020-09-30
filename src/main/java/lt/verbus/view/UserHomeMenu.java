package lt.verbus.view;

import lt.verbus.model.User;

public class UserHomeMenu {
    public static void display(User user){
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
