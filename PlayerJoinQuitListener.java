package com.blisssmp.listeners;

import com.blisssmp.BlissSMP;
import com.blisssmp.utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitListener implements Listener {

    private final BlissSMP plugin;

    public PlayerJoinQuitListener(BlissSMP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        plugin.getNickManager().applyNickOnJoin(e.getPlayer());

        String template = plugin.getConfig().getString("join-message", "&a+ &e{player} &ajoined the server!");
        String msg = template.replace("{player}", e.getPlayer().getName());
        e.setJoinMessage(Utils.color(msg));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        String template = plugin.getConfig().getString("quit-message", "&c- &e{player} &cleft the server.");
        String msg = template.replace("{player}", e.getPlayer().getName());
        e.setQuitMessage(Utils.color(msg));
    }
}
