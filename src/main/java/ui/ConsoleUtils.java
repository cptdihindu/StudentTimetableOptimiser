package ui;

import java.util.ArrayList;
import java.util.Scanner;

public class ConsoleUtils {
    private static final String LINE = "--------------------------------------------------";
    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String WHITE = "\u001B[37m";

    private static boolean colorsEnabled = true;

    public static void printLine() {
        System.out.println(LINE);
    }

    public static void printSectionTitle(String title) {
        printLine();
        System.out.println(titleText(title));
        printLine();
    }

    public static void printSuccess(String message) {
        System.out.println(successText("SUCCESS: " + message));
    }

    public static void printError(String message) {
        System.out.println(errorText("ERROR: " + message));
    }

    public static void printWarning(String message) {
        System.out.println(warningText("WARNING: " + message));
    }

    public static void pause(Scanner scanner) {
        System.out.println();
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    public static void clearScreen() {
        try {
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls")
                        .inheritIO()
                        .start()
                        .waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println();
            System.out.println();
            System.out.println();
        }
    }

    public static void clearAndPrintSection(String title) {
        clearScreen();
        printSectionTitle(title);
    }

    public static int readIntChoice(Scanner scanner) {
        if (scanner.hasNextInt()) {
            int value = scanner.nextInt();
            scanner.nextLine();
            return value;
        }

        scanner.nextLine();
        return -1;
    }

    public static String readRequiredText(Scanner scanner, String prompt) {
        String value;
        do {
            System.out.println(prompt);
            value = scanner.nextLine().trim();
            if (value.isEmpty()) {
                printError("This field is required.");
            }
        } while (value.isEmpty());

        return value;
    }

    public static String readRequiredTextOrCancel(Scanner scanner, String prompt) {
        String value;
        do {
            System.out.println(prompt + " or 0 to cancel:");
            value = scanner.nextLine().trim();
            if (value.equals("0")) {
                return null;
            }
            if (value.isEmpty()) {
                printError("This field is required.");
            }
        } while (value.isEmpty());

        return value;
    }

    public static String readOptionalText(Scanner scanner, String prompt) {
        System.out.println(prompt);
        return scanner.nextLine().trim();
    }

    public static String readOptionalTextOrCancel(Scanner scanner, String prompt) {
        System.out.println(prompt + " or 0 to cancel:");
        String value = scanner.nextLine().trim();
        if (value.equals("0")) {
            return null;
        }
        return value;
    }

    public static boolean readYesNo(Scanner scanner, String prompt) {
        while (true) {
            System.out.println(prompt);
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("Y")) {
                return true;
            }
            if (input.equalsIgnoreCase("N")) {
                return false;
            }
            printError("Please enter Y or N.");
        }
    }

    public static Boolean readYesNoOrCancel(Scanner scanner, String prompt) {
        while (true) {
            System.out.println(prompt + " (Y/N or 0 to cancel):");
            String input = scanner.nextLine().trim();
            if (input.equals("0")) {
                return null;
            }
            if (input.equalsIgnoreCase("Y")) {
                return true;
            }
            if (input.equalsIgnoreCase("N")) {
                return false;
            }
            printError("Please enter Y, N, or 0 to cancel.");
        }
    }

    public static Integer readSingleNumberOrCancel(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.println(prompt);
            String input = scanner.nextLine().trim();
            if (input.equals("0")) {
                return null;
            }
            int value;
            try {
                value = Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                printError("Please enter a valid number.");
                continue;
            }
            if (value < min || value > max) {
                printError("Please enter a number from " + min + " to " + max + ".");
                continue;
            }
            return value;
        }
    }

    public static ArrayList<Integer> readMultipleNumbersOrCancel(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.println(prompt);
            String input = scanner.nextLine().trim();
            if (input.equals("0")) {
                return null;
            }
            if (input.isEmpty()) {
                printError("Please enter at least one number.");
                continue;
            }

            String[] parts = input.split(",");
            ArrayList<Integer> selections = new ArrayList<>();
            boolean hasError = false;

            for (String part : parts) {
                String trimmed = part.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }
                int value;
                try {
                    value = Integer.parseInt(trimmed);
                } catch (NumberFormatException ex) {
                    hasError = true;
                    break;
                }
                if (value < min || value > max) {
                    hasError = true;
                    break;
                }
                if (!selections.contains(value)) {
                    selections.add(value);
                }
            }

            if (hasError || selections.isEmpty()) {
                printError("Please enter numbers between " + min + " and " + max + ", separated by commas.");
                continue;
            }

            return selections;
        }
    }

    public static void printNumberedOptions(ArrayList<String> options) {
        if (options == null || options.isEmpty()) {
            return;
        }
        for (int i = 0; i < options.size(); i++) {
            System.out.println(menuOptionText("[" + (i + 1) + "]") + " " + options.get(i));
        }
    }

    public static String chooseFromOptionsOrCancel(Scanner scanner, String title, ArrayList<String> options) {
        if (options == null || options.isEmpty()) {
            return null;
        }
        if (title != null && !title.trim().isEmpty()) {
            System.out.println(title);
        }
        printNumberedOptions(options);
        Integer selection = readSingleNumberOrCancel(scanner, "Select a number or 0 to cancel:", 1, options.size());
        if (selection == null) {
            return null;
        }
        return options.get(selection - 1);
    }

    public static ArrayList<String> chooseMultipleFromOptionsOrCancel(Scanner scanner,
                                                                      String title,
                                                                      ArrayList<String> options) {
        if (options == null || options.isEmpty()) {
            return null;
        }
        if (title != null && !title.trim().isEmpty()) {
            System.out.println(title);
        }
        printNumberedOptions(options);
        ArrayList<Integer> selections = readMultipleNumbersOrCancel(
                scanner,
                "Select numbers separated by commas, for example 1,2. Enter 0 to cancel:",
                1,
                options.size()
        );
        if (selections == null) {
            return null;
        }

        ArrayList<String> chosen = new ArrayList<>();
        for (Integer selection : selections) {
            chosen.add(options.get(selection - 1));
        }

        return chosen;
    }

    public static void setColorsEnabled(boolean enabled) {
        colorsEnabled = enabled;
    }

    public static boolean isColorsEnabled() {
        return colorsEnabled;
    }

    public static String successText(String text) {
        return color(text, GREEN);
    }

    public static String errorText(String text) {
        return color(text, RED);
    }

    public static String warningText(String text) {
        return color(text, YELLOW);
    }

    public static String infoText(String text) {
        return color(text, CYAN);
    }

    public static String titleText(String text) {
        return color(text, BOLD + CYAN);
    }

    public static String menuOptionText(String text) {
        return color(text, YELLOW);
    }

    private static String color(String text, String ansiCode) {
        if (!colorsEnabled || !supportsAnsi()) {
            return text;
        }
        return ansiCode + text + RESET;
    }

    private static boolean supportsAnsi() {
        String wtSession = System.getenv("WT_SESSION");
        if (wtSession != null && !wtSession.trim().isEmpty()) {
            return true;
        }

        String term = System.getenv("TERM");
        if (term != null && !term.trim().isEmpty()) {
            return true;
        }

        String os = System.getProperty("os.name").toLowerCase();
        if (!os.contains("win") && System.console() != null) {
            return true;
        }

        return false;
    }
}


