package com.code12.worldtp.gui.worldtpmenu;

import com.code12.worldtp.files.ConfigManager;
import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import com.code12.worldtp.worldtpobjects.WorldTPWorld;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DimensionsSelectionGui {
    private final ConfigManager config = References.config;
    private final DataManager data = References.data;

    @Getter
    private ChestGui gui;

    public DimensionsSelectionGui(World worldGroup, Player player){
        Location playerLocation = player.getLocation();

        String worldGroupName = worldGroup.getName();
        String worldGroupDisplayName = data.getConfig().getString("menuGroupID." + worldGroupName + ".displayName");

        WorldTPWorld worldToLeave = new WorldTPWorld(player.getWorld());
        String worldGroupToLeave = worldToLeave.getWorldGroup();

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
            ItemStack spawnItem = processItemStack(Material.GRASS_BLOCK, "Spawn");

            GuiItem guiItem = new GuiItem(spawnItem, event -> {
                if (data.getConfig().getString("worldGroup." + worldGroupName + ".overworld") == null) {
                    player.sendMessage(ChatColor.YELLOW + "That place does not exist.");
                    return;
                }

                player.teleport(Bukkit.getWorld(data.getConfig().getString("worldGroup." + worldGroupName + ".overworld")).getSpawnLocation());

                data.getConfig().set("playerLocations." + player.getName() + "." + worldGroupToLeave, playerLocation);

                player.sendMessage(ChatColor.DARK_AQUA + "You have been teleported to " + worldGroupName + ".");
                data.saveConfig();
            });

            dimensionsPane.addItem(guiItem, Math.abs(numberOfDimensionsLeft - numberOfDimensions), 0);
            numberOfDimensionsLeft --;
        }

        // -------------------------------------------------------------------------------------------------------------
        // Nether GuiItem
        // -------------------------------------------------------------------------------------------------------------
        if(allowNether){
            ItemStack netherItem = processItemStack(Material.NETHERRACK, "Nether");

            GuiItem guiItem = new GuiItem(netherItem, event -> {
                if(data.getConfig().getString("worldGroup." + worldGroupName + ".nether") == null){
                   player.sendMessage(ChatColor.YELLOW + "That place doesn't exist.");
                   return;
                }
                player.teleport(Bukkit.getWorld(data.getConfig().getString("worldGroup." + worldGroupName + ".nether")).getSpawnLocation());

                data.getConfig().set("playerLocations." + player.getName() + "." + worldGroupToLeave, playerLocation);

                player.sendMessage(ChatColor.DARK_AQUA + "You have been teleported to " + worldGroupDisplayName + ".");
                data.saveConfig();
            });

            dimensionsPane.addItem(guiItem, Math.abs(numberOfDimensionsLeft - numberOfDimensions), 0);
            numberOfDimensionsLeft --;
        }

        // -------------------------------------------------------------------------------------------------------------
        // End GuiItem
        // -------------------------------------------------------------------------------------------------------------
        if(allowEnd){
            ItemStack endItem = processItemStack(Material.END_STONE, "End");

            GuiItem guiItem = new GuiItem(endItem, event -> {
                if(data.getConfig().getString("worldGroup." + worldGroupName + ".the_end") == null){
                    player.sendMessage(ChatColor.YELLOW + "That place doesn't exist.");
                    return;
                }

                player.teleport(Bukkit.getWorld(data.getConfig().getString("worldGroup." + worldGroupName + ".the_end")).getSpawnLocation());

                data.getConfig().set("playerLocations." + player.getName() + "." + worldGroupToLeave, playerLocation);
                data.saveConfig();

                player.sendMessage(ChatColor.DARK_AQUA + "You have been teleported to " + worldGroupDisplayName + ".");
            });

            dimensionsPane.addItem(guiItem, Math.abs(numberOfDimensionsLeft - numberOfDimensions), 0);
            numberOfDimensionsLeft --;
        }

        // -------------------------------------------------------------------------------------------------------------
        // Resume GuiItem
        // -------------------------------------------------------------------------------------------------------------
        ItemStack resumeItem = processItemStack(Material.LEATHER_BOOTS, "Resume");

        GuiItem resumeGuiItem = new GuiItem(resumeItem, event -> {
            Location locationToTP;

            if (player.getWorld().getName().startsWith(worldGroupName)){
                player.sendMessage(ChatColor.YELLOW + "You are already in the world: " + worldGroupDisplayName);
                return;
            }
            else if (data.getConfig().getLocation("menuGroupID." + worldGroupName + ".WorldTPWorldSpawnPoint") != null) {
                locationToTP = data.getConfig().getLocation("menuGroupID." + worldGroupName + ".WorldTPWorldSpawnPoint");
            }
            else if (data.getConfig().getLocation("playerLocations." + player.getName() + "." + worldGroupName) != null) {
                locationToTP = data.getConfig().getLocation("playerLocations." + player.getName() + "." + worldGroupName);
            }
            else{
                locationToTP = Bukkit.getWorld(worldGroupName).getSpawnLocation();
            }

            data.getConfig().set("playerLocations." + player.getName() + "." + worldGroupToLeave, playerLocation);
            data.saveConfig();

            player.teleport(locationToTP);
        });

        dimensionsPane.addItem(resumeGuiItem, numberOfDimensions, 0);

        //--------------------------------------------------------------------------------------------------------------
        // Navigation Pane
        // -------------------------------------------------------------------------------------------------------------
        StaticPane navigationPane = new StaticPane(0, 1, 9, 1);

        ItemStack backItem = processItemStack(Material.ARROW, "Back");

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

    public ItemStack processItemStack(Material material, String name){
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.setDisplayName(name);
        item.setItemMeta(itemMeta);
        return item;
    }
}
