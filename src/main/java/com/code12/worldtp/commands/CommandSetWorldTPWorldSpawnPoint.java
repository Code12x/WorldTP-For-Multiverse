package com.code12.worldtp.commands;

import com.code12.worldtp.WorldTP;
import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSetWorldTPWorldSpawnPoint implements CommandExecutor {
    WorldTP plugin;

    public DataManager data = References.data;

    public CommandSetWorldTPWorldSpawnPoint(WorldTP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

        if(!(sender instanceof Player)){
            sender.sendMessage("Only players can run this command.");
        }

        if(!(sender.hasPermission("worldtp.setworldtpworldspawnpoint"))) {
            sender.sendMessage(ChatColor.YELLOW + "You don't have the necessary permission to use this command.");
            return true;
        }

        Player player = (Player) sender;

        Location loc = player.getLocation();

        String world = player.getWorld().getName();

        data.getConfig().set("menuGroupID." + world + ".WorldTPWorldSpawnPoint", loc);

        data.saveConfig();

        player.sendMessage(ChatColor.YELLOW + "WorldTP World Spawn Point set!");

        return true;
    }

}