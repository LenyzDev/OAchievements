package oachievements.storages;

import oachievements.enums.QuestType;
import oachievements.enums.QuestVariable;
import oachievements.objects.PlayerQuest;
import oachievements.objects.Quest;
import oachievements.objects.User;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class QuestStorage {

    private HashMap<String, Quest> quests;

    public QuestStorage(FileConfiguration config) {
        this.quests = new HashMap<>();
        for (String questId : config.getConfigurationSection("quests").getKeys(false)) {
            Quest quest = new Quest(
                    questId,
                    config.getString("quests." + questId + ".name"),
                    Material.matchMaterial(config.getString("quests." + questId + ".material")),
                    config.getInt("quests." + questId + ".custom-model-data"),
                    config.getStringList("quests." + questId + ".lore"),
                    QuestType.valueOf(config.getString("quests." + questId + ".type")),
                    QuestVariable.valueOf(config.getString("quests." + questId + ".variable")),
                    config.getDouble("quests." + questId + ".goal"),
                    config.getStringList("quests." + questId + ".commands")
            );
            this.quests.put(questId, quest);
        }
    }

    public void addQuest(String questId, Quest quest) {
        this.quests.put(questId, quest);
    }

    public Quest getQuest(String questId) {
        return this.quests.get(questId);
    }

    public void removeQuest(String questId) {
        this.quests.remove(questId);
    }

    public boolean containsQuest(String questId) {
        return this.quests.containsKey(questId);
    }

    public void createPlayersQuests(Player player, User user) {
        for (Quest quest : this.quests.values()) {
            if(user.containsQuest(quest.getId())) continue;
            String id = quest.getId()+"."+player.getUniqueId();
            user.addQuest(quest.getId(), new PlayerQuest(
                    id,
                    quest.getId(),
                    quest.getGoal()
            ));
        }
    }

    public HashMap<String, Quest> getQuests() {
        return this.quests;
    }
}
