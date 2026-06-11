package model;

public class Topic {
    private String topicCode;
    private String topicName;

    public Topic() {
    }

    public Topic(String topicCode, String topicName) {
        this.topicCode = topicCode;
        this.topicName = topicName;
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

    public String getDisplayName() {
        String code = topicCode == null ? "" : topicCode.trim();
        String name = topicName == null ? "" : topicName.trim();
        return (code + " " + name).trim();
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}

