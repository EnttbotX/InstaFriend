package x.Entt.InstaFriend.Utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import x.Entt.InstaFriend.IF;

import java.io.File;

public class FileHandler {

    private final IF plugin;
    private FileConfiguration config;
    private File configFile;

    public FileHandler(IF plugin) {
        this.plugin = plugin;
        initializeConfig();
    }

    private void initializeConfig() {
        configFile = new File(plugin.getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public FileConfiguration getConfig() {
        if (config == null) {
            reloadConfig();
        }
        return config;
    }

    public void reloadConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), "config.yml");
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public String getMessage(String path, String defaultValue) {
        return config.getString(path, defaultValue).replace("&", "§");
    }
}