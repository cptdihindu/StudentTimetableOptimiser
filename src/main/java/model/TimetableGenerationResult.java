package model;

import java.util.ArrayList;

public class TimetableGenerationResult {
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_PARTIAL_SUCCESS = "PARTIAL SUCCESS";
    public static final String STATUS_FAILED = "FAILED";

    private Timetable timetable;
    private String status;
    private String errorMessage;
    private ArrayList<GenerationWarning> generationWarnings;
    private ArrayList<String> missingClasses;
    private ValidationSummary validationSummary;
    private ArrayList<String> suggestions;
    private int candidateRecordsChecked;
    private int selectedRecordsCount;
    private int rejectedDueToClash;
    private int rejectedDueToTravelTime;
    private int rejectedDueToCampusRule;
    private int rejectedDueToSemesterOrFilter;
    private int optimisationScore;
    private String optimisationSummary;

    public TimetableGenerationResult() {
        this.timetable = null;
        this.status = STATUS_FAILED;
        this.errorMessage = "";
        this.generationWarnings = new ArrayList<>();
        this.missingClasses = new ArrayList<>();
        this.validationSummary = new ValidationSummary();
        this.suggestions = new ArrayList<>();
        this.candidateRecordsChecked = 0;
        this.selectedRecordsCount = 0;
        this.rejectedDueToClash = 0;
        this.rejectedDueToTravelTime = 0;
        this.rejectedDueToCampusRule = 0;
        this.rejectedDueToSemesterOrFilter = 0;
        this.optimisationScore = 0;
        this.optimisationSummary = "";
    }

    public Timetable getTimetable() {
        return timetable;
    }

    public void setTimetable(Timetable timetable) {
        this.timetable = timetable;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ArrayList<GenerationWarning> getGenerationWarnings() {
        return generationWarnings;
    }

    public void setGenerationWarnings(ArrayList<GenerationWarning> generationWarnings) {
        this.generationWarnings = generationWarnings == null ? new ArrayList<>() : generationWarnings;
    }

    public void addGenerationWarning(GenerationWarning warning) {
        if (warning != null) {
            generationWarnings.add(warning);
        }
    }

    public ArrayList<String> getMissingClasses() {
        return missingClasses;
    }

    public void setMissingClasses(ArrayList<String> missingClasses) {
        this.missingClasses = missingClasses == null ? new ArrayList<>() : missingClasses;
    }

    public void addMissingClass(String classDescription) {
        if (classDescription != null && !classDescription.trim().isEmpty()) {
            missingClasses.add(classDescription);
        }
    }

    public ValidationSummary getValidationSummary() {
        return validationSummary;
    }

    public void setValidationSummary(ValidationSummary validationSummary) {
        this.validationSummary = validationSummary == null ? new ValidationSummary() : validationSummary;
    }

    public ArrayList<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(ArrayList<String> suggestions) {
        this.suggestions = suggestions == null ? new ArrayList<>() : suggestions;
    }

    public void addSuggestion(String suggestion) {
        if (suggestion != null && !suggestion.trim().isEmpty()) {
            suggestions.add(suggestion);
        }
    }

    public int getCandidateRecordsChecked() {
        return candidateRecordsChecked;
    }

    public void setCandidateRecordsChecked(int candidateRecordsChecked) {
        this.candidateRecordsChecked = candidateRecordsChecked;
    }

    public int getSelectedRecordsCount() {
        return selectedRecordsCount;
    }

    public void setSelectedRecordsCount(int selectedRecordsCount) {
        this.selectedRecordsCount = selectedRecordsCount;
    }

    public int getRejectedDueToClash() {
        return rejectedDueToClash;
    }

    public void setRejectedDueToClash(int rejectedDueToClash) {
        this.rejectedDueToClash = rejectedDueToClash;
    }

    public int getRejectedDueToTravelTime() {
        return rejectedDueToTravelTime;
    }

    public void setRejectedDueToTravelTime(int rejectedDueToTravelTime) {
        this.rejectedDueToTravelTime = rejectedDueToTravelTime;
    }

    public int getRejectedDueToCampusRule() {
        return rejectedDueToCampusRule;
    }

    public void setRejectedDueToCampusRule(int rejectedDueToCampusRule) {
        this.rejectedDueToCampusRule = rejectedDueToCampusRule;
    }

    public int getRejectedDueToSemesterOrFilter() {
        return rejectedDueToSemesterOrFilter;
    }

    public void setRejectedDueToSemesterOrFilter(int rejectedDueToSemesterOrFilter) {
        this.rejectedDueToSemesterOrFilter = rejectedDueToSemesterOrFilter;
    }

    public int getOptimisationScore() {
        return optimisationScore;
    }

    public void setOptimisationScore(int optimisationScore) {
        this.optimisationScore = optimisationScore;
    }

    public String getOptimisationSummary() {
        return optimisationSummary == null ? "" : optimisationSummary;
    }

    public void setOptimisationSummary(String optimisationSummary) {
        this.optimisationSummary = optimisationSummary == null ? "" : optimisationSummary;
    }

    public boolean isSuccessful() {
        return STATUS_SUCCESS.equals(status);
    }

    public boolean isPartialSuccess() {
        return STATUS_PARTIAL_SUCCESS.equals(status);
    }

    public boolean isFailed() {
        return STATUS_FAILED.equals(status);
    }

    public String getGenerationDetailsDisplay() {
        StringBuilder builder = new StringBuilder();
        builder.append("Generation Details:\n");
        builder.append("- Candidate class records checked: ").append(candidateRecordsChecked).append("\n");
        builder.append("- Selected class records: ").append(selectedRecordsCount).append("\n");
        builder.append("- Rejected due to clash: ").append(rejectedDueToClash).append("\n");
        builder.append("- Rejected due to travel time: ").append(rejectedDueToTravelTime).append("\n");
        builder.append("- Rejected due to campus rule: ").append(rejectedDueToCampusRule).append("\n");
        builder.append("- Rejected due to semester/campus filter: ").append(rejectedDueToSemesterOrFilter).append("\n");
        builder.append("- Optimisation score: ").append(optimisationScore);
        if (!getOptimisationSummary().isEmpty()) {
            builder.append("\n").append(optimisationSummary);
        }
        return builder.toString();
    }
}
