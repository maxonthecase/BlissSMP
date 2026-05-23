package com.blisssmp.commands;

import com.blisssmp.BlissSMP;
import com.blisssmp.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommand implements CommandExecutor {

    private final BlissSMP plugin;

    public MainCommand(BlissSMP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String prefix = plugin.getConfig().getString("prefix", "&8[&bBliss&3SMP&8] ");
        Utils.msg(sender, "&3&m                                        ");
        Utils.msg(sender, " &bBliss&3SMP &7v" + plugin.getDescription().getVersion());
        Utils.msg(sender, " &7Custom SMP plugin");
        Utils.msg(sender, " ");
        Utils.msg(sender, " &aCommands: &e/homes /home /sethome /delhome");
        Utils.msg(sender, "           &e/tpa /tpaccept /tpdeny");
        Utils.msg(sender, "           &e/spawn /back /kit /kits");
        Utils.msg(sender, "           &e/msg /reply /nick /heal /feed");
        Utils.msg(sender, "           &e/fly /god");
        Utils.msg(sender, "&3&m                                        ");

        if (args.length > 0 && args[0].equalsIgnoreCase("reload") && sender.hasPermission("blisssmp.admin")) {
            plugin.reloadConfig();
            Utils.msg(sender, prefix + "&aConfig reloaded!");
        }
        return true;
    }
}
