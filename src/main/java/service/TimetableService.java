package service;

import model.ClassRecord;
import model.GenerationWarning;
import model.Preference;
import model.RejectionReason;
import model.Timetable;
import model.TimetableEntry;
import model.TimetableGenerationResult;
import model.ValidationSummary;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TimetableService {
    private ArrayList<Timetable> timetables;
    private ValidationService validationService;
    private int autoNameCounter;
    private String lastErrorMessage;
    private ArrayList<String> lastGenerationWarnings;

    public TimetableService() {
        this.timetables = new ArrayList<>();
        this.validationService = new ValidationService();
        this.autoNameCounter = 1;
        this.lastGenerationWarnings = new ArrayList<>();
        this.lastErrorMessage = "";
    }

    public TimetableGenerationResult generateTimetableWithDetails(String timetableName,
                                                                   String semester,
                                                                   ArrayList<String> selectedTopicCodes,
                                                                   ArrayList<String> selectedCampuses,
                                                                   boolean allowLectureOverlap,
                                                                   ArrayList<Preference> preferences,
                                                                   ArrayList<ClassRecord> availableRecords,
                                                                   int requiredTravelMinutes) {
        TimetableGenerationResult result = new TimetableGenerationResult();

        // Validate input
        if (isBlank(semester)) {
            result.setErrorMessage("Semester is required.");
            result.setStatus(TimetableGenerationResult.STATUS_FAILED);
            return result;
        }
        if (selectedTopicCodes == null || selectedTopicCodes.isEmpty()) {
            result.setErrorMessage("At least one topic code is required.");
            result.setStatus(TimetableGenerationResult.STATUS_FAILED);
            return result;
        }
        if (selectedCampuses == null) {
            result.setErrorMessage("Campus selection is required.");
            result.setStatus(TimetableGenerationResult.STATUS_FAILED);
            return result;
        }
        if (availableRecords == null || availableRecords.isEmpty()) {
            result.setErrorMessage("No class records are available for timetable generation.");
            result.setStatus(TimetableGenerationResult.STATUS_FAILED);
            return result;
        }

        // Prepare parameters
        String name = isBlank(timetableName) ? generateAutomaticTimetableName() : timetableName.trim();
        if (!isBlank(timetableName) && !isTimetableNameUnique(name)) {
            result.setErrorMessage("A timetable with that name already exists.");
            result.setStatus(TimetableGenerationResult.STATUS_FAILED);
            return result;
        }

        ArrayList<String> topicCodes = normaliseStringList(selectedTopicCodes);
        ArrayList<String> campuses = normaliseStringList(selectedCampuses);
        String selectedSemester = semester.trim();

        // Filter records
        ArrayList<ClassRecord> filteredRecords = filterRecords(availableRecords, selectedSemester, topicCodes, campuses);
        if (filteredRecords.isEmpty()) {
            result.setErrorMessage("No class records matched the selected filters.");
            result.setStatus(TimetableGenerationResult.STATUS_FAILED);
            return result;
        }

        result.setCandidateRecordsChecked(filteredRecords.size());

        // Group records by topic and type
        Map<String, Map<String, ArrayList<ClassRecord>>> grouped = groupByTopicAndType(filteredRecords);

        // Try to generate timetable with backtracking
        ArrayList<ClassRecord> selectedRecords = new ArrayList<>();
        ArrayList<GenerationWarning> warnings = new ArrayList<>();
        ArrayList<String> missingClasses = new ArrayList<>();

        int attemptedSelections = 0;
        int rejectedCount = 0;
        int rejectedDueToClash = 0;
        int rejectedDueToTravelTime = 0;
        int rejectedDueToCampusRule = 0;
        int rejectedDueToSemesterOrFilter = 0;

        for (String topicCode : topicCodes) {
            String topicKey = safe(topicCode).toLowerCase();
            if (topicKey.isEmpty()) {
                continue;
            }

            Map<String, ArrayList<ClassRecord>> byType = grouped.get(topicKey);
            if (byType == null || byType.isEmpty()) {
                missingClasses.add(topicCode + " (no class types found)");
                continue;
            }

            // Try to find a valid combination of all class types for this topic
            BacktrackingResult backtrackResult = tryFindBestCombinationForTopic(
                    topicCode,
                    byType,
                    selectedRecords,
                    allowLectureOverlap,
                    requiredTravelMinutes
            );

            // Process results from backtracking attempt
            selectedRecords.addAll(backtrackResult.selectedRecords);
            attemptedSelections += backtrackResult.attemptedSelections;
            rejectedCount += backtrackResult.rejectedCount;
            rejectedDueToClash += backtrackResult.rejectedDueToClash;
            rejectedDueToTravelTime += backtrackResult.rejectedDueToTravelTime;
            rejectedDueToCampusRule += backtrackResult.rejectedDueToCampusRule;
            rejectedDueToSemesterOrFilter += backtrackResult.rejectedDueToSemesterOrFilter;
            
            if (!backtrackResult.warnings.isEmpty()) {
                warnings.addAll(backtrackResult.warnings);
            }
            if (!backtrackResult.missingClasses.isEmpty()) {
                missingClasses.addAll(backtrackResult.missingClasses);
            }
        }

        result.setSelectedRecordsCount(selectedRecords.size());
        result.setGenerationWarnings(warnings);
        result.setMissingClasses(missingClasses);
        result.setRejectedDueToClash(rejectedDueToClash);
        result.setRejectedDueToTravelTime(rejectedDueToTravelTime);
        result.setRejectedDueToCampusRule(rejectedDueToCampusRule);
        result.setRejectedDueToSemesterOrFilter(rejectedDueToSemesterOrFilter);

        // Determine status
        if (selectedRecords.isEmpty()) {
            result.setStatus(TimetableGenerationResult.STATUS_FAILED);
            result.setErrorMessage("No valid timetable could be generated.");
            return result;
        }

        if (!missingClasses.isEmpty()) {
            result.setStatus(TimetableGenerationResult.STATUS_PARTIAL_SUCCESS);
        } else {
            result.setStatus(TimetableGenerationResult.STATUS_SUCCESS);
        }

        // Create timetable
        Timetable timetable = new Timetable();
        timetable.setTimetableName(name);
        timetable.setSemester(normalizeSemesterForStorage(selectedSemester));
        timetable.setAllowLectureOverlap(allowLectureOverlap);
        timetable.setPreferences(preferences);

        for (ClassRecord record : selectedRecords) {
            timetable.addEntry(new TimetableEntry(record));
        }

        // Sort timetable entries by day and time
        sortTimetableEntries(timetable);

        // Build validation summary from actual timetable and missing classes
        ValidationSummary summary = new ValidationSummary();
        summary.setMissingRequiredClassesCount(missingClasses.size());
        if (missingClasses.isEmpty() && selectedRecords.size() > 0) {
            summary.setStatus("Complete timetable");
        } else if (!missingClasses.isEmpty() && selectedRecords.size() > 0) {
            summary.setStatus("Incomplete timetable");
        } else {
            summary.setStatus("Failed timetable");
        }
        result.setValidationSummary(summary);

        // Add suggestions based on status
        if (result.isPartialSuccess() || result.isFailed()) {
            addSuggestionsForPartialOrFailed(result, selectedRecords, selectedSemester, campuses, allowLectureOverlap);
        }

        timetables.add(timetable);
        result.setTimetable(timetable);
        return result;
    }

    private void sortTimetableEntries(Timetable timetable) {
        if (timetable == null || timetable.getEntries() == null) {
            return;
        }

        timetable.getEntries().sort(Comparator
                .comparingInt((TimetableEntry e) -> getDayOrder(e.getClassRecord()))
                .thenComparing((TimetableEntry e) -> e.getClassRecord().getStartTime() == null
                        ? LocalTime.MAX : e.getClassRecord().getStartTime()));
    }

    private int getDayOrder(ClassRecord record) {
        if (record == null) {
            return 100;
        }
        String baseDay = safe(record.getBaseDay()).toLowerCase();
        switch (baseDay) {
            case "monday":
                return 1;
            case "tuesday":
                return 2;
            case "wednesday":
                return 3;
            case "thursday":
                return 4;
            case "friday":
                return 5;
            case "saturday":
                return 6;
            case "sunday":
                return 7;
            default:
                return 100;
        }
    }

    private void addSuggestionsForPartialOrFailed(TimetableGenerationResult result,
                                                  ArrayList<ClassRecord> selectedRecords,
                                                  String semester,
                                                  ArrayList<String> campuses,
                                                  boolean allowLectureOverlap) {
        if (selectedRecords.isEmpty()) {
            result.addSuggestion("Try removing strict preferences.");
            result.addSuggestion("Check whether the imported CSV files contain all required class types.");
        }

        // Only suggest removing campus filter if one is active
        if (!campuses.isEmpty()) {
            result.addSuggestion("Try removing the campus filter to allow more class options.");
        }

        // Only suggest enabling lecture overlap if it's currently disabled
        if (!allowLectureOverlap) {
            result.addSuggestion("Try enabling lecture overlap if lecture time clashes are acceptable.");
        }

        result.addSuggestion("Try selecting fewer topics.");
    }

    public Timetable generateTimetable(String timetableName,
                                       String semester,
                                       ArrayList<String> selectedTopicCodes,
                                       ArrayList<String> selectedCampuses,
                                       boolean allowLectureOverlap,
                                       ArrayList<Preference> preferences,
                                       ArrayList<ClassRecord> availableRecords,
                                       int requiredTravelMinutes) {
        lastErrorMessage = "";
        lastGenerationWarnings = new ArrayList<>();

        if (isBlank(semester)) {
            lastErrorMessage = "Semester is required.";
            return null;
        }
        if (selectedTopicCodes == null || selectedTopicCodes.isEmpty()) {
            lastErrorMessage = "At least one topic code is required.";
            return null;
        }
        if (selectedCampuses == null) {
            lastErrorMessage = "Campus selection is required.";
            return null;
        }
        if (availableRecords == null || availableRecords.isEmpty()) {
            lastErrorMessage = "No class records are available for timetable generation.";
            return null;
        }

        String name = isBlank(timetableName) ? generateAutomaticTimetableName() : timetableName.trim();
        if (!isBlank(timetableName) && !isTimetableNameUnique(name)) {
            lastErrorMessage = "A timetable with that name already exists.";
            return null;
        }

        ArrayList<String> topicCodes = normaliseStringList(selectedTopicCodes);
        ArrayList<String> campuses = normaliseStringList(selectedCampuses);
        String selectedSemester = semester.trim();

        ArrayList<ClassRecord> filteredRecords = filterRecords(availableRecords, selectedSemester, topicCodes, campuses);
        if (filteredRecords.isEmpty()) {
            lastErrorMessage = "No class records matched the selected filters.";
            return null;
        }

        Map<String, Map<String, ArrayList<ClassRecord>>> grouped = groupByTopicAndType(filteredRecords);
        ArrayList<ClassRecord> selectedRecords = new ArrayList<>();

        for (String topicCode : topicCodes) {
            String topicKey = safe(topicCode).toLowerCase();
            if (topicKey.isEmpty()) {
                continue;
            }

            Map<String, ArrayList<ClassRecord>> byType = grouped.get(topicKey);
            if (byType == null || byType.isEmpty()) {
                lastGenerationWarnings.add("No class records found for topic " + topicCode + ".");
                continue;
            }

            for (Map.Entry<String, ArrayList<ClassRecord>> entry : byType.entrySet()) {
                String classTypeKey = entry.getKey();
                ArrayList<ClassRecord> options = entry.getValue();
                ClassRecord chosen = chooseBestValidRecord(options, selectedRecords,
                        allowLectureOverlap, requiredTravelMinutes, preferences);
                if (chosen == null) {
                    String label = classTypeKey.isEmpty() ? "class" : classTypeKey;
                    lastGenerationWarnings.add("Unable to select a valid " + label
                            + " class for topic " + topicCode + ".");
                } else {
                    selectedRecords.add(chosen);
                }
            }
        }

        lastGenerationWarnings.addAll(validationService.getValidationWarnings(
                selectedRecords, allowLectureOverlap, requiredTravelMinutes));

        if (!validationService.isTimetableValid(selectedRecords, allowLectureOverlap, requiredTravelMinutes)) {
            lastErrorMessage = "Unable to generate a valid timetable with the selected options.";
            return null;
        }

        Timetable timetable = new Timetable();
        timetable.setTimetableName(name);
        timetable.setSemester(normalizeSemesterForStorage(selectedSemester));
        timetable.setAllowLectureOverlap(allowLectureOverlap);
        timetable.setPreferences(preferences);

        for (ClassRecord record : selectedRecords) {
            timetable.addEntry(new TimetableEntry(record));
        }

        timetables.add(timetable);
        return timetable;
    }

    public ArrayList<Timetable> getAllTimetables() {
        return new ArrayList<>(timetables);
    }

    public boolean hasTimetables() {
        return !timetables.isEmpty();
    }

    public int getTimetableCount() {
        return timetables.size();
    }

    public Timetable getTimetableByIndex(int displayIndex) {
        int index = displayIndex - 1;
        if (index < 0 || index >= timetables.size()) {
            return null;
        }
        return timetables.get(index);
    }

    public Timetable getTimetableByName(String name) {
        if (isBlank(name)) {
            return null;
        }

        String target = name.trim().toLowerCase();
        for (Timetable timetable : timetables) {
            if (timetable == null) {
                continue;
            }
            String existing = safe(timetable.getTimetableName()).toLowerCase();
            if (!existing.isEmpty() && existing.equals(target)) {
                return timetable;
            }
        }
        return null;
    }

    public String getBrowseSummary() {
        if (timetables.isEmpty()) {
            return "No timetables have been generated yet.";
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < timetables.size(); i++) {
            Timetable timetable = timetables.get(i);
            builder.append(i + 1).append(". ")
                    .append(timetable == null ? "" : timetable.getSummary());
            if (i < timetables.size() - 1) {
                builder.append("\n");
            }
        }

        return builder.toString();
    }

    public String getFullDetailsByIndex(int displayIndex) {
        Timetable timetable = getTimetableByIndex(displayIndex);
        if (timetable == null) {
            return "Invalid timetable number.";
        }
        return timetable.getFullDetails();
    }

    public boolean deleteTimetableByIndex(int displayIndex) {
        int index = displayIndex - 1;
        if (index < 0 || index >= timetables.size()) {
            return false;
        }
        timetables.remove(index);
        return true;
    }

    public boolean isTimetableNameUnique(String name) {
        if (isBlank(name)) {
            return true;
        }
        return getTimetableByName(name) == null;
    }

    public String generateAutomaticTimetableName() {
        while (true) {
            String name = "Timetable_" + autoNameCounter;
            autoNameCounter++;
            if (isTimetableNameUnique(name)) {
                return name;
            }
        }
    }

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    public ArrayList<String> getLastGenerationWarnings() {
        return new ArrayList<>(lastGenerationWarnings);
    }

    public boolean swapTimetableEntry(int timetableDisplayIndex,
                                      int entryDisplayIndex,
                                      ClassRecord replacementRecord,
                                      boolean allowInvalidSwap,
                                      int requiredTravelMinutes) {
        lastErrorMessage = "";
        lastGenerationWarnings = new ArrayList<>();

        Timetable timetable = getTimetableByIndex(timetableDisplayIndex);
        if (timetable == null) {
            lastErrorMessage = "Invalid timetable number.";
            return false;
        }

        TimetableEntry entry = getEntryByIndex(timetable, entryDisplayIndex);
        if (entry == null) {
            lastErrorMessage = "Invalid timetable entry number.";
            return false;
        }

        if (replacementRecord == null) {
            lastErrorMessage = "Replacement class record is required.";
            return false;
        }

        ClassRecord current = entry.getClassRecord();
        if (current == null) {
            lastErrorMessage = "Selected timetable entry is empty.";
            return false;
        }

        if (!sameText(current.getTopicCode(), replacementRecord.getTopicCode())) {
            lastErrorMessage = "Replacement must have the same topic code.";
            return false;
        }
        if (!sameText(current.getClassType(), replacementRecord.getClassType())) {
            lastErrorMessage = "Replacement must have the same class type.";
            return false;
        }

        ArrayList<ClassRecord> tempRecords = getRecordsFromTimetable(timetable);
        int entryIndex = entryDisplayIndex - 1;
        if (entryIndex < 0 || entryIndex >= tempRecords.size()) {
            lastErrorMessage = "Invalid timetable entry number.";
            return false;
        }
        tempRecords.set(entryIndex, replacementRecord);

        ArrayList<String> warnings = validationService.getValidationWarnings(
                tempRecords,
                timetable.isAllowLectureOverlap(),
                requiredTravelMinutes
        );
        if (warnings == null) {
            warnings = new ArrayList<>();
        }
        boolean valid = validationService.isTimetableValid(
                tempRecords,
                timetable.isAllowLectureOverlap(),
                requiredTravelMinutes
        );

        if (!valid && !allowInvalidSwap) {
            lastGenerationWarnings = warnings;
            lastErrorMessage = "Swap causes timetable validation issues.";
            return false;
        }

        entry.setClassRecord(replacementRecord);
        lastGenerationWarnings = warnings;
        return true;
    }

    public ArrayList<ClassRecord> findReplacementOptions(Timetable timetable,
                                                         int entryDisplayIndex,
                                                         ArrayList<ClassRecord> allClassRecords) {
        ArrayList<ClassRecord> options = new ArrayList<>();
        if (timetable == null || allClassRecords == null) {
            return options;
        }

        TimetableEntry entry = getEntryByIndex(timetable, entryDisplayIndex);
        if (entry == null || entry.getClassRecord() == null) {
            return options;
        }

        ClassRecord current = entry.getClassRecord();
        ArrayList<ClassRecord> sameRecords = new ArrayList<>();

        for (ClassRecord record : allClassRecords) {
            if (record == null) {
                continue;
            }
            if (!sameText(current.getTopicCode(), record.getTopicCode())) {
                continue;
            }
            if (!sameText(current.getClassType(), record.getClassType())) {
                continue;
            }
            if (sameClassRecord(current, record)) {
                sameRecords.add(record);
            } else {
                options.add(record);
            }
        }

        if (options.isEmpty()) {
            options.addAll(sameRecords);
        }

        options.sort(Comparator.comparing((ClassRecord record) -> safe(record.getClassInstance()).toLowerCase())
                .thenComparing(record -> safe(record.getDay()).toLowerCase())
                .thenComparing(record -> safe(record.getTimeDisplay()).toLowerCase()));

        return options;
    }

    private ClassRecord chooseBestValidRecord(ArrayList<ClassRecord> options,
                                              ArrayList<ClassRecord> selectedRecords,
                                              boolean allowLectureOverlap,
                                              int requiredTravelMinutes,
                                              ArrayList<Preference> preferences) {
        if (options == null || options.isEmpty()) {
            return null;
        }

        ArrayList<ClassRecord> sorted = new ArrayList<>(options);
        sorted.sort(Comparator.comparingInt(record -> -scoreRecordByPreferences(record, preferences)));

        for (ClassRecord option : sorted) {
            ArrayList<ClassRecord> temp = new ArrayList<>(selectedRecords);
            temp.add(option);
            if (validationService.isTimetableValid(temp, allowLectureOverlap, requiredTravelMinutes)) {
                return option;
            }
        }

        return null;
    }

    private ArrayList<ClassRecord> getRecordsFromTimetable(Timetable timetable) {
        ArrayList<ClassRecord> records = new ArrayList<>();
        if (timetable == null) {
            return records;
        }

        ArrayList<TimetableEntry> entries = timetable.getEntries();
        if (entries == null) {
            return records;
        }

        for (TimetableEntry entry : entries) {
            if (entry == null || entry.getClassRecord() == null) {
                records.add(null);
            } else {
                records.add(entry.getClassRecord());
            }
        }

        return records;
    }

    private TimetableEntry getEntryByIndex(Timetable timetable, int entryDisplayIndex) {
        if (timetable == null) {
            return null;
        }
        int index = entryDisplayIndex - 1;
        ArrayList<TimetableEntry> entries = timetable.getEntries();
        if (entries == null || index < 0 || index >= entries.size()) {
            return null;
        }
        return entries.get(index);
    }

    private boolean sameText(String a, String b) {
        String left = safe(a);
        String right = safe(b);
        if (left.isEmpty() || right.isEmpty()) {
            return false;
        }
        return left.equalsIgnoreCase(right);
    }

    private boolean sameClassRecord(ClassRecord a, ClassRecord b) {
        if (a == null || b == null) {
            return false;
        }
        return sameText(a.getTopicCode(), b.getTopicCode())
                && sameText(a.getClassType(), b.getClassType())
                && sameText(a.getClassInstance(), b.getClassInstance())
            && sameText(a.getCampus(), b.getCampus())
                && sameText(a.getDay(), b.getDay())
                && sameText(a.getBuilding(), b.getBuilding())
                && sameText(a.getRoom(), b.getRoom())
                && sameText(a.getTimeDisplay(), b.getTimeDisplay());
    }

    private int scoreRecordByPreferences(ClassRecord record, ArrayList<Preference> preferences) {
        if (record == null || preferences == null || preferences.isEmpty()) {
            return 0;
        }

        int score = 0;
        String campus = safe(record.getCampus());
        String baseDay = safe(record.getBaseDay());
        LocalTime startTime = record.getStartTime();

        for (Preference preference : preferences) {
            if (preference == null) {
                continue;
            }
            String prefName = safe(preference.getPreferenceName()).toLowerCase();
            int weight = Math.max(1, preference.getRanking());

            if (!campus.isEmpty() && prefName.equals(campus.toLowerCase())) {
                score += weight;
                continue;
            }

            if (prefName.equals("mornings") && startTime != null && startTime.isBefore(LocalTime.NOON)) {
                score += weight;
                continue;
            }
            if (prefName.equals("afternoons") && startTime != null && !startTime.isBefore(LocalTime.NOON)) {
                score += weight;
                continue;
            }

            if (!baseDay.isEmpty() && matchesDayPreference(baseDay, prefName)) {
                score += weight;
            }
        }

        return score;
    }

    private boolean matchesDayPreference(String baseDay, String preference) {
        String day = safe(baseDay).toLowerCase();
        String pref = safe(preference).toLowerCase();
        if (day.isEmpty() || pref.isEmpty()) {
            return false;
        }
        if (pref.equals(day) || pref.equals(day + "s")) {
            return true;
        }
        return day.equals(pref + "s");
    }

    private ArrayList<ClassRecord> filterRecords(ArrayList<ClassRecord> records,
                                                 String selectedSemester,
                                                 ArrayList<String> selectedTopicCodes,
                                                 ArrayList<String> selectedCampuses) {
        ArrayList<ClassRecord> filtered = new ArrayList<>();
        if (records == null) {
            return filtered;
        }

        for (ClassRecord record : records) {
            if (record == null) {
                continue;
            }

            if (!matchesSemester(record.getSemester(), selectedSemester)) {
                continue;
            }

            if (!matchesAnyIgnoreCase(record.getTopicCode(), selectedTopicCodes)) {
                continue;
            }

            // Apply campus filter: 
            // - If campus filter is specified, only include records from those campuses
            // - If no campus filter (empty list), include all campuses
            if (!selectedCampuses.isEmpty()) {
                // Campus filter is active - only include matching campuses
                if (!matchesAnyIgnoreCase(record.getCampus(), selectedCampuses)) {
                    continue;
                }
            }
            
            // Passed all filters
            filtered.add(record);
        }

        return filtered;
    }

    private Map<String, Map<String, ArrayList<ClassRecord>>> groupByTopicAndType(ArrayList<ClassRecord> records) {
        Map<String, Map<String, ArrayList<ClassRecord>>> grouped = new HashMap<>();
        if (records == null) {
            return grouped;
        }

        for (ClassRecord record : records) {
            if (record == null) {
                continue;
            }
            String topicKey = safe(record.getTopicCode()).toLowerCase();
            // Normalize class type by removing instance numbers (e.g., "Tutorial-1" -> "Tutorial")
            String normalizedType = normalizeClassType(record.getClassType());
            String typeKey = safe(normalizedType).toLowerCase();
            if (topicKey.isEmpty() || typeKey.isEmpty()) {
                continue;
            }

            Map<String, ArrayList<ClassRecord>> byType = grouped.computeIfAbsent(topicKey, key -> new HashMap<>());
            ArrayList<ClassRecord> list = byType.computeIfAbsent(typeKey, key -> new ArrayList<>());
            list.add(record);
        }

        return grouped;
    }

    private boolean matchesSemester(String recordSemester, String selectedSemester) {
        String recordValue = normalizeSemesterValue(recordSemester);
        String selectedValue = normalizeSemesterValue(selectedSemester);
        if (recordValue.isEmpty() || selectedValue.isEmpty()) {
            return false;
        }
        if (selectedValue.equals("both")) {
            return recordValue.equals("s1") || recordValue.equals("s2");
        }
        return recordValue.equals(selectedValue);
    }

    private String normalizeSemesterValue(String value) {
        String trimmed = safe(value).toLowerCase();
        if (trimmed.isEmpty()) {
            return "";
        }
        if (trimmed.equals("semester 1")) {
            return "s1";
        }
        if (trimmed.equals("semester 2")) {
            return "s2";
        }
        if (trimmed.equals("s1") || trimmed.equals("s2") || trimmed.equals("both")) {
            return trimmed;
        }
        return trimmed;
    }

    private String normalizeSemesterForStorage(String value) {
        String normalized = normalizeSemesterValue(value);
        if (normalized.equals("s1")) {
            return "1";
        }
        if (normalized.equals("s2")) {
            return "2";
        }
        if (normalized.equals("both")) {
            return "Both";
        }
        return safe(value);
    }

    private boolean matchesAnyIgnoreCase(String value, ArrayList<String> choices) {
        if (isBlank(value) || choices == null || choices.isEmpty()) {
            return false;
        }

        String target = value.trim().toLowerCase();
        for (String choice : choices) {
            if (choice == null) {
                continue;
            }
            if (target.equals(choice.trim().toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    private ArrayList<String> normaliseStringList(ArrayList<String> list) {
        ArrayList<String> normalized = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        if (list == null) {
            return normalized;
        }

        for (String value : list) {
            String trimmed = safe(value);
            if (trimmed.isEmpty()) {
                continue;
            }
            String key = trimmed.toLowerCase();
            if (seen.contains(key)) {
                continue;
            }
            seen.add(key);
            normalized.add(trimmed);
        }

        return normalized;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }

    private String normalizeClassType(String classType) {
        String trimmed = safe(classType);
        // Remove instance numbers like "-1", "-2", etc. from class type
        // E.g., "Tutorial-1" -> "Tutorial", "Lecture-2" -> "Lecture"
        return trimmed.replaceAll("-\\d+$", "");
    }

    /**
     * Attempts to find the best valid combination of class instances for all required
     * class types of a topic. Uses backtracking to explore combinations.
     * 
     * Priority:
     * 1. Find a complete timetable (all class types selected)
     * 2. If no complete timetable, find best partial (most classes selected)
     * 3. Collect rejection reasons for failed attempts
     */
    private BacktrackingResult tryFindBestCombinationForTopic(
            String topicCode,
            Map<String, ArrayList<ClassRecord>> byType,
            ArrayList<ClassRecord> selectedSoFar,
            boolean allowLectureOverlap,
            int requiredTravelMinutes
    ) {
        BacktrackingResult result = new BacktrackingResult();
        
        if (byType == null || byType.isEmpty()) {
            return result;
        }

        // Collect all class types needed for this topic
        ArrayList<String> classTypes = new ArrayList<>(byType.keySet());
        
        // Try to find a valid complete combination using backtracking
        ArrayList<ClassRecord> completeCombination = tryBacktrackForCombination(
                topicCode,
                classTypes,
                0,
                byType,
                selectedSoFar,
                new ArrayList<>(),
                allowLectureOverlap,
                requiredTravelMinutes
        );

        if (completeCombination != null) {
            // Found a complete valid combination
            result.selectedRecords.addAll(completeCombination);
            result.attemptedSelections = classTypes.size();
            return result;
        }

        // No complete combination found - use greedy approach but collect rejection reasons
        // Try each class type individually and collect rejection reasons
        for (String classType : classTypes) {
            ArrayList<ClassRecord> options = byType.get(classType);
            String classTypeLabel = classType.isEmpty() ? "class" : classType;
            
            boolean selected = false;
            GenerationWarning warning = new GenerationWarning(topicCode, classTypeLabel);

            for (ClassRecord option : options) {
                if (option == null) {
                    continue;
                }

                // Create temp list to test
                ArrayList<ClassRecord> temp = new ArrayList<>(selectedSoFar);
                temp.addAll(result.selectedRecords); // Include records selected so far in this topic
                temp.add(option);

                // Check if this option is valid
                if (validationService.isTimetableValid(temp, allowLectureOverlap, requiredTravelMinutes)) {
                    // This option is valid, add it to selected records
                    result.selectedRecords.add(option);
                    selected = true;
                    result.attemptedSelections++;
                    break;
                } else {
                    // Collect rejection reason
                    ArrayList<ClassRecord> testList = new ArrayList<>(selectedSoFar);
                    testList.addAll(result.selectedRecords);
                    
                    String rejectionReason = validationService.getDetailedRejectionReason(
                            option,
                            testList,
                            allowLectureOverlap,
                            requiredTravelMinutes,
                            true,
                            true
                    );
                    warning.addRejectionReason(new RejectionReason(safe(option.getClassInstance()), rejectionReason));
                    result.rejectedCount++;
                    result.attemptedSelections++;
                    
                    // Categorize rejection reason
                    if (rejectionReason.toLowerCase().contains("clash") || rejectionReason.toLowerCase().contains("overlap")) {
                        result.rejectedDueToClash++;
                    } else if (rejectionReason.toLowerCase().contains("travel time") || rejectionReason.toLowerCase().contains("travel")) {
                        result.rejectedDueToTravelTime++;
                    } else if (rejectionReason.toLowerCase().contains("campus") || rejectionReason.toLowerCase().contains("bedford") || 
                               rejectionReason.toLowerCase().contains("tonsley") || rejectionReason.toLowerCase().contains("city")) {
                        result.rejectedDueToCampusRule++;
                    } else {
                        result.rejectedDueToSemesterOrFilter++;
                    }
                }
            }

            if (!selected) {
                result.warnings.add(warning);
                result.missingClasses.add(topicCode + " " + classTypeLabel);
            }
        }

        return result;
    }

    /**
     * Recursive backtracking method to find a valid combination of all class types for a topic.
     * Returns the combination if found, null otherwise.
     */
    private ArrayList<ClassRecord> tryBacktrackForCombination(
            String topicCode,
            ArrayList<String> classTypes,
            int typeIndex,
            Map<String, ArrayList<ClassRecord>> byType,
            ArrayList<ClassRecord> selectedSoFar,
            ArrayList<ClassRecord> currentCombination,
            boolean allowLectureOverlap,
            int requiredTravelMinutes
    ) {
        // Base case: we've selected one instance for each class type
        if (typeIndex == classTypes.size()) {
            // Test if this combination is valid
            ArrayList<ClassRecord> fullList = new ArrayList<>(selectedSoFar);
            fullList.addAll(currentCombination);
            
            if (validationService.isTimetableValid(fullList, allowLectureOverlap, requiredTravelMinutes)) {
                return new ArrayList<>(currentCombination); // Found a valid combination
            }
            return null; // This combination doesn't work
        }

        // Recursive case: try each option for the current class type
        String classType = classTypes.get(typeIndex);
        ArrayList<ClassRecord> options = byType.get(classType);
        
        if (options == null || options.isEmpty()) {
            return null; // No options for this class type
        }

        // Try each option for this class type
        for (ClassRecord option : options) {
            if (option == null) {
                continue;
            }

            currentCombination.add(option);

            // Recursively try to complete the combination
            ArrayList<ClassRecord> result = tryBacktrackForCombination(
                    topicCode,
                    classTypes,
                    typeIndex + 1,
                    byType,
                    selectedSoFar,
                    currentCombination,
                    allowLectureOverlap,
                    requiredTravelMinutes
            );

            if (result != null) {
                return result; // Found a valid combination
            }

            // Backtrack: remove this option and try the next one
            currentCombination.remove(currentCombination.size() - 1);
        }

        return null; // No valid combination found with any option for this class type
    }

    /**
     * Helper class to hold backtracking results for a topic
     */
    private static class BacktrackingResult {
        ArrayList<ClassRecord> selectedRecords = new ArrayList<>();
        ArrayList<GenerationWarning> warnings = new ArrayList<>();
        ArrayList<String> missingClasses = new ArrayList<>();
        int attemptedSelections = 0;
        int rejectedCount = 0;
        int rejectedDueToClash = 0;
        int rejectedDueToTravelTime = 0;
        int rejectedDueToCampusRule = 0;
        int rejectedDueToSemesterOrFilter = 0;
    }
}

