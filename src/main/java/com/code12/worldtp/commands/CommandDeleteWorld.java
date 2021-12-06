package com.code12.worldtp.commands;

import com.code12.worldtp.WorldTP;
import com.code12.worldtp.worldtpobjects.WorldTPWorldGroup;
import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandDeleteWorld implements CommandExecutor {
    WorldTP plugin;
    public DataManager data = References.data;

    public CommandDeleteWorld(WorldTP plugin) {
        this.plugin = plugin;
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

        WorldTPWorldGroup worldTPWorldGroup = new WorldTPWorldGroup(plugin, world, displayName);

        worldTPWorldGroup.deleteWorldGroup(sender);

        return true;
    }
}
