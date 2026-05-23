package com.blisssmp.commands;

import com.blisssmp.BlissSMP;
import com.blisssmp.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MsgCommand implements CommandExecutor {

    private final BlissSMP plugin;
    private final String prefix;
    private final Map<UUID, UUID> lastMessagedBy = new HashMap<>();

    public MsgCommand(BlissSMP plugin) {
        this.plugin = plugin;
        this.prefix = plugin.getConfig().getString("prefix", "&8[&bBliss&3SMP&8] ");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) { sender.sendMessage("Only players can use this."); return true; }
        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("reply")) {
            UUID lastUUID = lastMessagedBy.get(player.getUniqueId());
            if (lastUUID == null) {
                Utils.msg(player, prefix + "&cYou have nobody to reply to.");
                return true;
            }
            Player target = plugin.getServer().getPlayer(lastUUID);
            if (target == null || !target.isOnline()) {
                Utils.msg(player, prefix + "&cThat player is no longer online.");
                return true;
            }
            if (args.length == 0) {
                Utils.msg(player, prefix + "&cUsage: /reply <message>");
                return true;
            }
            sendMessage(player, target, args, 0);
            return true;
        }

        // /msg
        if (args.length < 2) {
            Utils.msg(player, prefix + "&cUsage: /msg <player> <message>");
            return true;
        }
        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            Utils.msg(player, prefix + "&cPlayer &e" + args[0] + " &cis not online.");
            return true;
        }
        if (target.equals(player)) {
            Utils.msg(player, prefix + "&cYou can't message yourself!");
            return true;
        }
        sendMessage(player, target, args, 1);
        return true;
    }

    private void sendMessage(Player from, Player to, String[] args, int startIndex) {
        StringBuilder sb = new StringBuilder();
        for (int i = startIndex; i < args.length; i++) {
            if (i > startIndex) sb.append(" ");
            sb.append(args[i]);
        }
        String message = sb.toString();

        from.sendMessage(Utils.color("&7[&fMe &7→ &f" + to.getDisplayName() + "&7] &f" + message));
        to.sendMessage(Utils.color("&7[&f" + from.getDisplayName() + " &7→ &fMe&7] &f" + message));

        // Track for /reply
        lastMessagedBy.put(to.getUniqueId(), from.getUniqueId());
        lastMessagedBy.put(from.getUniqueId(), to.getUniqueId());

        // Log to console
        plugin.getLogger().info("[MSG] " + from.getName() + " -> " + to.getName() + ": " + message);
    }
}
