package com.blisssmp.listeners;

import com.blisssmp.BlissSMP;
import com.blisssmp.utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private final BlissSMP plugin;

    public PlayerDeathListener(BlissSMP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (plugin.getConfig().getBoolean("back-on-death", true)) {
            plugin.getBackManager().setBack(e.getEntity(), e.getEntity().getLocation());
        }

        if (plugin.getConfig().getBoolean("show-death-coordinates", true)) {
            int x = e.getEntity().getLocation().getBlockX();
            int y = e.getEntity().getLocation().getBlockY();
            int z = e.getEntity().getLocation().getBlockZ();
            e.getEntity().sendMessage(Utils.color("&7You died at &e" + x + ", " + y + ", " + z + "&7. Use &e/back &7to return."));
        }
    }
}
