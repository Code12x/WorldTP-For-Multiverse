package com.code12.worldtp.gui.util;

import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TeleportUtils {
    static DataManager data = References.data;

    public static Location resume(Player player, String worldGroupName){
        Location locationToTP;

        String worldGroupDisplayName = data.getConfig().getString("menuGroupID." + worldGroupName + ".displayName");

        if (player.getWorld().getName().startsWith(worldGroupName)) {
            player.sendMessage(ChatColor.YELLOW + "You are already in the world: " + worldGroupDisplayName);
            return null;
        } else if (data.getConfig().getLocation("menuGroupID." + worldGroupName + ".WorldTPWorldSpawnPoint") != null) {
            locationToTP = data.getConfig().getLocation("menuGroupID." + worldGroupName + ".WorldTPWorldSpawnPoint");
        } else if (data.getConfig().getLocation("playerLocations." + player.getName() + "." + worldGroupName) != null) {
            locationToTP = data.getConfig().getLocation("playerLocations." + player.getName() + "." + worldGroupName);
        } else {
            locationToTP = Bukkit.getWorld(worldGroupName).getSpawnLocation();
        }

        return locationToTP;
    }

    public static void teleport(Player player, Location location, String worldGroupToLeaveName, String worldGroupName){
        Location playerLocation = player.getLocation();

        player.teleport(location);

        data.getConfig().set("playerLocations." + player.getName() + "." + worldGroupToLeaveName, playerLocation);
        data.saveConfig();

        String worldGroupDisplayName = data.getConfig().getString("menuGroupID." + worldGroupName + ".displayName");
        player.sendMessage(ChatColor.DARK_AQUA + "You have been teleported to " + worldGroupDisplayName + ".");
    }
}
