package com.blisssmp.commands;

import com.blisssmp.BlissSMP;
import com.blisssmp.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class HomeCommand implements CommandExecutor {

    private final BlissSMP plugin;
    private final String prefix;

    public HomeCommand(BlissSMP plugin) {
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

            case "sethome": {
                String name = args.length > 0 ? args[0].toLowerCase() : "home";
                if (!name.matches("[a-zA-Z0-9_]+")) {
                    Utils.msg(player, prefix + "&cHome names can only contain letters, numbers, and underscores.");
                    return true;
                }
                boolean set = plugin.getHomeManager().setHome(player, name);
                if (set) {
                    Utils.msg(player, prefix + "&aHome &e" + name + " &ahas been set!");
                } else {
                    int max = plugin.getHomeManager().getMaxHomes(player);
                    Utils.msg(player, prefix + "&cYou have reached your maximum of &e" + max + " &chomes.");
                }
                break;
            }

            case "home": {
                String name = args.length > 0 ? args[0].toLowerCase() : "home";
                org.bukkit.Location loc = plugin.getHomeManager().getHome(player, name);
                if (loc == null) {
                    Utils.msg(player, prefix + "&cHome &e" + name + " &cdoes not exist.");
                    return true;
                }
                int delay = plugin.getConfig().getInt("teleport-delay", 3);
                plugin.getBackManager().setBack(player, player.getLocation());
                if (delay <= 0) {
                    player.teleport(loc);
                    Utils.msg(player, prefix + "&aTeleported to home &e" + name + "&a!");
                } else {
                    Utils.msg(player, prefix + "&aTeleporting to &e" + name + " &ain &e" + delay + " &aseconds...");
                    plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                        player.teleport(loc);
                        Utils.msg(player, prefix + "&aTeleported to home &e" + name + "&a!");
                    }, delay * 20L);
                }
                break;
            }

            case "delhome": {
                if (args.length == 0) {
                    Utils.msg(player, prefix + "&cUsage: /delhome <name>");
                    return true;
                }
                String name = args[0].toLowerCase();
                boolean deleted = plugin.getHomeManager().deleteHome(player, name);
                if (deleted) {
                    Utils.msg(player, prefix + "&aDeleted home &e" + name + "&a.");
                } else {
                    Utils.msg(player, prefix + "&cHome &e" + name + " &cdoes not exist.");
                }
                break;
            }

            case "homes": {
                Set<String> homeSet = plugin.getHomeManager().getHomes(player);
                if (homeSet.isEmpty()) {
                    Utils.msg(player, prefix + "&cYou have no homes set. Use /sethome to set one!");
                } else {
                    Utils.msg(player, prefix + "&aYour homes &7(" + homeSet.size() + "/" + plugin.getHomeManager().getMaxHomes(player) + ")&a: &e" + String.join("&a, &e", homeSet));
                }
                break;
            }
        }
        return true;
    }
}
