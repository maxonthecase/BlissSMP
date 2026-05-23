package com.blisssmp.listeners;

import com.blisssmp.BlissSMP;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AntiGriefListener implements Listener {

    private final BlissSMP plugin;
    private final Set<Material> dangerousItems = new HashSet<>(Arrays.asList(
        Material.FLINT_AND_STEEL, Material.FIRE_CHARGE
    ));

    public AntiGriefListener(BlissSMP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTNTExplode(EntityExplodeEvent e) {
        // Remove block damage from explosions (configurable)
        if (plugin.getConfig().getBoolean("anti-grief.no-explosion-damage", false)) {
            e.blockList().clear();
        }
    }

    @EventHandler
    public void onFireSpread(BlockSpreadEvent e) {
        // Optionally prevent fire spread
        if (plugin.getConfig().getBoolean("anti-grief.no-fire-spread", false)) {
            if (e.getNewState().getType() == Material.FIRE) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent e) {
        if (plugin.getConfig().getBoolean("anti-grief.no-fire-spread", false)) {
            e.setCancelled(true);
        }
    }
}
