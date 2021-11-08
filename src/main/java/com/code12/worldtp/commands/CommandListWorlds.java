package com.code12.worldtp.commands;

import com.code12.worldtp.WorldTP;
import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandListWorlds implements CommandExecutor {
    WorldTP plugin;
    public DataManager data = References.data;

    public CommandListWorlds(WorldTP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

        if(!sender.hasPermission("worldtp.listworlds")){
            sender.sendMessage(ChatColor.YELLOW + "You don't have the necessary permission to use this command.");
            return true;
        }

        sender.sendMessage(ChatColor.YELLOW + "Worlds:\n " + ChatColor.WHITE + data.getConfig().getStringList("menuGroupList"));

        return true;
    }
}