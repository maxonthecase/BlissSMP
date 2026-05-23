package com.blisssmp.managers;

import com.blisssmp.BlissSMP;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class HomeManager {

    private final BlissSMP plugin;
    private final File dataFile;
    private FileConfiguration data;
    private final Map<UUID, Map<String, Location>> homes = new HashMap<>();

    public HomeManager(BlissSMP plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "homes.yml");
        loadData();
    }

    private void loadData() {
        if (!dataFile.exists()) {
            try { dataFile.createNewFile(); } catch (IOException e) { e.printStackTrace(); }
        }
        data = YamlConfiguration.loadConfiguration(dataFile);
        if (data.getConfigurationSection("homes") == null) return;
        for (String uuidStr : data.getConfigurationSection("homes").getKeys(false)) {
            UUID uuid = UUID.fromString(uuidStr);
            Map<String, Location> playerHomes = new HashMap<>();
            for (String name : data.getConfigurationSection("homes." + uuidStr).getKeys(false)) {
                String path = "homes." + uuidStr + "." + name;
                World world = Bukkit.getWorld(data.getString(path + ".world"));
                if (world == null) continue;
                double x = data.getDouble(path + ".x");
                double y = data.getDouble(path + ".y");
                double z = data.getDouble(path + ".z");
                float yaw = (float) data.getDouble(path + ".yaw");
                float pitch = (float) data.getDouble(path + ".pitch");
                playerHomes.put(name, new Location(world, x, y, z, yaw, pitch));
            }
            homes.put(uuid, playerHomes);
        }
    }

    public void saveData() {
        for (Map.Entry<UUID, Map<String, Location>> entry : homes.entrySet()) {
            String uuidStr = entry.getKey().toString();
            for (Map.Entry<String, Location> homeEntry : entry.getValue().entrySet()) {
                String path = "homes." + uuidStr + "." + homeEntry.getKey();
                Location loc = homeEntry.getValue();
                data.set(path + ".world", loc.getWorld().getName());
                data.set(path + ".x", loc.getX());
                data.set(path + ".y", loc.getY());
                data.set(path + ".z", loc.getZ());
                data.set(path + ".yaw", loc.getYaw());
                data.set(path + ".pitch", loc.getPitch());
            }
        }
        try { data.save(dataFile); } catch (IOException e) { e.printStackTrace(); }
    }

    public int getMaxHomes(Player player) {
        for (int i = 100; i >= 1; i--) {
            if (player.hasPermission("blisssmp.homes." + i)) return i;
        }
        return plugin.getConfig().getInt("max-homes", 3);
    }

    public boolean setHome(Player player, String name) {
        UUID uuid = player.getUniqueId();
        homes.computeIfAbsent(uuid, k -> new HashMap<>());
        Map<String, Location> playerHomes = homes.get(uuid);
        if (!playerHomes.containsKey(name) && playerHomes.size() >= getMaxHomes(player)) return false;
        playerHomes.put(name.toLowerCase(), player.getLocation());
        saveData();
        return true;
    }

    public Location getHome(Player player, String name) {
        Map<String, Location> playerHomes = homes.get(player.getUniqueId());
        if (playerHomes == null) return null;
        return playerHomes.get(name.toLowerCase());
    }

    public boolean deleteHome(Player player, String name) {
        Map<String, Location> playerHomes = homes.get(player.getUniqueId());
        if (playerHomes == null) return false;
        if (!playerHomes.containsKey(name.toLowerCase())) return false;
        playerHomes.remove(name.toLowerCase());
        saveData();
        return true;
    }

    public Set<String> getHomes(Player player) {
        Map<String, Location> playerHomes = homes.get(player.getUniqueId());
        if (playerHomes == null) return new HashSet<>();
        return playerHomes.keySet();
    }
}
