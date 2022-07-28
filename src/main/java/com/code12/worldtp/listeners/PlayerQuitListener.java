package com.code12.worldtp.listeners;

import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import com.code12.worldtp.worldtpobjects.WorldTPWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    public DataManager data = References.data;

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event){
        Player player = event.getPlayer();

        WorldTPWorld worldTPWorld = new WorldTPWorld(player.getWorld());
        String worldGroup = worldTPWorld.getWorldGroup();

        data.getConfig().set("playerLocations." + player.getName() + "." + worldGroup, player.getLocation());
        data.saveConfig();
    }
}
