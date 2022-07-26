package com.code12.worldtp.gui.worldtpmenu;

import com.code12.worldtp.files.ConfigManager;
import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import com.code12.worldtp.gui.util.GuiMath;
import com.code12.worldtp.gui.util.ProcessItemStack;
import com.code12.worldtp.worldtpobjects.WorldTPWorld;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class WorldSelectionGui {

    DataManager data = References.data;
    ConfigManager config = References.config;

    @Getter
    private ChestGui gui;

    public WorldSelectionGui(Player player){
        // -------------------------------------------------------------------------------------------------------------
        // gui init
        // -------------------------------------------------------------------------------------------------------------
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
            menuGroupList = notAdminWorlds;
        }

        HashMap<String, Integer> worldPositionMap = new HashMap<>();
        menuGroupList.forEach(menuGroup -> worldPositionMap.put(menuGroup, data.getConfig().getInt("menuGroupID." + menuGroup + ".position")));

        menuGroupList = sortWorldsFromPositions(worldPositionMap);

        int rows = 1;
        int slots = 9;

        while (numberOfWorlds / slots > 1) {
            rows ++;
            slots += 9;
        }

        gui = new ChestGui(rows, "World Menu");
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        OutlinePane worldsPane = new OutlinePane(0, 0, 9, rows);

        // -------------------------------------------------------------------------------------------------------------
        // Add menuGroups to the worldsPane
        // -------------------------------------------------------------------------------------------------------------
        for (String menuGroup : menuGroupList) {
            Material material = data.getConfig().getItemStack("menuGroupID." + menuGroup + ".item").getType() != null ?
                    data.getConfig().getItemStack("menuGroupID." + menuGroup + ".item").getType() : Material.GRASS_BLOCK;

            String menuGroupDisplayName = data.getConfig().getString("menuGroupID." + menuGroup + ".displayName");

            ItemStack menuGroupItem = new ProcessItemStack().setMaterial(material)
                    .setDisplayName(menuGroupDisplayName)
                    .setItemFlags(List.of(ItemFlag.HIDE_ATTRIBUTES)).getItemStack();

            Boolean spawn = config.getConfig().getBoolean(menuGroup + ".Spawn_Teleporting");
            Boolean nether = config.getConfig().getBoolean(menuGroup + ".Nether_Teleporting");
            Boolean end = config.getConfig().getBoolean(menuGroup + ".End_Teleporting");

            GuiItem menuGroupGuiItem = new GuiItem(menuGroupItem, event -> {
                if(spawn || nether || end){
                    player.closeInventory();

                    DimensionsSelectionGui dimensionsSelectionGui = new DimensionsSelectionGui(Bukkit.getWorld(menuGroup), player);
                    dimensionsSelectionGui.getGui().show(player);
                } else{
                    Location playerLocation = player.getLocation();

                    WorldTPWorld worldGroupToLeave = new WorldTPWorld(player.getWorld());
                    String worldGroupToLeaveName = worldGroupToLeave.getWorldGroup();

                    Location locationToTP;

                    if (player.getWorld().getName().startsWith(menuGroup)){
                        player.sendMessage(ChatColor.YELLOW + "You are already in the world: " + menuGroupDisplayName);
                        return;
                    }
                    else if (data.getConfig().getLocation("menuGroupID." + menuGroup + ".WorldTPWorldSpawnPoint") != null) {
                        locationToTP = data.getConfig().getLocation("menuGroupID." + menuGroup + ".WorldTPWorldSpawnPoint");
                    }
                    else if (data.getConfig().getLocation("playerLocations." + player.getName() + "." + menuGroup) != null) {
                        locationToTP = data.getConfig().getLocation("playerLocations." + player.getName() + "." + menuGroup);
                    }
                    else{
                        locationToTP = Bukkit.getWorld(menuGroup).getSpawnLocation();
                    }

                    data.getConfig().set("playerLocations." + player.getName() + "." + worldGroupToLeaveName, playerLocation);
                    data.saveConfig();

                    player.teleport(locationToTP);
                }
            });

            worldsPane.addItem(menuGroupGuiItem);
        }
        // -------------------------------------------------------------------------------------------------------------
        // Add panes to gui
        // -------------------------------------------------------------------------------------------------------------
        gui.addPane(worldsPane);
    }

    private List<String> sortWorldsFromPositions(HashMap<String, Integer> map){
        List<Map.Entry<String, Integer>> list = new LinkedList<>(map.entrySet());

        list.sort(Map.Entry.comparingByValue());

        ArrayList<String> temp = new ArrayList<>();
        list.forEach(entry -> temp.add(entry.getKey()));

        return temp;
    }
}
