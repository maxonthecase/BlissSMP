package com.blisssmp.commands;

import com.blisssmp.BlissSMP;
import com.blisssmp.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyGodCommand implements CommandExecutor {

    private final BlissSMP plugin;
    private final String prefix;

    public FlyGodCommand(BlissSMP plugin) {
        this.plugin = plugin;
        this.prefix = plugin.getConfig().getString("prefix", "&8[&bBliss&3SMP&8] ");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        boolean isFly = cmd.getName().equalsIgnoreCase("fly");

        Player target;
        if (args.length > 0 && sender.hasPermission("blisssmp.admin")) {
            target = plugin.getServer().getPlayer(args[0]);
            if (target == null) {
                Utils.msgPrefixed(sender, prefix, "&cPlayer not found.");
                return true;
            }
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Specify a player.");
                return true;
            }
            target = (Player) sender;
        }

        if (isFly) {
            boolean newState = !target.getAllowFlight();
            target.setAllowFlight(newState);
            if (!newState) target.setFlying(false);
            Utils.msg(target, prefix + "&aFlight " + (newState ? "&aenabled" : "&cdisabled") + "&a.");
            if (!target.equals(sender))
                Utils.msgPrefixed(sender, prefix, "&aToggled fly for &e" + target.getName() + "&a.");
        } else {
            boolean newState = !target.isInvulnerable();
            target.setInvulnerable(newState);
            Utils.msg(target, prefix + "&aGod mode " + (newState ? "&aenabled" : "&cdisabled") + "&a.");
            if (!target.equals(sender))
                Utils.msgPrefixed(sender, prefix, "&aToggled god mode for &e" + target.getName() + "&a.");
        }
        return true;
    }
}
