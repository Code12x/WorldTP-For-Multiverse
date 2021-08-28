package com.code12.worldtp;

import com.code12.worldtp.commands.*;
import com.code12.worldtp.files.ConfigManager;
import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.listeners.InventoryListener;
import com.code12.worldtp.listeners.PlayerJoinListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class WorldTP extends JavaPlugin {

    public DataManager data;
    public ConfigManager config;

    @Override
    public void onEnable() {
        // config.yml
        loadConfig();

        // data.yml
        loadData();

        // Register Events
        registerEvents();

        // getting all the player commands
        loadCommands();

        // Finish Startup
        getLogger().info("WorldTP has been ENABLED");

    }

    @Override
    public void onDisable() {
        getLogger().info("WorldTP has been DISABLED");
        data.saveConfig();
    }

    private void loadCommands() {
        getCommand("worldtp").setExecutor(new CommandWorldTP(this, data)); // anyone can access
        getCommand("editworld").setExecutor(new CommandEditWorld(this, data)); // only admins
        getCommand("deleteworld").setExecutor(new CommandDeleteWorld(this, data)); // only admins
        getCommand("listworlds").setExecutor(new CommandListWorlds(this)); // anyone
        getCommand("reloadworlds").setExecutor(new CommandReloadWorlds(this, data));// only admins
        getCommand("setlobby").setExecutor(new CommandSetLobby(this, data)); // only admins
        getCommand("setworldtpworldspawnpoint").setExecutor(new CommandSetWorldTPWorldSpawnPoint(this, data)); //admins only
        getCommand("removelobby").setExecutor(new CommandRemoveLobby(this, data)); //admins only
        getCommand("removeworldtpworldspawnpoint").setExecutor(new CommandRemoveWorldTPWorldSpawnPoint(this, data)); //admins only
        getCommand("sethome").setExecutor(new CommandSetHome(this, data));
        getCommand("home").setExecutor(new CommandHome(this, data));
        getCommand("spawn").setExecutor(new CommandSpawn(this, data));
    }

    private void registerEvents() {
        // Plugin manager
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new InventoryListener(this, data), this);
        pm.registerEvents(new PlayerJoinListener(this), this);
    }

    private void loadConfig() {
        this.config = new ConfigManager(this);
    }

    private void loadData() {
        this.data = new DataManager(this);
    }
}
