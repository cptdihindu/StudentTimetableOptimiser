package controller;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import model.ClassRecord;
import model.SearchCriteria;
import ui.ConsoleUtils;
import service.AppConfigService;
import service.ClassService;
import service.SearchService;

public class ClassController {
    private final AppConfigService appConfigService;
    private final ClassService classService;

    public ClassController(ClassService classService, AppConfigService appConfigService) {
        this.classService = classService == null ? new ClassService() : classService;
        this.appConfigService = appConfigService == null ? new AppConfigService() : appConfigService;
    }

    public void showClassDataMenu(Scanner scanner) {
        int choice;

        do {
            ConsoleUtils.clearScreen();
            printClassDataMenu();
            choice = ConsoleUtils.readIntChoice(scanner);
            handleClassDataChoice(choice, scanner);
        } while (choice != 0);
    }

    private void printClassDataMenu() {
        ConsoleUtils.printCompactBanner();
        ConsoleUtils.printSectionTitle("CLASS DATA MANAGEMENT");
        ConsoleUtils.printMenuOption(1, "Import class data from CSV");
        ConsoleUtils.printMenuOption(2, "Browse imported classes");
        ConsoleUtils.printMenuOption(3, "View class details");
        ConsoleUtils.printMenuOption(4, "Search class records");
        ConsoleUtils.printMenuOption(5, "Edit class record");
        ConsoleUtils.printMenuOption(6, "Delete class record");
        ConsoleUtils.printBackOption("Back to main menu");
        ConsoleUtils.printPrompt("Enter your choice");
    }

    private void handleClassDataChoice(int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                showImportClassDataScreen(scanner);
                break;
            case 2:
                showBrowseClassesScreen(scanner);
                break;
            case 3:
                showViewClassDetailsScreen(scanner);
                break;
            case 4:
                showSearchClassRecordsScreen(scanner);
                break;
            case 5:
                showEditClassRecordScreen(scanner);
                break;
            case 6:
                showDeleteClassRecordScreen(scanner);
                break;
            case 0:
                break;
            default:
                ConsoleUtils.printError("Invalid choice. Please enter a number from 0 to 6.");
                ConsoleUtils.pause(scanner);
                break;
        }
    }

    private void showImportClassDataScreen(Scanner scanner) {
        ConsoleUtils.clearAndPrintSection("IMPORT CLASS DATA FROM CSV");
        ConsoleUtils.printTip("Type 0 at any input prompt to cancel and go back.");
        Path configuredFolder = appConfigService.getResolvedCsvFolderPath();
        boolean hasConfiguredFolder = configuredFolder != null
                && Files.exists(configuredFolder)
                && Files.isDirectory(configuredFolder);
        ArrayList<Path> availableCsvFiles = hasConfiguredFolder
                ? getCsvFilesFromFolder(configuredFolder)
                : new ArrayList<>();

        if (hasConfiguredFolder) {
            if (availableCsvFiles.isEmpty()) {
                ConsoleUtils.printWarning("No CSV files were found in the configured CSV folder:");
                System.out.println(configuredFolder);
                System.out.println("You can still import a custom CSV file manually.");
            } else {
                printAvailableCsvFiles(availableCsvFiles);
            }
        } else {
            ConsoleUtils.printWarning("Configured CSV folder does not exist:");
            System.out.println(configuredFolder);
            System.out.println("You can update it from Main Menu -> Configuration, or import a custom CSV file manually.");
        }

        System.out.println();
        printCsvInputOptions();

        ArrayList<Path> selectedPaths = readCsvSelection(scanner, availableCsvFiles,
                hasConfiguredFolder ? configuredFolder : null);
        if (selectedPaths == null || selectedPaths.isEmpty()) {
            ConsoleUtils.printWarning("Import cancelled.");
            ConsoleUtils.pause(scanner);
            return;
        }

        ImportSummary importSummary = ConsoleUtils.runWithSpinner("Importing selected CSV file(s)...", () ->
                importSelectedCsvFiles(selectedPaths));

        if (importSummary.anySuccess) {
            ConsoleUtils.printSuccess("CSV import completed.");
            System.out.println("New records imported: " + importSummary.totalNew);
            System.out.println("Existing records updated: " + importSummary.totalUpdated);
            System.out.println("Total class records stored: " + classService.getClassRecordCount());
        } else if (importSummary.errors.size() == 1) {
            ConsoleUtils.printError(importSummary.errors.get(0));
        } else {
            ConsoleUtils.printError("No CSV files were imported.");
        }

        if (!importSummary.errors.isEmpty() && importSummary.anySuccess) {
            System.out.println();
            System.out.println("Import errors:");
            for (String error : importSummary.errors) {
                System.out.println("- " + error);
            }
        }

        if (!importSummary.warnings.isEmpty()) {
            System.out.println();
            System.out.println("Import warnings:");
            for (String warning : importSummary.warnings) {
                System.out.println("- " + warning);
            }
        }

        ConsoleUtils.pause(scanner);
    }

    private ImportSummary importSelectedCsvFiles(ArrayList<Path> selectedPaths) {
        ImportSummary summary = new ImportSummary();
        if (selectedPaths == null) {
            return summary;
        }

        for (Path selectedPath : selectedPaths) {
            boolean success = classService.importFromCsv(selectedPath.toString());
            if (success) {
                summary.anySuccess = true;
                summary.totalNew += classService.getLastImportNewCount();
                summary.totalUpdated += classService.getLastImportUpdatedCount();
                summary.warnings.addAll(classService.getLastImportWarnings());
            } else {
                String name = selectedPath.getFileName() == null
                        ? selectedPath.toString()
                        : selectedPath.getFileName().toString();
                summary.errors.add(name + ": " + classService.getLastErrorMessage());
            }
        }

        return summary;
    }

    private void showBrowseClassesScreen(Scanner scanner) {
        ConsoleUtils.clearAndPrintSection("BROWSE IMPORTED CLASSES");
        System.out.println(classService.getBrowseSummary());

        ConsoleUtils.pause(scanner);
    }

    private void showViewClassDetailsScreen(Scanner scanner) {
        ConsoleUtils.clearAndPrintSection("VIEW CLASS DETAILS");
        if (!classService.hasClassRecords()) {
            ConsoleUtils.printWarning("No class records have been imported yet.");
            ConsoleUtils.pause(scanner);
            return;
        }

        System.out.println(classService.getBrowseSummary());
        System.out.println();
        ConsoleUtils.printPrompt("Enter class record number or 0 to cancel");
        int recordNumber = ConsoleUtils.readIntChoice(scanner);
        if (recordNumber == 0) {
            ConsoleUtils.printWarning("View class details cancelled.");
            ConsoleUtils.pause(scanner);
            return;
        }
        if (recordNumber < 0 || classService.getClassRecordByIndex(recordNumber) == null) {
            ConsoleUtils.printError("Invalid class record number.");
            ConsoleUtils.pause(scanner);
            return;
        }

        System.out.println(classService.getFullDetailsByIndex(recordNumber));

        ConsoleUtils.pause(scanner);
    }

    private void showSearchClassRecordsScreen(Scanner scanner) {
        if (!classService.hasClassRecords()) {
            ConsoleUtils.clearAndPrintSection("SEARCH CLASS RECORDS");
            ConsoleUtils.printWarning("No class records have been imported yet.");
            ConsoleUtils.pause(scanner);
            return;
        }

        int choice;
        do {
            ConsoleUtils.clearAndPrintSection("SEARCH CLASS RECORDS");
            printSearchMenu();
            choice = ConsoleUtils.readIntChoice(scanner);
            handleSearchChoice(choice, scanner);
        } while (choice != 0);
    }

    private void showEditClassRecordScreen(Scanner scanner) {
        ConsoleUtils.clearAndPrintSection("EDIT CLASS RECORD");
        if (!classService.hasClassRecords()) {
            ConsoleUtils.printWarning("No class records have been imported yet.");
            ConsoleUtils.pause(scanner);
            return;
        }

        System.out.println(classService.getBrowseSummary());
        System.out.println();
        ConsoleUtils.printPrompt("Enter class record number or 0 to cancel");
        int recordNumber = ConsoleUtils.readIntChoice(scanner);
        if (recordNumber == 0) {
            ConsoleUtils.printWarning("Edit cancelled.");
            ConsoleUtils.pause(scanner);
            return;
        }
        if (recordNumber < 0 || classService.getClassRecordByIndex(recordNumber) == null) {
            ConsoleUtils.printError("Invalid class record number.");
            ConsoleUtils.pause(scanner);
            return;
        }

        System.out.println();
        System.out.println(classService.getFullDetailsByIndex(recordNumber));
        System.out.println();
        printEditFieldOptions();
        ConsoleUtils.printPrompt("Select field to edit or 0 to cancel");

        int fieldChoice = ConsoleUtils.readIntChoice(scanner);
        if (fieldChoice == 0) {
            ConsoleUtils.printWarning("Edit cancelled.");
            ConsoleUtils.pause(scanner);
            return;
        }

        String fieldName = mapFieldChoiceToName(fieldChoice);
        if (fieldName == null) {
            ConsoleUtils.printError("Invalid field selection.");
            ConsoleUtils.pause(scanner);
            return;
        }

        if (fieldName.equals("startTime") || fieldName.equals("endTime")) {
            System.out.println("Use HH:mm format, for example 09:00 or 14:30.");
        }

        String newValue;
        if (fieldName.equals("campus")) {
            newValue = readValueFromOptionsOrCustom(scanner, "Select campus:", buildCampusOptions());
        } else if (fieldName.equals("semester")) {
            newValue = readValueFromOptionsOrCustom(scanner, "Select semester:", buildSemesterOptions());
        } else if (fieldName.equals("classType")) {
            newValue = readValueFromOptionsOrCustom(scanner, "Select class type:", classService.getAvailableClassTypes());
        } else if (fieldName.equals("day")) {
            newValue = readValueFromOptionsOrCustom(scanner, "Select day:", classService.getAvailableDays());
        } else {
            newValue = ConsoleUtils.readRequiredTextOrCancel(scanner, "Enter new value");
        }
        if (newValue == null) {
            ConsoleUtils.printWarning("Edit cancelled.");
            ConsoleUtils.pause(scanner);
            return;
        }

        Boolean confirm = ConsoleUtils.readYesNoOrCancel(scanner,
                "Warning: You are about to edit this class record. Are you sure?");

        if (confirm == null || !confirm) {
            ConsoleUtils.printWarning("Edit cancelled.");
            ConsoleUtils.pause(scanner);
            return;
        }

        boolean updated = classService.editClassRecordField(recordNumber, fieldName, newValue);
        if (updated) {
            ConsoleUtils.printSuccess("Class record updated successfully.");
            System.out.println();
            System.out.println(classService.getFullDetailsByIndex(recordNumber));
        } else {
            ConsoleUtils.printError(classService.getLastErrorMessage());
        }

        ConsoleUtils.pause(scanner);
    }

    private void showDeleteClassRecordScreen(Scanner scanner) {
        ConsoleUtils.clearAndPrintSection("DELETE CLASS RECORD");
        if (!classService.hasClassRecords()) {
            ConsoleUtils.printWarning("No class records have been imported yet.");
            ConsoleUtils.pause(scanner);
            return;
        }

        System.out.println(classService.getBrowseSummary());
        System.out.println();
        ConsoleUtils.printPrompt("Enter class record number or 0 to cancel");
        int recordNumber = ConsoleUtils.readIntChoice(scanner);
        if (recordNumber == 0) {
            ConsoleUtils.printWarning("Delete cancelled.");
            ConsoleUtils.pause(scanner);
            return;
        }
        if (recordNumber < 0 || classService.getClassRecordByIndex(recordNumber) == null) {
            ConsoleUtils.printError("Invalid class record number.");
            ConsoleUtils.pause(scanner);
            return;
        }

        System.out.println();
        System.out.println(classService.getFullDetailsByIndex(recordNumber));
        System.out.println();

        Boolean confirm = ConsoleUtils.readYesNoOrCancel(scanner,
                "Warning: This class record will be deleted. Confirm delete?");

        if (confirm == null || !confirm) {
            ConsoleUtils.printWarning("Delete cancelled.");
            ConsoleUtils.pause(scanner);
            return;
        }

        boolean deleted = classService.deleteClassRecordByIndex(recordNumber);
        if (deleted) {
            ConsoleUtils.printSuccess("Class record deleted successfully.");
        } else {
            ConsoleUtils.printError("Invalid class record number.");
        }

        ConsoleUtils.pause(scanner);
    }

    private void printEditFieldOptions() {
        ConsoleUtils.printMenuOption(1, "Topic Code");
        ConsoleUtils.printMenuOption(2, "Topic Name");
        ConsoleUtils.printMenuOption(3, "Attendance Mode");
        ConsoleUtils.printMenuOption(4, "Campus");
        ConsoleUtils.printMenuOption(5, "Semester");
        ConsoleUtils.printMenuOption(6, "Availability Number");
        ConsoleUtils.printMenuOption(7, "Class Type");
        ConsoleUtils.printMenuOption(8, "Class Instance");
        ConsoleUtils.printMenuOption(9, "First Class Date");
        ConsoleUtils.printMenuOption(10, "Last Class Date");
        ConsoleUtils.printMenuOption(11, "Day");
        ConsoleUtils.printMenuOption(12, "Start Time");
        ConsoleUtils.printMenuOption(13, "End Time");
        ConsoleUtils.printMenuOption(14, "Building");
        ConsoleUtils.printMenuOption(15, "Room");
        ConsoleUtils.printBackOption("Cancel");
        System.out.println();
    }

    private String mapFieldChoiceToName(int choice) {
        switch (choice) {
            case 1:
                return "topicCode";
            case 2:
                return "topicName";
            case 3:
                return "attendanceMode";
            case 4:
                return "campus";
            case 5:
                return "semester";
            case 6:
                return "availabilityNumber";
            case 7:
                return "classType";
            case 8:
                return "classInstance";
            case 9:
                return "firstClassDate";
            case 10:
                return "lastClassDate";
            case 11:
                return "day";
            case 12:
                return "startTime";
            case 13:
                return "endTime";
            case 14:
                return "building";
            case 15:
                return "room";
            default:
                return null;
        }
    }

    private void cancelSearch(Scanner scanner) {
        ConsoleUtils.printWarning("Search cancelled.");
        ConsoleUtils.pause(scanner);
    }

    private void printSearchMenu() {
        ConsoleUtils.printMenuOption(1, "Search by topic");
        ConsoleUtils.printMenuOption(2, "Search by campus");
        ConsoleUtils.printMenuOption(3, "Search by semester");
        ConsoleUtils.printMenuOption(4, "Search by class type");
        ConsoleUtils.printMenuOption(5, "Search by day");
        ConsoleUtils.printMenuOption(6, "Advanced manual search");
        ConsoleUtils.printBackOption("Back");
        ConsoleUtils.printPrompt("Enter your choice");
    }

    private void handleSearchChoice(int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                searchByTopic(scanner);
                break;
            case 2:
                searchByCampus(scanner);
                break;
            case 3:
                searchBySemester(scanner);
                break;
            case 4:
                searchByClassType(scanner);
                break;
            case 5:
                searchByDay(scanner);
                break;
            case 6:
                runAdvancedSearch(scanner);
                break;
            case 0:
                break;
            default:
                ConsoleUtils.printError("Invalid choice. Please enter a number from 0 to 6.");
                ConsoleUtils.pause(scanner);
                break;
        }
    }

    private void searchByTopic(Scanner scanner) {
        ArrayList<String> topicOptions = classService.getAvailableTopicDisplayNames();
        if (topicOptions.isEmpty()) {
            ConsoleUtils.printWarning("No topics are available. Import class records first.");
            ConsoleUtils.pause(scanner);
            return;
        }

        String selected = ConsoleUtils.chooseFromOptionsOrCancel(scanner, "Available topics:", topicOptions);
        if (selected == null) {
            return;
        }

        SearchCriteria criteria = new SearchCriteria();
        criteria.setTopicCode(extractTopicCode(selected));
        runSearch(criteria, scanner);
    }

    private void searchByCampus(Scanner scanner) {
        ArrayList<String> campusOptions = classService.getAvailableCampuses();
        if (campusOptions.isEmpty()) {
            ConsoleUtils.printWarning("No campus values are available. Import class records first.");
            ConsoleUtils.pause(scanner);
            return;
        }

        String selected = ConsoleUtils.chooseFromOptionsOrCancel(scanner, "Available campuses:", campusOptions);
        if (selected == null) {
            return;
        }

        SearchCriteria criteria = new SearchCriteria();
        criteria.setCampus(selected);
        runSearch(criteria, scanner);
    }

    private void searchBySemester(Scanner scanner) {
        ArrayList<String> semesterOptions = classService.getAvailableSemesters();
        if (semesterOptions.isEmpty()) {
            semesterOptions = buildSemesterOptions();
        }

        String selected = ConsoleUtils.chooseFromOptionsOrCancel(scanner, "Available semesters:", semesterOptions);
        if (selected == null) {
            return;
        }

        SearchCriteria criteria = new SearchCriteria();
        criteria.setSemester(selected);
        runSearch(criteria, scanner);
    }

    private void searchByClassType(Scanner scanner) {
        ArrayList<String> classTypeOptions = classService.getAvailableClassTypes();
        if (classTypeOptions.isEmpty()) {
            ConsoleUtils.printWarning("No class type values are available. Import class records first.");
            ConsoleUtils.pause(scanner);
            return;
        }

        String selected = ConsoleUtils.chooseFromOptionsOrCancel(scanner, "Available class types:", classTypeOptions);
        if (selected == null) {
            return;
        }

        SearchCriteria criteria = new SearchCriteria();
        criteria.setClassType(selected);
        runSearch(criteria, scanner);
    }

    private void searchByDay(Scanner scanner) {
        ArrayList<String> dayOptions = classService.getAvailableDays();
        if (dayOptions.isEmpty()) {
            ConsoleUtils.printWarning("No day values are available. Import class records first.");
            ConsoleUtils.pause(scanner);
            return;
        }

        String selected = ConsoleUtils.chooseFromOptionsOrCancel(scanner, "Available days:", dayOptions);
        if (selected == null) {
            return;
        }

        SearchCriteria criteria = new SearchCriteria();
        criteria.setDay(selected);
        runSearch(criteria, scanner);
    }

    private void runAdvancedSearch(Scanner scanner) {
        ConsoleUtils.printTip("Leave a field blank to ignore it. Type 0 at any field to cancel and go back.");
        String topicCode = ConsoleUtils.readOptionalTextOrCancel(scanner, "Topic code");
        if (topicCode == null) {
            cancelSearch(scanner);
            return;
        }
        String topicName = ConsoleUtils.readOptionalTextOrCancel(scanner, "Topic name");
        if (topicName == null) {
            cancelSearch(scanner);
            return;
        }
        String campus = ConsoleUtils.readOptionalTextOrCancel(scanner, "Campus");
        if (campus == null) {
            cancelSearch(scanner);
            return;
        }
        String semester = ConsoleUtils.readOptionalTextOrCancel(scanner, "Semester");
        if (semester == null) {
            cancelSearch(scanner);
            return;
        }
        String classType = ConsoleUtils.readOptionalTextOrCancel(scanner, "Class type");
        if (classType == null) {
            cancelSearch(scanner);
            return;
        }
        String day = ConsoleUtils.readOptionalTextOrCancel(scanner, "Day");
        if (day == null) {
            cancelSearch(scanner);
            return;
        }
        String startTime = ConsoleUtils.readOptionalTextOrCancel(scanner, "Start time");
        if (startTime == null) {
            cancelSearch(scanner);
            return;
        }
        String endTime = ConsoleUtils.readOptionalTextOrCancel(scanner, "End time");
        if (endTime == null) {
            cancelSearch(scanner);
            return;
        }
        String building = ConsoleUtils.readOptionalTextOrCancel(scanner, "Building");
        if (building == null) {
            cancelSearch(scanner);
            return;
        }
        String room = ConsoleUtils.readOptionalTextOrCancel(scanner, "Room");
        if (room == null) {
            cancelSearch(scanner);
            return;
        }

        SearchCriteria criteria = new SearchCriteria();
        criteria.setTopicCode(topicCode);
        criteria.setTopicName(topicName);
        criteria.setCampus(campus);
        criteria.setSemester(semester);
        criteria.setClassType(classType);
        criteria.setDay(day);
        criteria.setStartTime(startTime);
        criteria.setEndTime(endTime);
        criteria.setBuilding(building);
        criteria.setRoom(room);

        runSearch(criteria, scanner);
    }

    private void runSearch(SearchCriteria criteria, Scanner scanner) {
        SearchService searchService = new SearchService();
        ArrayList<ClassRecord> results = ConsoleUtils.runWithSpinner("Searching class records...", () ->
                searchService.search(classService.getAllClassRecords(), criteria));

        System.out.println();
        System.out.println("Search criteria:");
        System.out.println(criteria.toString());
        System.out.println();

        if (results.isEmpty()) {
            System.out.println("No matching class records found.");
            ConsoleUtils.pause(scanner);
            return;
        }

        System.out.println("Search results:");
        for (int i = 0; i < results.size(); i++) {
            System.out.println((i + 1) + ". " + results.get(i).getSummary());
        }

        Integer selection = ConsoleUtils.readSingleNumberOrCancel(scanner,
                "View full details of a result? Enter result number or 0 to go back:",
                1,
                results.size());
        if (selection == null) {
            return;
        }

        System.out.println();
        System.out.println(results.get(selection - 1).getFullDetails());
        ConsoleUtils.pause(scanner);
    }

    private String readValueFromOptionsOrCustom(Scanner scanner, String title, ArrayList<String> options) {
        ArrayList<String> menuOptions = new ArrayList<>();
        if (options != null) {
            for (String option : options) {
                if (option != null && !option.trim().isEmpty()) {
                    menuOptions.add(option.trim());
                }
            }
        }
        menuOptions.add("Custom value");

        String selection = ConsoleUtils.chooseFromOptionsOrCancel(scanner, title, menuOptions);
        if (selection == null) {
            return null;
        }
        if (selection.equals("Custom value")) {
            return ConsoleUtils.readRequiredTextOrCancel(scanner, "Enter custom value");
        }
        return selection;
    }

    private ArrayList<String> buildSemesterOptions() {
        ArrayList<String> options = new ArrayList<>();
        options.add("S1");
        options.add("S2");
        options.add("Both");
        options.add("Semester 1");
        options.add("Semester 2");
        return options;
    }

    private ArrayList<String> buildCampusOptions() {
        ArrayList<String> options = new ArrayList<>();
        options.addAll(classService.getAvailableCampuses());
        addUniqueIgnoreCase(options, "Bedford Park");
        addUniqueIgnoreCase(options, "Tonsley");
        addUniqueIgnoreCase(options, "Flinders City Campus");
        addUniqueIgnoreCase(options, "Online");
        return options;
    }

    private void addUniqueIgnoreCase(ArrayList<String> list, String value) {
        if (list == null || value == null || value.trim().isEmpty()) {
            return;
        }
        for (String existing : list) {
            if (existing != null && existing.trim().equalsIgnoreCase(value.trim())) {
                return;
            }
        }
        list.add(value.trim());
    }

    private String extractTopicCode(String display) {
        if (display == null) {
            return "";
        }
        String trimmed = display.trim();
        if (trimmed.isEmpty()) {
            return "";
        }
        int spaceIndex = trimmed.indexOf(' ');
        if (spaceIndex <= 0) {
            return trimmed;
        }
        return trimmed.substring(0, spaceIndex).trim();
    }

    private ArrayList<Path> getCsvFilesFromFolder(Path folder) {
        ArrayList<Path> csvFiles = new ArrayList<>();
        if (folder == null || !Files.exists(folder) || !Files.isDirectory(folder)) {
            return csvFiles;
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder, "*.csv")) {
            for (Path entry : stream) {
                if (Files.isRegularFile(entry)) {
                    csvFiles.add(entry);
                }
            }
        } catch (Exception ex) {
            return csvFiles;
        }

        csvFiles.sort(Comparator.comparing(path -> path.getFileName().toString().toLowerCase()));
        return csvFiles;
    }

    private void printAvailableCsvFiles(ArrayList<Path> csvFiles) {
        System.out.println("Available CSV files in configured folder:");
        for (int i = 0; i < csvFiles.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + csvFiles.get(i).getFileName());
        }
        System.out.println(ConsoleUtils.menuOptionText("[all]") + " Import all files");
        System.out.println();
    }

    private void printCsvInputOptions() {
        System.out.println("Input options:");
        System.out.println("- Type a number from the list to import that CSV file.");
        System.out.println("- Type comma-separated numbers to import multiple CSV files, for example 1,3.");
        System.out.println("- Type \"all\" to import all available CSV files.");
        System.out.println("- Type an absolute CSV file path, for example:");
        System.out.println("  D:\\Shortcuts\\Documents\\Classes\\COMP1002.csv");
        System.out.println("- Type a relative CSV file path, for example:");
        System.out.println("  CSVs\\COMP1002.csv");
        System.out.println("- Relative paths are resolved from the folder where you launched the app.");
        System.out.println("- Type 0 to cancel.");
        System.out.println();
    }

    private ArrayList<Path> readCsvSelection(Scanner scanner, ArrayList<Path> availableCsvFiles, Path configuredFolder) {
        while (true) {
            ConsoleUtils.printPrompt("Enter CSV number(s), custom CSV path, all, or 0 to cancel");
            String input = scanner.nextLine().trim();
            if (input.equals("0")) {
                return null;
            }
            if (input.isEmpty()) {
                ConsoleUtils.printError("Please enter a value.");
                continue;
            }

            // Handle "all" command
            if (input.equalsIgnoreCase("all")) {
                if (availableCsvFiles != null && !availableCsvFiles.isEmpty()) {
                    return new ArrayList<>(availableCsvFiles);
                } else {
                    ConsoleUtils.printError("No CSV files available to import.");
                    continue;
                }
            }

            ArrayList<Integer> selectedNumbers = parseSelectionNumbers(input);
            if (selectedNumbers != null) {
                ArrayList<Path> resolvedPaths = resolveCsvSelections(selectedNumbers, availableCsvFiles);
                if (resolvedPaths != null) {
                    return resolvedPaths;
                }
                ConsoleUtils.printError("Invalid selection.");
                continue;
            }

            Path resolvedPath = resolveCsvPathInput(input, availableCsvFiles, configuredFolder);
            if (resolvedPath != null) {
                ArrayList<Path> single = new ArrayList<>();
                single.add(resolvedPath);
                return single;
            }

            ConsoleUtils.printError("Invalid selection.");
        }
    }

    private ArrayList<Path> resolveCsvSelections(ArrayList<Integer> numbers, ArrayList<Path> availableCsvFiles) {
        if (availableCsvFiles == null || availableCsvFiles.isEmpty()) {
            return null;
        }
        ArrayList<Path> selections = new ArrayList<>();
        for (Integer number : numbers) {
            if (number == null) {
                return null;
            }
            int index = number - 1;
            if (index < 0 || index >= availableCsvFiles.size()) {
                return null;
            }
            selections.add(availableCsvFiles.get(index));
        }
        return selections;
    }

    private Path resolveCsvPathInput(String input, ArrayList<Path> availableCsvFiles, Path configuredFolder) {
        if (configuredFolder != null && isFileNameOnly(input)) {
            Path configuredPath = configuredFolder.resolve(input.trim());
            if (Files.exists(configuredPath) && Files.isRegularFile(configuredPath)) {
                return configuredPath;
            }
        }

        return resolvePathFromWorkingFolder(input);
    }

    private Path resolvePathFromWorkingFolder(String pathText) {
        try {
            Path path = Paths.get(pathText.trim());
            if (path.isAbsolute()) {
                return path.normalize();
            }
            Path currentFolder = Paths.get("").toAbsolutePath();
            return currentFolder.resolve(path).normalize();
        } catch (InvalidPathException ex) {
            return null;
        }
    }

    private boolean isFileNameOnly(String pathText) {
        try {
            Path path = Paths.get(pathText.trim());
            return !path.isAbsolute() && path.getParent() == null;
        } catch (InvalidPathException ex) {
            return false;
        }
    }

    private Integer parsePositiveNumber(String input) {
        try {
            int value = Integer.parseInt(input.trim());
            if (value > 0) {
                return value;
            }
            return null;
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private ArrayList<Integer> parseSelectionNumbers(String input) {
        ArrayList<Integer> results = new ArrayList<>();
        if (input == null) {
            return null;
        }
        String trimmedInput = input.trim();
        if (trimmedInput.isEmpty()) {
            return null;
        }

        String[] parts = trimmedInput.split(",");
        for (String part : parts) {
            String trimmed = part.trim();
            if (trimmed.isEmpty()) {
                return null;
            }
            Integer value = parsePositiveNumber(trimmed);
            if (value == null) {
                return null;
            }
            results.add(value);
        }

        return results.isEmpty() ? null : results;
    }

    private static class ImportSummary {
        private int totalNew;
        private int totalUpdated;
        private boolean anySuccess;
        private final ArrayList<String> warnings = new ArrayList<>();
        private final ArrayList<String> errors = new ArrayList<>();
    }
}

