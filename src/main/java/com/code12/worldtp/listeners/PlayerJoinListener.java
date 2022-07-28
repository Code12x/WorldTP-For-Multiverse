package com.code12.worldtp.listeners;

import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final DataManager data = References.data;

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event){
        if(data.getConfig().getLocation("lobby") != null){
            event.getPlayer().teleport(data.getConfig().getLocation("lobby"));
        }
    }
}
