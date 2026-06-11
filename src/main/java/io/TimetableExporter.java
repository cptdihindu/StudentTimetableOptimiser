package io;

import model.ClassRecord;
import model.Timetable;
import model.TimetableEntry;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TimetableExporter {
    private String lastErrorMessage;

    public boolean exportTimetableToCsv(Timetable timetable, String filePath) {
        lastErrorMessage = "";

        if (timetable == null) {
            lastErrorMessage = "No timetable was selected for export.";
            return false;
        }

        if (filePath == null || filePath.trim().isEmpty()) {
            lastErrorMessage = "Export file path cannot be blank.";
            return false;
        }

        Path exportPath;
        try {
            exportPath = Paths.get(filePath.trim());
        } catch (InvalidPathException ex) {
            lastErrorMessage = "Export file path is invalid.";
            return false;
        }

        if (!exportPath.toString().toLowerCase().endsWith(".csv")) {
            lastErrorMessage = "Export file path must end with .csv.";
            return false;
        }

        try {
            Path parent = exportPath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            try (BufferedWriter writer = Files.newBufferedWriter(exportPath, StandardCharsets.UTF_8)) {
                writer.write("Timetable Name,Semester,Topic Code,Topic Name,Attendance Mode,Campus,"
                        + "Class Type,Class Instance,Availability Number,First Class Date,Last Class Date,"
                        + "Day,Start Time,End Time,Building,Room");
                writer.newLine();

                for (TimetableEntry entry : timetable.getEntries()) {
                    if (entry == null || entry.getClassRecord() == null) {
                        continue;
                    }
                    writeRecord(writer, timetable, entry.getClassRecord());
                }
            }

            return true;
        } catch (IOException ex) {
            lastErrorMessage = "Unable to export timetable: " + ex.getMessage();
            return false;
        }
    }

    public String getLastErrorMessage() {
        return lastErrorMessage == null ? "" : lastErrorMessage;
    }

    public Path getDefaultExportPath(Timetable timetable) {
        String timetableName = timetable == null ? "" : timetable.getTimetableName();
        String fileName = sanitizeFileName(timetableName);
        if (fileName.isEmpty()) {
            fileName = "Timetable";
        }
        return Paths.get("exports", fileName + ".csv");
    }

    public boolean exportTimetableToDefaultCsv(Timetable timetable) {
        Path exportPath = getDefaultExportPath(timetable);
        return exportTimetableToCsv(timetable, exportPath.toString());
    }

    private void writeRecord(BufferedWriter writer, Timetable timetable, ClassRecord record) throws IOException {
        String[] values = {
                timetable.getTimetableName(),
                timetable.getSemester(),
                record.getTopicCode(),
                record.getTopicName(),
                record.getAttendanceMode(),
                record.getCampus(),
                record.getClassType(),
                record.getClassInstance(),
                record.getAvailabilityNumber(),
                record.getFirstClassDate(),
                record.getLastClassDate(),
                record.getDay(),
                record.getStartTime() == null ? "" : record.getStartTime().toString(),
                record.getEndTime() == null ? "" : record.getEndTime().toString(),
                record.getBuilding(),
                record.getRoom()
        };

        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                writer.write(",");
            }
            writer.write(toCsvValue(values[i]));
        }
        writer.newLine();
    }

    private String toCsvValue(String value) {
        String safeValue = value == null ? "" : value;
        boolean mustQuote = safeValue.contains(",")
                || safeValue.contains("\"")
                || safeValue.contains("\n")
                || safeValue.contains("\r");
        if (!mustQuote) {
            return safeValue;
        }
        return "\"" + safeValue.replace("\"", "\"\"") + "\"";
    }

    private String sanitizeFileName(String value) {
        String name = value == null ? "" : value.trim();
        name = name.replaceAll("[\\\\/:*?\"<>|]", "_");
        name = name.replaceAll("\\p{Cntrl}", "_");
        name = name.replaceAll("\\s+", " ").trim();
        return name;
    }
}

