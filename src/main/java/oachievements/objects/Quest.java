package oachievements.objects;

import oachievements.enums.QuestType;
import oachievements.enums.QuestVariable;
import org.bukkit.Material;

import java.util.List;

public class Quest {

    private String id;
    private String name;
    private Material material;
    private int customModelData;
    private List<String> lore;
    private QuestType questType;
    private QuestVariable questVariable;
    private double goal;
    private List<String> commands;

    public Quest(String id, String name, Material material, int customModelData, List<String> lore, QuestType questType, QuestVariable questVariable, double goal, List<String> commands) {
        this.id = id;
        this.name = name;
        this.material = material;
        this.customModelData = customModelData;
        this.lore = lore;
        this.questType = questType;
        this.questVariable = questVariable;
        this.goal = goal;
        this.commands = commands;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public void setCustomModelData(int customModelData) {
        this.customModelData = customModelData;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public QuestType getQuestType() {
        return questType;
    }

    public void setQuestType(QuestType questType) {
        this.questType = questType;
    }

    public QuestVariable getQuestVariable() {
        return questVariable;
    }

    public void setQuestVariable(QuestVariable questVariable) {
        this.questVariable = questVariable;
    }

    public double getGoal() {
        return goal;
    }

    public void setGoal(double goal) {
        this.goal = goal;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }
}
