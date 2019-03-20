package me.trilis.au.java.hw4;

import com.mongodb.MongoClient;

import java.util.Scanner;

public class Main {

    private static Scanner scanner;

    private static final String usage =
            "0: exit\n" +
            "1: add new entry\n" +
            "2: find numbers by name\n" +
            "3: find names by number\n" +
            "4: remove entry\n" +
            "5: change name in entry\n" +
            "6: change number in entry\n" +
            "7: display all entries\n";

    private static final String error = "Illegal command";

    private static String ask(String message) {
        System.out.print("Enter " + message + ": ");
        return scanner.next();
    }

    /**
     * Starts command line phone book manager in the loop.
     *
     * @param args the first argument is the database's host address,
     *             the second argument is the port on which the database is running,
     *             the third argument is the name of the database.
     */
    public static void main(String[] args) {
        if (args.length < 3) {
            throw new IllegalArgumentException("Too few arguments");
        }
        int port;
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The port must be integer", e);
        }
        var phoneBook = new PhoneBook(new MongoClient(args[0], port), args[2]);
        try (var scanner = new Scanner(System.in)) {
            Main.scanner = scanner;
            System.out.print(usage);
            while (true) {
                if (!scanner.hasNextInt()) {
                    System.out.println(error);
                    scanner.next();
                    continue;
                }
                var command = scanner.nextInt();
                switch (command) {
                    case 0:
                        return;
                    case 1:
                        if (!phoneBook.addEntry(ask("name"), ask("number"))) {
                            System.out.println("This entry already exists");
                        }
                        break;
                    case 2:
                        for (var entry : phoneBook.getByName(ask("name"))) {
                            System.out.println(entry.getNumber());
                        }
                        break;
                    case 3:
                        for (var entry : phoneBook.getByNumber(ask("number"))) {
                            System.out.println(entry.getName());
                        }
                        break;
                    case 4:
                        if (!phoneBook.removeEntry(ask("name"), ask("number"))) {
                            System.out.println("The phone book does not contain this entry");
                        }
                        break;
                    case 5:
                        var oldName = ask("old name");
                        var oldNumber = ask("old number");
                        var newName = ask("new name");
                        if (!phoneBook.changeEntry(oldName, oldNumber, newName, oldNumber)) {
                            System.out.println("The phone book does not contain this entry");
                        }
                        break;
                    case 6:
                        oldName = ask("old name");
                        oldNumber = ask("old number");
                        var newNumber = ask("new number");
                        if (!phoneBook.changeEntry(oldName, oldNumber, oldName, newNumber)) {
                            System.out.println("The phone book does not contain this entry");
                        }
                        break;
                    case 7:
                        for (var entry : phoneBook.getAll()) {
                            System.out.println(entry);
                        }
                        break;
                    default:
                        System.out.println(error);
                }
            }
        }
    }
}
