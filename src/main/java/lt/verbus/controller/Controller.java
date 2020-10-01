package lt.verbus.controller;

import lt.verbus.exception.InsufficientFundsException;
import lt.verbus.exception.EntityNotFoundException;
import lt.verbus.model.Bank;
import lt.verbus.model.BankAccount;
import lt.verbus.model.CardType;
import lt.verbus.model.Transaction;
import lt.verbus.model.User;
import lt.verbus.services.BankAccountService;
import lt.verbus.services.BankService;
import lt.verbus.services.CreditService;
import lt.verbus.services.TransactionService;
import lt.verbus.services.UserService;
import lt.verbus.view.ErrorMessenger;
import lt.verbus.view.InfoMessenger;
import lt.verbus.view.ListPrinter;
import lt.verbus.view.LoginMenu;
import lt.verbus.view.MainMenu;
import lt.verbus.view.OpenNewAccountMenu;
import lt.verbus.view.RegisterMenu;
import lt.verbus.view.UserChooseFromListMenu;
import lt.verbus.view.UserHomeMenu;
import lt.verbus.view.UserSpecifyFileNameMenu;
import lt.verbus.view.UserTopUpMenu;
import lt.verbus.view.UserTransferMenu;
import lt.verbus.view.UserWithdrawMenu;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Controller {
    private BankService bankService;
    private UserService userService;
    private BankAccountService bankAccountService;
    private TransactionService transactionService;
    private CreditService creditService;

    private Scanner consoleInput;

    private User currentUser;

    public Controller() throws IOException, SQLException {
        bankService = new BankService();
        userService = new UserService();
        bankAccountService = new BankAccountService();
        transactionService = new TransactionService();
        creditService = new CreditService();
        consoleInput = new Scanner(System.in);
    }

    public void launchApp() throws SQLException, EntityNotFoundException, IOException {
        boolean stayInMenu = true;
        currentUser = null;
        while (stayInMenu) {
            MainMenu.display();
            switch (consoleInput.nextLine()) {
                case "0":
                    System.exit(1);
                case "1":
                    switchToLoginMenu();
                    break;
                case "2":
                    switchToRegistrationMenu();
                    break;
                default:
                    ErrorMessenger.warnAboutFalseMenuEntry();
            }
        }
    }

    private void switchToRegistrationMenu() throws SQLException, EntityNotFoundException, IOException {
        RegisterMenu.displayTitle();
        User user = new User();
        while (true) {
            RegisterMenu.displayUsernameRequest();
            String usernameAttempt = consoleInput.nextLine();
            user.setUsername(usernameAttempt);
            try {
                userService.findByUsername(user.getUsername());
            } catch (EntityNotFoundException ex) {
                ErrorMessenger.warnAboutUserNotFound(usernameAttempt);
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            ErrorMessenger.warnAboutUserExists();
        }
        RegisterMenu.displayFullNameRequest();
        user.setFullName(consoleInput.nextLine());
        RegisterMenu.displayPhonenumberRequest();
        user.setPhoneNumber(consoleInput.nextLine());
        userService.save(user);
        InfoMessenger.informAboutSuccessfullyCreatedUser();
    }

    public void switchToLoginMenu() throws SQLException, IOException {
        boolean stayInMenu = true;
        while (stayInMenu) {
            LoginMenu.display();
            ListPrinter.printUsernameCheatList(userService.findAll());
            String userChoice = consoleInput.nextLine();
            if (userChoice.equals("0")) {
                stayInMenu = false;
            } else {
                try {
                    currentUser = userService.findByUsername(userChoice);
                    switchToUserMenu();
                    stayInMenu = false;
                } catch (EntityNotFoundException ex) {
                    ErrorMessenger.warnAboutUserNotFound(userChoice);
                } catch (InsufficientFundsException e) {
                    ErrorMessenger.warnAboutInsufficientFunds();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void switchToUserMenu() throws SQLException, EntityNotFoundException, IOException, InsufficientFundsException {
        boolean stayInMenu = true;
        while (stayInMenu) {
            UserHomeMenu.display(currentUser);
            switch (consoleInput.nextLine()) {
                case "0":
                    stayInMenu = false;
                    break;
                case "1":
                    ListPrinter.printNumeratedListToConsole(bankAccountService.findAllBelongingToUser(currentUser));
                    ListPrinter.printNumeratedListToConsole(creditService.findAllByDebtor(currentUser));
                    break;
                case "2":
                    switchToTransactionListMenu();
                    break;
                case "3":
                    switchToExportTransactionsMenu();
                    break;
                case "4":
                    switchToTopUpMenu();
                    break;
                case "5":
                    switchToWithdrawMenu();
                    break;
                case "6":
                    switchToTransferMoney();
                    break;
                case "7":
                    switchToOpenNewAccountMenu();
                    break;
                default:
                    ErrorMessenger.warnAboutFalseMenuEntry();
            }
        }
    }

    private void switchToTransactionListMenu() throws SQLException, IOException {
        BankAccount selectedBankAccount = askToChooseFromList(
                bankAccountService.findAllBelongingToUser(currentUser), "bank account");
        ListPrinter.printNumeratedListToConsole(transactionService.findAllByBankAccount(selectedBankAccount));
    }

    private void switchToExportTransactionsMenu() throws SQLException, IOException {
        BankAccount selectedBankAccount = askToChooseFromList(
                bankAccountService.findAllBelongingToUser(currentUser), "bank account");
        UserSpecifyFileNameMenu.display();
        ListPrinter.printListToFile(transactionService
                .findAllByBankAccount(selectedBankAccount), consoleInput.nextLine());
        InfoMessenger.informAboutSuccesfulExport();
    }

    public void switchToTopUpMenu() throws SQLException, InsufficientFundsException, EntityNotFoundException, IOException {
        UserTopUpMenu.display();
        double amount = Double.parseDouble(consoleInput.nextLine());
        //TODO: validate for numeric input
        BankAccount selectedBankAccount = askToChooseFromList(
                bankAccountService.findAllBelongingToUser(currentUser), "bank account");
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setSender(null);
        transaction.setReceiver(selectedBankAccount);
        transactionService.execute(transaction);
        InfoMessenger.informAboutSuccessfulTopUp();
    }

    public void switchToWithdrawMenu() throws SQLException, EntityNotFoundException, IOException {
        UserWithdrawMenu.display();
        double amount = Double.parseDouble(consoleInput.nextLine());
        //TODO: validate for numeric input
        BankAccount selectedBankAccount = askToChooseFromList(
                bankAccountService.findAllBelongingToUser(currentUser), "bank account");
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setSender(selectedBankAccount);
        transaction.setReceiver(null);
        try {
            transactionService.execute(transaction);
            InfoMessenger.informAboutSuccessfulWithdrawal();
        } catch (InsufficientFundsException e) {
            ErrorMessenger.warnAboutInsufficientFunds();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToTransferMoney() throws SQLException, IOException {
        UserTransferMenu.displayAmountRequest();
        double amount = Double.parseDouble(consoleInput.nextLine());
        //TODO: validate for numeric input
        BankAccount selectedSenderBankAccount = askToChooseFromList(
                bankAccountService.findAllBelongingToUser(currentUser), "bank account");
        User receivingUser = null;
        UserTransferMenu.displayReceiverRequest();
        ListPrinter.printUsernameCheatList(userService.findAll());
        String receiverUsernameEntered = consoleInput.nextLine();
        try {
            receivingUser = userService.findByUsername(receiverUsernameEntered);
            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setSender(selectedSenderBankAccount);
            transaction.setReceiver(bankAccountService.findAllBelongingToUser(receivingUser).get(0));
            transactionService.execute(transaction);
            InfoMessenger.informAboutSuccessfulTransfer();
        } catch (EntityNotFoundException e) {
            ErrorMessenger.warnAboutUserNotFound(receiverUsernameEntered);
        } catch (InsufficientFundsException ex) {
            ErrorMessenger.warnAboutInsufficientFunds();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void switchToOpenNewAccountMenu() throws SQLException, EntityNotFoundException, IOException {
        OpenNewAccountMenu.displayTitle();
        BankAccount bankAccount = new BankAccount();
        Bank bank = askToChooseFromList(bankService.findAll(), "bank");
        bankAccount.setBank(bank);
        boolean stayInMenu = true;
        while (stayInMenu) {
            OpenNewAccountMenu.displayCardTypeSelectRequest();
            switch (consoleInput.nextLine()) {
                case "1": {
                    bankAccount.setCardType(CardType.CREDIT);
                    stayInMenu = false;
                }
                break;
                case "2": {
                    bankAccount.setCardType(CardType.DEBIT);
                    stayInMenu = false;
                }
                break;
                default:
                    ErrorMessenger.warnAboutFalseMenuEntry();
            }
        }
        bankAccount.setHolder(currentUser);
        bankAccount.setBalance(0);
        bankAccount.setIban(bankAccountService.generateIban());
        bankAccountService.save(bankAccount);
        InfoMessenger.informAboutSuccessfullyOpenedBankAccount(bankAccount);
    }

    private <T> T askToChooseFromList(List<T> list, String nameOfListEntity) {
        int choice = 1;
        boolean stayInMenu = true;
        while (stayInMenu) {
            UserChooseFromListMenu.display(nameOfListEntity);
            ListPrinter.printNumeratedListToConsole(list);
            choice = Integer.parseInt(consoleInput.nextLine());
            if (choice < 1 || choice > list.size()) {
                ErrorMessenger.warnAboutFalseMenuEntry();
            } else stayInMenu = false;
        }
        return list.get(choice - 1);
    }

}
