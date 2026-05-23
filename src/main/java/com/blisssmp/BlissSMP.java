package com.blisssmp;

import com.blisssmp.commands.*;
import com.blisssmp.listeners.*;
import com.blisssmp.managers.*;
import org.bukkit.plugin.java.JavaPlugin;

public class BlissSMP extends JavaPlugin {

    private static BlissSMP instance;
    private HomeManager homeManager;
    private TpaManager tpaManager;
    private KitManager kitManager;
    private SpawnManager spawnManager;
    private NickManager nickManager;
    private BackManager backManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        // Init managers
        this.homeManager = new HomeManager(this);
        this.tpaManager = new TpaManager(this);
        this.kitManager = new KitManager(this);
        this.spawnManager = new SpawnManager(this);
        this.nickManager = new NickManager(this);
        this.backManager = new BackManager(this);

        // Register commands
        getCommand("sethome").setExecutor(new HomeCommand(this));
        getCommand("home").setExecutor(new HomeCommand(this));
        getCommand("delhome").setExecutor(new HomeCommand(this));
        getCommand("homes").setExecutor(new HomeCommand(this));
        getCommand("tpa").setExecutor(new TpaCommand(this));
        getCommand("tpaccept").setExecutor(new TpaCommand(this));
        getCommand("tpdeny").setExecutor(new TpaCommand(this));
        getCommand("spawn").setExecutor(new SpawnCommand(this));
        getCommand("setspawn").setExecutor(new SpawnCommand(this));
        getCommand("back").setExecutor(new BackCommand(this));
        getCommand("kit").setExecutor(new KitCommand(this));
        getCommand("kits").setExecutor(new KitCommand(this));
        getCommand("heal").setExecutor(new HealFeedCommand(this));
        getCommand("feed").setExecutor(new HealFeedCommand(this));
        getCommand("fly").setExecutor(new FlyGodCommand(this));
        getCommand("god").setExecutor(new FlyGodCommand(this));
        getCommand("nick").setExecutor(new NickCommand(this));
        getCommand("msg").setExecutor(new MsgCommand(this));
        getCommand("reply").setExecutor(new MsgCommand(this));
        getCommand("blisssmp").setExecutor(new MainCommand(this));

        // Register listeners
        getServer().getPluginManager().registerEvents(new PlayerJoinQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new AntiGriefListener(this), this);

        getLogger().info("BlissSMP v" + getDescription().getVersion() + " enabled!");
    }

    @Override
    public void onDisable() {
        homeManager.saveData();
        kitManager.saveData();
        spawnManager.saveData();
        nickManager.saveData();
        getLogger().info("BlissSMP disabled. Data saved.");
    }

    public static BlissSMP getInstance() { return instance; }
    public HomeManager getHomeManager() { return homeManager; }
    public TpaManager getTpaManager() { return tpaManager; }
    public KitManager getKitManager() { return kitManager; }
    public SpawnManager getSpawnManager() { return spawnManager; }
    public NickManager getNickManager() { return nickManager; }
    public BackManager getBackManager() { return backManager; }
}
