package x.Entt.InstaFriend.CMDs;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;

import x.Entt.InstaFriend.IF;
import x.Entt.InstaFriend.Utils.FileHandler;
import x.Entt.InstaFriend.Utils.MSG;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static x.Entt.InstaFriend.IF.prefix;

public class CMDs implements CommandExecutor, TabCompleter {
    private final IF plugin;

    public CMDs(IF plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        FileHandler fh = plugin.getFh();
        FileConfiguration config = fh.getConfig();

        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }

        if (!player.hasPermission("if.admin")) {
            String noPermMessage = config.getString("messages.no-perms", "&cYou don't have permissions to do that!");
            player.sendMessage(MSG.color(PlaceholderAPI.setPlaceholders(player, noPermMessage)));
            return false;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            fh.reloadConfig();
            player.sendMessage(MSG.color(prefix + "Config reloaded!"));
            return true;
        }

        player.sendMessage(MSG.color(prefix + "&cInvalid command usage!"));
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (sender instanceof Player player && player.hasPermission("if.admin")) {
            if (args.length == 1) {
                List<String> completions = new ArrayList<>();
                completions.add("reload");
                return completions;
            }
        }

        return Collections.emptyList();
    }
}