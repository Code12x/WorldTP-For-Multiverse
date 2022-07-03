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
        // Set plugin
        References.setPlugin(this);

        // config.yml
        References.loadConfig();

        // data.yml
        References.loadData();

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
        getCommand("worldtp").setExecutor(new CommandWorldTP());
        getCommand("editworld").setExecutor(new CommandEditWorld());
        getCommand("deleteworld").setExecutor(new CommandDeleteWorld());
        getCommand("listworlds").setExecutor(new CommandListWorlds());
        getCommand("reloadworlds").setExecutor(new CommandReloadWorlds());
        getCommand("setlobby").setExecutor(new CommandSetLobby());
        getCommand("setworldtpworldspawnpoint").setExecutor(new CommandSetWorldTPWorldSpawnPoint());
        getCommand("removelobby").setExecutor(new CommandRemoveLobby());
        getCommand("removeworldtpworldspawnpoint").setExecutor(new CommandRemoveWorldTPWorldSpawnPoint());
        getCommand("spawn").setExecutor(new CommandSpawn());
    }

    private void registerEvents() {
        // Plugin manager
        PluginManager pm = getServer().getPluginManager();
        //pm.registerEvents(new InventoryListener(), this);
        pm.registerEvents(new PlayerJoinListener(), this);
    }
}
