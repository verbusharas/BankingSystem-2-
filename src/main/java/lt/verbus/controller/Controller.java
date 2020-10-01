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
import lt.verbus.view.ListPrinter;
import lt.verbus.view.Menu;
import lt.verbus.view.Messenger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Controller {

    private final BankService bankService;
    private final UserService userService;
    private final BankAccountService bankAccountService;
    private final TransactionService transactionService;
    private final CreditService creditService;

    private final Scanner consoleInput;

    private User currentUser;
    private BankAccount currentBankAccount;

    public Controller() throws IOException, SQLException {
        bankService = new BankService();
        userService = new UserService();
        bankAccountService = new BankAccountService();
        transactionService = new TransactionService();
        creditService = new CreditService();
        consoleInput = new Scanner(System.in);
    }

    public void launchApp() throws SQLException, EntityNotFoundException, IOException {
        currentUser = null;
        while (true) {
            Menu.Home.displayFullMenu();
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
                    Messenger.Error.warnAboutFalseMenuEntry();
            }
        }
    }

    private void switchToRegistrationMenu() throws SQLException, EntityNotFoundException, IOException {
        Menu.UserRegistration.displayTitle();
        User user = new User();
        while (true) {
            Menu.UserRegistration.displayUsernameRequest();
            String usernameAttempt = consoleInput.nextLine();
            user.setUsername(usernameAttempt);
            try {
                userService.findByUsername(user.getUsername());
            } catch (EntityNotFoundException ex) {
                Messenger.Error.warnAboutUserNotFound(usernameAttempt);
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            Messenger.Error.warnAboutUserExists();
        }

        Menu.UserRegistration.displayFullNameRequest();
        user.setFullName(consoleInput.nextLine());

        Menu.UserRegistration.displayPhoneNumberRequest();
        user.setPhoneNumber(consoleInput.nextLine());

        userService.save(user);
        Messenger.Info.informAboutSuccessfullyCreatedUser();
    }

    public void switchToLoginMenu() throws SQLException, IOException {
        boolean stayInMenu = true;
        while (stayInMenu) {
            Menu.UserLoginRequest.displayFullMenu();
            Menu.HintUserList(userService.findAll());
            String userChoice = consoleInput.nextLine();
            if (userChoice.equals("0")) {
                stayInMenu = false;
            } else {
                try {
                    currentUser = userService.findByUsername(userChoice);
                    switchToUserMenu();
                    stayInMenu = false;
                } catch (EntityNotFoundException ex) {
                    Messenger.Error.warnAboutUserNotFound(userChoice);
                } catch (InsufficientFundsException e) {
                    Messenger.Error.warnAboutInsufficientFunds();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void switchToUserMenu() throws SQLException, EntityNotFoundException, IOException, InsufficientFundsException {
        boolean stayInMenu = true;
        while (stayInMenu) {
            Menu.UserProfile.displayFullMenu(currentUser);
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
                    switchToTransferMoneyMenu();
                    break;
                case "7":
                    switchToOpenNewAccountMenu();
                    break;
                default:
                    Messenger.Error.warnAboutFalseMenuEntry();
            }
        }
    }

    private void switchToTransactionListMenu() throws SQLException, IOException {
        BankAccount selectedBankAccount = askToChooseFromList(
                bankAccountService.findAllBelongingToUser(currentUser), "bank account");
        ListPrinter.printNumeratedListToConsole(transactionService.findAllByBankAccount(selectedBankAccount));
    }

    private void switchToExportTransactionsMenu() throws SQLException, IOException {
        Menu.ExportToFile.displayTitle();
        BankAccount selectedBankAccount = askToChooseFromList(
                bankAccountService.findAllBelongingToUser(currentUser), "bank account");
        Menu.ExportToFile.displayFileNameRequest();
        ListPrinter.printListToFile(transactionService
                .findAllByBankAccount(selectedBankAccount), consoleInput.nextLine());
        Messenger.Info.informAboutSuccesfulExport();
    }

    public void switchToTopUpMenu() throws SQLException, InsufficientFundsException, EntityNotFoundException, IOException {
        Menu.TopUpFunds.displayTitle();
        Transaction transaction = prepareTransaction();
        transaction.setReceiver(currentBankAccount);
        transactionService.execute(transaction);
        Messenger.Info.informAboutSuccessfulTopUp();
    }

    public void switchToWithdrawMenu() throws SQLException, EntityNotFoundException, IOException {
        Menu.WithdrawFunds.displayTitle();
        Transaction transaction = prepareTransaction();
        transaction.setSender(currentBankAccount);
        try {
            transactionService.execute(transaction);
            Messenger.Info.informAboutSuccessfulWithdrawal();
        } catch (InsufficientFundsException e) {
            Messenger.Error.warnAboutInsufficientFunds();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToTransferMoneyMenu() throws SQLException, IOException {
        Menu.TransferFunds.displayTitle();
        Transaction transaction = prepareTransaction();

        User receivingUser = null;
        Menu.TransferFunds.displayReceiverRequest();
        Menu.HintUserList(userService.findAll());
        String specifiedReceiverUsername = consoleInput.nextLine();
        try {
            receivingUser = userService.findByUsername(specifiedReceiverUsername);
            transaction.setSender(currentBankAccount);
            transaction.setReceiver(bankAccountService.findAllBelongingToUser(receivingUser).get(0));
            transactionService.execute(transaction);
            Messenger.Info.informAboutSuccessfulTransfer();
        } catch (EntityNotFoundException e) {
            Messenger.Error.warnAboutUserNotFound(specifiedReceiverUsername);
        } catch (InsufficientFundsException ex) {
            Messenger.Error.warnAboutInsufficientFunds();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void switchToOpenNewAccountMenu() throws SQLException, EntityNotFoundException, IOException {
        Menu.OpenNewAccount.displayTitle();
        BankAccount bankAccount = new BankAccount();
        Bank bank = askToChooseFromList(bankService.findAll(), "bank");
        bankAccount.setBank(bank);
        boolean stayInMenu = true;
        while (stayInMenu) {
            Menu.OpenNewAccount.displayCardTypeSelectionRequest();
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
                    Messenger.Error.warnAboutFalseMenuEntry();
            }
        }
        bankAccount.setHolder(currentUser);
        bankAccount.setBalance(0);
        bankAccount.setIban(bankAccountService.generateIban());
        bankAccountService.save(bankAccount);
        Messenger.Info.informAboutSuccessfullyOpenedBankAccount(bankAccount);
    }

    private Transaction prepareTransaction() throws IOException, SQLException {
        Menu.DisplayAmountRequest();
        double amount = Double.parseDouble(consoleInput.nextLine());
        //TODO: validate for numeric input
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        currentBankAccount = askToChooseFromList(
                bankAccountService.findAllBelongingToUser(currentUser), "bank account");
        return transaction;
    }

    private <T> T askToChooseFromList(List<T> list, String nameOfListEntity) {
        int choice = 1;
        boolean stayInMenu = true;
        while (stayInMenu) {
            Menu.DisplayChooseFromListRequest(nameOfListEntity);
            ListPrinter.printNumeratedListToConsole(list);
            choice = Integer.parseInt(consoleInput.nextLine());
            if (choice < 1 || choice > list.size()) {
                Messenger.Error.warnAboutFalseMenuEntry();
            } else stayInMenu = false;
        }
        return list.get(choice - 1);
    }

}
