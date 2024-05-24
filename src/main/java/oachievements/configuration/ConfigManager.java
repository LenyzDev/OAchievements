package oachievements.configuration;

import oachievements.OAchievements;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.logging.Logger;

public class ConfigManager {

    private ConfigUtils configUtils;
    private HashMap<String, FileConfiguration> configurationMap = new HashMap<>();

    public ConfigManager(OAchievements main, Logger logger){
        configUtils = new ConfigUtils(main, logger);

        logger.info("Loading configurations...");
        configUtils.createConfig("config");
        configUtils.createConfig("quests");
        configurationMap.put("config", configUtils.getConfig("config"));
        logger.info("Configurations loaded.");
    }

    public FileConfiguration getConfig(String config){
        return configurationMap.get(config);
    }

    public FileConfiguration getConfigOutMemory(String config){
        return configUtils.getConfig(config);
    }
}