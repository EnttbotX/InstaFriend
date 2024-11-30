package x.Entt.InstaFriend.Events;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import x.Entt.InstaFriend.IF;
import x.Entt.InstaFriend.Utils.FileHandler;
import x.Entt.InstaFriend.Utils.MSG;

import java.util.Objects;

import static x.Entt.InstaFriend.IF.econ;

public class Events implements Listener {
    private final IF plugin;

    public Events(IF plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        FileHandler fh = plugin.getFh();
        if (fh == null) return;

        FileConfiguration config = fh.getConfig();
        if (config == null) return;

        if (!(event.getEntity() instanceof Wolf wolf)) return;

        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.SPAWNER_EGG || wolf.getOwner() != null) return;

        Player nearbyPlayer = event.getEntity().getWorld()
                .getNearbyEntities(event.getLocation(), 5, 5, 5).stream()
                .filter(entity -> entity instanceof Player)
                .map(entity -> (Player) entity)
                .findFirst()
                .orElse(null);

        if (nearbyPlayer == null) return;

        if (!nearbyPlayer.hasPermission("if.use")) {
            nearbyPlayer.sendMessage(MSG.color(IF.prefix + "&cYou don't have permission to tame this wolf!"));
            return;
        }

        if (!config.getBoolean("vault.enabled", true)) return;

        double spawnCost = config.getDouble("vault.spawn-cost", 100);
        if (econ.getBalance(nearbyPlayer) < spawnCost) {
            nearbyPlayer.sendMessage(MSG.color(IF.prefix + "&cYou don't have enough money to tame this wolf!"));
            return;
        }

        econ.withdrawPlayer(nearbyPlayer, spawnCost);

        wolf.setTamed(true);
        wolf.setOwner(nearbyPlayer);

        String wolfName = config.getString("dog-name", nearbyPlayer.getName() + "'s dog")
                .replace("%player_name%", nearbyPlayer.getName());

        wolf.setCustomName(MSG.color(wolfName));
        wolf.setCustomNameVisible(true);

        if (config.getBoolean("tame-message.enabled", true)) {
            for (String line : config.getStringList("tame-message.message")) {
                String formattedMessage = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(
                        nearbyPlayer,
                        line.replace("%wolf_name%", Objects.requireNonNull(wolf.getCustomName()))
                );
                nearbyPlayer.sendMessage(MSG.color(IF.prefix + formattedMessage));
            }
        }
    }
}