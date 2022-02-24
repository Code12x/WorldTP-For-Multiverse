package com.code12.worldtp.commands;

import com.code12.worldtp.WorldTP;
import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import com.code12.worldtp.worldtpobjects.WorldTPWorld;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSpawn implements CommandExecutor {
    WorldTP plugin;

    public DataManager data = References.data;

    public CommandSpawn(WorldTP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

        if(!(sender.hasPermission("worldtp.spawn"))){
            sender.sendMessage(ChatColor.YELLOW + "You don't have the necessary permission to use this command.");
            return true;
        }

        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.YELLOW + "Only players can run this command.");
            return true;
        }

        Player player = (Player) sender;
        String playerName = player.getName();
        WorldTPWorld world = new WorldTPWorld(plugin, player.getWorld().getName());
        String worldGroup = world.getWorldGroup();

        if(data.getConfig().getLocation("menuGroupID." + worldGroup + ".WorldTPWorldSpawnPoint") != null){
            Location location = data.getConfig().getLocation("menuGroupID." + worldGroup + ".WorldTPWorldSpawnPoint");
            player.teleport(location);
        }else{
            Location location = Bukkit.getWorld(worldGroup).getSpawnLocation();
            player.teleport(location);
        }

        Location loc = player.getLocation();
        data.getConfig().set("playerLocations." + playerName + "." + worldGroup, loc);

        data.saveConfig();

        return true;
    }

}
