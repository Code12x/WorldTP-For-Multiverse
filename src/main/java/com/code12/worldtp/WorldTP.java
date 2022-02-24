package com.code12.worldtp;

import com.code12.worldtp.commands.*;
import com.code12.worldtp.files.ConfigManager;
import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import com.code12.worldtp.listeners.InventoryListener;
import com.code12.worldtp.listeners.PlayerJoinListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class WorldTP extends JavaPlugin {

    @Override
    public void onEnable() {
        // config.yml
        References.loadConfig(this);

        // data.yml
        References.loadData(this);

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
        References.data.saveConfig();
    }

    private void loadCommands() {
        getCommand("worldtp").setExecutor(new CommandWorldTP(this)); // anyone can access
        getCommand("editworld").setExecutor(new CommandEditWorld(this)); // only admins
        getCommand("deleteworld").setExecutor(new CommandDeleteWorld(this)); // only admins
        getCommand("listworlds").setExecutor(new CommandListWorlds(this)); // anyone
        getCommand("reloadworlds").setExecutor(new CommandReloadWorlds(this));// only admins
        getCommand("setlobby").setExecutor(new CommandSetLobby(this)); // only admins
        getCommand("setworldtpworldspawnpoint").setExecutor(new CommandSetWorldTPWorldSpawnPoint(this)); //admins only
        getCommand("removelobby").setExecutor(new CommandRemoveLobby(this)); //admins only
        getCommand("removeworldtpworldspawnpoint").setExecutor(new CommandRemoveWorldTPWorldSpawnPoint(this)); //admins only
        getCommand("spawn").setExecutor(new CommandSpawn(this));
    }

    private void registerEvents() {
        // Plugin manager
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new InventoryListener(this), this);
        pm.registerEvents(new PlayerJoinListener(this), this);
    }

    private void loadConfig() {
        References.config = new ConfigManager(this);
    }

    private void loadData() {
        References.data = new DataManager(this);
    }
}
