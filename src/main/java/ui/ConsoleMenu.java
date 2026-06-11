package ui;

import controller.ClassController;
import controller.TimetableController;
import io.TimetableExporter;
import model.Timetable;
import service.AppConfigService;
import service.ClassService;
import service.TimetableService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class ConsoleMenu {
    private final AppConfigService appConfigService;
    private final ClassService classService;
    private final TimetableService timetableService;
    private final TimetableExporter timetableExporter;
    private final ClassController classController;
    private final TimetableController timetableController;

    public ConsoleMenu() {
        this.appConfigService = new AppConfigService();
        ConsoleUtils.setColorsEnabled(appConfigService.isColorsEnabled());
        this.classService = new ClassService();
        this.timetableService = new TimetableService();
        this.timetableExporter = new TimetableExporter();
        this.classController = new ClassController(classService, appConfigService);
        this.timetableController = new TimetableController(timetableService, classService, appConfigService);
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        ConsoleUtils.clearScreen();
        printAppHeader();
        ConsoleUtils.pause(scanner);

        do {
            ConsoleUtils.clearScreen();
            printMainHeader();
            printMainMenu();
            choice = ConsoleUtils.readIntChoice(scanner);
            handleMainMenuChoice(choice, scanner);
        } while (choice != 0);

        scanner.close();
    }

    private void printAppHeader() {
        ConsoleUtils.printLine();
        ConsoleUtils.printAsciiTitle();
        ConsoleUtils.printLine();
        System.out.println(ConsoleUtils.titleText("STUDENT TIMETABLE OPTIMISER"));
        System.out.println(ConsoleUtils.mutedText("Build complete timetables from imported class CSVs"));
        System.out.println();
        ConsoleUtils.printInfo("Start by importing CSV files, then generate and export a timetable.");
    }

    private void printMainHeader() {
        ConsoleUtils.printAppBanner("Student Timetable Optimiser",
                "Main Menu");
    }

    private void printMainMenu() {
        ConsoleUtils.printMenuOption(1, "Class Data Management");
        ConsoleUtils.printMenuOption(2, "Timetable Generation");
        ConsoleUtils.printMenuOption(3, "Timetable Management");
        ConsoleUtils.printMenuOption(4, "Export Timetable");
        ConsoleUtils.printMenuOption(5, "Configuration");
        ConsoleUtils.printMenuOption(6, "Help / About");
        ConsoleUtils.printBackOption("Exit");
        ConsoleUtils.printPrompt("Enter your choice");
    }

    private void handleMainMenuChoice(int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                classController.showClassDataMenu(scanner);
                break;
            case 2:
                timetableController.showGenerateTimetableScreen(scanner);
                break;
            case 3:
                timetableController.showTimetableMenu(scanner);
                break;
            case 4:
                showExportTimetableScreen(scanner);
                break;
            case 5:
                showConfigurationMenu(scanner);
                break;
            case 6:
                showHelpAboutScreen(scanner);
                break;
            case 0:
                System.out.println("Thank you for using Student Timetable Optimiser. Goodbye!");
                break;
            default:
                ConsoleUtils.printError("Invalid choice. Please enter a number from 0 to 6.");
                ConsoleUtils.pause(scanner);
                break;
        }
    }

    private void showConfigurationMenu(Scanner scanner) {
        int choice;

        do {
            ConsoleUtils.clearScreen();
            printConfigurationMenu();
            choice = ConsoleUtils.readIntChoice(scanner);
            handleConfigurationChoice(choice, scanner);
        } while (choice != 0);
    }

    private void printConfigurationMenu() {
        ConsoleUtils.printSectionTitle("APPLICATION CONFIGURATION");
        ConsoleUtils.printMenuOption(1, "View current configuration");
        ConsoleUtils.printMenuOption(2, "Set CSV folder path");
        ConsoleUtils.printMenuOption(3, "Set travel time minutes");
        ConsoleUtils.printMenuOption(4, "Toggle colour output");
        ConsoleUtils.printBackOption("Back to main menu");
        ConsoleUtils.printPrompt("Enter your choice");
    }

    private void handleConfigurationChoice(int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                showViewConfigurationScreen(scanner);
                break;
            case 2:
                showSetCsvFolderPathScreen(scanner);
                break;
            case 3:
                showSetTravelTimeMinutesScreen(scanner);
                break;
            case 4:
                showToggleColorsScreen(scanner);
                break;
            case 0:
                break;
            default:
                ConsoleUtils.printError("Invalid choice. Please enter a number from 0 to 4.");
                ConsoleUtils.pause(scanner);
                break;
        }
    }

    private void showViewConfigurationScreen(Scanner scanner) {
        ConsoleUtils.clearAndPrintSection("VIEW CURRENT CONFIGURATION");
        System.out.println(appConfigService.getConfigSummary());
        System.out.println();
        printPathInputHelp();
        ConsoleUtils.pause(scanner);
    }

    private void showSetCsvFolderPathScreen(Scanner scanner) {
        ConsoleUtils.clearAndPrintSection("SET CSV FOLDER PATH");
        ConsoleUtils.printTip("You can enter an absolute or relative folder path.");
        System.out.println();
        System.out.println(ConsoleUtils.highlightText("Absolute path example:"));
        System.out.println(ConsoleUtils.mutedText("D:\\Shortcuts\\Documents\\#MyProjects\\#IsuruJavaApp\\StudentTimetableOptimiser\\CSVs"));
        System.out.println();
        System.out.println(ConsoleUtils.highlightText("Relative path examples:"));
        System.out.println(ConsoleUtils.mutedText("CSVs"));
        System.out.println(ConsoleUtils.mutedText("..\\SharedCSVs"));
        System.out.println();
        System.out.println("Relative paths are resolved from the current working directory where the app is launched.");
        System.out.println();

        String currentPath = appConfigService.getCsvFolderPath();
        Path resolvedPath = appConfigService.getResolvedCsvFolderPath();
        System.out.println("Current CSV folder path: " + currentPath);
        System.out.println("Resolved CSV folder path: " + resolvedPath);
        System.out.println();

        String input = ConsoleUtils.readRequiredTextOrCancel(scanner, "Enter new CSV folder path");
        if (input == null) {
            ConsoleUtils.printWarning("Configuration update cancelled.");
            ConsoleUtils.pause(scanner);
            return;
        }

        Path resolvedInput = appConfigService.resolvePath(input);
        System.out.println();
        System.out.println("Resolved path:");
        System.out.println(resolvedInput);

        if (resolvedInput == null || !Files.exists(resolvedInput) || !Files.isDirectory(resolvedInput)) {
            ConsoleUtils.printError("The folder does not exist or is not a directory. Configuration was not saved.");
            ConsoleUtils.pause(scanner);
            return;
        }

        Boolean confirm = ConsoleUtils.readYesNoOrCancel(scanner, "Save this CSV folder path?");
        if (confirm == null || !confirm) {
            ConsoleUtils.printWarning("Configuration update cancelled.");
            ConsoleUtils.pause(scanner);
            return;
        }

        boolean saved = appConfigService.setCsvFolderPath(input);
        if (saved) {
            ConsoleUtils.printSuccess("CSV folder path saved successfully.");
        } else {
            ConsoleUtils.printError("The folder does not exist or is not a directory. Configuration was not saved.");
        }

        ConsoleUtils.pause(scanner);
    }

    private void showSetTravelTimeMinutesScreen(Scanner scanner) {
        ConsoleUtils.clearAndPrintSection("SET TRAVEL TIME MINUTES");
        System.out.println("Current travel time minutes: " + appConfigService.getTravelTimeMinutes());
        System.out.println();

        while (true) {
            ConsoleUtils.printPrompt("Enter travel time in minutes or 0 to cancel");
            String input = scanner.nextLine().trim();
            if (input.equals("0")) {
                ConsoleUtils.printWarning("Configuration update cancelled.");
                ConsoleUtils.pause(scanner);
                return;
            }

            int minutes;
            try {
                minutes = Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                ConsoleUtils.printError("Please enter a valid positive number.");
                continue;
            }

            if (minutes <= 0) {
                ConsoleUtils.printError("Travel time must be a positive number.");
                continue;
            }

            boolean saved = appConfigService.setTravelTimeMinutes(minutes);
            if (saved) {
                ConsoleUtils.printSuccess("Travel time minutes saved successfully.");
            } else {
                ConsoleUtils.printError("Travel time minutes must be a positive number.");
            }

            ConsoleUtils.pause(scanner);
            return;
        }
    }

    private void showToggleColorsScreen(Scanner scanner) {
        ConsoleUtils.clearAndPrintSection("TOGGLE COLOUR OUTPUT");
        boolean isEnabled = appConfigService.isColorsEnabled();
        System.out.println("Current colour output: " + (isEnabled ? "Enabled" : "Disabled"));
        System.out.println();
        ConsoleUtils.printMenuOption(1, "Enable colours");
        ConsoleUtils.printMenuOption(2, "Disable colours");
        ConsoleUtils.printBackOption("Cancel");

        Integer selection = ConsoleUtils.readSingleNumberOrCancel(scanner,
                "Select colour option:", 1, 2);
        if (selection == null) {
            ConsoleUtils.printWarning("Configuration update cancelled.");
            ConsoleUtils.pause(scanner);
            return;
        }

        boolean enable = selection == 1;
        appConfigService.setColorsEnabled(enable);
        ConsoleUtils.setColorsEnabled(enable);
        ConsoleUtils.printSuccess("Colour output setting saved successfully.");
        ConsoleUtils.pause(scanner);
    }

    private void showExportTimetableScreen(Scanner scanner) {
        ConsoleUtils.clearAndPrintSection("EXPORT TIMETABLE");
        ConsoleUtils.printTip("Type 0 at any input prompt to cancel and go back.");
        System.out.println();

        if (!timetableService.hasTimetables()) {
            ConsoleUtils.printWarning("No timetables have been generated yet.");
            ConsoleUtils.pause(scanner);
            return;
        }

        System.out.println(timetableService.getBrowseSummary());
        System.out.println();

        String timetableId = ConsoleUtils.readRequiredTextOrCancel(scanner,
                "Enter timetable name or number");
        if (timetableId == null) {
            ConsoleUtils.printWarning("Export cancelled.");
            ConsoleUtils.pause(scanner);
            return;
        }

        Timetable selectedTimetable = findTimetableForExport(timetableId);
        if (selectedTimetable == null) {
            ConsoleUtils.printError("No timetable was found with that name or number.");
            ConsoleUtils.pause(scanner);
            return;
        }

        Path exportPath = timetableExporter.getDefaultExportPath(selectedTimetable);
        System.out.println();
        System.out.println(ConsoleUtils.highlightText("Export file:"));
        System.out.println(ConsoleUtils.mutedText(exportPath.toString()));

        Boolean confirm = ConsoleUtils.readYesNoOrCancel(scanner, "Export this timetable to the exports folder?");
        if (confirm == null) {
            ConsoleUtils.printWarning("Export cancelled.");
        } else if (confirm) {
            boolean exported = timetableExporter.exportTimetableToDefaultCsv(selectedTimetable);
            if (exported) {
                ConsoleUtils.printSuccess("Timetable exported successfully to: " + exportPath);
            } else {
                ConsoleUtils.printError(timetableExporter.getLastErrorMessage());
            }
        } else {
            ConsoleUtils.printWarning("Export cancelled.");
        }

        ConsoleUtils.pause(scanner);
    }

    private Timetable findTimetableForExport(String timetableId) {
        if (timetableId == null || timetableId.trim().isEmpty()) {
            return null;
        }

        String trimmed = timetableId.trim();
        try {
            int displayIndex = Integer.parseInt(trimmed);
            Timetable timetable = timetableService.getTimetableByIndex(displayIndex);
            if (timetable != null) {
                return timetable;
            }
        } catch (NumberFormatException ex) {
            // Treat non-numeric input as a timetable name.
        }

        return timetableService.getTimetableByName(trimmed);
    }

    private void showHelpAboutScreen(Scanner scanner) {
        ConsoleUtils.clearAndPrintSection("HELP / ABOUT");
        System.out.println("Student Timetable Optimiser is a Java console application.");
        System.out.println("It helps students import university class data from CSV files and generate convenient timetables.");
        System.out.println("The app supports class data management, timetable generation,");
        System.out.println("clash checking, travel-time validation, preference scoring, and CSV export.");
        System.out.println();
        System.out.println(ConsoleUtils.highlightText("Rules:"));
        System.out.println("- This is a console application only.");
        System.out.println("- No graphical interface is used.");
        System.out.println("- The CSV folder path can be changed from Main Menu -> Configuration.");
        System.out.println("- The app creates app-config.properties automatically if it is missing.");
        System.out.println("- CSV folder paths can be absolute or relative.");

        ConsoleUtils.pause(scanner);
    }

    private void printPathInputHelp() {
        System.out.println("Path input help:");
        System.out.println("- Absolute path example:");
        System.out.println("  " + ConsoleUtils.mutedText("D:\\Shortcuts\\Documents\\CSVs"));
        System.out.println("- Relative path example:");
        System.out.println("  " + ConsoleUtils.mutedText("CSVs"));
        System.out.println("- Another relative path example:");
        System.out.println("  " + ConsoleUtils.mutedText("..\\SharedCSVs"));
        System.out.println("Relative paths are resolved from the folder where you run the app.");
    }
}

