package com.code12.worldtp.gui.worldtpmenu;

import com.code12.worldtp.files.ConfigManager;
import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import com.code12.worldtp.gui.util.ProcessItemStack;
import com.code12.worldtp.gui.util.TeleportUtils;
import com.code12.worldtp.worlds.WorldTPWorld;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class DimensionsSelectionGui {
    private final ConfigManager config = References.config;
    private final DataManager data = References.data;

    @Getter
    private ChestGui gui;

    public DimensionsSelectionGui(World worldGroup, Player player){
        String worldGroupName = worldGroup.getName();
        String worldGroupDisplayName = data.getConfig().getString("menuGroupID." + worldGroupName + ".displayName");

        WorldTPWorld worldToLeave = new WorldTPWorld(player.getWorld());
        String worldGroupToLeaveName = worldToLeave.getWorldGroup();

        gui = new ChestGui(2, "Dimensions for " + worldGroupDisplayName);
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        StaticPane dimensionsPane = new StaticPane(0, 0, 9, 1);

        boolean allowSpawn = config.getConfig().getBoolean(worldGroupName + ".Spawn_Teleporting");
        boolean allowNether = config.getConfig().getBoolean(worldGroupName + ".Nether_Teleporting");
        boolean allowEnd = config.getConfig().getBoolean(worldGroupName + ".End_Teleporting");

        int numberOfDimensions = 0;
        if(allowSpawn) numberOfDimensions++;
        if(allowNether) numberOfDimensions++;
        if(allowEnd) numberOfDimensions ++;

        int numberOfDimensionsLeft = numberOfDimensions;

        // -------------------------------------------------------------------------------------------------------------
        // Spawn GuiItem
        // -------------------------------------------------------------------------------------------------------------
        if(allowSpawn){
            ItemStack spawnItem = new ProcessItemStack()
                    .setMaterial(Material.GRASS_BLOCK)
                    .setDisplayName("Spawn")
                    .getItemStack();

            GuiItem guiItem = new GuiItem(spawnItem, event -> {
                if (data.getConfig().getString("worldGroup." + worldGroupName + ".overworld") == null) {
                    player.sendMessage(ChatColor.YELLOW + "That place does not exist.");
                    return;
                }

                Location locationToTP = Bukkit.getWorld(
                        data.getConfig().getString("worldGroup." + worldGroupName + ".overworld"))
                        .getSpawnLocation();

                TeleportUtils.teleport(player, locationToTP, worldGroupToLeaveName, worldGroupName);
            });

            dimensionsPane.addItem(guiItem, Math.abs(numberOfDimensionsLeft - numberOfDimensions), 0);
            numberOfDimensionsLeft --;
        }

        // -------------------------------------------------------------------------------------------------------------
        // Nether GuiItem
        // -------------------------------------------------------------------------------------------------------------
        if(allowNether){
            ItemStack netherItem = new ProcessItemStack()
                    .setMaterial(Material.NETHERRACK)
                    .setDisplayName("Nether")
                    .getItemStack();

            GuiItem guiItem = new GuiItem(netherItem, event -> {
                if(data.getConfig().getString("worldGroup." + worldGroupName + ".nether") == null){
                   player.sendMessage(ChatColor.YELLOW + "That place doesn't exist.");
                   return;
                }

                Location locationToTP = Bukkit.getWorld(
                        data.getConfig().getString("worldGroup." + worldGroupName + ".nether"))
                        .getSpawnLocation();

                TeleportUtils.teleport(player, locationToTP, worldGroupToLeaveName, worldGroupName);
            });

            dimensionsPane.addItem(guiItem, Math.abs(numberOfDimensionsLeft - numberOfDimensions), 0);
            numberOfDimensionsLeft --;
        }

        // -------------------------------------------------------------------------------------------------------------
        // End GuiItem
        // -------------------------------------------------------------------------------------------------------------
        if(allowEnd){
            ItemStack endItem = new ProcessItemStack()
                    .setMaterial(Material.END_STONE)
                    .setDisplayName("End")
                    .getItemStack();

            GuiItem guiItem = new GuiItem(endItem, event -> {
                if(data.getConfig().getString("worldGroup." + worldGroupName + ".the_end") == null){
                    player.sendMessage(ChatColor.YELLOW + "That place doesn't exist.");
                    return;
                }

                Location locationToTP = Bukkit.getWorld(
                        data.getConfig().getString("worldGroup." + worldGroupName + ".the_end"))
                        .getSpawnLocation();

                TeleportUtils.teleport(player, locationToTP, worldGroupToLeaveName, worldGroupName);
            });

            dimensionsPane.addItem(guiItem, Math.abs(numberOfDimensionsLeft - numberOfDimensions), 0);
            numberOfDimensionsLeft --;
        }

        // -------------------------------------------------------------------------------------------------------------
        // Resume GuiItem
        // -------------------------------------------------------------------------------------------------------------
        if(!worldGroupName.equals(worldGroupToLeaveName)) {
            ItemStack resumeItem = new ProcessItemStack()
                    .setMaterial(Material.LEATHER_BOOTS)
                    .setDisplayName("Resume")
                    .setItemFlags(List.of(ItemFlag.HIDE_ATTRIBUTES))
                    .getItemStack();

            GuiItem resumeGuiItem = new GuiItem(resumeItem, event -> {
                Location locationToTP = TeleportUtils.resume(player, worldGroupName);

                if(locationToTP != null) TeleportUtils.teleport(player, locationToTP, worldGroupToLeaveName, worldGroupName);
            });

            dimensionsPane.addItem(resumeGuiItem, numberOfDimensions, 0);
        }

        //--------------------------------------------------------------------------------------------------------------
        // Navigation Pane
        // -------------------------------------------------------------------------------------------------------------
        StaticPane navigationPane = new StaticPane(0, 1, 9, 1);

        ItemStack backItem = new ProcessItemStack()
                .setMaterial(Material.ARROW)
                .setDisplayName("Back")
                .getItemStack();

        GuiItem backGuiItem = new GuiItem(backItem, event -> {
            WorldSelectionGui worldSelectionGui = new WorldSelectionGui(player);
            worldSelectionGui.getGui().show(player);
        });

        navigationPane.addItem(backGuiItem, 0, 0);

        // =============================================================================================================
        // Add panes to gui
        // =============================================================================================================
        gui.addPane(dimensionsPane);
        gui.addPane(navigationPane);
    }
}
