package com.blisssmp.commands;

import com.blisssmp.BlissSMP;
import com.blisssmp.utils.Utils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BackCommand implements CommandExecutor {

    private final BlissSMP plugin;
    private final String prefix;

    public BackCommand(BlissSMP plugin) {
        this.plugin = plugin;
        this.prefix = plugin.getConfig().getString("prefix", "&8[&bBliss&3SMP&8] ");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) { sender.sendMessage("Only players can use this."); return true; }
        Player player = (Player) sender;

        if (!plugin.getBackManager().hasBack(player)) {
            Utils.msg(player, prefix + "&cNo previous location found.");
            return true;
        }

        Location back = plugin.getBackManager().getBack(player);
        Location current = player.getLocation();
        plugin.getBackManager().setBack(player, current);
        player.teleport(back);
        Utils.msg(player, prefix + "&aTeleported to your last location.");
        return true;
    }
}
