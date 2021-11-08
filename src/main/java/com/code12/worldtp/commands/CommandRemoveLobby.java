package com.code12.worldtp.commands;

import com.code12.worldtp.WorldTP;
import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandRemoveLobby implements CommandExecutor {
    WorldTP plugin;

    public DataManager data = References.data;

    public CommandRemoveLobby(WorldTP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

        if(!sender.hasPermission("worldtp.removelobby")){
            sender.sendMessage(ChatColor.YELLOW + "You don't have the necessary permission to use this command.");
        }

        data.getConfig().set("lobby", null);

        sender.sendMessage(ChatColor.YELLOW + "There are no longer any lobbies registered.");

        data.saveConfig();

        return true;
    }

}
