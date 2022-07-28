package com.code12.worldtp;

import com.code12.worldtp.commands.*;
import com.code12.worldtp.files.References;
import com.code12.worldtp.listeners.PlayerJoinListener;
import com.code12.worldtp.listeners.PlayerQuitListener;
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
        getCommand("worldtp").setExecutor(new CommandWorldTP());
        getCommand("worldtpsettings").setExecutor(new CommandWorldTPSettings());
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
        pm.registerEvents(new PlayerJoinListener(), this);
        pm.registerEvents(new PlayerQuitListener(), this);
    }
}
