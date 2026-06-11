package model;

public class RejectionReason {
    private String instanceNumber;
    private String reason;

    public RejectionReason(String instanceNumber, String reason) {
        this.instanceNumber = instanceNumber;
        this.reason = reason;
    }

    public String getInstanceNumber() {
        return instanceNumber;
    }

    public void setInstanceNumber(String instanceNumber) {
        this.instanceNumber = instanceNumber;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDisplay() {
        String instance = (instanceNumber == null || instanceNumber.trim().isEmpty())
                ? "" : "Instance " + instanceNumber.trim() + " ";
        return instance + "rejected: " + (reason == null ? "" : reason.trim());
    }

    @Override
    public String toString() {
        return getDisplay();
    }
}
