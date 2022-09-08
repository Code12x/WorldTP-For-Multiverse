package com.code12.worldtp.commands;

import com.code12.worldtp.files.ConfigManager;
import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import com.code12.worldtp.gui.util.TeleportUtils;
import com.code12.worldtp.worldtpobjects.WorldTPWorld;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSpawn implements CommandExecutor {
    private final DataManager data = References.data;
    private final ConfigManager config = References.config;

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
        Location playerLocation = player.getLocation();
        WorldTPWorld world = new WorldTPWorld(player.getWorld());
        String worldGroup = world.getWorldGroup();

        if(!config.getConfig().getBoolean(worldGroup + ".Spawn_Teleporting")){
            sender.sendMessage(ChatColor.YELLOW + "You can not teleport to spawn in this world.");
            return true;
        }

        Location location = Bukkit.getWorld(worldGroup).getSpawnLocation();

        TeleportUtils.teleport(player, location, worldGroup, worldGroup);

        return true;
    }
}
