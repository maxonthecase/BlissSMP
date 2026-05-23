package com.blisssmp.commands;

import com.blisssmp.BlissSMP;
import com.blisssmp.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TpaCommand implements CommandExecutor {

    private final BlissSMP plugin;
    private final String prefix;

    public TpaCommand(BlissSMP plugin) {
        this.plugin = plugin;
        this.prefix = plugin.getConfig().getString("prefix", "&8[&bBliss&3SMP&8] ");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }
        Player player = (Player) sender;

        switch (cmd.getName().toLowerCase()) {

            case "tpa": {
                if (args.length == 0) {
                    Utils.msg(player, prefix + "&cUsage: /tpa <player>");
                    return true;
                }
                Player target = plugin.getServer().getPlayer(args[0]);
                if (target == null || !target.isOnline()) {
                    Utils.msg(player, prefix + "&cPlayer &e" + args[0] + " &cis not online.");
                    return true;
                }
                if (target.equals(player)) {
                    Utils.msg(player, prefix + "&cYou can't TPA to yourself!");
                    return true;
                }
                plugin.getTpaManager().sendRequest(player, target);
                Utils.msg(player, prefix + "&aTeleport request sent to &e" + target.getName() + "&a.");
                Utils.msg(target, prefix + "&e" + player.getName() + " &awants to teleport to you. Use &e/tpaccept &aor &e/tpdeny&a.");
                break;
            }

            case "tpaccept": {
                if (!plugin.getTpaManager().hasPendingRequest(player)) {
                    Utils.msg(player, prefix + "&cYou have no pending teleport request.");
                    return true;
                }
                UUID requesterUUID = plugin.getTpaManager().getRequester(player);
                Player requester = plugin.getServer().getPlayer(requesterUUID);
                plugin.getTpaManager().acceptRequest(player);
                if (requester != null && requester.isOnline()) {
                    Utils.msg(requester, prefix + "&e" + player.getName() + " &aaccepted your teleport request!");
                }
                Utils.msg(player, prefix + "&aAccepted teleport request.");
                break;
            }

            case "tpdeny": {
                if (!plugin.getTpaManager().hasPendingRequest(player)) {
                    Utils.msg(player, prefix + "&cYou have no pending teleport request.");
                    return true;
                }
                UUID requesterUUID = plugin.getTpaManager().getRequester(player);
                Player requester = plugin.getServer().getPlayer(requesterUUID);
                plugin.getTpaManager().denyRequest(player);
                if (requester != null && requester.isOnline()) {
                    Utils.msg(requester, prefix + "&e" + player.getName() + " &cdenied your teleport request.");
                }
                Utils.msg(player, prefix + "&cTeleport request denied.");
                break;
            }
        }
        return true;
    }
}
