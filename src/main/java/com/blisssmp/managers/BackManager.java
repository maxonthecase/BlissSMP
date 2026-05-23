package com.blisssmp.managers;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BackManager {

    private final Map<UUID, Location> lastLocations = new HashMap<>();

    public BackManager(com.blisssmp.BlissSMP plugin) {}

    public void setBack(Player player, Location location) {
        lastLocations.put(player.getUniqueId(), location);
    }

    public Location getBack(Player player) {
        return lastLocations.get(player.getUniqueId());
    }

    public boolean hasBack(Player player) {
        return lastLocations.containsKey(player.getUniqueId());
    }
}
