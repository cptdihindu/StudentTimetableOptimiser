package model;

public class TimetableEntry {
    private ClassRecord classRecord;

    public TimetableEntry() {
    }

    public TimetableEntry(ClassRecord classRecord) {
        this.classRecord = classRecord;
    }

    public ClassRecord getClassRecord() {
        return classRecord;
    }

    public void setClassRecord(ClassRecord classRecord) {
        this.classRecord = classRecord;
    }

    public String getSummary() {
        if (classRecord == null) {
            return "No class selected";
        }
        return classRecord.getSummary();
    }

    @Override
    public String toString() {
        return getSummary();
    }
}

