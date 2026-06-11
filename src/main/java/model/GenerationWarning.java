package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GenerationWarning {
    private String topicCode;
    private String classType;
    private ArrayList<RejectionReason> rejectionReasons;
    private Set<String> deduplicationKeys; // Track unique reason combinations

    public GenerationWarning(String topicCode, String classType) {
        this.topicCode = topicCode;
        this.classType = classType;
        this.rejectionReasons = new ArrayList<>();
        this.deduplicationKeys = new HashSet<>();
    }

    public String getTopicCode() {
        return topicCode;
    }

    public void setTopicCode(String topicCode) {
        this.topicCode = topicCode;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public ArrayList<RejectionReason> getRejectionReasons() {
        return rejectionReasons;
    }

    public void setRejectionReasons(ArrayList<RejectionReason> rejectionReasons) {
        this.rejectionReasons = rejectionReasons == null ? new ArrayList<>() : rejectionReasons;
    }

    public void addRejectionReason(RejectionReason reason) {
        if (reason == null) {
            return;
        }
        
        // Create a deduplication key: instance + reason message
        String dedupKey = buildDeduplicationKey(reason);
        
        // Only add if we haven't seen this combination before
        if (!deduplicationKeys.contains(dedupKey)) {
            rejectionReasons.add(reason);
            deduplicationKeys.add(dedupKey);
        }
    }

    public String getSummary() {
        return "Could not fit " + (topicCode == null ? "" : topicCode.trim()) + " "
                + (classType == null ? "class" : classType.trim()) + " into this timetable.";
    }

    private String buildDeduplicationKey(RejectionReason reason) {
        if (reason == null) {
            return "";
        }
        
        // Key is: instance number + reason text
        // This ensures we don't show the same rejection reason twice for the same instance
        String instance = safe(reason.getInstanceNumber());
        String reasonText = safe(reason.getReason());
        
        return instance + "|" + reasonText;
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }

    @Override
    public String toString() {
        return getSummary();
    }
}
