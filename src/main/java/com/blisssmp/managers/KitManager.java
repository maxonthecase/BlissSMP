package com.blisssmp.managers;

import com.blisssmp.BlissSMP;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class KitManager {

    private final BlissSMP plugin;
    private final File cooldownFile;
    private FileConfiguration cooldownData;
    private final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();

    public KitManager(BlissSMP plugin) {
        this.plugin = plugin;
        this.cooldownFile = new File(plugin.getDataFolder(), "kit_cooldowns.yml");
        loadData();
    }

    private void loadData() {
        if (!cooldownFile.exists()) {
            try { cooldownFile.createNewFile(); } catch (IOException e) { e.printStackTrace(); }
        }
        cooldownData = YamlConfiguration.loadConfiguration(cooldownFile);
        if (cooldownData.getConfigurationSection("cooldowns") == null) return;
        for (String uuidStr : cooldownData.getConfigurationSection("cooldowns").getKeys(false)) {
            UUID uuid = UUID.fromString(uuidStr);
            Map<String, Long> playerCooldowns = new HashMap<>();
            ConfigurationSection sec = cooldownData.getConfigurationSection("cooldowns." + uuidStr);
            for (String kit : sec.getKeys(false)) {
                playerCooldowns.put(kit, sec.getLong(kit));
            }
            cooldowns.put(uuid, playerCooldowns);
        }
    }

    public void saveData() {
        for (Map.Entry<UUID, Map<String, Long>> entry : cooldowns.entrySet()) {
            for (Map.Entry<String, Long> kitEntry : entry.getValue().entrySet()) {
                cooldownData.set("cooldowns." + entry.getKey() + "." + kitEntry.getKey(), kitEntry.getValue());
            }
        }
        try { cooldownData.save(cooldownFile); } catch (IOException e) { e.printStackTrace(); }
    }

    public Set<String> getKitNames() {
        ConfigurationSection sec = plugin.getConfig().getConfigurationSection("kits");
        if (sec == null) return new HashSet<>();
        return sec.getKeys(false);
    }

    public boolean kitExists(String name) {
        return plugin.getConfig().getConfigurationSection("kits." + name.toLowerCase()) != null;
    }

    public long getCooldownRemaining(Player player, String kitName) {
        long cooldown = plugin.getConfig().getLong("kits." + kitName + ".cooldown", 0);
        if (cooldown < 0) return -1;
        Map<String, Long> playerCooldowns = cooldowns.get(player.getUniqueId());
        if (playerCooldowns == null) return 0;
        Long lastClaimed = playerCooldowns.get(kitName.toLowerCase());
        if (lastClaimed == null) return 0;
        if (cooldown == 0) return -1;
        long elapsed = (System.currentTimeMillis() - lastClaimed) / 1000;
        long remaining = cooldown - elapsed;
        return Math.max(0, remaining);
    }

    public boolean hasClaimedOnce(Player player, String kitName) {
        Map<String, Long> playerCooldowns = cooldowns.get(player.getUniqueId());
        if (playerCooldowns == null) return false;
        return playerCooldowns.containsKey(kitName.toLowerCase());
    }

    public List<ItemStack> giveKit(Player player, String kitName) {
        String path = "kits." + kitName.toLowerCase();
        List<String> itemStrings = plugin.getConfig().getStringList(path + ".items");
        List<ItemStack> items = new ArrayList<>();
        List<ItemStack> overflow = new ArrayList<>();

        for (String itemStr : itemStrings) {
            String[] parts = itemStr.split(":");
            Material mat = Material.getMaterial(parts[0]);
            if (mat == null) continue;
            int amount = parts.length > 1 ? Integer.parseInt(parts[1]) : 1;
            ItemStack item = new ItemStack(mat, amount);
            HashMap<Integer, ItemStack> leftover = player.getInventory().addItem(item);
            overflow.addAll(leftover.values());
            items.add(item);
        }

        cooldowns.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>())
                 .put(kitName.toLowerCase(), System.currentTimeMillis());
        saveData();

        return overflow;
    }
}
