package oachievements;

import com.github.fierioziy.particlenativeapi.api.ParticleNativeAPI;
import com.github.fierioziy.particlenativeapi.plugin.ParticleNativePlugin;
import oachievements.commands.AchievementsCommand;
import oachievements.configuration.ConfigManager;
import oachievements.database.DatabaseManager;
import oachievements.database.QuestDAO;
import oachievements.listeners.PlayerListeners;
import oachievements.listeners.QuestListeners;
import oachievements.storages.QuestStorage;
import oachievements.storages.UserStorage;
import opets.commands.PetCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Logger;

public final class OAchievements extends JavaPlugin {

    private Logger logger;
    private ConfigManager configManager;
    private UserStorage userStorage;
    private QuestStorage questStorage;
    private QuestDAO questDAO;
    private ParticleNativeAPI particleNativeAPI;

    @Override
    public void onEnable() {
        this.logger = getLogger();
        this.configManager = new ConfigManager(this, logger);

        this.questStorage = new QuestStorage(configManager.getConfigOutMemory("quests"));

        DatabaseManager.connect(logger, configManager.getConfig("config"));
        this.questDAO = new QuestDAO(questStorage, logger);

        this.userStorage = new UserStorage(questDAO);

        Plugin particlePlugin = this.getServer().getPluginManager().getPlugin("ParticleNativeAPI");
        if (particlePlugin != null) {
            if (!ParticleNativePlugin.isValid()) logger.warning("ParticleNativeAPI is not enabled!");
            particleNativeAPI = ParticleNativePlugin.getAPI();
        }else{
            logger.warning("ParticleNativeAPI is not installed!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(new PlayerListeners(userStorage, questStorage, questDAO), this);
        getServer().getPluginManager().registerEvents(new QuestListeners(userStorage, questStorage, particleNativeAPI), this);
        this.getCommand("achievement").setExecutor(new AchievementsCommand(userStorage, questStorage));
        autoReconnect();
    }

    @Override
    public void onDisable() {
        this.questDAO.unloadAll(this.userStorage);
        DatabaseManager.disconnect(logger);
    }

    private void autoReconnect() {
        new BukkitRunnable() {
            @Override
            public void run() {
                DatabaseManager.reloadConnection(logger, configManager.getConfig("config"));
            }
        }.runTaskTimerAsynchronously(this, 20 * 60 * 60, 20 * 60 * 60);
    }
}
