package com.blisssmp.commands;

import com.blisssmp.BlissSMP;
import com.blisssmp.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NickCommand implements CommandExecutor {

    private final BlissSMP plugin;
    private final String prefix;

    public NickCommand(BlissSMP plugin) {
        this.plugin = plugin;
        this.prefix = plugin.getConfig().getString("prefix", "&8[&bBliss&3SMP&8] ");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) { sender.sendMessage("Only players can use this."); return true; }
        Player player = (Player) sender;

        if (args.length == 0) {
            Utils.msg(player, prefix + "&cUsage: /nick <nickname|off>");
            return true;
        }

        if (args[0].equalsIgnoreCase("off")) {
            plugin.getNickManager().removeNick(player);
            Utils.msg(player, prefix + "&aYour nickname has been removed.");
            return true;
        }

        String nick = String.join(" ", args);
        // Strip color codes for length check
        String stripped = nick.replaceAll("&[0-9a-fA-FlLkKmMnNoOrR]", "");
        if (stripped.length() > 16) {
            Utils.msg(player, prefix + "&cNickname too long! Max 16 characters (excluding color codes).");
            return true;
        }
        if (stripped.length() < 2) {
            Utils.msg(player, prefix + "&cNickname must be at least 2 characters.");
            return true;
        }

        plugin.getNickManager().setNick(player, nick);
        Utils.msg(player, prefix + "&aYour nickname is now: " + Utils.color(nick));
        return true;
    }
}
