package x.Entt.InstaFriend;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import x.Entt.InstaFriend.CMDs.CMDs;
import x.Entt.InstaFriend.Events.Events;
import x.Entt.InstaFriend.Utils.*;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class IF extends JavaPlugin {
    public String version = getDescription().getVersion();
    public static String prefix;
    public static Economy econ;
    private FileHandler fh;
    int bStatsID = 24029;

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        Metrics metrics = new Metrics(this, bStatsID);
        Updater updater = new Updater(this, 121036);

        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveResource("config.yml", false);
        }

        saveDefaultConfig();

        fh = new FileHandler(this);

        prefix = getConfig().getString("prefix", "&d&l{InstaFriend} > ");

        if(fh.getConfig().getBoolean("check-updates", true)) {
            try {
                searchUpdates();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PAPI(this).register();
            logToConsole("&aPlaceholderAPI support enabled!");
        } else {
            logToConsole("&cPlaceholderAPI not found. Placeholders won't work.");
        }

        if (getConfig().getBoolean("vault.enabled", true) && !setupEconomy()) {
            getLogger().severe("Vault dependency not found, disabling plugin!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        registerEvents();
        registerCommands();

        logToConsole("&aPlugin v" + version + " enabled!");
    }

    @Override
    public void onDisable() {
        logToConsole("&cPlugin disabled.");
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new Events(this), this);
        getServer().getPluginManager().registerEvents(new Events(this), this);
    }

    private void registerCommands() {
        if (getCommand("if") != null) {
            Objects.requireNonNull(getCommand("if")).setExecutor(new CMDs(this));
        } else {
            logToConsole("&cCommand '/if' is not defined in plugin.yml.");
        }
    }

    public void searchUpdates() throws IOException {
        String downloadUrl = "https://www.spigotmc.org/resources/instafriend-1-8-1-21.121036/";
        TextComponent link = new TextComponent(MSG.color("&e&lDownload at: &f" + downloadUrl));
        link.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, downloadUrl));

        for (Player player : Bukkit.getOnlinePlayers()) {
            if(player.hasPermission("atg.admin")) {
                if (Updater.isUpdateAvailable()) {
                    logToConsole("&2&l============= " + prefix + "&2&l=============");
                    logToConsole("&6&lNEW VERSION AVAILABLE!");
                    logToConsole("&e&lCurrent Version: &f" + version);
                    logToConsole("&e&lLatest Version: &f" + Updater.getLatestVersion());
                    logToConsole("&2&============= " + prefix + "&2&l=============");
                }
            }
        }
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> provider = getServer().getServicesManager().getRegistration(Economy.class);
        econ = (provider != null) ? provider.getProvider() : null;

        if (econ == null) {
            logToConsole("&cEconomyProvider is null");
        }
        return econ != null;
    }

    public void logToConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(MSG.color(prefix + message));
    }

    public FileHandler getFh() {
        return fh;
    }

    public String getVersion() {
        return version;
    }
}