package model;

import java.time.LocalTime;

public class ClassRecord {
    private String topicCode;
    private String topicName;
    private String attendanceMode;
    private String campus;
    private String semester;
    private String availabilityNumber;
    private String classType;
    private String classInstance;
    private String firstClassDate;
    private String lastClassDate;
    private String day;
    private LocalTime startTime;
    private LocalTime endTime;
    private String building;
    private String room;

    public ClassRecord() {
    }

    public ClassRecord(String topicCode, String topicName, String attendanceMode, String campus, String semester,
                       String availabilityNumber, String classType, String classInstance, String firstClassDate,
                       String lastClassDate, String day, LocalTime startTime, LocalTime endTime,
                       String building, String room) {
        this.topicCode = topicCode;
        this.topicName = topicName;
        this.attendanceMode = attendanceMode;
        this.campus = campus;
        this.semester = semester;
        this.availabilityNumber = availabilityNumber;
        this.classType = classType;
        this.classInstance = classInstance;
        this.firstClassDate = firstClassDate;
        this.lastClassDate = lastClassDate;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.building = building;
        this.room = room;
    }

    public String getTopicCode() {
        return topicCode;
    }

    public void setTopicCode(String topicCode) {
        this.topicCode = topicCode;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getAttendanceMode() {
        return attendanceMode;
    }

    public void setAttendanceMode(String attendanceMode) {
        this.attendanceMode = attendanceMode;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getAvailabilityNumber() {
        return availabilityNumber;
    }

    public void setAvailabilityNumber(String availabilityNumber) {
        this.availabilityNumber = availabilityNumber;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getClassInstance() {
        return classInstance;
    }

    public void setClassInstance(String classInstance) {
        this.classInstance = classInstance;
    }

    public String getFirstClassDate() {
        return firstClassDate;
    }

    public void setFirstClassDate(String firstClassDate) {
        this.firstClassDate = firstClassDate;
    }

    public String getLastClassDate() {
        return lastClassDate;
    }

    public void setLastClassDate(String lastClassDate) {
        this.lastClassDate = lastClassDate;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTopicDisplayName() {
        String code = topicCode == null ? "" : topicCode.trim();
        String name = topicName == null ? "" : topicName.trim();
        return (code + " " + name).trim();
    }

    public String getTimeDisplay() {
        if (startTime == null || endTime == null) {
            return "";
        }
        return startTime + " - " + endTime;
    }

    public String getLocationDisplay() {
        String buildingText = building == null ? "" : building.trim();
        String roomText = room == null ? "" : room.trim();
        if (!buildingText.isEmpty() && !roomText.isEmpty()) {
            return buildingText + ", " + roomText;
        }
        if (!buildingText.isEmpty()) {
            return buildingText;
        }
        if (!roomText.isEmpty()) {
            return roomText;
        }
        return "";
    }

    public String getSummary() {
        return getTopicDisplayName() + " | " + safeText(campus) + " | " + safeText(semester) + " | "
                + safeText(classType) + " | " + safeText(classInstance);
    }

    public String getFullDetails() {
        StringBuilder builder = new StringBuilder();
        builder.append("Topic Code: ").append(safeText(topicCode)).append("\n");
        builder.append("Topic Name: ").append(safeText(topicName)).append("\n");
        builder.append("Attendance Mode: ").append(safeText(attendanceMode)).append("\n");
        builder.append("Campus: ").append(safeText(campus)).append("\n");
        builder.append("Semester: ").append(safeText(semester)).append("\n");
        builder.append("Availability Number: ").append(safeText(availabilityNumber)).append("\n");
        builder.append("Class Type: ").append(safeText(classType)).append("\n");
        builder.append("Class Instance: ").append(safeText(classInstance)).append("\n");
        builder.append("First Class Date: ").append(safeText(firstClassDate)).append("\n");
        builder.append("Last Class Date: ").append(safeText(lastClassDate)).append("\n");
        builder.append("Day: ").append(safeText(day)).append("\n");
        builder.append("Time: ").append(getTimeDisplay()).append("\n");
        builder.append("Building: ").append(safeText(building)).append("\n");
        builder.append("Room: ").append(safeText(room));
        return builder.toString();
    }

    public boolean isLecture() {
        return containsIgnoreCase(classType, "lecture");
    }

    public boolean isOnline() {
        return containsIgnoreCase(campus, "online")
                || containsIgnoreCase(building, "online")
                || containsIgnoreCase(room, "online");
    }

    public String getBaseDay() {
        if (day == null || day.trim().isEmpty()) {
            return "";
        }
        String trimmed = day.trim();
        int bracketIndex = trimmed.indexOf('(');
        if (bracketIndex > 0) {
            return trimmed.substring(0, bracketIndex).trim();
        }
        return trimmed;
    }

    public boolean hasSameIdentity(ClassRecord other) {
        if (other == null) {
            return false;
        }
        return equalsIgnoreCaseTrim(topicCode, other.topicCode)
                && equalsIgnoreCaseTrim(topicName, other.topicName)
                && equalsIgnoreCaseTrim(attendanceMode, other.attendanceMode)
                && equalsIgnoreCaseTrim(campus, other.campus)
                && equalsIgnoreCaseTrim(semester, other.semester)
                && equalsIgnoreCaseTrim(availabilityNumber, other.availabilityNumber)
                && equalsIgnoreCaseTrim(classType, other.classType)
                && equalsIgnoreCaseTrim(classInstance, other.classInstance)
                && equalsIgnoreCaseTrim(firstClassDate, other.firstClassDate)
                && equalsIgnoreCaseTrim(lastClassDate, other.lastClassDate)
                && equalsIgnoreCaseTrim(day, other.day);
    }

    public void updateTimeAndLocationFrom(ClassRecord other) {
        if (other == null) {
            return;
        }
        this.startTime = other.startTime;
        this.endTime = other.endTime;
        this.building = other.building;
        this.room = other.room;
    }

    @Override
    public String toString() {
        return getSummary();
    }

    private boolean equalsIgnoreCaseTrim(String a, String b) {
        String left = a == null ? "" : a.trim();
        String right = b == null ? "" : b.trim();
        return left.equalsIgnoreCase(right);
    }

    private boolean containsIgnoreCase(String value, String token) {
        if (value == null || token == null) {
            return false;
        }
        return value.toLowerCase().contains(token.toLowerCase());
    }

    private String safeText(String value) {
        return value == null ? "" : value.trim();
    }
}

