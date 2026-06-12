package ui;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class ConsoleUtils {
    private static final int WIDTH = 72;
    private static final String LINE = repeat("=", WIDTH);
    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String ITALIC = "\u001B[3m";
    private static final String UNDERLINE = "\u001B[4m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String BLACK = "\u001B[30m";
    private static final String BRIGHT_RED = "\u001B[91m";
    private static final String BRIGHT_GREEN = "\u001B[92m";
    private static final String BRIGHT_YELLOW = "\u001B[93m";
    private static final String BRIGHT_CYAN = "\u001B[96m";
    private static final String BG_RED = "\u001B[41m";
    private static final String BG_GREEN = "\u001B[42m";
    private static final String BG_YELLOW = "\u001B[43m";
    private static final String BG_BLUE = "\u001B[44m";
    private static final String BG_CYAN = "\u001B[46m";

    private static boolean colorsEnabled = true;

    public static void printLine() {
        System.out.println(LINE);
    }

    public static void printSectionTitle(String title) {
        String text = safe(title).toUpperCase();
        System.out.println(sectionBar(" " + text + " "));
    }

    public static void printSuccess(String message) {
        System.out.println(statusBadge(" OK ", BG_GREEN + BLACK) + " " + message);
    }

    public static void printError(String message) {
        System.out.println(statusBadge(" ERROR ", BG_RED + BOLD) + " " + message);
    }

    public static void printWarning(String message) {
        System.out.println(statusBadge(" WARN ", BG_YELLOW + BLACK) + " " + message);
    }

    public static void printInfo(String message) {
        System.out.println(statusBadge(" INFO ", BG_BLUE + BOLD) + " " + message);
    }

    public static void printTip(String message) {
        System.out.println(statusBadge(" TIP ", BG_CYAN + BLACK) + " " + mutedText(message));
    }

    public static void pause(Scanner scanner) {
        System.out.println();
        System.out.println(infoText("Press Enter to continue..."));
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
        printCompactBanner();
        printSectionTitle(title);
    }

    public static void printAppBanner(String appName, String subtitle) {
        String sub = safe(subtitle);
        printLine();
        printAsciiTitle();
        if (!sub.isEmpty()) {
            System.out.println(infoText(center(sub, WIDTH)));
        }
        printLine();
    }

    public static void printAsciiTitle() {
        String[] lines = {
                " ____  _____ _   _ ____  _____ _   _ _____",
                "/ ___||_   _| | | |  _ \\| ____| \\ | |_   _|",
                "\\___ \\  | | | | | | | | |  _| |  \\| | | |",
                " ___) | | | | |_| | |_| | |___| |\\  | | |",
                "|____/  |_|  \\___/|____/|_____|_| \\_| |_|",
                " _____ ___ __  __ _____ _____  _    ____  _     _____",
                "|_   _|_ _|  \\/  | ____|_   _|/ \\  | __ )| |   | ____|",
                "  | |  | || |\\/| |  _|   | | / _ \\ |  _ \\| |   |  _|",
                "  | |  | || |  | | |___  | |/ ___ \\| |_) | |___| |___",
                "  |_| |___|_|  |_|_____| |_/_/   \\_\\____/|_____|_____|",
                "  ___  ____ _____ ___ __  __ ___ ____  _____ ____",
                " / _ \\|  _ \\_   _|_ _|  \\/  |_ _/ ___|| ____|  _ \\",
                "| | | | |_) || |  | || |\\/| || |\\___ \\|  _| | |_) |",
                "| |_| |  __/ | |  | || |  | || | ___) | |___|  _ <",
                " \\___/|_|    |_| |___|_|  |_|___|____/|_____|_| \\_\\"
        };

        for (String line : lines) {
            System.out.println(asciiText(center(line, WIDTH)));
        }
    }

    public static void printCompactBanner() {
        System.out.println(accentLine());
        System.out.println(asciiText(center(" ____ _____ ___", WIDTH)));
        System.out.println(asciiText(center("/ ___|_   _/ _ \\", WIDTH)));
        System.out.println(asciiText(center("\\___ \\ | || | | |", WIDTH)));
        System.out.println(asciiText(center(" ___) || || |_| |", WIDTH)));
        System.out.println(asciiText(center("|____/ |_| \\___/", WIDTH)));
        System.out.println(titleText(center("STUDENT TIMETABLE OPTIMISER", WIDTH)));
        System.out.println(mutedText(center("smart console scheduling assistant", WIDTH)));
        System.out.println(accentLine());
    }

    public static void printMenuOption(int number, String label) {
        System.out.println(menuOptionText(String.format("[%2d]", number)) + " " + label);
    }

    public static void printBackOption(String label) {
        System.out.println(menuOptionText("[ 0]") + " " + label);
    }

    public static void printPrompt(String prompt) {
        System.out.println();
        System.out.print(prompt + " > ");
    }

    public static <T> T runWithSpinner(String message, Supplier<T> task) {
        if (task == null) {
            return null;
        }

        String safeMessage = safe(message);
        AtomicBoolean running = new AtomicBoolean(true);
        Thread spinnerThread = new Thread(() -> animateSpinner(safeMessage, running));
        spinnerThread.setDaemon(true);
        spinnerThread.start();

        try {
            return task.get();
        } finally {
            running.set(false);
            try {
                spinnerThread.join(250);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            clearSpinnerLine(safeMessage);
        }
    }

    public static void runWithSpinner(String message, Runnable task) {
        runWithSpinner(message, () -> {
            if (task != null) {
                task.run();
            }
            return null;
        });
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
            printPrompt(prompt);
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
            printPrompt(prompt + " or 0 to cancel");
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
        printPrompt(prompt);
        return scanner.nextLine().trim();
    }

    public static String readOptionalTextOrCancel(Scanner scanner, String prompt) {
        printPrompt(prompt + " or 0 to cancel");
        String value = scanner.nextLine().trim();
        if (value.equals("0")) {
            return null;
        }
        return value;
    }

    public static boolean readYesNo(Scanner scanner, String prompt) {
        while (true) {
            printPrompt(prompt);
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
            printPrompt(prompt + " (Y/N or 0 to cancel)");
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
            printPrompt(prompt);
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
            printPrompt(prompt);
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
            printMenuOption(i + 1, options.get(i));
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
        return color(text, BOLD + BRIGHT_GREEN);
    }

    public static String errorText(String text) {
        return color(text, BOLD + BRIGHT_RED);
    }

    public static String warningText(String text) {
        return color(text, BOLD + BRIGHT_YELLOW);
    }

    public static String infoText(String text) {
        return color(text, BRIGHT_CYAN);
    }

    public static String titleText(String text) {
        return color(text, BOLD + BRIGHT_CYAN);
    }

    public static String asciiText(String text) {
        return color(text, BOLD + CYAN);
    }

    public static String menuOptionText(String text) {
        return color(text, BOLD + BRIGHT_YELLOW);
    }

    public static String mutedText(String text) {
        return color(text, ITALIC + BLUE);
    }

    public static String highlightText(String text) {
        return color(text, BOLD + MAGENTA);
    }

    private static String statusBadge(String text, String ansiCode) {
        if (!colorsEnabled || !supportsAnsi()) {
            return "[" + safe(text) + "]";
        }
        return color(text, ansiCode);
    }

    private static String sectionBar(String text) {
        String label = safe(text).toUpperCase();
        int side = Math.max(2, (WIDTH - label.length()) / 2);
        String bar = repeat("-", side) + label + repeat("-", Math.max(2, WIDTH - side - label.length()));
        return color(bar, BG_CYAN + BLACK);
    }

    private static String accentLine() {
        return color(repeat("=", WIDTH), BOLD + BRIGHT_CYAN);
    }

    private static String repeat(String value, int count) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            builder.append(value);
        }
        return builder.toString();
    }

    private static String center(String text, int width) {
        String value = text == null ? "" : text;
        if (value.length() >= width) {
            return value;
        }
        int padding = width - value.length();
        int left = padding / 2;
        int right = padding - left;
        return repeat(" ", left) + value + repeat(" ", right);
    }

    private static String padRight(String text, int width) {
        String value = safe(text);
        if (value.length() >= width) {
            return value.substring(0, width);
        }
        return value + repeat(" ", width - value.length());
    }

    private static void animateSpinner(String message, AtomicBoolean running) {
        char[] frames = {'-', '/', '|', '\\'};
        int index = 0;

        while (running.get()) {
            String text = "\r" + infoText(String.valueOf(frames[index])) + " " + message;
            System.out.print(text);
            System.out.flush();
            index = (index + 1) % frames.length;

            try {
                Thread.sleep(120);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private static void clearSpinnerLine(String message) {
        int width = Math.max(WIDTH, safe(message).length() + 8);
        System.out.print("\r" + repeat(" ", width) + "\r");
        System.out.flush();
    }

    private static String safe(String value) {
        return value == null ? "" : value.trim();
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


