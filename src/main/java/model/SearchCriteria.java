package model;

public class SearchCriteria {
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
    private String startTime;
    private String endTime;
    private String building;
    private String room;

    public SearchCriteria() {
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
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

    public boolean isEmpty() {
        return isBlank(topicCode)
                && isBlank(topicName)
                && isBlank(attendanceMode)
                && isBlank(campus)
                && isBlank(semester)
                && isBlank(availabilityNumber)
                && isBlank(classType)
                && isBlank(classInstance)
                && isBlank(firstClassDate)
                && isBlank(lastClassDate)
                && isBlank(day)
                && isBlank(startTime)
                && isBlank(endTime)
                && isBlank(building)
                && isBlank(room);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        appendField(builder, "Topic Code", topicCode);
        appendField(builder, "Topic Name", topicName);
        appendField(builder, "Attendance Mode", attendanceMode);
        appendField(builder, "Campus", campus);
        appendField(builder, "Semester", semester);
        appendField(builder, "Availability Number", availabilityNumber);
        appendField(builder, "Class Type", classType);
        appendField(builder, "Class Instance", classInstance);
        appendField(builder, "First Class Date", firstClassDate);
        appendField(builder, "Last Class Date", lastClassDate);
        appendField(builder, "Day", day);
        appendField(builder, "Start Time", startTime);
        appendField(builder, "End Time", endTime);
        appendField(builder, "Building", building);
        appendField(builder, "Room", room);
        return builder.length() == 0 ? "(no criteria)" : builder.toString().trim();
    }

    private void appendField(StringBuilder builder, String label, String value) {
        if (isBlank(value)) {
            return;
        }
        builder.append(label).append(": ").append(value.trim()).append("\n");
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}

