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

        ArrayList<String> missingClasses = new ArrayList<>();
        ArrayList<Requirement> requirements = buildRequirements(topicCodes, grouped, missingClasses, preferences);
        SearchResult searchResult = findBestGlobalCombination(
                requirements,
                allowLectureOverlap,
                requiredTravelMinutes,
                preferences
        );

        ArrayList<ClassRecord> selectedRecords = searchResult.selectedRecords;
        ArrayList<GenerationWarning> warnings = searchResult.warnings;
        for (GenerationWarning warning : warnings) {
            String topic = safe(warning.getTopicCode());
            String classType = safe(warning.getClassType());
            String label = (topic + " " + classType).trim();
            if (!label.isEmpty() && !missingClasses.contains(label)) {
                missingClasses.add(label);
            }
        }

        result.setSelectedRecordsCount(selectedRecords.size());
        result.setGenerationWarnings(warnings);
        result.setMissingClasses(missingClasses);
        result.setRejectedDueToClash(searchResult.rejectedDueToClash);
        result.setRejectedDueToTravelTime(searchResult.rejectedDueToTravelTime);
        result.setRejectedDueToCampusRule(0);
        result.setRejectedDueToSemesterOrFilter(searchResult.rejectedDueToSemesterOrFilter);
        result.setOptimisationScore(searchResult.optimisationScore);
        result.setOptimisationSummary(buildOptimisationSummary(selectedRecords, preferences));

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
            int weight = getPreferenceWeight(preference, preferences.size());

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

    private int scoreTimetableByPreferences(ArrayList<ClassRecord> records, ArrayList<Preference> preferences) {
        if (records == null || records.isEmpty()) {
            return 0;
        }

        int score = 0;
        for (ClassRecord record : records) {
            score += scoreRecordByPreferences(record, preferences);
        }

        if (preferences == null || preferences.isEmpty()) {
            return score;
        }

        for (Preference preference : preferences) {
            if (preference == null) {
                continue;
            }
            String prefName = safe(preference.getPreferenceName()).toLowerCase();
            int weight = getPreferenceWeight(preference, preferences.size());

            if (prefName.equals("all at the same campus")) {
                score += scoreSameCampusPreference(records, weight);
            } else if (prefName.equals("evenly spread classes across days")) {
                score += scoreSpreadAcrossDaysPreference(records, weight);
            } else if (prefName.equals("compact classes to as few days as possible")) {
                score += scoreCompactDaysPreference(records, weight);
            }
        }

        return score;
    }

    private int getPreferenceWeight(Preference preference, int preferenceCount) {
        if (preference == null) {
            return 0;
        }
        int ranking = Math.max(1, preference.getRanking());
        int count = Math.max(1, preferenceCount);
        return (count - ranking + 1) * 100;
    }

    private int scoreSameCampusPreference(ArrayList<ClassRecord> records, int weight) {
        Map<String, Integer> campusCounts = new HashMap<>();
        int physicalClassCount = 0;

        for (ClassRecord record : records) {
            if (record == null || record.isOnline()) {
                continue;
            }
            String campus = safe(record.getCampus()).toLowerCase();
            if (campus.isEmpty()) {
                continue;
            }
            physicalClassCount++;
            campusCounts.put(campus, campusCounts.getOrDefault(campus, 0) + 1);
        }

        if (physicalClassCount == 0) {
            return weight;
        }

        int largestCampusCount = 0;
        for (Integer count : campusCounts.values()) {
            if (count != null) {
                largestCampusCount = Math.max(largestCampusCount, count);
            }
        }

        return largestCampusCount * weight;
    }

    private int scoreSpreadAcrossDaysPreference(ArrayList<ClassRecord> records, int weight) {
        int uniqueDays = getUniqueDayCount(records);
        return uniqueDays * weight;
    }

    private int scoreCompactDaysPreference(ArrayList<ClassRecord> records, int weight) {
        int uniqueDays = getUniqueDayCount(records);
        if (uniqueDays <= 0) {
            return 0;
        }
        return (8 - Math.min(uniqueDays, 7)) * weight;
    }

    private int getUniqueDayCount(ArrayList<ClassRecord> records) {
        Set<String> days = new HashSet<>();
        if (records == null) {
            return 0;
        }
        for (ClassRecord record : records) {
            if (record == null) {
                continue;
            }
            String day = safe(record.getBaseDay()).toLowerCase();
            if (!day.isEmpty()) {
                days.add(day);
            }
        }
        return days.size();
    }

    private String buildOptimisationSummary(ArrayList<ClassRecord> records, ArrayList<Preference> preferences) {
        StringBuilder builder = new StringBuilder();
        builder.append("- Days used: ").append(getUniqueDayCount(records));
        builder.append("\n- Campus spread: ").append(getCampusSpreadSummary(records));
        if (preferences == null || preferences.isEmpty()) {
            builder.append("\n- Preferences applied: none");
        } else {
            builder.append("\n- Preferences applied: ").append(preferences.size());
        }
        return builder.toString();
    }

    private String getCampusSpreadSummary(ArrayList<ClassRecord> records) {
        Map<String, Integer> campusCounts = new HashMap<>();
        if (records != null) {
            for (ClassRecord record : records) {
                if (record == null) {
                    continue;
                }
                String campus = safe(record.getCampus());
                if (!campus.isEmpty()) {
                    campusCounts.put(campus, campusCounts.getOrDefault(campus, 0) + 1);
                }
            }
        }

        if (campusCounts.isEmpty()) {
            return "none";
        }

        StringBuilder builder = new StringBuilder();
        int index = 0;
        for (Map.Entry<String, Integer> entry : campusCounts.entrySet()) {
            if (index > 0) {
                builder.append(", ");
            }
            builder.append(entry.getKey()).append(" ").append(entry.getValue());
            index++;
        }
        return builder.toString();
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
            if (!containsSameScheduleOption(list, record)) {
                list.add(record);
            }
        }

        return grouped;
    }

    private boolean containsSameScheduleOption(ArrayList<ClassRecord> records, ClassRecord candidate) {
        if (records == null || candidate == null) {
            return false;
        }
        for (ClassRecord record : records) {
            if (record == null) {
                continue;
            }
            if (sameText(record.getTopicCode(), candidate.getTopicCode())
                    && sameText(record.getClassType(), candidate.getClassType())
                    && sameText(record.getClassInstance(), candidate.getClassInstance())
                    && sameText(record.getBaseDay(), candidate.getBaseDay())
                    && sameText(record.getTimeDisplay(), candidate.getTimeDisplay())
                    && sameText(record.getCampus(), candidate.getCampus())) {
                return true;
            }
        }
        return false;
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

    private ArrayList<Requirement> buildRequirements(ArrayList<String> topicCodes,
                                                      Map<String, Map<String, ArrayList<ClassRecord>>> grouped,
                                                      ArrayList<String> missingClasses,
                                                      ArrayList<Preference> preferences) {
        ArrayList<Requirement> requirements = new ArrayList<>();
        if (topicCodes == null || grouped == null) {
            return requirements;
        }

        for (String topicCode : topicCodes) {
            String topicKey = safe(topicCode).toLowerCase();
            if (topicKey.isEmpty()) {
                continue;
            }

            Map<String, ArrayList<ClassRecord>> byType = grouped.get(topicKey);
            if (byType == null || byType.isEmpty()) {
                missingClasses.add(topicCode + " has no classes available for the selected semester and campus filter.");
                continue;
            }

            ArrayList<String> classTypes = new ArrayList<>(byType.keySet());
            classTypes.sort(String::compareToIgnoreCase);
            for (String classType : classTypes) {
                ArrayList<ClassRecord> options = byType.get(classType);
                if (options == null || options.isEmpty()) {
                    missingClasses.add(topicCode + " " + classType);
                    continue;
                }

                ArrayList<ClassRecord> sortedOptions = new ArrayList<>(options);
                sortedOptions.sort(Comparator
                        .comparingInt((ClassRecord record) -> -scoreRecordByPreferences(record, preferences))
                        .thenComparing(record -> safe(record.getBaseDay()).toLowerCase())
                        .thenComparing(record -> record.getStartTime() == null ? LocalTime.MAX : record.getStartTime())
                        .thenComparing(record -> safe(record.getClassInstance()).toLowerCase()));

                requirements.add(new Requirement(topicCode, classType, sortedOptions));
            }
        }

        requirements.sort(Comparator
                .comparingInt((Requirement requirement) -> requirement.options.size())
                .thenComparing(requirement -> safe(requirement.topicCode).toLowerCase())
                .thenComparing(requirement -> safe(requirement.classType).toLowerCase()));
        return requirements;
    }

    private SearchResult findBestGlobalCombination(ArrayList<Requirement> requirements,
                                                   boolean allowLectureOverlap,
                                                   int requiredTravelMinutes,
                                                   ArrayList<Preference> preferences) {
        SearchResult complete = new SearchResult();
        if (requirements == null || requirements.isEmpty()) {
            return complete;
        }

        tryBacktrackGlobally(
                requirements,
                0,
                new ArrayList<>(),
                allowLectureOverlap,
                requiredTravelMinutes,
                preferences,
                complete
        );

        if (complete.hasCompleteTimetable) {
            complete.warnings.clear();
            return complete;
        }

        SearchResult partial = buildBestPartialCombination(
                requirements,
                allowLectureOverlap,
                requiredTravelMinutes,
                preferences
        );
        partial.rejectedDueToClash += complete.rejectedDueToClash;
        partial.rejectedDueToTravelTime += complete.rejectedDueToTravelTime;
        partial.rejectedDueToSemesterOrFilter += complete.rejectedDueToSemesterOrFilter;
        return partial;
    }

    private boolean tryBacktrackGlobally(ArrayList<Requirement> requirements,
                                         int requirementIndex,
                                         ArrayList<ClassRecord> selected,
                                         boolean allowLectureOverlap,
                                         int requiredTravelMinutes,
                                         ArrayList<Preference> preferences,
                                         SearchResult result) {
        if (requirementIndex == requirements.size()) {
            int score = scoreTimetableByPreferences(selected, preferences);
            if (!result.hasCompleteTimetable || score > result.optimisationScore) {
                result.selectedRecords = new ArrayList<>(selected);
                result.optimisationScore = score;
                result.hasCompleteTimetable = true;
            }
            return true;
        }

        Requirement requirement = requirements.get(requirementIndex);
        for (ClassRecord option : requirement.options) {
            result.attemptedSelections++;
            ArrayList<ClassRecord> test = new ArrayList<>(selected);
            test.add(option);

            if (validationService.isTimetableValid(test, allowLectureOverlap, requiredTravelMinutes)) {
                selected.add(option);
                tryBacktrackGlobally(requirements, requirementIndex + 1, selected,
                        allowLectureOverlap, requiredTravelMinutes, preferences, result);
                selected.remove(selected.size() - 1);
            } else {
                countRejection(result, option, selected, allowLectureOverlap, requiredTravelMinutes);
            }
        }

        return false;
    }

    private SearchResult buildBestPartialCombination(ArrayList<Requirement> requirements,
                                                     boolean allowLectureOverlap,
                                                     int requiredTravelMinutes,
                                                     ArrayList<Preference> preferences) {
        SearchResult result = new SearchResult();
        ArrayList<Requirement> ordered = new ArrayList<>(requirements);
        ordered.sort(Comparator
                .comparingInt((Requirement requirement) -> requirement.options.size())
                .thenComparing(requirement -> -getBestRequirementPreferenceScore(requirement, preferences)));

        for (Requirement requirement : ordered) {
            GenerationWarning warning = new GenerationWarning(requirement.topicCode, requirement.classType);
            ClassRecord chosen = null;

            for (ClassRecord option : requirement.options) {
                result.attemptedSelections++;
                ArrayList<ClassRecord> test = new ArrayList<>(result.selectedRecords);
                test.add(option);
                if (validationService.isTimetableValid(test, allowLectureOverlap, requiredTravelMinutes)) {
                    chosen = option;
                    break;
                }

                String reason = validationService.getDetailedRejectionReason(
                        option,
                        result.selectedRecords,
                        allowLectureOverlap,
                        requiredTravelMinutes,
                        true,
                        true
                );
                warning.addRejectionReason(new RejectionReason(safe(option.getClassInstance()), makeFriendlyReason(reason)));
                countRejection(result, option, result.selectedRecords, allowLectureOverlap, requiredTravelMinutes);
            }

            if (chosen == null) {
                result.warnings.add(limitWarningReasons(warning, 3));
            } else {
                result.selectedRecords.add(chosen);
            }
        }

        result.optimisationScore = scoreTimetableByPreferences(result.selectedRecords, preferences);
        return result;
    }

    private int getBestRequirementPreferenceScore(Requirement requirement, ArrayList<Preference> preferences) {
        int best = 0;
        if (requirement == null || requirement.options == null) {
            return best;
        }
        for (ClassRecord option : requirement.options) {
            best = Math.max(best, scoreRecordByPreferences(option, preferences));
        }
        return best;
    }

    private GenerationWarning limitWarningReasons(GenerationWarning warning, int maxReasons) {
        if (warning == null || warning.getRejectionReasons().size() <= maxReasons) {
            return warning;
        }
        ArrayList<RejectionReason> limited = new ArrayList<>();
        for (int i = 0; i < maxReasons; i++) {
            limited.add(warning.getRejectionReasons().get(i));
        }
        warning.setRejectionReasons(limited);
        return warning;
    }

    private void countRejection(SearchResult result,
                                ClassRecord option,
                                ArrayList<ClassRecord> selected,
                                boolean allowLectureOverlap,
                                int requiredTravelMinutes) {
        String reason = validationService.getDetailedRejectionReason(
                option,
                selected,
                allowLectureOverlap,
                requiredTravelMinutes,
                true,
                true
        );
        String lower = reason.toLowerCase();
        if (lower.contains("clash") || lower.contains("overlap")) {
            result.rejectedDueToClash++;
        } else if (lower.contains("travel")) {
            result.rejectedDueToTravelTime++;
        } else {
            result.rejectedDueToSemesterOrFilter++;
        }
    }

    private String makeFriendlyReason(String reason) {
        String text = safe(reason);
        if (text.isEmpty() || text.equalsIgnoreCase("unknown reason")) {
            return "it does not fit with the selected classes.";
        }
        if (text.startsWith("time clash with ")) {
            return "it overlaps with " + text.substring("time clash with ".length()) + ".";
        }
        if (text.startsWith("not enough travel time")) {
            return text + ".";
        }
        if (text.equals("does not match selected semester")) {
            return "it is not offered in the selected semester.";
        }
        if (text.equals("does not match selected campus filter")) {
            return "it is outside the selected campus filter.";
        }
        return text;
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
    private static class Requirement {
        String topicCode;
        String classType;
        ArrayList<ClassRecord> options;

        Requirement(String topicCode, String classType, ArrayList<ClassRecord> options) {
            this.topicCode = topicCode;
            this.classType = classType;
            this.options = options == null ? new ArrayList<>() : options;
        }
    }

    private static class SearchResult {
        ArrayList<ClassRecord> selectedRecords = new ArrayList<>();
        ArrayList<GenerationWarning> warnings = new ArrayList<>();
        boolean hasCompleteTimetable = false;
        int optimisationScore = 0;
        int attemptedSelections = 0;
        int rejectedDueToClash = 0;
        int rejectedDueToTravelTime = 0;
        int rejectedDueToSemesterOrFilter = 0;
    }

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

