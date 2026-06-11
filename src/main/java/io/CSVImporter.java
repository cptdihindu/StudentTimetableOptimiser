package io;

import model.ClassRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CSVImporter {
    private String lastErrorMessage;
    private ArrayList<String> rowWarnings;
    private int importedCount;
    private boolean successful;

    public CSVImporter() {
        this.rowWarnings = new ArrayList<>();
    }

    public ArrayList<ClassRecord> importFromFile(String filePath) {
        resetState();
        ArrayList<ClassRecord> records = new ArrayList<>();

        if (filePath == null || filePath.trim().isEmpty()) {
            lastErrorMessage = "File path cannot be empty.";
            return records;
        }

        if (!filePath.trim().toLowerCase().endsWith(".csv")) {
            lastErrorMessage = "Only CSV files are supported.";
            return records;
        }

        Path path = Path.of(filePath.trim());
        if (!Files.exists(path)) {
            lastErrorMessage = "File not found.";
            return records;
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String headerLine = reader.readLine();
            if (headerLine == null || headerLine.trim().isEmpty()) {
                lastErrorMessage = "Invalid CSV format. Missing header row.";
                return records;
            }

            Map<String, Integer> columnIndexes = parseHeader(headerLine);
            if (columnIndexes == null) {
                return records;
            }

            String line;
            int rowNumber = 1;
            while ((line = reader.readLine()) != null) {
                rowNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    ArrayList<String> rowValues = parseCsvLine(line);
                    ClassRecord record = createClassRecordFromRow(rowValues, columnIndexes);
                    if (record != null) {
                        records.add(record);
                        importedCount++;
                    }
                } catch (IllegalArgumentException ex) {
                    rowWarnings.add("Row " + rowNumber + " skipped: " + ex.getMessage());
                }
            }
        } catch (IOException ex) {
            lastErrorMessage = "Unable to read the CSV file.";
            return records;
        }

        if (importedCount == 0) {
            lastErrorMessage = "No valid records were imported.";
        }

        successful = lastErrorMessage == null && importedCount > 0;
        return records;
    }

    public boolean wasSuccessful() {
        return successful;
    }

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    public ArrayList<String> getRowWarnings() {
        return rowWarnings;
    }

    public int getImportedCount() {
        return importedCount;
    }

    private void resetState() {
        lastErrorMessage = null;
        rowWarnings = new ArrayList<>();
        importedCount = 0;
        successful = false;
    }

    private Map<String, Integer> parseHeader(String headerLine) {
        ArrayList<String> headers = parseCsvLine(headerLine);
        Map<String, Integer> columnIndexes = new HashMap<>();

        for (int i = 0; i < headers.size(); i++) {
            String key = normalizeHeader(headers.get(i));
            if (!key.isEmpty()) {
                columnIndexes.put(key, i);
            }
        }

        if (!columnIndexes.containsKey("topic")) {
            lastErrorMessage = "Invalid CSV format. Missing required column: Topic";
            return null;
        }
        if (!columnIndexes.containsKey("availability")) {
            lastErrorMessage = "Invalid CSV format. Missing required column: Availability";
            return null;
        }
        if (!columnIndexes.containsKey("class")) {
            lastErrorMessage = "Invalid CSV format. Missing required column: Class";
            return null;
        }
        if (!columnIndexes.containsKey("class instance")) {
            lastErrorMessage = "Invalid CSV format. Missing required column: Class instance";
            return null;
        }
        if (!columnIndexes.containsKey("date")) {
            lastErrorMessage = "Invalid CSV format. Missing required column: Date";
            return null;
        }
        if (!columnIndexes.containsKey("day")) {
            lastErrorMessage = "Invalid CSV format. Missing required column: Day";
            return null;
        }
        if (!columnIndexes.containsKey("time")) {
            lastErrorMessage = "Invalid CSV format. Missing required column: Time";
            return null;
        }
        if (!columnIndexes.containsKey("location") && !columnIndexes.containsKey("room")) {
            lastErrorMessage = "Invalid CSV format. Missing required column: Location";
            return null;
        }

        if (columnIndexes.containsKey("room") && !columnIndexes.containsKey("location")) {
            columnIndexes.put("location", columnIndexes.get("room"));
        }

        return columnIndexes;
    }

    private String normalizeHeader(String value) {
        if (value == null) {
            return "";
        }
        String trimmed = value.trim();
        if (!trimmed.isEmpty() && trimmed.charAt(0) == '\uFEFF') {
            trimmed = trimmed.substring(1);
        }
        return trimmed.toLowerCase();
    }

    private ArrayList<String> parseCsvLine(String line) {
        ArrayList<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);

            if (ch == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (ch == ',' && !inQuotes) {
                values.add(cleanCsvValue(current.toString()));
                current.setLength(0);
            } else {
                current.append(ch);
            }
        }

        values.add(cleanCsvValue(current.toString()));
        return values;
    }

    private String cleanCsvValue(String value) {
        String trimmed = value == null ? "" : value.trim();
        if (trimmed.startsWith("\"") && trimmed.endsWith("\"") && trimmed.length() >= 2) {
            trimmed = trimmed.substring(1, trimmed.length() - 1).trim();
        }
        return trimmed;
    }

    private String[] parseTopic(String topicText) {
        String trimmed = topicText == null ? "" : topicText.trim();
        if (trimmed.isEmpty()) {
            return new String[]{"", ""};
        }
        String[] parts = trimmed.split("\\s+", 2);
        String code = parts[0];
        String name = parts.length > 1 ? parts[1] : "";
        return new String[]{code, name};
    }

    private String[] parseAvailability(String availabilityText) {
        String trimmed = availabilityText == null ? "" : availabilityText.trim();
        String[] parts = trimmed.split("\\s*-\\s*");
        String attendanceMode = parts.length > 0 ? parts[0].trim() : "";
        String campus = parts.length > 1 ? parts[1].trim() : "";
        String semester = parts.length > 2 ? parts[2].trim() : "";
        String availabilityNumber = parts.length > 3 ? parts[3].trim() : "";
        return new String[]{attendanceMode, campus, semester, availabilityNumber};
    }

    private String[] parseDateRange(String dateText) {
        String trimmed = dateText == null ? "" : dateText.trim();
        if (trimmed.isEmpty()) {
            return new String[]{"", ""};
        }
        String[] parts = trimmed.split("\\s*-\\s*");
        String first = parts.length > 0 ? parts[0].trim() : "";
        String last = parts.length > 1 ? parts[1].trim() : "";
        return new String[]{first, last};
    }

    private LocalTime[] parseTimeRange(String timeText) {
        String trimmed = timeText == null ? "" : timeText.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Time value is missing.");
        }
        String[] parts = trimmed.split("\\s*-\\s*");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid time range format.");
        }

        try {
            LocalTime start = LocalTime.parse(parts[0].trim());
            LocalTime end = LocalTime.parse(parts[1].trim());
            return new LocalTime[]{start, end};
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Invalid time format.");
        }
    }

    private String[] parseLocation(String locationText) {
        String trimmed = locationText == null ? "" : locationText.trim();
        if (trimmed.isEmpty()) {
            return new String[]{"", ""};
        }
        int commaIndex = trimmed.indexOf(',');
        if (commaIndex >= 0) {
            String building = trimmed.substring(0, commaIndex).trim();
            String room = trimmed.substring(commaIndex + 1).trim();
            return new String[]{building, room};
        }
        return new String[]{trimmed, ""};
    }

    private ClassRecord createClassRecordFromRow(ArrayList<String> rowValues,
                                                 Map<String, Integer> columnIndexes) {
        String topicText = getValue(rowValues, columnIndexes.get("topic"));
        String availabilityText = getValue(rowValues, columnIndexes.get("availability"));
        String classType = getValue(rowValues, columnIndexes.get("class"));
        String classInstance = getValue(rowValues, columnIndexes.get("class instance"));
        String dateText = getValue(rowValues, columnIndexes.get("date"));
        String day = getValue(rowValues, columnIndexes.get("day"));
        String timeText = getValue(rowValues, columnIndexes.get("time"));
        String locationText = getValue(rowValues, columnIndexes.get("location"));

        if (topicText.isEmpty()) {
            throw new IllegalArgumentException("Missing topic value.");
        }
        if (availabilityText.isEmpty()) {
            throw new IllegalArgumentException("Missing availability value.");
        }
        if (classType.isEmpty()) {
            throw new IllegalArgumentException("Missing class type value.");
        }
        if (timeText.isEmpty()) {
            throw new IllegalArgumentException("Missing time value.");
        }

        String[] topicParts = parseTopic(topicText);
        String[] availabilityParts = parseAvailability(availabilityText);
        String[] dateParts = parseDateRange(dateText);
        LocalTime[] timeParts = parseTimeRange(timeText);
        String[] locationParts = parseLocation(locationText);

        return new ClassRecord(
                topicParts[0],
                topicParts[1],
                availabilityParts[0],
                availabilityParts[1],
                availabilityParts[2],
                availabilityParts[3],
                classType,
                classInstance,
                dateParts[0],
                dateParts[1],
                day,
                timeParts[0],
                timeParts[1],
                locationParts[0],
                locationParts[1]
        );
    }

    private String getValue(ArrayList<String> rowValues, Integer index) {
        if (index == null || index < 0 || index >= rowValues.size()) {
            return "";
        }
        String value = rowValues.get(index);
        return value == null ? "" : value.trim();
    }
}

