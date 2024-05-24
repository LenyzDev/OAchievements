package oachievements.listeners;

import oachievements.database.QuestDAO;
import oachievements.objects.User;
import oachievements.storages.QuestStorage;
import oachievements.storages.UserStorage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListeners implements Listener {

    private UserStorage userStorage;
    private QuestStorage questStorage;
    private QuestDAO questDAO;

    public PlayerListeners(UserStorage userStorage, QuestStorage questStorage, QuestDAO questDAO) {
        this.userStorage = userStorage;
        this.questStorage = questStorage;
        this.questDAO = questDAO;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        User user = userStorage.getUser(event.getPlayer().getUniqueId());
        questStorage.createPlayersQuests(event.getPlayer(), user);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if(!userStorage.containsUser(event.getPlayer().getUniqueId())) return;
        User user = userStorage.getUser(event.getPlayer().getUniqueId());
        questDAO.unloadUser(event.getPlayer().getUniqueId(), user);
        userStorage.removeUser(event.getPlayer().getUniqueId());
    }

}
