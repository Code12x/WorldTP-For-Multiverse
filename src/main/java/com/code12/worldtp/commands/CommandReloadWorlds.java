package com.code12.worldtp.commands;

import com.code12.worldtp.files.ConfigManager;
import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import com.code12.worldtp.gui.util.ProcessItemStack;
import com.code12.worldtp.worldtpobjects.WorldTPWorld;
import com.code12.worldtp.worldtpobjects.WorldTPWorldGroup;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;

public class CommandReloadWorlds implements CommandExecutor {

    private final DataManager data = References.data;
    private final ConfigManager config = References.config;

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

        ArrayList<String> menuGroupList = new ArrayList<>();
        for(MultiverseWorld multiverseWorld : multiverseWorldList){
            World world = Bukkit.getWorld(multiverseWorld.getName());
            WorldTPWorld worldTPWorld = new WorldTPWorld(world);

            if(worldTPWorld.getWorldType().equals("overworld")){
                menuGroupList.add(world.getName());
            }
        }

        if(menuGroupList.size() > 54){
            sender.sendMessage(ChatColor.RED + "ERROR!" + ChatColor.RESET + " There are too many worlds loaded for this server." +
                    "WorldTP only supports up to 54 worlds, because it is highly unlikely that anyone will have more than 54 worlds" +
                    "in one server. If you have any questions about this, submit an issue on the github issues page.");
            return true;
        }

        //gets the menuGroupList and registers them with WorldTPWorldGroup.registerWorldGroup()
        data.getConfig().set("menuGroupList", null);
        data.saveConfig();

        for(String worldGroup : menuGroupList) {
            Material material;
            String displayName;
            boolean adminOnly;
            int position = menuGroupList.indexOf(worldGroup);

            if (data.getConfig().get("menuGroupID." + worldGroup) != null) { // worldGroup HAS been registered before
                material = Material.valueOf(data.getConfig().getString("menuGroupID." + worldGroup + ".material"));
                displayName = data.getConfig().getString("menuGroupID." + worldGroup + ".displayName");
                adminOnly = data.getConfig().getBoolean("menuGroupID." + worldGroup + ".admin");
            } else { // worldGroup has NOT been registered before
                material = Material.GRASS_BLOCK;
                displayName = worldGroup;
                adminOnly = false;
            }

            WorldTPWorldGroup worldTPWorldGroup = new WorldTPWorldGroup(worldGroup);
            worldTPWorldGroup.setMaterial(material);
            worldTPWorldGroup.setDisplayName(displayName);
            worldTPWorldGroup.setAdminOnly(adminOnly);
            worldTPWorldGroup.setPosition(position);

            worldTPWorldGroup.registerWorldGroup();

            setConfig(worldGroup);

            for (MultiverseWorld multiverseWorld : multiverseWorldList) {
                WorldTPWorld world = new WorldTPWorld(Bukkit.getWorld(multiverseWorld.getName()));
                if (world.getName().startsWith(worldGroup)) {
                    String worldType = world.getWorldType();
                    switch (worldType) {
                        case "overworld" ->
                                data.getConfig().set("worldGroup." + worldGroup + ".overworld", world.getName());
                        case "nether" -> data.getConfig().set("worldGroup." + worldGroup + ".nether", world.getName());
                        case "the_end" ->
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
