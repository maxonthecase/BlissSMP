package com.blisssmp.managers;

import com.blisssmp.BlissSMP;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class SpawnManager {

    private final BlissSMP plugin;
    private final File dataFile;
    private FileConfiguration data;
    private Location spawnLocation;

    public SpawnManager(BlissSMP plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "spawn.yml");
        loadData();
    }

    private void loadData() {
        if (!dataFile.exists()) {
            try { dataFile.createNewFile(); } catch (IOException e) { e.printStackTrace(); }
        }
        data = YamlConfiguration.loadConfiguration(dataFile);
        if (data.getString("spawn.world") == null) return;
        World world = Bukkit.getWorld(data.getString("spawn.world"));
        if (world == null) return;
        spawnLocation = new Location(world,
            data.getDouble("spawn.x"), data.getDouble("spawn.y"), data.getDouble("spawn.z"),
            (float) data.getDouble("spawn.yaw"), (float) data.getDouble("spawn.pitch"));
    }

    public void saveData() {
        if (spawnLocation == null) return;
        data.set("spawn.world", spawnLocation.getWorld().getName());
        data.set("spawn.x", spawnLocation.getX());
        data.set("spawn.y", spawnLocation.getY());
        data.set("spawn.z", spawnLocation.getZ());
        data.set("spawn.yaw", spawnLocation.getYaw());
        data.set("spawn.pitch", spawnLocation.getPitch());
        try { data.save(dataFile); } catch (IOException e) { e.printStackTrace(); }
    }

    public void setSpawn(Location location) {
        this.spawnLocation = location;
        saveData();
    }

    public Location getSpawn() {
        if (spawnLocation != null) return spawnLocation;
        return Bukkit.getWorlds().get(0).getSpawnLocation();
    }

    public boolean hasSpawn() { return spawnLocation != null; }
}
