package lt.verbus.view;

import lt.verbus.model.User;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ListPrinter {

    public static <T> void printNumeratedListToConsole(List<T> list) {
        for (int i = 1; i <= list.size(); i++) {
            System.out.println(ConsoleColor.GREEN + i + ". " + list.get(i - 1) + ConsoleColor.DEFAULT);
        }
    }

    public static <T> void printListToFile(List<T> list, String filename) {
        if (filename.length() > 4) {
            if (!filename.substring(filename.length() - 4).equals(".txt")) {
                filename += ".txt";
            }
        } else {
            filename += ".txt";
        }
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filename))) {
            for (T t : list) {
                writer.write(t.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printUsernameCheatList(List<User> users) {
        System.out.print("If in doubt, try one of these: { ");
        users.forEach(user -> System.out.print(user.getUsername() + " "));
        System.out.print("}");
        System.out.print("\n");
    }


}
