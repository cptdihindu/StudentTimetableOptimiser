package model;

public class Availability {
    private String attendanceMode;
    private String campus;
    private String semester;
    private String availabilityNumber;

    public Availability() {
    }

    public Availability(String attendanceMode, String campus, String semester, String availabilityNumber) {
        this.attendanceMode = attendanceMode;
        this.campus = campus;
        this.semester = semester;
        this.availabilityNumber = availabilityNumber;
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

    public String getDisplayText() {
        String mode = attendanceMode == null ? "" : attendanceMode.trim();
        String camp = campus == null ? "" : campus.trim();
        String sem = semester == null ? "" : semester.trim();
        String number = availabilityNumber == null ? "" : availabilityNumber.trim();
        return (mode + " - " + camp + " - " + sem + " - " + number).trim();
    }

    @Override
    public String toString() {
        return getDisplayText();
    }
}

