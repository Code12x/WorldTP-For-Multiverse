package com.code12.worldtp.commands;

import com.code12.worldtp.WorldTP;
import com.code12.worldtp.apimethods.WorldTPWorldGroup;
import com.code12.worldtp.files.DataManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandDeleteWorld implements CommandExecutor {
    WorldTP plugin;
    public DataManager data;

    public CommandDeleteWorld(WorldTP plugin, DataManager data) {
        this.plugin = plugin;
        this.data = data;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(!(sender.hasPermission("worldtp.deleteworld"))){
            sender.sendMessage(ChatColor.YELLOW + "You do not have the necessary permission to perform this command.");
            return true;
        }

        if(!(args.length == 1)){
            sender.sendMessage(ChatColor.YELLOW + "Command \"deleteworld\" requires 1 argument: world to delete.\n" + ChatColor.AQUA + "Currently registered worlds:\n"+ChatColor.WHITE + data.getConfig().getStringList("menuGroupList"));
            return true;
        }

        String world = args[0];
        String displayName = data.getConfig().getString("menuGroupID." + world + ".displayName");

        WorldTPWorldGroup worldTPWorldGroup = new WorldTPWorldGroup(plugin, data, world, displayName);

        worldTPWorldGroup.deleteWorldGroup(sender);

        return true;
    }
}
