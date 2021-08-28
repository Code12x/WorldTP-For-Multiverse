package com.code12.worldtp.listeners;

import com.code12.worldtp.WorldTP;
import com.code12.worldtp.files.DataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    WorldTP plugin;

    public DataManager data;

    public PlayerJoinListener(WorldTP plugin){
        this.plugin = plugin;
        this.data = new DataManager(plugin);
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event){
        if(data.getConfig().getLocation("lobby") != null){
            event.getPlayer().teleport(data.getConfig().getLocation("lobby"));
        }
    }
}
