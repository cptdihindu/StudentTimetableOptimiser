package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Timetable {
    private String timetableName;
    private String semester;
    private ArrayList<TimetableEntry> entries;
    private ArrayList<Preference> preferences;
    private boolean allowLectureOverlap;

    public Timetable() {
        this.entries = new ArrayList<>();
        this.preferences = new ArrayList<>();
    }

    public Timetable(String timetableName, String semester, boolean allowLectureOverlap) {
        this.timetableName = timetableName;
        this.semester = semester;
        this.allowLectureOverlap = allowLectureOverlap;
        this.entries = new ArrayList<>();
        this.preferences = new ArrayList<>();
    }

    public String getTimetableName() {
        return timetableName;
    }

    public void setTimetableName(String timetableName) {
        this.timetableName = timetableName;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public ArrayList<TimetableEntry> getEntries() {
        return entries;
    }

    public void setEntries(ArrayList<TimetableEntry> entries) {
        this.entries = entries == null ? new ArrayList<>() : entries;
    }

    public ArrayList<Preference> getPreferences() {
        return preferences;
    }

    public void setPreferences(ArrayList<Preference> preferences) {
        this.preferences = preferences == null ? new ArrayList<>() : preferences;
    }

    public boolean isAllowLectureOverlap() {
        return allowLectureOverlap;
    }

    public void setAllowLectureOverlap(boolean allowLectureOverlap) {
        this.allowLectureOverlap = allowLectureOverlap;
    }

    public void addEntry(TimetableEntry entry) {
        if (entry != null) {
            entries.add(entry);
        }
    }

    public void removeEntry(TimetableEntry entry) {
        entries.remove(entry);
    }

    public void addPreference(Preference preference) {
        if (preference != null) {
            preferences.add(preference);
        }
    }

    public int getEntryCount() {
        return entries.size();
    }

    public int getTopicCount() {
        Set<String> topics = new HashSet<>();
        for (TimetableEntry entry : entries) {
            if (entry == null || entry.getClassRecord() == null) {
                continue;
            }
            String code = entry.getClassRecord().getTopicCode();
            if (code != null && !code.trim().isEmpty()) {
                topics.add(code.trim().toLowerCase());
            }
        }
        return topics.size();
    }

    public int getDayCount() {
        Set<String> days = new HashSet<>();
        for (TimetableEntry entry : entries) {
            if (entry == null || entry.getClassRecord() == null) {
                continue;
            }
            String day = entry.getClassRecord().getBaseDay();
            if (day != null && !day.trim().isEmpty()) {
                days.add(day.trim().toLowerCase());
            }
        }
        return days.size();
    }

    public String getSummary() {
        String name = timetableName == null || timetableName.trim().isEmpty()
                ? "Unnamed Timetable"
                : timetableName.trim();
        String sem = semester == null || semester.trim().isEmpty()
                ? "Semester ?"
                : "Semester " + semester.trim();
        return name + " | " + sem + " | " + getTopicCount() + " topics | " + getDayCount() + " days";
    }

    public String getTimetableTableDisplay() {
        StringBuilder builder = new StringBuilder();
        builder.append("Timetable Name: ")
                .append(timetableName == null ? "" : timetableName.trim())
                .append("\n");
        builder.append("Semester: ")
                .append(semester == null ? "" : semester.trim())
                .append("\n");
        builder.append("Allow Lecture Overlap: ")
                .append(allowLectureOverlap ? "Yes" : "No")
                .append("\n");

        builder.append("Preferences:");
        if (preferences.isEmpty()) {
            builder.append(" (none)\n");
        } else {
            builder.append("\n");
            for (int i = 0; i < preferences.size(); i++) {
                Preference pref = preferences.get(i);
                builder.append("  ").append(i + 1).append(") ")
                        .append(pref == null ? "" : pref.toString())
                        .append("\n");
            }
        }

        builder.append("\n");
        builder.append("Timetable Entries:\n");
        builder.append("--------------------------------------------------------------------------------\n");

        if (entries.isEmpty()) {
            builder.append("No entries in timetable.\n");
        } else {
            for (int i = 0; i < entries.size(); i++) {
                TimetableEntry entry = entries.get(i);
                if (entry == null || entry.getClassRecord() == null) {
                    continue;
                }
                ClassRecord cr = entry.getClassRecord();
                
                // Format each entry with better readability
                String topic = cr.getTopicCode() == null ? "" : cr.getTopicCode().trim();
                String topicName = cr.getTopicName() == null ? "" : cr.getTopicName().trim();
                String campus = cr.getCampus() == null ? "" : cr.getCampus().trim();
                String classType = cr.getClassType() == null ? "" : cr.getClassType().trim();
                String day = cr.getDay() == null ? "" : cr.getDay().trim();
                String time = "";
                if (cr.getStartTime() != null && cr.getEndTime() != null) {
                    time = cr.getStartTime() + "-" + cr.getEndTime();
                }
                String building = cr.getBuilding() == null ? "" : cr.getBuilding().trim();
                String room = cr.getRoom() == null ? "" : cr.getRoom().trim();
                
                String location = "";
                if (!building.isEmpty() && !room.isEmpty()) {
                    location = building + ", " + room;
                } else if (!building.isEmpty()) {
                    location = building;
                } else if (!room.isEmpty()) {
                    location = room;
                }
                
                builder.append(String.format("%d. %s", i + 1, topic));
                if (!topicName.isEmpty()) {
                    builder.append(" - ").append(topicName);
                }
                builder.append("\n");
                builder.append("   Campus: ").append(campus).append(" | Class Type: ").append(classType).append("\n");
                builder.append("   Day: ").append(day).append(" | Time: ").append(time).append("\n");
                if (!location.isEmpty()) {
                    builder.append("   Location: ").append(location).append("\n");
                }
                builder.append("\n");
            }
        }
        
        return builder.toString();
    }

    private String truncate(String str, int maxLength) {
        if (str == null) {
            return "";
        }
        if (str.length() > maxLength) {
            return str.substring(0, maxLength - 1);
        }
        return str;
    }

    public String getFullDetails() {
        StringBuilder builder = new StringBuilder();
        builder.append("Timetable Name: ")
                .append(timetableName == null ? "" : timetableName.trim())
                .append("\n");
        builder.append("Semester: ")
                .append(semester == null ? "" : semester.trim())
                .append("\n");
        builder.append("Allow Lecture Overlap: ")
                .append(allowLectureOverlap ? "Yes" : "No")
                .append("\n");

        builder.append("Preferences:");
        if (preferences.isEmpty()) {
            builder.append(" (none)\n");
        } else {
            builder.append("\n");
            int index = 1;
            for (Preference preference : preferences) {
                builder.append("  ").append(index).append(") ")
                        .append(preference == null ? "" : preference.toString())
                        .append("\n");
                index++;
            }
        }

        builder.append("Entries:");
        if (entries.isEmpty()) {
            builder.append(" (none)");
        } else {
            builder.append("\n");
            int index = 1;
            for (TimetableEntry entry : entries) {
                builder.append("  ").append(index).append(") ")
                        .append(entry == null ? "" : entry.getSummary())
                        .append("\n");
                index++;
            }
        }

        return builder.toString().trim();
    }

    @Override
    public String toString() {
        return getSummary();
    }
}

