package model;

public class Preference {
    private String preferenceName;
    private int ranking;

    public Preference() {
    }

    public Preference(String preferenceName, int ranking) {
        this.preferenceName = preferenceName;
        this.ranking = ranking;
    }

    public String getPreferenceName() {
        return preferenceName;
    }

    public void setPreferenceName(String preferenceName) {
        this.preferenceName = preferenceName;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    @Override
    public String toString() {
        String name = preferenceName == null ? "" : preferenceName.trim();
        return ranking + ". " + name;
    }
}

