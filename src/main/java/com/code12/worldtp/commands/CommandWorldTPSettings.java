package com.code12.worldtp.commands;

import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandWorldTPSettings implements CommandExecutor {
    private final DataManager data = References.data;

    // The command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        // makes sure the sender is an admin
        if(!(sender.hasPermission("worldtp.editworld"))){
            sender.sendMessage(ChatColor.YELLOW + "You don't have the necessary permission to use this command.");
            return true;
        }



        return true;
    }
}
