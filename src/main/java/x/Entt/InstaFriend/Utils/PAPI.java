package x.Entt.InstaFriend.Utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.entity.Player;
import org.bukkit.Location;

import org.jetbrains.annotations.NotNull;
import x.Entt.InstaFriend.IF;

import java.util.Objects;

public class PAPI extends PlaceholderExpansion {

    private final IF plugin;

    public PAPI(IF plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "if";
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null) {
            return "";
        }

        if (identifier.equals("wolf_name")) {
            String wolfName = plugin.getConfig().getString("dog-name", player.getName() + "'s dog")
                    .replace("%player_name%", player.getName());
            return MSG.color(wolfName);
        }

        return null;
    }
}