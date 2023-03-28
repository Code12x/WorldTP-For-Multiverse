package com.code12.worldtp.commands;

import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import com.code12.worldtp.worlds.WorldTPWorldGroup;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandDeleteWorld implements CommandExecutor {
    private final DataManager data = References.data;

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

        WorldTPWorldGroup worldTPWorldGroup = new WorldTPWorldGroup(world);

        worldTPWorldGroup.deleteWorldGroup(sender);

        return true;
    }
}
