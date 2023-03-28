package com.code12.worldtp.gui.worldtpmenu;

import com.code12.worldtp.files.ConfigManager;
import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import com.code12.worldtp.gui.util.ProcessItemStack;
import com.code12.worldtp.gui.util.TeleportUtils;
import com.code12.worldtp.worlds.WorldTPWorld;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

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
            Material material = data.getConfig().get("menuGroupID." + menuGroup + ".material") != null ?
                    Material.valueOf(data.getConfig().getString("menuGroupID." + menuGroup + ".material")) : Material.GRASS_BLOCK;

            String menuGroupDisplayName = data.getConfig().getString("menuGroupID." + menuGroup + ".displayName");

            ItemStack menuGroupItem = new ProcessItemStack().setMaterial(material)
                    .setDisplayName(menuGroupDisplayName)
                    .setItemFlags(List.of(ItemFlag.HIDE_ATTRIBUTES)).getItemStack();

            Boolean spawn = config.getConfig().getBoolean(menuGroup + ".Spawn_Teleporting");
            Boolean nether = config.getConfig().getBoolean(menuGroup + ".Nether_Teleporting");
            Boolean end = config.getConfig().getBoolean(menuGroup + ".End_Teleporting");

            GuiItem menuGroupGuiItem = new GuiItem(menuGroupItem, event -> {
                WorldTPWorld worldGroupToLeave = new WorldTPWorld(player.getWorld());
                String worldGroupToLeaveName = worldGroupToLeave.getWorldGroup();

                if((spawn || nether || end)){
                    if(!worldGroupToLeaveName.equals(menuGroup) && (data.getConfig()
                            .getLocation("menuGroupID." + menuGroup + ".WorldTPWorldSpawnPoint") != null)){
                        Location locationToTP = TeleportUtils.resume(player, menuGroup);

                        if(locationToTP != null) TeleportUtils.teleport(player, locationToTP, worldGroupToLeaveName, menuGroup);
                    }else{
                        DimensionsSelectionGui dimensionsSelectionGui = new DimensionsSelectionGui(Bukkit.getWorld(menuGroup), player);
                        dimensionsSelectionGui.getGui().show(player);
                    }
                } else{
                    Location locationToTP = TeleportUtils.resume(player, menuGroup);

                    if(locationToTP != null) TeleportUtils.teleport(player, locationToTP, worldGroupToLeaveName, menuGroup);
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
