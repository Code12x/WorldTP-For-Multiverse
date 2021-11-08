package com.code12.worldtp.commands;

import com.code12.worldtp.WorldTP;
import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import com.code12.worldtp.menues.AdvancedWorldTPMenu;
import com.code12.worldtp.menues.WorldTPMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandWorldTP implements CommandExecutor {
    WorldTP plugin;

    public DataManager data = References.data;

    public CommandWorldTP(WorldTP plugin) {
        this.plugin = plugin;
    }

    // the command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(sender instanceof Player){
            Player player = (Player) sender;

            if(data.getConfig().getStringList("menuGroupList").isEmpty()){
                player.sendMessage("The worlds have not been registered on WorldTP. To register the worlds, have an admin run the command /reloadworlds.");
                return true;
            }

            if(player.hasPermission("worldtp.worldtp")){
                AdvancedWorldTPMenu advancedMenu = new AdvancedWorldTPMenu(plugin);
                player.openInventory(advancedMenu.tpMenu);
            }else{
                WorldTPMenu menu = new WorldTPMenu(plugin);
                player.openInventory(menu.tpMenu);
            }
        }
        return true;
    }
}
