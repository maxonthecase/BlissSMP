package com.blisssmp.commands;

import com.blisssmp.BlissSMP;
import com.blisssmp.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

public class KitCommand implements CommandExecutor {

    private final BlissSMP plugin;
    private final String prefix;

    public KitCommand(BlissSMP plugin) {
        this.plugin = plugin;
        this.prefix = plugin.getConfig().getString("prefix", "&8[&bBliss&3SMP&8] ");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) { sender.sendMessage("Only players can use this."); return true; }
        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("kits")) {
            Set<String> kits = plugin.getKitManager().getKitNames();
            if (kits.isEmpty()) {
                Utils.msg(player, prefix + "&cNo kits are currently configured.");
            } else {
                Utils.msg(player, prefix + "&aAvailable kits: &e" + String.join("&a, &e", kits));
                Utils.msg(player, prefix + "&7Use /kit <name> to claim a kit.");
            }
            return true;
        }

        // /kit <name>
        if (args.length == 0) {
            Utils.msg(player, prefix + "&cUsage: /kit <name>");
            return true;
        }

        String kitName = args[0].toLowerCase();
        if (!plugin.getKitManager().kitExists(kitName)) {
            Utils.msg(player, prefix + "&cKit &e" + kitName + " &cdoes not exist. Use /kits to see available kits.");
            return true;
        }

        long cooldownConfig = plugin.getConfig().getLong("kits." + kitName + ".cooldown", 0);
        // One-time kit
        if (cooldownConfig == 0 && plugin.getKitManager().hasClaimedOnce(player, kitName)) {
            Utils.msg(player, prefix + "&cYou have already claimed the &e" + kitName + " &ckit (one-time only).");
            return true;
        }
        // Timed cooldown
        if (cooldownConfig > 0) {
            long remaining = plugin.getKitManager().getCooldownRemaining(player, kitName);
            if (remaining > 0) {
                Utils.msg(player, prefix + "&cKit &e" + kitName + " &cis on cooldown! Available in &e" + Utils.formatTime(remaining) + "&c.");
                return true;
            }
        }

        List<ItemStack> overflow = plugin.getKitManager().giveKit(player, kitName);
        Utils.msg(player, prefix + "&aYou claimed the &e" + kitName + " &akit!");
        if (!overflow.isEmpty()) {
            Utils.msg(player, prefix + "&eYour inventory was full — some items were dropped.");
            for (ItemStack item : overflow) {
                player.getWorld().dropItem(player.getLocation(), item);
            }
        }
        return true;
    }
}
