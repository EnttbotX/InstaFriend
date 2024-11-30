package x.Entt.InstaFriend.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import x.Entt.InstaFriend.IF;
import x.Entt.InstaFriend.Utils.MSG;
import x.Entt.InstaFriend.Utils.Updater;

import java.io.IOException;

import static x.Entt.InstaFriend.IF.prefix;

public class AEvents implements Listener {
    private IF plugin;

    public AEvents(IF plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void OnJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if(player.hasPermission("if.admin") && Updater.isUpdateAvailable()) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                sendMSG("&2&l============= " + prefix + "&2&l=============");
                sendMSG("&6&lNEW VERSION AVAILABLE!");
                sendMSG("&e&lCurrent Version: &f" + plugin.getVersion());

                try {
                    sendMSG("&e&lLatest Version: &f" + Updater.getLatestVersion());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                sendMSG("&2&============= " + prefix + "&2&l=============");
            }, 80L);
        }
    }

    private void sendMSG(String message) {
        Bukkit.getConsoleSender().sendMessage(MSG.color(prefix + message));
    }
}
