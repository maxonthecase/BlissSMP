package com.blisssmp.listeners;

import com.blisssmp.BlissSMP;
import com.blisssmp.utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private final BlissSMP plugin;

    public ChatListener(BlissSMP plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChat(AsyncPlayerChatEvent e) {
        String format = plugin.getConfig().getString("chat-format", "&7[{world}] &r{displayname}&7: &f{message}");
        format = format
            .replace("{world}", e.getPlayer().getWorld().getName())
            .replace("{player}", e.getPlayer().getName())
            .replace("{displayname}", e.getPlayer().getDisplayName())
            .replace("{message}", e.getMessage());
        e.setFormat(Utils.color(format));
    }
}
