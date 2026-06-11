package model;

public class ValidationSummary {
    private int timeClashCount;
    private int travelTimeIssueCount;
    private int invalidCampusCombinationCount;
    private int missingRequiredClassesCount;
    private String status;

    public ValidationSummary() {
        this.timeClashCount = 0;
        this.travelTimeIssueCount = 0;
        this.invalidCampusCombinationCount = 0;
        this.missingRequiredClassesCount = 0;
        this.status = "Complete timetable";
    }

    public int getTimeClashCount() {
        return timeClashCount;
    }

    public void setTimeClashCount(int timeClashCount) {
        this.timeClashCount = timeClashCount;
    }

    public int getTravelTimeIssueCount() {
        return travelTimeIssueCount;
    }

    public void setTravelTimeIssueCount(int travelTimeIssueCount) {
        this.travelTimeIssueCount = travelTimeIssueCount;
    }

    public int getInvalidCampusCombinationCount() {
        return invalidCampusCombinationCount;
    }

    public void setInvalidCampusCombinationCount(int invalidCampusCombinationCount) {
        this.invalidCampusCombinationCount = invalidCampusCombinationCount;
    }

    public int getMissingRequiredClassesCount() {
        return missingRequiredClassesCount;
    }

    public void setMissingRequiredClassesCount(int missingRequiredClassesCount) {
        this.missingRequiredClassesCount = missingRequiredClassesCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDisplay() {
        StringBuilder builder = new StringBuilder();
        builder.append("Validation Summary:\n");
        builder.append("- Time clashes: ").append(timeClashCount).append("\n");
        builder.append("- Travel-time issues: ").append(travelTimeIssueCount).append("\n");
        builder.append("- Invalid campus combinations: ").append(invalidCampusCombinationCount).append("\n");
        builder.append("- Missing required classes: ").append(missingRequiredClassesCount).append("\n");
        builder.append("- Status: ").append(status);
        return builder.toString();
    }

    @Override
    public String toString() {
        return getDisplay();
    }
}
