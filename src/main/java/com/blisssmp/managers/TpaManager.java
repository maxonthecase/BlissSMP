package com.blisssmp.managers;

import com.blisssmp.BlissSMP;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TpaManager {

    private final BlissSMP plugin;
    private final Map<UUID, UUID> pendingRequests = new HashMap<>();
    private final Map<UUID, UUID> incomingRequests = new HashMap<>();
    private final Map<UUID, BukkitTask> timeoutTasks = new HashMap<>();

    public TpaManager(BlissSMP plugin) {
        this.plugin = plugin;
    }

    public void sendRequest(Player requester, Player target) {
        cancelRequest(requester.getUniqueId());
        pendingRequests.put(requester.getUniqueId(), target.getUniqueId());
        incomingRequests.put(target.getUniqueId(), requester.getUniqueId());

        int timeout = plugin.getConfig().getInt("tpa-timeout", 60);
        BukkitTask task = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (pendingRequests.containsKey(requester.getUniqueId())) {
                pendingRequests.remove(requester.getUniqueId());
                incomingRequests.remove(target.getUniqueId());
                timeoutTasks.remove(requester.getUniqueId());
                if (requester.isOnline()) requester.sendMessage(
                    plugin.getConfig().getString("prefix","&8[&bBliss&3SMP&8] ").replace("&","§") +
                    "§cYour TPA request to §e" + target.getName() + " §cexpired.");
                if (target.isOnline()) target.sendMessage(
                    plugin.getConfig().getString("prefix","&8[&bBliss&3SMP&8] ").replace("&","§") +
                    "§cTPA request from §e" + requester.getName() + " §cexpired.");
            }
        }, timeout * 20L);
        timeoutTasks.put(requester.getUniqueId(), task);
    }

    public UUID getRequester(Player target) {
        return incomingRequests.get(target.getUniqueId());
    }

    public void acceptRequest(Player target) {
        UUID requesterUUID = incomingRequests.remove(target.getUniqueId());
        if (requesterUUID == null) return;
        pendingRequests.remove(requesterUUID);
        BukkitTask t = timeoutTasks.remove(requesterUUID);
        if (t != null) t.cancel();

        Player requester = Bukkit.getPlayer(requesterUUID);
        if (requester == null || !requester.isOnline()) return;

        int delay = plugin.getConfig().getInt("teleport-delay", 3);
        if (delay <= 0) {
            requester.teleport(target.getLocation());
        } else {
            requester.sendMessage("§aYou will be teleported in §e" + delay + " §aseconds. Don't move!");
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (requester.isOnline()) requester.teleport(target.getLocation());
            }, delay * 20L);
        }
    }

    public void denyRequest(Player target) {
        UUID requesterUUID = incomingRequests.remove(target.getUniqueId());
        if (requesterUUID == null) return;
        pendingRequests.remove(requesterUUID);
        BukkitTask t = timeoutTasks.remove(requesterUUID);
        if (t != null) t.cancel();
    }

    private void cancelRequest(UUID requester) {
        UUID target = pendingRequests.remove(requester);
        if (target != null) incomingRequests.remove(target);
        BukkitTask t = timeoutTasks.remove(requester);
        if (t != null) t.cancel();
    }

    public boolean hasPendingRequest(Player target) {
        return incomingRequests.containsKey(target.getUniqueId());
    }
}
