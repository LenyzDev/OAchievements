package oachievements.commands;

import com.lib.color.Colors;
import oachievements.inventories.AchievementsInventory;
import oachievements.storages.QuestStorage;
import oachievements.storages.UserStorage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AchievementsCommand implements CommandExecutor {

    private UserStorage userStorage;
    private QuestStorage questStorage;

    public AchievementsCommand(UserStorage userStorage, QuestStorage questStorage) {
        this.userStorage = userStorage;
        this.questStorage = questStorage;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Colors.process("&cYou must be a player to execute this command!"));
            return false;
        }
        Player player = (Player) sender;
        AchievementsInventory.getInventory(questStorage, userStorage).open(player);
        return true;
    }
}