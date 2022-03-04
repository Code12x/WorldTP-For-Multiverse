package com.code12.worldtp.commands;

import com.code12.worldtp.WorldTP;
import com.code12.worldtp.files.ConfigManager;
import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import com.code12.worldtp.menues.AdvancedWorldTPMenu;
import com.code12.worldtp.menues.WorldTPMenu;
import com.code12.worldtp.worldtpobjects.WorldTPWorld;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandWorldTP implements CommandExecutor {
    WorldTP plugin;

    public DataManager data = References.data;
    public ConfigManager config = References.config;

    public CommandWorldTP(WorldTP plugin) {
        this.plugin = plugin;
    }

    // the command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (data.getConfig().getStringList("worldList").isEmpty()) {
                player.sendMessage("The worlds have not been registered on WorldTP. To register the worlds, have an admin run the command /reloadworlds.");
                return true;
            }

            // worlds info
            Boolean hasAccess = false;
            int numberOfWorlds;
            List<String> worldList = data.getConfig().getStringList("worldList");

            if (player.hasPermission("worldtp.worldtp")) {
                hasAccess = true;

                numberOfWorlds = worldList.size();
            }else {
                ArrayList<String> notAdminWorlds = new ArrayList<String>();

                for(String world : worldList){
                    if(!data.getConfig().getBoolean("worldID." + world + ".admin")){
                        notAdminWorlds.add(world);
                    }
                }

                numberOfWorlds = notAdminWorlds.size();
            }


            int rows = 1;
            int slots = 9;

            while (numberOfWorlds / slots > 1) {
                rows ++;
                slots += 9;
            }

            // gui info
            ChestGui gui;
            Boolean hasPages = false;

            if(rows >= 9) {
                gui = new ChestGui(9, "World Menu");
                hasPages = true;
            } else {
                gui = new ChestGui(rows, "World Menu");
            }

            gui.setOnGlobalClick(event -> event.setCancelled(true));

            // main task
            if(hasPages){
                //TODO: Make the configuration for when there are more than 36 worlds
            } else {
                OutlinePane mainPain = new OutlinePane(0, 0, 9, rows);

                for (String world : worldList) {
                    ItemStack itemStack = new ItemStack(Material.GRASS_BLOCK);

                    if(data.getConfig().getItemStack("worldID." + world + ".iten") != null){
                        itemStack = data.getConfig().getItemStack("worldID." + world + ".iten");
                    }

                    String worldToLeaveName = player.getWorld().getName();
                    WorldTPWorld worldToLeave = new WorldTPWorld(plugin, worldToLeaveName);
                    String worldGroupToLeave = worldToLeave.getWorldGroup();

                    Boolean spawn = config.getConfig().getBoolean(world + ".Spawn_Teleporting");
                    Boolean nether = config.getConfig().getBoolean(world + ".Nether_Teleporting");
                    Boolean end = config.getConfig().getBoolean(world + ".End_Teleporting");

                    Location playerLocation = player.getLocation();

                    GuiItem item = new GuiItem(itemStack, event -> {

                        Location locationToTP = null;

                        if(spawn || nether || end){

                        } else{
                            if (data.getConfig().getLocation("playerLocations." + player.getName() + "." + world) != null) {
                                locationToTP = data.getConfig().getLocation("playerLocations." + player.getName() + "." + world);
                            }

                            if (data.getConfig().getLocation("worldID." + world + ".WorldTPWorldSpawnPoint") != null) {
                                locationToTP = data.getConfig().getLocation("worldID." + world + ".WorldTPWorldSpawnPoint");
                            }

                            if (playerLocation.getWorld().getName().startsWith(world)){
                                player.sendMessage(ChatColor.YELLOW + "You are already in the world: " + worldGroupToLeave);
                                return;
                            }

                            if(locationToTP == null) {
                                if (player.getBedSpawnLocation() != null) {
                                    locationToTP = player.getBedSpawnLocation();
                                } else{
                                    World bukkitWorld = Bukkit.getWorld(world);
                                    locationToTP = bukkitWorld.getSpawnLocation();
                                }
                            }
                            player.teleport(locationToTP);
                        }
                    });

                    mainPain.addItem(item);
                }

                gui.update();
                gui.show(player);
            }

        }
        return true;
    }
}
