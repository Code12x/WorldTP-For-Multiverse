package com.code12.worldtp.commands;

import com.code12.worldtp.WorldTP;
import com.code12.worldtp.apimethods.WorldTPWorld;
import com.code12.worldtp.files.DataManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSetHome implements CommandExecutor {
    WorldTP plugin;

    public DataManager data;

    public CommandSetHome(WorldTP plugin, DataManager data) {
        this.plugin = plugin;
        this.data = data;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

        if(!(sender.hasPermission("worldtp.sethome"))){
            sender.sendMessage(ChatColor.YELLOW + "You don't have the necessary permission to use this command.");
            return true;
        }

        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.YELLOW + "Only players can run this command.");
            return true;
        }
        Player player = (Player) sender;
        String playerName = player.getName();
        Location loc = player.getLocation();

        WorldTPWorld world = new WorldTPWorld(plugin, player.getWorld().getName(), data);

        String worldGroupName = world.getWorldGroup();

        data.getConfig().set("playerLocations." + playerName + "." + worldGroupName + "_HOME", loc);

        player.sendMessage(ChatColor.YELLOW + "Your home has been set.");

        data.saveConfig();

        return true;
    }
}
