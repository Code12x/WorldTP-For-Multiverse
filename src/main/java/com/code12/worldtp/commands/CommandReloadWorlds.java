package com.code12.worldtp.commands;

import com.code12.worldtp.WorldTP;
import com.code12.worldtp.worldtpobjects.WorldTPWorld;
import com.code12.worldtp.worldtpobjects.WorldTPWorldGroup;
import com.code12.worldtp.files.ConfigManager;
import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;

public class CommandReloadWorlds implements CommandExecutor {

    WorldTP plugin;
    public DataManager data = References.data;
    public ConfigManager config = References.config;

    public CommandReloadWorlds(WorldTP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

        if(!sender.hasPermission("worldtp.reloadworlds")){
            sender.sendMessage(ChatColor.YELLOW + "You don't have the necessary permission to use this command.");
            return true;
        }

        MultiverseCore core = References.core;
        MVWorldManager mvWorldManager = core.getMVWorldManager();

        Collection<MultiverseWorld> multiverseWorldList = mvWorldManager.getMVWorlds();

        /* Iterate through each world and then adds the world based on its name. */
        //gets a list of all the overworlds.

        data.getConfig().set("menuGroupList", null);

        ArrayList<String> menuGroupList = new ArrayList<>();
        for(MultiverseWorld multiverseWorld : multiverseWorldList){
            String world = multiverseWorld.getName();
            WorldTPWorld worldTPWorld = new WorldTPWorld(plugin, world);

            if(worldTPWorld.getWorldType().equals("overworld")){
                menuGroupList.add(world);
            }
        }

        //gets the menuGroupList and registers them with WorldTPWorldGroup.registerWorldGroup()
        for(String worldGroup : menuGroupList){
            ItemStack item = new ItemStack(Material.GRASS_BLOCK);
            String displayName = worldGroup;
            if(data.getConfig().getItemStack("menuGroupID." + worldGroup + ".item") != null){
                item = data.getConfig().getItemStack("menuGroupID." + worldGroup + ".item");
            }
            if(data.getConfig().getString("menuGroupID." + worldGroup + ".displayName") != null){
                displayName = data.getConfig().getString("menuGroupID." + worldGroup + ".displayName");
            }
            Boolean adminOnly = data.getConfig().getBoolean("menuGroupID." + worldGroup + ".admin");
            WorldTPWorldGroup worldTPWorldGroup = new WorldTPWorldGroup(plugin, worldGroup, displayName);
            worldTPWorldGroup.setItem(item);
            worldTPWorldGroup.setAdminOnly(adminOnly);
            worldTPWorldGroup.registerWorldGroup();
            data.saveConfig();

            setConfig(worldGroup);

            for(MultiverseWorld multiverseWorld : multiverseWorldList){
                String multiverseWorldName = multiverseWorld.getName();
                WorldTPWorld world = new WorldTPWorld(plugin, multiverseWorldName);
                if(world.getName().startsWith(worldGroup)){
                    String worldType = world.getWorldType();
                    if(worldType.equals("overworld")){
                        data.getConfig().set("worldGroup." + worldGroup + ".overworld", world.getName());
                    }else if(worldType.equals("nether")){
                        data.getConfig().set("worldGroup." + worldGroup + ".nether", world.getName());
                    }else if(worldType.equals("the_end")){
                        data.getConfig().set("worldGroup." + worldGroup + ".the_end", world.getName());
                    }
                    data.saveConfig();
                }
            }
        }

        data.saveConfig();

        sender.sendMessage(ChatColor.YELLOW + "Reload of worlds Complete");

        return true;
    }

    private void setConfig(String worldName){
        if(config.getConfig().get(worldName + ".Spawn_Teleporting") == null){
            config.getConfig().set(worldName + ".Spawn_Teleporting", false);
        }
        if(config.getConfig().get(worldName + ".Nether_Teleporting") == null){
            config.getConfig().set(worldName + ".Nether_Teleporting", false);
        }
        if(config.getConfig().get(worldName + ".End_Teleporting") == null){
            config.getConfig().set(worldName + ".End_Teleporting", false);
        }
        config.saveConfig();
    }
}
