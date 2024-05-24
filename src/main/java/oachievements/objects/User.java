package oachievements.objects;

import java.util.HashMap;

public class User {

    private HashMap<String, PlayerQuest> quests;

    public User(HashMap<String, PlayerQuest> quests) {
        this.quests = quests;
    }

    public void addQuest(String id, PlayerQuest quest) {
        this.quests.put(id, quest);
    }

    public PlayerQuest getQuest(String id) {
        return this.quests.get(id);
    }

    public void removeQuest(String id) {
        this.quests.remove(id);
    }

    public boolean containsQuest(String id) {
        return this.quests.containsKey(id);
    }

    public HashMap<String, PlayerQuest> getQuests() {
        return this.quests;
    }
}
