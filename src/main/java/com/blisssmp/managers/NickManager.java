package com.blisssmp.managers;

import com.blisssmp.BlissSMP;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NickManager {

    private final BlissSMP plugin;
    private final File dataFile;
    private FileConfiguration data;
    private final Map<UUID, String> nicknames = new HashMap<>();

    public NickManager(BlissSMP plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "nicknames.yml");
        loadData();
    }

    private void loadData() {
        if (!dataFile.exists()) {
            try { dataFile.createNewFile(); } catch (IOException e) { e.printStackTrace(); }
        }
        data = YamlConfiguration.loadConfiguration(dataFile);
        if (data.getConfigurationSection("nicks") == null) return;
        for (String uuidStr : data.getConfigurationSection("nicks").getKeys(false)) {
            nicknames.put(UUID.fromString(uuidStr), data.getString("nicks." + uuidStr));
        }
    }

    public void saveData() {
        for (Map.Entry<UUID, String> entry : nicknames.entrySet()) {
            data.set("nicks." + entry.getKey().toString(), entry.getValue());
        }
        try { data.save(dataFile); } catch (IOException e) { e.printStackTrace(); }
    }

    public void setNick(Player player, String nick) {
        String colored = ChatColor.translateAlternateColorCodes('&', nick);
        nicknames.put(player.getUniqueId(), colored);
        player.setDisplayName(colored + ChatColor.RESET);
        player.setPlayerListName(colored + ChatColor.RESET);
        saveData();
    }

    public void removeNick(Player player) {
        nicknames.remove(player.getUniqueId());
        player.setDisplayName(player.getName());
        player.setPlayerListName(player.getName());
        saveData();
    }

    public String getNick(Player player) {
        return nicknames.get(player.getUniqueId());
    }

    public boolean hasNick(Player player) {
        return nicknames.containsKey(player.getUniqueId());
    }

    public void applyNickOnJoin(Player player) {
        String nick = nicknames.get(player.getUniqueId());
        if (nick != null) {
            player.setDisplayName(nick + ChatColor.RESET);
            player.setPlayerListName(nick + ChatColor.RESET);
        }
    }
}
