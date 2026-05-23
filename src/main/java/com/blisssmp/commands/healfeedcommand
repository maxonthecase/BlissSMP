package com.blisssmp.commands;

import com.blisssmp.BlissSMP;
import com.blisssmp.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealFeedCommand implements CommandExecutor {

    private final BlissSMP plugin;
    private final String prefix;

    public HealFeedCommand(BlissSMP plugin) {
        this.plugin = plugin;
        this.prefix = plugin.getConfig().getString("prefix", "&8[&bBliss&3SMP&8] ");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        boolean isHeal = cmd.getName().equalsIgnoreCase("heal");

        Player target;
        if (args.length > 0) {
            if (!sender.hasPermission("blisssmp.admin")) {
                Utils.msgPrefixed(sender, prefix, "&cYou can only " + cmd.getName() + " yourself.");
                return true;
            }
            target = plugin.getServer().getPlayer(args[0]);
            if (target == null) {
                Utils.msgPrefixed(sender, prefix, "&cPlayer not found.");
                return true;
            }
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Specify a player: /" + cmd.getName() + " <player>");
                return true;
            }
            target = (Player) sender;
        }

        if (isHeal) {
            target.setHealth(target.getMaxHealth());
            target.setFoodLevel(20);
            target.setSaturation(20f);
            target.setFireTicks(0);
            Utils.msg(target, prefix + "&aYou have been healed!");
            if (!target.equals(sender))
                Utils.msgPrefixed(sender, prefix, "&aHealed &e" + target.getName() + "&a.");
        } else {
            target.setFoodLevel(20);
            target.setSaturation(20f);
            Utils.msg(target, prefix + "&aYou have been fed!");
            if (!target.equals(sender))
                Utils.msgPrefixed(sender, prefix, "&eFed &e" + target.getName() + "&a.");
        }
        return true;
    }
}
