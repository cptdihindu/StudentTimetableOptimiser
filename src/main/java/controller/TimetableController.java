package controller;

import model.ClassRecord;
import model.GenerationWarning;
import model.Preference;
import model.RejectionReason;
import model.Timetable;
import model.TimetableEntry;
import model.TimetableGenerationResult;
import service.AppConfigService;
import service.ClassService;
import service.TimetableService;
import ui.ConsoleUtils;

import java.util.ArrayList;
import java.util.Scanner;

public class TimetableController {
    private final TimetableService timetableService;
    private final ClassService classService;
    private final AppConfigService appConfigService;

    public TimetableController(TimetableService timetableService,
                               ClassService classService,
                               AppConfigService appConfigService) {
        this.timetableService = timetableService == null ? new TimetableService() : timetableService;
        this.classService = classService == null ? new ClassService() : classService;
        this.appConfigService = appConfigService == null ? new AppConfigService() : appConfigService;
    }

    public void showTimetableMenu(Scanner scanner) {
        int choice;

        do {
            ConsoleUtils.clearScreen();
            printTimetableMenu();
            choice = ConsoleUtils.readIntChoice(scanner);
            handleTimetableChoice(choice, scanner);
        } while (choice != 0);
    }

    private void printTimetableMenu() {
        ConsoleUtils.printSectionTitle("TIMETABLE MANAGEMENT");
        System.out.println(ConsoleUtils.menuOptionText("[1]") + " Generate new timetable");
        System.out.println(ConsoleUtils.menuOptionText("[2]") + " Browse saved timetables");
        System.out.println(ConsoleUtils.menuOptionText("[3]") + " View timetable details");
        System.out.println(ConsoleUtils.menuOptionText("[4]") + " Edit timetable");
        System.out.println(ConsoleUtils.menuOptionText("[5]") + " Delete timetable");
        System.out.println(ConsoleUtils.menuOptionText("[0]") + " Back to main menu");
        System.out.println();
        System.out.println("Enter your choice:");
    }

    private void handleTimetableChoice(int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                showGenerateTimetableScreen(scanner);
                break;
            case 2:
                showBrowseTimetablesScreen(scanner);
                break;
            case 3:
                showViewTimetableDetailsScreen(scanner);
                break;
            case 4:
                showEditTimetableScreen(scanner);
                break;
            case 5:
                showDeleteTimetableScreen(scanner);
                break;
            case 0:
                break;
            default:
                System.out.println("Invalid choice. Please enter a number from 0 to 5.");
                ConsoleUtils.pause(scanner);
                break;
        }
    }

    public void showGenerateTimetableScreen(Scanner scanner) {
        ConsoleUtils.clearAndPrintSection("GENERATE TIMETABLE");
        if (!classService.hasClassRecords()) {
            ConsoleUtils.printWarning("No class records have been imported yet. Please import CSV data first.");
            ConsoleUtils.pause(scanner);
            return;
        }

        System.out.println("Tip: Type 0 at any input prompt to cancel and go back.");

        // Read and validate timetable name
        String name = readValidTimetableName(scanner);
        if (name == null) {
            cancelGeneration(scanner);
            return;
        }

        String semester = readSemesterSelection(scanner);
        if (semester == null) {
            cancelGeneration(scanner);
            return;
        }

        ArrayList<String> selectedTopics = readTopicSelection(scanner, semester);
        if (selectedTopics == null) {
            cancelGeneration(scanner);
            return;
        }

        ArrayList<String> selectedCampuses = readCampusSelection(scanner);
        if (selectedCampuses == null) {
            cancelGeneration(scanner);
            return;
        }

        // Validate campus filter compatibility with selected topics
        if (!selectedCampuses.isEmpty() && !selectedTopics.isEmpty()) {
            ArrayList<String> incompatibleTopics = validateCampusFilterForTopics(
                    selectedTopics, selectedCampuses, semester, classService.getAllClassRecords());
            if (!incompatibleTopics.isEmpty()) {
                showCampusFilterIncompatibilityWarning(scanner, incompatibleTopics, selectedCampuses);
                // User chose to retry - restart timetable generation
                showGenerateTimetableScreen(scanner);
                return;
            }
        }

        Boolean allowOverlap = readLectureOverlapSelection(scanner);
        if (allowOverlap == null) {
            cancelGeneration(scanner);
            return;
        }

        ArrayList<Preference> preferences = readPreferenceSelection(scanner, selectedCampuses);
        int travelMinutes = appConfigService.getTravelTimeMinutes();

        System.out.println();
        System.out.println("Timetable generation request:");
        System.out.println("- Name: " + (name.isEmpty() ? "Automatic name" : name));
        System.out.println("- Semester: " + semester);
        System.out.println("- Topics: " + displayList(selectedTopics));
        System.out.println("- Campuses: " + displayCampusFilter(selectedCampuses));
        System.out.println("- Allow lecture overlap: " + (allowOverlap ? "Yes" : "No"));
        System.out.println("- Preferences: " + displayPreferences(preferences));
        System.out.println("- Travel time minutes: " + travelMinutes);

        Boolean confirm = ConsoleUtils.readYesNoOrCancel(scanner, "Generate timetable with these settings?");
        if (confirm == null || !confirm) {
            ConsoleUtils.printWarning("Timetable generation cancelled.");
            ConsoleUtils.pause(scanner);
            return;
        }

        // Generate timetable with detailed results
        TimetableGenerationResult result = timetableService.generateTimetableWithDetails(
                name,
                semester,
                selectedTopics,
                selectedCampuses,
                allowOverlap,
                preferences,
                classService.getAllClassRecords(),
                travelMinutes
        );

        // Display results
        if (result == null) {
            ConsoleUtils.printError("Unexpected error: Unable to generate timetable.");
            ConsoleUtils.pause(scanner);
            return;
        }

        // Display status with color
        if (result.isSuccessful()) {
            ConsoleUtils.printSuccess("Timetable generated successfully.");
        } else if (result.isPartialSuccess()) {
            System.out.println();
            System.out.println(ConsoleUtils.warningText("PARTIAL SUCCESS: A timetable was created, but some required class types could not fit."));
        } else {
            ConsoleUtils.printError("Unable to generate timetable.");
            if (!result.getErrorMessage().isEmpty()) {
                System.out.println("Reason: " + result.getErrorMessage());
            }
            ConsoleUtils.pause(scanner);
            return;
        }

        System.out.println();

        // Display the timetable
        Timetable timetable = result.getTimetable();
        if (timetable != null) {
            System.out.println(timetable.getTimetableTableDisplay());
        }

        // Display missing classes
        ArrayList<String> missingClasses = result.getMissingClasses();
        if (!missingClasses.isEmpty()) {
            System.out.println();
            System.out.println("Missing Classes:");
            for (String missing : missingClasses) {
                System.out.println("- " + missing);
            }
        }

        // Display detailed generation warnings with rejection reasons
        ArrayList<GenerationWarning> warnings = result.getGenerationWarnings();
        if (!warnings.isEmpty()) {
            System.out.println();
            System.out.println("Why some classes could not be added:");
            for (GenerationWarning warning : warnings) {
                System.out.println("- " + warning.getSummary());
                ArrayList<RejectionReason> reasons = warning.getRejectionReasons();
                if (!reasons.isEmpty()) {
                    System.out.println("  Main reasons:");
                    for (RejectionReason reason : reasons) {
                        System.out.println("  - " + reason.getDisplay());
                    }
                }
            }
        }

        // Display validation summary
        System.out.println();
        System.out.println(result.getValidationSummary().getDisplay());

        // Display generation details
        System.out.println();
        System.out.println(result.getGenerationDetailsDisplay());

        // Display suggestions
        ArrayList<String> suggestions = result.getSuggestions();
        if (!suggestions.isEmpty()) {
            System.out.println();
            System.out.println("Suggestions:");
            for (String suggestion : suggestions) {
                System.out.println("- " + suggestion);
            }
        }

        ConsoleUtils.pause(scanner);
    }

    private void showBrowseTimetablesScreen(Scanner scanner) {
        ConsoleUtils.clearAndPrintSection("BROWSE SAVED TIMETABLES");
        System.out.println(timetableService.getBrowseSummary());
        ConsoleUtils.pause(scanner);
    }

    private void showViewTimetableDetailsScreen(Scanner scanner) {
        ConsoleUtils.clearAndPrintSection("VIEW TIMETABLE DETAILS");
        if (!timetableService.hasTimetables()) {
            ConsoleUtils.printWarning("No timetables have been generated yet.");
            ConsoleUtils.pause(scanner);
            return;
        }

        System.out.println(timetableService.getBrowseSummary());
        Integer selection = ConsoleUtils.readSingleNumberOrCancel(scanner,
                "Enter timetable number or 0 to cancel:",
                1,
                timetableService.getTimetableCount());
        if (selection == null) {
            ConsoleUtils.printWarning("View timetable cancelled.");
            ConsoleUtils.pause(scanner);
            return;
        }

        System.out.println();
        System.out.println(timetableService.getFullDetailsByIndex(selection));
        ConsoleUtils.pause(scanner);
    }

    private void showEditTimetableScreen(Scanner scanner) {
        ConsoleUtils.clearAndPrintSection("EDIT TIMETABLE");
        if (!timetableService.hasTimetables()) {
            ConsoleUtils.printWarning("No timetables have been generated yet.");
            ConsoleUtils.pause(scanner);
            return;
        }

        System.out.println(timetableService.getBrowseSummary());
        Integer timetableSelection = ConsoleUtils.readSingleNumberOrCancel(scanner,
                "Enter timetable number or 0 to cancel:",
                1,
                timetableService.getTimetableCount());
        if (timetableSelection == null) {
            ConsoleUtils.printWarning("Edit timetable cancelled.");
            ConsoleUtils.pause(scanner);
            return;
        }

        Timetable timetable = timetableService.getTimetableByIndex(timetableSelection);
        if (timetable == null) {
            ConsoleUtils.printError("Invalid timetable number.");
            ConsoleUtils.pause(scanner);
            return;
        }

        System.out.println();
        System.out.println(timetable.getFullDetails());

        ArrayList<TimetableEntry> entries = timetable.getEntries();
        if (entries == null || entries.isEmpty()) {
            ConsoleUtils.printWarning("This timetable has no entries to swap.");
            ConsoleUtils.pause(scanner);
            return;
        }

        System.out.println();
        System.out.println("Select an entry to swap:");
        for (int i = 0; i < entries.size(); i++) {
            TimetableEntry entry = entries.get(i);
            String summary = entry == null ? "(no class selected)" : entry.getSummary();
            System.out.println("[" + (i + 1) + "] " + summary);
        }

        Integer entrySelection = ConsoleUtils.readSingleNumberOrCancel(scanner,
                "Select entry number or 0 to cancel:",
                1,
                entries.size());
        if (entrySelection == null) {
            ConsoleUtils.printWarning("Edit timetable cancelled.");
            ConsoleUtils.pause(scanner);
            return;
        }

        ArrayList<ClassRecord> replacements = timetableService.findReplacementOptions(
                timetable,
                entrySelection,
                classService.getAllClassRecords()
        );

        if (replacements.isEmpty()) {
            ConsoleUtils.printWarning("No replacement class instances were found for the same topic and class type.");
            ConsoleUtils.pause(scanner);
            return;
        }

        System.out.println();
        System.out.println("Available replacements:");
        ArrayList<String> replacementSummaries = new ArrayList<>();
        for (ClassRecord record : replacements) {
            replacementSummaries.add(record == null ? "(unknown class)" : record.getSummary());
        }
        ConsoleUtils.printNumberedOptions(replacementSummaries);

        Integer replacementSelection = ConsoleUtils.readSingleNumberOrCancel(scanner,
                "Select replacement number or 0 to cancel:",
                1,
                replacements.size());
        if (replacementSelection == null) {
            ConsoleUtils.printWarning("Edit timetable cancelled.");
            ConsoleUtils.pause(scanner);
            return;
        }

        ClassRecord replacement = replacements.get(replacementSelection - 1);
        if (replacement == null) {
            ConsoleUtils.printError("Selected replacement is not available.");
            ConsoleUtils.pause(scanner);
            return;
        }

        System.out.println();
        System.out.println("Replacement details:");
        System.out.println(replacement.getFullDetails());

        int travelMinutes = appConfigService.getTravelTimeMinutes();
        boolean swapped = timetableService.swapTimetableEntry(
                timetableSelection,
                entrySelection,
                replacement,
                false,
                travelMinutes
        );

        if (swapped) {
            ConsoleUtils.printSuccess("Timetable entry swapped successfully.");
            printValidationWarnings(timetableService.getLastGenerationWarnings());
            System.out.println();
            System.out.println(timetableService.getFullDetailsByIndex(timetableSelection));
            ConsoleUtils.pause(scanner);
            return;
        }

        String errorMessage = timetableService.getLastErrorMessage();
        if ("Swap causes timetable validation issues.".equals(errorMessage)) {
            ConsoleUtils.printError(errorMessage);
            printValidationWarnings(timetableService.getLastGenerationWarnings());

            Boolean confirm = ConsoleUtils.readYesNoOrCancel(scanner,
                    "This swap may cause clashes or travel-time issues. Save anyway?");
            if (confirm == null || !confirm) {
                ConsoleUtils.printWarning("Swap cancelled.");
                ConsoleUtils.pause(scanner);
                return;
            }

            boolean forced = timetableService.swapTimetableEntry(
                    timetableSelection,
                    entrySelection,
                    replacement,
                    true,
                    travelMinutes
            );

            if (forced) {
                ConsoleUtils.printWarning("Swap saved with validation warnings.");
                printValidationWarnings(timetableService.getLastGenerationWarnings());
                System.out.println();
                System.out.println(timetableService.getFullDetailsByIndex(timetableSelection));
            } else {
                ConsoleUtils.printError(timetableService.getLastErrorMessage());
            }

            ConsoleUtils.pause(scanner);
            return;
        }

        ConsoleUtils.printError(errorMessage);
        ConsoleUtils.pause(scanner);
    }

    private void showDeleteTimetableScreen(Scanner scanner) {
        ConsoleUtils.clearAndPrintSection("DELETE TIMETABLE");
        if (!timetableService.hasTimetables()) {
            ConsoleUtils.printWarning("No timetables have been generated yet.");
            ConsoleUtils.pause(scanner);
            return;
        }

        System.out.println(timetableService.getBrowseSummary());
        Integer selection = ConsoleUtils.readSingleNumberOrCancel(scanner,
                "Enter timetable number or 0 to cancel:",
                1,
                timetableService.getTimetableCount());
        if (selection == null) {
            ConsoleUtils.printWarning("Delete timetable cancelled.");
            ConsoleUtils.pause(scanner);
            return;
        }

        System.out.println();
        System.out.println(timetableService.getFullDetailsByIndex(selection));
        Boolean confirm = ConsoleUtils.readYesNoOrCancel(scanner,
                "Warning: This timetable will be deleted. Confirm delete?");

        if (confirm == null || !confirm) {
            ConsoleUtils.printWarning("Delete timetable cancelled.");
            ConsoleUtils.pause(scanner);
            return;
        }

        boolean deleted = timetableService.deleteTimetableByIndex(selection);
        if (deleted) {
            ConsoleUtils.printSuccess("Timetable deleted successfully.");
        } else {
            ConsoleUtils.printError("Invalid timetable number.");
        }

        ConsoleUtils.pause(scanner);
    }

    private String displayList(ArrayList<String> list) {
        if (list == null || list.isEmpty()) {
            return "(none)";
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i));
            if (i < list.size() - 1) {
                builder.append(", ");
            }
        }

        return builder.toString();
    }

    private String displayCampusFilter(ArrayList<String> selectedCampuses) {
        if (selectedCampuses == null || selectedCampuses.isEmpty()) {
            return "No filter / All campuses allowed";
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < selectedCampuses.size(); i++) {
            builder.append(selectedCampuses.get(i));
            if (i < selectedCampuses.size() - 1) {
                builder.append(", ");
            }
        }

        return builder.toString();
    }

    private String readValidTimetableName(Scanner scanner) {
        while (true) {
            System.out.println("Timetable name (leave blank for automatic name) or 0 to cancel:");
            String input = scanner.nextLine().trim();
            
            // 0 means cancel
            if (input.equals("0")) {
                return null;
            }
            
            // Empty means automatic
            if (input.isEmpty()) {
                return "";
            }
            
            // Validate characters: allow letters, numbers, spaces, hyphens, underscores
            if (!input.matches("[a-zA-Z0-9\\s\\-_]*")) {
                ConsoleUtils.printError("Timetable name can only contain letters, numbers, spaces, hyphens, and underscores.");
                continue;
            }
            
            // Timetable name is valid
            return input;
        }
    }

    private String displayPreferences(ArrayList<Preference> preferences) {
        if (preferences == null || preferences.isEmpty()) {
            return "(none)";
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < preferences.size(); i++) {
            Preference preference = preferences.get(i);
            if (preference != null) {
                builder.append(preference.getPreferenceName());
            }
            if (i < preferences.size() - 1) {
                builder.append(", ");
            }
        }

        return builder.toString();
    }

    private void printWarnings(ArrayList<String> warnings) {
        if (warnings == null || warnings.isEmpty()) {
            return;
        }

        System.out.println();
        System.out.println("Generation warnings:");
        for (String warning : warnings) {
            System.out.println("- " + warning);
        }
    }

    private void printValidationWarnings(ArrayList<String> warnings) {
        if (warnings == null || warnings.isEmpty()) {
            return;
        }

        System.out.println();
        System.out.println("Validation warnings:");
        for (String warning : warnings) {
            System.out.println("- " + warning);
        }
    }

    private String readSemesterSelection(Scanner scanner) {
        ArrayList<String> options = new ArrayList<>();
        options.add("Semester 1 / S1");
        options.add("Semester 2 / S2");
        options.add("Both");

        System.out.println("Select semester:");
        ConsoleUtils.printNumberedOptions(options);
        Integer selection = ConsoleUtils.readSingleNumberOrCancel(scanner,
                "Select semester number or 0 to cancel:", 1, options.size());
        if (selection == null) {
            return null;
        }
        if (selection == 1) {
            return "S1";
        }
        if (selection == 2) {
            return "S2";
        }
        return "Both";
    }

    private ArrayList<String> readTopicSelection(Scanner scanner, String semester) {
        ArrayList<String> topicOptions = classService.getAvailableTopicDisplayNamesBySemester(semester);
        if (topicOptions.isEmpty()) {
            ConsoleUtils.printWarning("No topics are available for the selected semester.");
            ConsoleUtils.pause(scanner);
            return null;
        }

        ArrayList<String> options = new ArrayList<>(topicOptions);
        options.add("Select all topics");

        System.out.println("Available topics:");
        ConsoleUtils.printNumberedOptions(options);
        ArrayList<Integer> selections = ConsoleUtils.readMultipleNumbersOrCancel(
                scanner,
                "Select numbers separated by commas, for example 1,2. Enter 0 to cancel:",
                1,
                options.size()
        );

        if (selections == null) {
            return null;
        }

        ArrayList<String> selected = new ArrayList<>();
        if (selections.contains(options.size())) {
            // "Select all topics" was chosen
            selected.addAll(topicOptions);
        } else {
            // Individual topics were chosen
            for (int index : selections) {
                if (index > 0 && index < options.size()) {
                    selected.add(options.get(index - 1));
                }
            }
        }

        ArrayList<String> topicCodes = new ArrayList<>();
        for (String option : selected) {
            String code = extractTopicCode(option);
            if (!code.isEmpty()) {
                topicCodes.add(code);
            }
        }

        return topicCodes;
    }

    private ArrayList<String> readCampusSelection(Scanner scanner) {
        ArrayList<String> campusOptions = buildCampusOptions();
        System.out.println("Campus filter (optional - leave blank for no filter):");
        ConsoleUtils.printNumberedOptions(campusOptions);
        
        while (true) {
            System.out.println("Select campus numbers separated by commas, leave blank for no filter, or type 'cancel' to go back:");
            String input = scanner.nextLine().trim();
            
            // Empty input or 0 means no campus filter
            if (input.isEmpty() || input.equals("0")) {
                return new ArrayList<>();
            }
            
            // "cancel" means cancel and go back
            if (input.equalsIgnoreCase("cancel")) {
                return null;
            }
            
            // Parse the input for campus selections
            String[] parts = input.split(",");
            ArrayList<String> selected = new ArrayList<>();
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
                    ConsoleUtils.printError("Please enter valid numbers.");
                    hasError = true;
                    break;
                }
                if (value < 1 || value > campusOptions.size()) {
                    ConsoleUtils.printError("Please enter numbers between 1 and " + campusOptions.size() + ".");
                    hasError = true;
                    break;
                }
                if (!selected.contains(campusOptions.get(value - 1))) {
                    selected.add(campusOptions.get(value - 1));
                }
            }
            
            if (!hasError) {
                return selected;
            }
        }
    }

    private Boolean readLectureOverlapSelection(Scanner scanner) {
        ArrayList<String> options = new ArrayList<>();
        options.add("Yes");
        options.add("No");
        System.out.println("Lecture overlap:");
        ConsoleUtils.printNumberedOptions(options);
        Integer selection = ConsoleUtils.readSingleNumberOrCancel(scanner,
                "Select option or 0 to cancel:", 1, options.size());
        if (selection == null) {
            return null;
        }
        return selection == 1;
    }

    private ArrayList<Preference> readPreferenceSelection(Scanner scanner, ArrayList<String> selectedCampuses) {
        ArrayList<String> options = buildPreferenceOptions(selectedCampuses);
        System.out.println("Preferences:");
        ConsoleUtils.printNumberedOptions(options);
        
        while (true) {
            System.out.println("Select preference numbers in priority order, separated by commas, leave blank for no preferences, or type 'cancel' to go back:");
            String input = scanner.nextLine().trim();
            
            // Empty input or 0 means no preferences
            ArrayList<Preference> preferences = new ArrayList<>();
            if (input.isEmpty() || input.equals("0")) {
                return preferences;
            }
            
            // "cancel" means cancel and go back
            if (input.equalsIgnoreCase("cancel")) {
                return null;
            }
            
            // Parse the input for preference selections
            String[] parts = input.split(",");
            boolean hasError = false;
            
            for (int i = 0; i < parts.length; i++) {
                String trimmed = parts[i].trim();
                if (trimmed.isEmpty()) {
                    continue;
                }
                int value;
                try {
                    value = Integer.parseInt(trimmed);
                } catch (NumberFormatException ex) {
                    ConsoleUtils.printError("Please enter valid numbers.");
                    hasError = true;
                    break;
                }
                if (value < 1 || value > options.size()) {
                    ConsoleUtils.printError("Please enter numbers between 1 and " + options.size() + ".");
                    hasError = true;
                    break;
                }
                if (value >= 1 && value <= options.size()) {
                    // Use position in input order as ranking, not i
                    int ranking = preferences.size() + 1;
                    preferences.add(new Preference(options.get(value - 1), ranking));
                }
            }
            
            if (!hasError) {
                return preferences;
            }
        }
    }

    private ArrayList<String> buildCampusOptions() {
        ArrayList<String> options = new ArrayList<>();
        addUnique(options, classService.getAvailableCampuses());
        addUnique(options, "Bedford Park");
        addUnique(options, "Tonsley");
        addUnique(options, "Flinders City Campus");
        addUnique(options, "Online");
        return options;
    }

    private ArrayList<String> buildPreferenceOptions(ArrayList<String> selectedCampuses) {
        ArrayList<String> options = new ArrayList<>();
        
        // Only add campus preferences if NO hard campus filter is applied
        if (selectedCampuses.isEmpty()) {
            options.add("Bedford Park");
            options.add("Tonsley");
            options.add("Flinders City Campus");
        }
        
        options.add("All at the same campus");
        options.add("Mornings");
        options.add("Afternoons");
        options.add("Mondays");
        options.add("Tuesdays");
        options.add("Wednesdays");
        options.add("Thursdays");
        options.add("Fridays");
        options.add("Evenly spread classes across days");
        options.add("Compact classes to as few days as possible");
        return options;
    }

    private void addUnique(ArrayList<String> options, ArrayList<String> values) {
        if (values == null) {
            return;
        }
        for (String value : values) {
            addUnique(options, value);
        }
    }

    private void addUnique(ArrayList<String> options, String value) {
        if (options == null || value == null) {
            return;
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return;
        }
        for (String existing : options) {
            if (existing != null && existing.trim().equalsIgnoreCase(trimmed)) {
                return;
            }
        }
        options.add(trimmed);
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

    private void cancelGeneration(Scanner scanner) {
        ConsoleUtils.printWarning("Timetable generation cancelled.");
        ConsoleUtils.pause(scanner);
    }

    private ArrayList<String> validateCampusFilterForTopics(ArrayList<String> selectedTopics,
                                                            ArrayList<String> selectedCampuses,
                                                            String semester,
                                                            ArrayList<ClassRecord> allRecords) {
        ArrayList<String> incompatibleTopics = new ArrayList<>();

        for (String topicCode : selectedTopics) {
            // Get all class types for this topic from ALL records (no filter)
            ArrayList<String> allClassTypes = getClassTypesForTopic(topicCode, semester, allRecords);
            
            // Get all class types for this topic available in selected campuses only
            ArrayList<String> availableInFilter = getClassTypesForTopicInCampuses(
                    topicCode, semester, selectedCampuses, allRecords);

            // Check if all required class types are available
            for (String classType : allClassTypes) {
                boolean isAvailable = false;
                for (String available : availableInFilter) {
                    if (available.equalsIgnoreCase(classType)) {
                        isAvailable = true;
                        break;
                    }
                }
                if (!isAvailable) {
                    // This topic is missing at least one required class type
                    if (!incompatibleTopics.contains(topicCode)) {
                        incompatibleTopics.add(topicCode);
                    }
                    break;
                }
            }
        }

        return incompatibleTopics;
    }

    private ArrayList<String> getClassTypesForTopic(String topicCode, String semester, 
                                                     ArrayList<ClassRecord> records) {
        ArrayList<String> classTypes = new ArrayList<>();
        if (records == null) {
            return classTypes;
        }

        for (ClassRecord record : records) {
            if (record == null) {
                continue;
            }
            if (!matchesSemester(record.getSemester(), semester)) {
                continue;
            }
            if (!matchesTopicCode(record.getTopicCode(), topicCode)) {
                continue;
            }
            
            String normalizedType = normalizeClassType(record.getClassType());
            addUnique(classTypes, normalizedType);
        }
        
        return classTypes;
    }

    private ArrayList<String> getClassTypesForTopicInCampuses(String topicCode, String semester,
                                                              ArrayList<String> campuses,
                                                              ArrayList<ClassRecord> records) {
        ArrayList<String> classTypes = new ArrayList<>();
        if (records == null || campuses == null) {
            return classTypes;
        }

        for (ClassRecord record : records) {
            if (record == null) {
                continue;
            }
            if (!matchesSemester(record.getSemester(), semester)) {
                continue;
            }
            if (!matchesTopicCode(record.getTopicCode(), topicCode)) {
                continue;
            }
            
            // Check if campus matches
            boolean campusMatch = false;
            for (String campus : campuses) {
                if (matchesIgnoreCase(record.getCampus(), campus)) {
                    campusMatch = true;
                    break;
                }
            }
            
            if (campusMatch) {
                String normalizedType = normalizeClassType(record.getClassType());
                addUnique(classTypes, normalizedType);
            }
        }
        
        return classTypes;
    }

    private void showCampusFilterIncompatibilityWarning(Scanner scanner, 
                                                        ArrayList<String> incompatibleTopics,
                                                        ArrayList<String> selectedCampuses) {
        ConsoleUtils.clearAndPrintSection("CAMPUS FILTER INCOMPATIBILITY WARNING");
        System.out.println("The following topics cannot be completed with your campus filter:");
        for (String topic : incompatibleTopics) {
            System.out.println("- " + topic);
        }
        System.out.println();
        System.out.println("Reason: These topics require class types (Lecture, Tutorial, Practical, etc.)");
        System.out.println("that are only available at campuses NOT in your filter:");
        for (String campus : selectedCampuses) {
            System.out.println("- Selected: " + campus);
        }
        System.out.println();
        System.out.println("Options:");
        System.out.println("1. Remove the campus filter (select all campuses)");
        System.out.println("2. Deselect these incompatible topics");
        System.out.println("3. Modify your campus filter to include other campuses");
        System.out.println();
        System.out.println("The timetable generation will restart so you can adjust your selections.");
        ConsoleUtils.pause(scanner);
    }

    private boolean matchesSemester(String recordSemester, String selectedSemester) {
        if (selectedSemester == null || recordSemester == null) {
            return false;
        }
        if (selectedSemester.equalsIgnoreCase("Both")) {
            return true;
        }
        return recordSemester.equalsIgnoreCase(selectedSemester);
    }

    private boolean matchesTopicCode(String recordCode, String selectedCode) {
        if (recordCode == null || selectedCode == null) {
            return false;
        }
        return recordCode.equalsIgnoreCase(selectedCode);
    }

    private boolean matchesIgnoreCase(String value1, String value2) {
        if (value1 == null || value2 == null) {
            return false;
        }
        return value1.equalsIgnoreCase(value2);
    }

    private String normalizeClassType(String classType) {
        String trimmed = classType == null ? "" : classType.trim();
        // Remove instance numbers like "-1", "-2", etc.
        return trimmed.replaceAll("-\\d+$", "");
    }
}

