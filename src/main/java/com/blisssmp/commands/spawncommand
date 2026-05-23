package com.blisssmp.commands;

import com.blisssmp.BlissSMP;
import com.blisssmp.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {

    private final BlissSMP plugin;
    private final String prefix;

    public SpawnCommand(BlissSMP plugin) {
        this.plugin = plugin;
        this.prefix = plugin.getConfig().getString("prefix", "&8[&bBliss&3SMP&8] ");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) { sender.sendMessage("Only players can use this."); return true; }
        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("setspawn")) {
            if (!player.hasPermission("blisssmp.admin")) {
                Utils.msg(player, prefix + "&cYou don't have permission to do that.");
                return true;
            }
            plugin.getSpawnManager().setSpawn(player.getLocation());
            Utils.msg(player, prefix + "&aSpawn has been set to your location!");
            return true;
        }

        // /spawn
        plugin.getBackManager().setBack(player, player.getLocation());
        int delay = plugin.getConfig().getInt("teleport-delay", 3);
        if (delay <= 0) {
            player.teleport(plugin.getSpawnManager().getSpawn());
            Utils.msg(player, prefix + "&aTeleported to spawn!");
        } else {
            Utils.msg(player, prefix + "&aTeleporting to spawn in &e" + delay + " &aseconds...");
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                player.teleport(plugin.getSpawnManager().getSpawn());
                Utils.msg(player, prefix + "&aTeleported to spawn!");
            }, delay * 20L);
        }
        return true;
    }
}
