package oachievements.inventories;

import com.lib.color.Colors;
import com.lib.items.ItemBuilder;
import com.lib.utils.Formatter;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import oachievements.objects.PlayerQuest;
import oachievements.objects.Quest;
import oachievements.objects.User;
import oachievements.storages.QuestStorage;
import oachievements.storages.UserStorage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AchievementsInventory implements InventoryProvider {

    private QuestStorage questStorage;
    private UserStorage userStorage;

    public AchievementsInventory(QuestStorage questStorage, UserStorage userStorage){
        this.questStorage = questStorage;
        this.userStorage = userStorage;
    }

    public static SmartInventory getInventory(QuestStorage questStorage, UserStorage userStorage){
        return SmartInventory.builder()
                .id("achievementsInventory")
                .provider(new AchievementsInventory(questStorage, userStorage){
                })
                .size(5, 9)
                .title(Colors.process("Achievements"))
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();
        User user = userStorage.getUser(player.getUniqueId());

        List<Quest> quests = new ArrayList<>(questStorage.getQuests().values());
        Collections.sort(quests, Comparator.comparing(Quest::getQuestType));

        ClickableItem[] questsItems = new ClickableItem[quests.size()];
        for (int i = 0; i < quests.size(); i++){
            Quest quest = quests.get(i);
            PlayerQuest playerQuest = user.getQuest(quest.getId());
            if(playerQuest == null) {
                questsItems[i] = ClickableItem.empty(new ItemBuilder(Material.BARRIER)
                        .setDisplayName(Colors.process("&c&lERROR"))
                        .build());
                continue;
            }
            List<String> lore = quest.getLore().stream( ).map(s ->
                    s.replace("{goal}", Formatter.formatDecimals(quest.getGoal(), 0, true))
                            .replace("{progress}", getProgress(playerQuest))
            ).collect(Collectors.toList());
            questsItems[i] = ClickableItem.empty(new ItemBuilder(quest.getMaterial())
                            .setDisplayName(Colors.process(quest.getName()))
                            .setLore(Colors.process(lore))
                            .setCustomModelData(quest.getCustomModelData())
                            .build());
        }

        pagination.setItems(questsItems);
        pagination.setItemsPerPage(15);
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1)
                .blacklist(1, 0)
                .blacklist(1, 1)
                .blacklist(1, 7)
                .blacklist(1, 8)
                .blacklist(2, 0)
                .blacklist(2, 1)
                .blacklist(2, 7)
                .blacklist(2, 8)
                .blacklist(3, 0)
                .blacklist(3, 1)
        );

        if(!pagination.isFirst()){
            contents.set(4, 0, ClickableItem.of(new ItemBuilder(Material.ARROW).setDisplayName("&aPrevious").setCustomModelData(1).build(),
                    e -> {
                        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
                        AchievementsInventory.getInventory(questStorage, userStorage).open(player, pagination.previous().getPage());
                    }));
        }

        if(!pagination.isLast()){
            contents.set(4, 8, ClickableItem.of(new ItemBuilder(Material.ARROW).setDisplayName("&aNext").setCustomModelData(2).build(),
                    e -> {
                        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
                        AchievementsInventory.getInventory(questStorage, userStorage).open(player, pagination.next().getPage());
                    }));
        }
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }

    private String getProgress(PlayerQuest playerQuest){
        if(playerQuest.isCompleted()){
            return "§aᴀᴄʜɪᴇᴠᴇᴍᴇɴᴛ ᴄᴏᴍᴘʟᴇᴛᴇᴅ";
        }else{
            return "§fᴘʀᴏɢʀᴇss: §a" + Formatter.formatDecimals(playerQuest.getProgress(), 0, true) + "§8/§a" + Formatter.formatDecimals(playerQuest.getGoal(), 0, true);
        }
    }
}
