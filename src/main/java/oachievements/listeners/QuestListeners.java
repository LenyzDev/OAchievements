package oachievements.listeners;

import com.github.fierioziy.particlenativeapi.api.ParticleNativeAPI;
import com.lib.color.Colors;
import com.lib.utils.Formatter;
import oachievements.enums.QuestType;
import oachievements.enums.QuestVariable;
import oachievements.objects.PlayerQuest;
import oachievements.objects.Quest;
import oachievements.objects.User;
import oachievements.storages.QuestStorage;
import oachievements.storages.UserStorage;
import ogens.events.BuyGenEvent;
import ogens.events.UpgradeGenEvent;
import omine.events.BreakRegenBlockEvent;
import omine.events.PickaxeLevelUpEvent;
import opets.events.FusePetEvent;
import opets.events.OpenGachaEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import osell.events.SellEvent;
import osell.events.SellWandEvent;

import java.util.ArrayList;

public class QuestListeners implements Listener {

    private UserStorage userStorage;
    private QuestStorage questStorage;
    private ParticleNativeAPI particleNativeAPI;

    public QuestListeners(UserStorage userStorage, QuestStorage questStorage, ParticleNativeAPI particleNativeAPI) {
        this.userStorage = userStorage;
        this.questStorage = questStorage;
        this.particleNativeAPI = particleNativeAPI;
    }

    @EventHandler
    public void onSellEvent(SellEvent event) {
        Player player = event.getPlayer();
        int amount = event.getAmount();
        double value = event.getValue();
        User user = userStorage.getUser(player.getUniqueId());

        ArrayList<PlayerQuest> quests = new ArrayList<>(user.getQuests().values());
        quests.removeIf(PlayerQuest::isCompleted);
        quests.removeIf(playerQuest -> !questStorage.getQuest(playerQuest.getQuestId()).getQuestType().equals(QuestType.SELL_AMOUNT));
        quests.forEach(playerQuest -> {
            Quest quest = questStorage.getQuest(playerQuest.getQuestId());
            if(quest.getQuestVariable().equals(QuestVariable.VALUE)) {
                if (quest.getGoal() <= value) {
                    completeQuest(player, playerQuest, quest);
                } else {
                    playerQuest.setProgress(value);
                }
            }else{
                if (quest.getGoal() <= playerQuest.getProgress() + amount) {
                    completeQuest(player, playerQuest, quest);
                } else {
                    playerQuest.setProgress(playerQuest.getProgress() + amount);
                }
            }
        });
    }

    @EventHandler
    public void onSellWandEvent(SellWandEvent event) {
        Player player = event.getPlayer();
        User user = userStorage.getUser(player.getUniqueId());

        ArrayList<PlayerQuest> quests = new ArrayList<>(user.getQuests().values());
        quests.removeIf(PlayerQuest::isCompleted);
        quests.removeIf(playerQuest -> !questStorage.getQuest(playerQuest.getQuestId()).getQuestType().equals(QuestType.USE_SELLWAND));
        quests.forEach(playerQuest -> {
            Quest quest = questStorage.getQuest(playerQuest.getQuestId());
            if (quest.getGoal() <= playerQuest.getProgress() + 1) {
                completeQuest(player, playerQuest, quest);
            } else {
                playerQuest.setProgress(playerQuest.getProgress() + 1);
            }
        });
    }

    @EventHandler
    public void onOpenGacha(OpenGachaEvent event){
        Player player = event.getPlayer();
        User user = userStorage.getUser(player.getUniqueId());
        int amount = event.getAmount();
        double value = event.getPrice();

        ArrayList<PlayerQuest> quests = new ArrayList<>(user.getQuests().values());
        quests.removeIf(PlayerQuest::isCompleted);
        quests.removeIf(playerQuest -> !questStorage.getQuest(playerQuest.getQuestId()).getQuestType().equals(QuestType.OPEN_GACHA));
        quests.forEach(playerQuest -> {
            Quest quest = questStorage.getQuest(playerQuest.getQuestId());
            if(quest.getQuestVariable().equals(QuestVariable.VALUE)) {
                if (quest.getGoal() <= playerQuest.getProgress() + value) {
                    completeQuest(player, playerQuest, quest);
                } else {
                    playerQuest.setProgress(playerQuest.getProgress() + value);
                }
            }else{
                if (quest.getGoal() <= playerQuest.getProgress() + amount) {
                    completeQuest(player, playerQuest, quest);
                } else {
                    playerQuest.setProgress(playerQuest.getProgress() + amount);
                }
            }
        });
    }

    @EventHandler
    public void onPetFuse(FusePetEvent event){
        Player player = event.getPlayer();
        User user = userStorage.getUser(player.getUniqueId());
        double stars = event.getStars();

        ArrayList<PlayerQuest> quests = new ArrayList<>(user.getQuests().values());
        quests.removeIf(PlayerQuest::isCompleted);
        quests.removeIf(playerQuest -> !questStorage.getQuest(playerQuest.getQuestId()).getQuestType().equals(QuestType.FUSE_PETS));
        quests.forEach(playerQuest -> {
            Quest quest = questStorage.getQuest(playerQuest.getQuestId());
            if(quest.getQuestVariable().equals(QuestVariable.VALUE)) {
                if (quest.getGoal() <= stars) {
                    completeQuest(player, playerQuest, quest);
                } else {
                    playerQuest.setProgress(stars);
                }
            }else{
                if (quest.getGoal() <= playerQuest.getProgress() + 1) {
                    completeQuest(player, playerQuest, quest);
                } else {
                    playerQuest.setProgress(playerQuest.getProgress() + 1);
                }
            }
        });
    }

    @EventHandler
    public void onPickaxeLevelUp(PickaxeLevelUpEvent event){
        Player player = event.getPlayer();
        User user = userStorage.getUser(player.getUniqueId());
        int level = event.getLevel();

        ArrayList<PlayerQuest> quests = new ArrayList<>(user.getQuests().values());
        quests.removeIf(PlayerQuest::isCompleted);
        quests.removeIf(playerQuest -> !questStorage.getQuest(playerQuest.getQuestId()).getQuestType().equals(QuestType.LEVELUP_PICKAXE));
        quests.forEach(playerQuest -> {
            Quest quest = questStorage.getQuest(playerQuest.getQuestId());
            if(quest.getQuestVariable().equals(QuestVariable.VALUE)) {
                if (quest.getGoal() <= level) {
                    completeQuest(player, playerQuest, quest);
                } else {
                    playerQuest.setProgress(level);
                }
            }else{
                if (quest.getGoal() <= playerQuest.getProgress() + 1) {
                    completeQuest(player, playerQuest, quest);
                } else {
                    playerQuest.setProgress(playerQuest.getProgress() + 1);
                }
            }
        });
    }

    @EventHandler
    public void onBreakRegenBlock(BreakRegenBlockEvent event){
        Player player = event.getPlayer();
        User user = userStorage.getUser(player.getUniqueId());

        ArrayList<PlayerQuest> quests = new ArrayList<>(user.getQuests().values());
        quests.removeIf(PlayerQuest::isCompleted);
        quests.removeIf(playerQuest -> !questStorage.getQuest(playerQuest.getQuestId()).getQuestType().equals(QuestType.BREAK_REGEN_BLOCK));
        quests.forEach(playerQuest -> {
            Quest quest = questStorage.getQuest(playerQuest.getQuestId());
            if (quest.getGoal() <= playerQuest.getProgress() + 1) {
                completeQuest(player, playerQuest, quest);
            } else {
                playerQuest.setProgress(playerQuest.getProgress() + 1);
            }
        });
    }

    @EventHandler
    public void onUpgradeGens(UpgradeGenEvent event){
        Player player = event.getPlayer();
        User user = userStorage.getUser(player.getUniqueId());

        ArrayList<PlayerQuest> quests = new ArrayList<>(user.getQuests().values());
        quests.removeIf(PlayerQuest::isCompleted);
        quests.removeIf(playerQuest -> !questStorage.getQuest(playerQuest.getQuestId()).getQuestType().equals(QuestType.UPGRADE_GENS));
        quests.forEach(playerQuest -> {
            Quest quest = questStorage.getQuest(playerQuest.getQuestId());
            if (quest.getGoal() <= playerQuest.getProgress() + 1) {
                completeQuest(player, playerQuest, quest);
            } else {
                playerQuest.setProgress(playerQuest.getProgress() + 1);
            }
        });
    }

    @EventHandler
    public void onBuyGens(BuyGenEvent event){
        Player player = event.getPlayer();
        User user = userStorage.getUser(player.getUniqueId());

        ArrayList<PlayerQuest> quests = new ArrayList<>(user.getQuests().values());
        quests.removeIf(PlayerQuest::isCompleted);
        quests.removeIf(playerQuest -> !questStorage.getQuest(playerQuest.getQuestId()).getQuestType().equals(QuestType.BUY_GENS));
        quests.forEach(playerQuest -> {
            Quest quest = questStorage.getQuest(playerQuest.getQuestId());
            if (quest.getGoal() <= playerQuest.getProgress() + 1) {
                completeQuest(player, playerQuest, quest);
            } else {
                playerQuest.setProgress(playerQuest.getProgress() + 1);
            }
        });
    }

    private void completeQuest(Player player, PlayerQuest playerQuest, Quest quest){
        player.sendTitle("§a§lACHIEVEMENT", "§fYou have §acompleted§f an achievement.", 10, 40, 10);
        player.sendMessage("§f", "§a§lACHIEVEMENT COMPLETED!", "§f", "§fYou have §acompleted§f the achievement §e" + Colors.process(quest.getName()) + "§f.", "§fQuest Goal: §a" + Formatter.formatDecimals(quest.getGoal(), 0, true) + "§8/§a" + Formatter.formatDecimals(quest.getGoal(), 0, true), "§f");
        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
        particleNativeAPI.LIST_1_13.TOTEM_OF_UNDYING.packet(true, player.getLocation().add(0, 1, 0), 1D, 1D, 1D, 100).sendTo(player);
        playerQuest.setProgress(quest.getGoal());
        quest.getCommands().forEach(command -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", player.getName()));
        });
    }

}
