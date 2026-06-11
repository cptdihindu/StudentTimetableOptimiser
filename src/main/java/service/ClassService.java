package service;

import io.CSVImporter;
import model.ClassRecord;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class ClassService {
    private ArrayList<ClassRecord> classRecords;
    private int lastImportNewCount;
    private int lastImportUpdatedCount;
    private ArrayList<String> lastImportWarnings;
    private String lastErrorMessage;

    public ClassService() {
        this.classRecords = new ArrayList<>();
        this.lastImportWarnings = new ArrayList<>();
    }

    public boolean importFromCsv(String filePath) {
        CSVImporter importer = new CSVImporter();
        ArrayList<ClassRecord> importedRecords = importer.importFromFile(filePath);

        lastImportWarnings = new ArrayList<>(importer.getRowWarnings());

        if (!importer.wasSuccessful()) {
            lastErrorMessage = importer.getLastErrorMessage();
            return false;
        }

        importRecords(importedRecords);
        lastErrorMessage = "";
        return true;
    }

    public void importRecords(ArrayList<ClassRecord> importedRecords) {
        lastImportNewCount = 0;
        lastImportUpdatedCount = 0;

        if (importedRecords == null) {
            return;
        }

        for (ClassRecord record : importedRecords) {
            if (record == null) {
                continue;
            }
            ClassRecord existing = findMatchingRecord(record);
            if (existing != null) {
                existing.updateTimeAndLocationFrom(record);
                lastImportUpdatedCount++;
            } else {
                classRecords.add(record);
                lastImportNewCount++;
            }
        }
    }

    public ArrayList<ClassRecord> getAllClassRecords() {
        return new ArrayList<>(classRecords);
    }

    public int getClassRecordCount() {
        return classRecords.size();
    }

    public boolean hasClassRecords() {
        return !classRecords.isEmpty();
    }

    public ClassRecord getClassRecordByIndex(int displayIndex) {
        int index = displayIndex - 1;
        if (index < 0 || index >= classRecords.size()) {
            return null;
        }
        return classRecords.get(index);
    }

    public String getBrowseSummary() {
        if (classRecords.isEmpty()) {
            return "No class records have been imported yet.";
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < classRecords.size(); i++) {
            builder.append(i + 1).append(". ")
                    .append(classRecords.get(i).getSummary());
            if (i < classRecords.size() - 1) {
                builder.append("\n");
            }
        }

        return builder.toString();
    }

    public String getFullDetailsByIndex(int displayIndex) {
        ClassRecord record = getClassRecordByIndex(displayIndex);
        if (record == null) {
            return "Invalid class record number.";
        }
        return record.getFullDetails();
    }

    public boolean deleteClassRecordByIndex(int displayIndex) {
        int index = displayIndex - 1;
        if (index < 0 || index >= classRecords.size()) {
            return false;
        }
        classRecords.remove(index);
        return true;
    }

    public boolean editClassRecordField(int displayIndex, String fieldName, String newValue) {
        ClassRecord record = getClassRecordByIndex(displayIndex);
        if (record == null) {
            lastErrorMessage = "Invalid class record number.";
            return false;
        }
        if (isBlank(fieldName) || isBlank(newValue)) {
            lastErrorMessage = "Field name and value are required.";
            return false;
        }

        String normalized = normalizeFieldName(fieldName);
        String value = newValue.trim();

        switch (normalized) {
            case "topiccode":
                record.setTopicCode(value);
                break;
            case "topicname":
                record.setTopicName(value);
                break;
            case "attendancemode":
                record.setAttendanceMode(value);
                break;
            case "campus":
                record.setCampus(value);
                break;
            case "semester":
                record.setSemester(value);
                break;
            case "availabilitynumber":
                record.setAvailabilityNumber(value);
                break;
            case "classtype":
                record.setClassType(value);
                break;
            case "classinstance":
                record.setClassInstance(value);
                break;
            case "firstclassdate":
                record.setFirstClassDate(value);
                break;
            case "lastclassdate":
                record.setLastClassDate(value);
                break;
            case "day":
                record.setDay(value);
                break;
            case "starttime":
                LocalTime startTime = parseTime(value);
                if (startTime == null) {
                    return false;
                }
                record.setStartTime(startTime);
                break;
            case "endtime":
                LocalTime endTime = parseTime(value);
                if (endTime == null) {
                    return false;
                }
                record.setEndTime(endTime);
                break;
            case "building":
                record.setBuilding(value);
                break;
            case "room":
                record.setRoom(value);
                break;
            default:
                lastErrorMessage = "Unknown field name.";
                return false;
        }

        lastErrorMessage = "";
        return true;
    }

    public ArrayList<ClassRecord> findByTopicCode(String topicCode) {
        return findByStringField(topicCode, "topicCode");
    }

    public ArrayList<ClassRecord> findByCampus(String campus) {
        return findByStringField(campus, "campus");
    }

    public ArrayList<ClassRecord> findBySemester(String semester) {
        return findByStringField(semester, "semester");
    }

    public int getLastImportNewCount() {
        return lastImportNewCount;
    }

    public int getLastImportUpdatedCount() {
        return lastImportUpdatedCount;
    }

    public ArrayList<String> getLastImportWarnings() {
        return new ArrayList<>(lastImportWarnings);
    }

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    public ArrayList<String> getAvailableTopicCodes() {
        ArrayList<String> results = new ArrayList<>();
        for (ClassRecord record : classRecords) {
            if (record == null) {
                continue;
            }
            addUniqueIgnoreCase(results, record.getTopicCode());
        }
        results.sort(String.CASE_INSENSITIVE_ORDER);
        return results;
    }

    public ArrayList<String> getAvailableTopicDisplayNames() {
        ArrayList<String> results = new ArrayList<>();
        for (ClassRecord record : classRecords) {
            if (record == null) {
                continue;
            }
            addUniqueIgnoreCase(results, record.getTopicDisplayName());
        }
        results.sort(String.CASE_INSENSITIVE_ORDER);
        return results;
    }

    public ArrayList<String> getAvailableTopicDisplayNamesBySemester(String semester) {
        ArrayList<String> results = new ArrayList<>();
        for (ClassRecord record : classRecords) {
            if (record == null || semester == null) {
                continue;
            }
            // Match semester - handle both "S1", "S2" and "Both"
            boolean matches = false;
            if (semester.equalsIgnoreCase("Both")) {
                matches = true;
            } else if (semester.equalsIgnoreCase(record.getSemester())) {
                matches = true;
            }
            if (matches) {
                addUniqueIgnoreCase(results, record.getTopicDisplayName());
            }
        }
        results.sort(String.CASE_INSENSITIVE_ORDER);
        return results;
    }

    public ArrayList<String> getAvailableCampuses() {
        ArrayList<String> results = new ArrayList<>();
        for (ClassRecord record : classRecords) {
            if (record == null) {
                continue;
            }
            addUniqueIgnoreCase(results, record.getCampus());
        }
        results.sort(String.CASE_INSENSITIVE_ORDER);
        return results;
    }

    public ArrayList<String> getAvailableSemesters() {
        ArrayList<String> results = new ArrayList<>();
        for (ClassRecord record : classRecords) {
            if (record == null) {
                continue;
            }
            addUniqueIgnoreCase(results, record.getSemester());
        }
        results.sort(String.CASE_INSENSITIVE_ORDER);
        return results;
    }

    public ArrayList<String> getAvailableClassTypes() {
        ArrayList<String> results = new ArrayList<>();
        for (ClassRecord record : classRecords) {
            if (record == null) {
                continue;
            }
            addUniqueIgnoreCase(results, record.getClassType());
        }
        results.sort(String.CASE_INSENSITIVE_ORDER);
        return results;
    }

    public ArrayList<String> getAvailableDays() {
        ArrayList<String> results = new ArrayList<>();
        for (ClassRecord record : classRecords) {
            if (record == null) {
                continue;
            }
            addUniqueIgnoreCase(results, record.getBaseDay());
        }
        results.sort(String.CASE_INSENSITIVE_ORDER);
        return results;
    }

    private ClassRecord findMatchingRecord(ClassRecord record) {
        for (ClassRecord existing : classRecords) {
            if (existing.hasSameIdentity(record)) {
                return existing;
            }
        }
        return null;
    }

    private ArrayList<ClassRecord> findByStringField(String searchValue, String fieldName) {
        ArrayList<ClassRecord> results = new ArrayList<>();
        if (isBlank(searchValue)) {
            return results;
        }

        for (ClassRecord record : classRecords) {
            String value = getFieldValue(record, fieldName);
            if (matchesIgnoreCaseTrim(value, searchValue)) {
                results.add(record);
            }
        }

        return results;
    }

    private String getFieldValue(ClassRecord record, String fieldName) {
        if (record == null) {
            return "";
        }
        if ("topicCode".equals(fieldName)) {
            return record.getTopicCode();
        }
        if ("campus".equals(fieldName)) {
            return record.getCampus();
        }
        if ("semester".equals(fieldName)) {
            return record.getSemester();
        }
        return "";
    }

    private LocalTime parseTime(String value) {
        try {
            return LocalTime.parse(value);
        } catch (DateTimeParseException ex) {
            lastErrorMessage = "Invalid time format. Use HH:mm.";
            return null;
        }
    }

    private String normalizeFieldName(String fieldName) {
        String normalized = fieldName.trim().toLowerCase();
        if (normalized.equals("topic code")) {
            return "topiccode";
        }
        if (normalized.equals("topic name")) {
            return "topicname";
        }
        if (normalized.equals("attendance mode")) {
            return "attendancemode";
        }
        if (normalized.equals("availability number")) {
            return "availabilitynumber";
        }
        if (normalized.equals("class type")) {
            return "classtype";
        }
        if (normalized.equals("class instance")) {
            return "classinstance";
        }
        if (normalized.equals("first class date")) {
            return "firstclassdate";
        }
        if (normalized.equals("last class date")) {
            return "lastclassdate";
        }
        if (normalized.equals("start time")) {
            return "starttime";
        }
        if (normalized.equals("end time")) {
            return "endtime";
        }
        return normalized.replace(" ", "");
    }

    private boolean matchesIgnoreCaseTrim(String value, String search) {
        if (value == null || search == null) {
            return false;
        }
        return value.trim().equalsIgnoreCase(search.trim());
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void addUniqueIgnoreCase(ArrayList<String> list, String value) {
        if (list == null || isBlank(value)) {
            return;
        }
        String trimmed = value.trim();
        for (String existing : list) {
            if (existing != null && existing.trim().equalsIgnoreCase(trimmed)) {
                return;
            }
        }
        list.add(trimmed);
    }
}

