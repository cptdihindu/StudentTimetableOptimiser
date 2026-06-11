package service;

import model.ClassRecord;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ValidationService {
    public boolean hasTimeClash(ClassRecord a, ClassRecord b, boolean allowLectureOverlap) {
        if (a == null || b == null) {
            return false;
        }
        if (!isSameBaseDay(a, b)) {
            return false;
        }
        if (allowLectureOverlap && (a.isLecture() || b.isLecture())) {
            return false;
        }
        if (a.getStartTime() == null || a.getEndTime() == null || b.getStartTime() == null || b.getEndTime() == null) {
            return false;
        }

        return a.getStartTime().isBefore(b.getEndTime()) && b.getStartTime().isBefore(a.getEndTime());
    }

    public boolean hasAnyTimeClash(ArrayList<ClassRecord> records, boolean allowLectureOverlap) {
        if (records == null) {
            return false;
        }

        for (int i = 0; i < records.size(); i++) {
            for (int j = i + 1; j < records.size(); j++) {
                if (hasTimeClash(records.get(i), records.get(j), allowLectureOverlap)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean hasEnoughTravelTime(ClassRecord a, ClassRecord b, int requiredMinutes) {
        if (a == null || b == null) {
            return true;
        }
        if (requiredMinutes <= 0) {
            return true;
        }
        if (!isSameBaseDay(a, b)) {
            return true;
        }
        if (a.isOnline() || b.isOnline()) {
            return true;
        }
        if (sameCampus(a, b)) {
            return true;
        }

        LocalTime aStart = a.getStartTime();
        LocalTime aEnd = a.getEndTime();
        LocalTime bStart = b.getStartTime();
        LocalTime bEnd = b.getEndTime();

        if (aStart == null || aEnd == null || bStart == null || bEnd == null) {
            return true;
        }

        if (!aEnd.isAfter(bStart)) {
            long gap = minutesBetween(aEnd, bStart);
            return gap >= requiredMinutes;
        }
        if (!bEnd.isAfter(aStart)) {
            long gap = minutesBetween(bEnd, aStart);
            return gap >= requiredMinutes;
        }

        return false;
    }

    public boolean hasAnyTravelTimeIssue(ArrayList<ClassRecord> records, int requiredMinutes) {
        if (records == null) {
            return false;
        }

        for (int i = 0; i < records.size(); i++) {
            for (int j = i + 1; j < records.size(); j++) {
                if (!hasEnoughTravelTime(records.get(i), records.get(j), requiredMinutes)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isValidCampusMixForSameTopic(ArrayList<ClassRecord> records) {
        return getInvalidCampusMixTopics(records).isEmpty();
    }

    public boolean isTimetableValid(ArrayList<ClassRecord> records,
                                    boolean allowLectureOverlap,
                                    int requiredTravelMinutes) {
        if (hasAnyTimeClash(records, allowLectureOverlap)) {
            return false;
        }
        if (hasAnyTravelTimeIssue(records, requiredTravelMinutes)) {
            return false;
        }
        return isValidCampusMixForSameTopic(records);
    }

    public ArrayList<String> getValidationWarnings(ArrayList<ClassRecord> records,
                                                   boolean allowLectureOverlap,
                                                   int requiredTravelMinutes) {
        ArrayList<String> warnings = new ArrayList<>();
        if (records == null || records.isEmpty()) {
            return warnings;
        }

        for (int i = 0; i < records.size(); i++) {
            for (int j = i + 1; j < records.size(); j++) {
                ClassRecord a = records.get(i);
                ClassRecord b = records.get(j);

                if (hasTimeClash(a, b, allowLectureOverlap)) {
                    warnings.add("Time clash: " + getRecordShortName(a)
                            + " overlaps with " + getRecordShortName(b));
                }

                if (!hasEnoughTravelTime(a, b, requiredTravelMinutes)) {
                    String travelMessage = buildTravelWarning(a, b, requiredTravelMinutes);
                    if (!isBlank(travelMessage)) {
                        warnings.add(travelMessage);
                    }
                }
            }
        }

        for (String topic : getInvalidCampusMixTopics(records)) {
            warnings.add("Campus rule issue: Topic " + topic
                    + " mixes Flinders City Campus with Bedford Park or Tonsley.");
        }

        return warnings;
    }

    private boolean isSameBaseDay(ClassRecord a, ClassRecord b) {
        if (a == null || b == null) {
            return false;
        }
        String dayA = safe(a.getBaseDay()).toLowerCase();
        String dayB = safe(b.getBaseDay()).toLowerCase();
        if (dayA.isEmpty() || dayB.isEmpty()) {
            return false;
        }
        return dayA.equals(dayB);
    }

    private boolean sameCampus(ClassRecord a, ClassRecord b) {
        String campusA = safe(a == null ? null : a.getCampus());
        String campusB = safe(b == null ? null : b.getCampus());
        if (isBlank(campusA) || isBlank(campusB)) {
            return true;
        }
        return campusA.equalsIgnoreCase(campusB);
    }

    private long minutesBetween(LocalTime end, LocalTime start) {
        if (end == null || start == null) {
            return 0;
        }
        return Duration.between(end, start).toMinutes();
    }

    private String buildTravelWarning(ClassRecord a, ClassRecord b, int requiredMinutes) {
        if (a == null || b == null) {
            return "";
        }
        if (!isSameBaseDay(a, b)) {
            return "";
        }
        if (a.isOnline() || b.isOnline()) {
            return "";
        }
        if (sameCampus(a, b)) {
            return "";
        }

        LocalTime aStart = a.getStartTime();
        LocalTime aEnd = a.getEndTime();
        LocalTime bStart = b.getStartTime();
        LocalTime bEnd = b.getEndTime();

        if (aStart == null || aEnd == null || bStart == null || bEnd == null) {
            return "";
        }

        ClassRecord first = a;
        ClassRecord second = b;
        LocalTime firstEnd = aEnd;
        LocalTime secondStart = bStart;

        if (!aEnd.isAfter(bStart)) {
            first = a;
            second = b;
            firstEnd = aEnd;
            secondStart = bStart;
        } else if (!bEnd.isAfter(aStart)) {
            first = b;
            second = a;
            firstEnd = bEnd;
            secondStart = aStart;
        }

        String campusA = safe(first.getCampus());
        String campusB = safe(second.getCampus());
        String endText = firstEnd.toString();
        String startText = secondStart.toString();

        return "Travel issue: " + campusA + " class ending at " + endText
                + " and " + campusB + " class starting at " + startText
                + " has less than " + requiredMinutes + " minutes travel time.";
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String getRecordShortName(ClassRecord record) {
        if (record == null) {
            return "(unknown class)";
        }

        StringBuilder builder = new StringBuilder();
        String topic = safe(record.getTopicCode());
        String classType = safe(record.getClassType());
        String day = safe(record.getDay());
        String time = safe(record.getTimeDisplay());

        builder.append(topic.isEmpty() ? "Unknown topic" : topic);
        if (!classType.isEmpty()) {
            builder.append(" ").append(classType);
        }
        if (!day.isEmpty()) {
            builder.append(" ").append(day);
        }
        if (!time.isEmpty()) {
            builder.append(" ").append(time);
        }

        return builder.toString().trim();
    }

    private Set<String> getInvalidCampusMixTopics(ArrayList<ClassRecord> records) {
        Set<String> invalidTopics = new HashSet<>();
        if (records == null) {
            return invalidTopics;
        }

        Map<String, Set<String>> campusByTopic = new HashMap<>();
        for (ClassRecord record : records) {
            if (record == null) {
                continue;
            }
            if (record.isOnline()) {
                continue;
            }

            String topicCode = safe(record.getTopicCode());
            if (topicCode.isEmpty()) {
                continue;
            }

            String campusKey = getPhysicalCampusKey(record.getCampus());
            if (campusKey.isEmpty()) {
                continue;
            }

            Set<String> campuses = campusByTopic.computeIfAbsent(topicCode, key -> new HashSet<>());
            campuses.add(campusKey);
        }

        for (Map.Entry<String, Set<String>> entry : campusByTopic.entrySet()) {
            Set<String> campuses = entry.getValue();
            if (campuses.contains("CITY") && (campuses.contains("BEDFORD") || campuses.contains("TONSLEY"))) {
                invalidTopics.add(entry.getKey());
            }
        }

        return invalidTopics;
    }

    private String getPhysicalCampusKey(String campus) {
        String value = safe(campus).toLowerCase();
        if (value.contains("flinders city")) {
            return "CITY";
        }
        if (value.contains("bedford park")) {
            return "BEDFORD";
        }
        if (value.contains("tonsley")) {
            return "TONSLEY";
        }
        return "";
    }

    // Method to check why a record would be rejected with the selected records
    public String getDetailedRejectionReason(ClassRecord candidate,
                                             ArrayList<ClassRecord> selectedRecords,
                                             boolean allowLectureOverlap,
                                             int requiredTravelMinutes,
                                             boolean candidateSemesterMatches,
                                             boolean candidateCampusFilterMatches) {
        if (candidate == null) {
            return "invalid or missing time/day data";
        }

        // Check time clash
        for (ClassRecord selected : selectedRecords) {
            if (selected == null) {
                continue;
            }
            if (hasTimeClash(candidate, selected, allowLectureOverlap)) {
                String selectedName = getRecordShortName(selected);
                return "time clash with " + selectedName;
            }
        }

        // Check travel time
        for (ClassRecord selected : selectedRecords) {
            if (selected == null) {
                continue;
            }
            if (!hasEnoughTravelTime(candidate, selected, requiredTravelMinutes)) {
                long actualGap = calculateGapMinutes(candidate, selected);
                String candidateCampus = safe(candidate.getCampus());
                String selectedCampus = safe(selected.getCampus());
                return "not enough travel time from " + selectedCampus + " to " + candidateCampus
                        + ". Gap: " + actualGap + " minutes. Required: " + requiredTravelMinutes + " minutes";
            }
        }

        // Check campus mix for same topic
        for (ClassRecord selected : selectedRecords) {
            if (selected == null) {
                continue;
            }
            if (!sameText(candidate.getTopicCode(), selected.getTopicCode())) {
                continue;
            }
            if (candidate.isOnline() || selected.isOnline()) {
                continue;
            }

            String candidateCampusKey = getPhysicalCampusKey(candidate.getCampus());
            String selectedCampusKey = getPhysicalCampusKey(selected.getCampus());
            
            if (candidateCampusKey.isEmpty() || selectedCampusKey.isEmpty()) {
                continue;
            }

            if (candidateCampusKey.equals("CITY") && (selectedCampusKey.equals("BEDFORD") || selectedCampusKey.equals("TONSLEY"))) {
                String readableSelected = getReadableCampusName(selectedCampusKey);
                return "invalid campus combination. This topic already uses " + readableSelected
                        + ", but this class is at Flinders City Campus.";
            }
            if ((candidateCampusKey.equals("BEDFORD") || candidateCampusKey.equals("TONSLEY")) && selectedCampusKey.equals("CITY")) {
                String readableCandidate = getReadableCampusName(candidateCampusKey);
                return "invalid campus combination. This topic already uses Flinders City Campus, but this class is at " + readableCandidate + ".";
            }
        }

        // Check semester match
        if (!candidateSemesterMatches) {
            return "does not match selected semester";
        }

        // Check campus filter match
        if (!candidateCampusFilterMatches) {
            return "does not match selected campus filter";
        }

        return "unknown reason";
    }

    private long calculateGapMinutes(ClassRecord a, ClassRecord b) {
        LocalTime aStart = a.getStartTime();
        LocalTime aEnd = a.getEndTime();
        LocalTime bStart = b.getStartTime();
        LocalTime bEnd = b.getEndTime();

        if (aStart == null || aEnd == null || bStart == null || bEnd == null) {
            return 0;
        }

        if (!aEnd.isAfter(bStart)) {
            return minutesBetween(aEnd, bStart);
        }
        if (!bEnd.isAfter(aStart)) {
            return minutesBetween(bEnd, aStart);
        }

        return 0;
    }

    private boolean sameText(String a, String b) {
        String left = safe(a);
        String right = safe(b);
        if (left.isEmpty() || right.isEmpty()) {
            return false;
        }
        return left.equalsIgnoreCase(right);
    }

    private String getReadableCampusName(String campusKey) {
        if (campusKey == null) {
            return "Unknown campus";
        }
        switch (campusKey.toUpperCase()) {
            case "CITY":
                return "Flinders City Campus";
            case "BEDFORD":
                return "Bedford Park";
            case "TONSLEY":
                return "Tonsley";
            default:
                return campusKey;
        }
    }
}

