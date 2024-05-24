package oachievements.objects;

public class PlayerQuest {

    private String id; // ID = QuestID.PlayerUUID
    private String questId;
    private double goal;
    private double progress;

    public PlayerQuest(String id, String questId, double goal) {
        this.id = id;
        this.questId = questId;
        this.goal = goal;
        this.progress = 0;
    }

    public PlayerQuest(String id, String questId, double goal, double progress) {
        this.id = id;
        this.questId = questId;
        this.goal = goal;
        this.progress = progress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestId() {
        return questId;
    }

    public void setQuestId(String questId) {
        this.questId = questId;
    }

    public double getGoal() {
        return goal;
    }

    public void setGoal(double goal) {
        this.goal = goal;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public boolean isCompleted() {
        return progress >= goal;
    }
}
