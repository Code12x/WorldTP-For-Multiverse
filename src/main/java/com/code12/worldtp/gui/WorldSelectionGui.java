package com.code12.worldtp.gui;

import com.code12.worldtp.files.ConfigManager;
import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import com.code12.worldtp.worldtpobjects.WorldTPWorld;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class WorldSelectionGui {

    DataManager data = References.data;
    ConfigManager config = References.config;

    @Getter
    public ChestGui gui;

    public WorldSelectionGui(Player player){
        int numberOfWorlds;
        List<String> menuGroupList = data.getConfig().getStringList("menuGroupList");

        if (player.hasPermission("worldtp.worldtp")) {
            numberOfWorlds = menuGroupList.size();
        }else {
            ArrayList<String> notAdminWorlds = new ArrayList<>();

            for(String world : menuGroupList){
                if(!data.getConfig().getBoolean("menuGroupID." + world + ".admin")){
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
        gui = new ChestGui(rows, "World Menu");
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        // main task
        OutlinePane mainPain = new OutlinePane(0, 0, 9, rows);

        for (String menuGroup : menuGroupList) {
            ItemStack itemStack = new ItemStack(Material.GRASS_BLOCK);

            if(data.getConfig().getItemStack("menuGroupID." + menuGroup + ".item") != null){
                itemStack = data.getConfig().getItemStack("menuGroupID." + menuGroup + ".item");
            }

            World playerWorld = player.getWorld();
            WorldTPWorld worldToLeave = new WorldTPWorld(playerWorld);
            String worldGroupToLeave = worldToLeave.getWorldGroup();

            Boolean spawn = config.getConfig().getBoolean(menuGroup + ".Spawn_Teleporting");
            Boolean nether = config.getConfig().getBoolean(menuGroup + ".Nether_Teleporting");
            Boolean end = config.getConfig().getBoolean(menuGroup + ".End_Teleporting");

            GuiItem item = new GuiItem(itemStack, event -> {

                Player eventPlayer = (Player) event.getWhoClicked();
                Location playerLocation = eventPlayer.getLocation();

                Location locationToTP = null;

                if(spawn || nether || end){
                    eventPlayer.closeInventory();

                    new DimensionsSelectionGui(Bukkit.getWorld(menuGroup), eventPlayer);

                } else{
                    if (playerLocation.getWorld().getName().startsWith(menuGroup)){
                        eventPlayer.sendMessage(ChatColor.YELLOW + "You are already in the world: " + data.getConfig().getString("menuGroupID." + menuGroup + ".displayName"));
                        return;
                    }

                    if (data.getConfig().getLocation("PlayerLocations." + player.getName() + "." + menuGroup) != null) {
                        locationToTP = data.getConfig().getLocation("PlayerLocations." + player.getName() + "." + menuGroup);
                    }

                    if (data.getConfig().getLocation("worldGroupID." + menuGroup + ".WorldTPWorldSpawnPoint") != null) {
                        locationToTP = data.getConfig().getLocation("menuGroupID." + menuGroup + ".WorldTPWorldSpawnPoint");
                    }

                    if(locationToTP == null) {
                        if (eventPlayer.getBedSpawnLocation() != null) {
                            locationToTP = eventPlayer.getBedSpawnLocation();
                        } else{
                            World bukkitWorld = Bukkit.getWorld(menuGroup);
                            locationToTP = bukkitWorld.getSpawnLocation();
                        }
                    }
                    eventPlayer.teleport(locationToTP);
                }
            });
            mainPain.addItem(item);
        }
        gui.addPane(mainPain);
        gui.show(player);
    }
}
