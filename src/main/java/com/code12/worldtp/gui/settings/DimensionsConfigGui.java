package com.code12.worldtp.gui.settings;

import com.code12.worldtp.files.ConfigManager;
import com.code12.worldtp.files.References;
import com.code12.worldtp.gui.util.ProcessItemStack;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class DimensionsConfigGui {
    ConfigManager config = References.config;
    ChestGui gui;

    public DimensionsConfigGui(Player player, World world){
        // -------------------------------------------------------------------------------------------------------------
        // gui init
        // -------------------------------------------------------------------------------------------------------------
        gui = new ChestGui(2, "Configure Dimensions for " + world.getName());
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        OutlinePane configPane = new OutlinePane(0, 0, 9, 1);

        // -------------------------------------------------------------------------------------------------------------
        // Creating the config items and add them to the configPane
        // -------------------------------------------------------------------------------------------------------------
        String[] dimensions = {"Spawn", "Nether", "End"};

        for (String dimension : dimensions){
            ItemStack itemStack;
            if(dimension.equals("Spawn")){
                itemStack = new ProcessItemStack()
                        .setMaterial(Material.GRASS_BLOCK)
                        .setDisplayName("Allow Spawn (Currently: " +
                                config.getConfig().getBoolean(world.getName() + ".Spawn_Teleporting") + ")")
                        .getItemStack();
            }

            else if(dimension.equals("Nether")){
                itemStack = new ProcessItemStack()
                        .setMaterial(Material.NETHERRACK)
                        .setDisplayName("Allow Nether (Currently: " +
                                config.getConfig().getBoolean(world.getName() + ".Nether_Teleporting") + ")")
                        .getItemStack();
            }

            else if(dimension.equals("End")){
                itemStack =new ProcessItemStack()
                        .setMaterial(Material.END_STONE)
                        .setDisplayName("Allow End (Currently: " +
                                config.getConfig().getBoolean(world.getName() + ".End_Teleporting") + ")")
                        .getItemStack();
            }

            else{
                itemStack = new ProcessItemStack()
                        .setMaterial(Material.BARRIER)
                        .setDisplayName("ERROR")
                        .setChatColor(ChatColor.RED)
                        .setItemFlags(List.of(ItemFlag.HIDE_ATTRIBUTES))
                        .getItemStack();
            }

            GuiItem dimensionGuiItem = new GuiItem(itemStack, event -> {
                config.getConfig().set(world.getName() + "." + dimension + "_Teleporting", !config.getConfig().getBoolean(world.getName() +
                        "." + dimension + "_Teleporting"));
                config.saveConfig();

                DimensionsConfigGui dimensionsConfigGui = new DimensionsConfigGui(player, world);
                dimensionsConfigGui.getGui().show(player);
            });

            configPane.addItem(dimensionGuiItem);
        }

        // -------------------------------------------------------------------------------------------------------------
        // Navigation Pane
        // -------------------------------------------------------------------------------------------------------------
        StaticPane navigationPane = new StaticPane(0, 1, 9, 1);

        navigationPane.addItem(new GuiItem(new ProcessItemStack()
                .setMaterial(Material.ARROW)
                .setDisplayName("Back")
                .getItemStack(),
                event -> {
            SettingsGui settingsGui = new SettingsGui(player, world);
            settingsGui.getGui().show(player);
        }), 0, 0);

        // -------------------------------------------------------------------------------------------------------------
        // Adding the panes to the gui
        // -------------------------------------------------------------------------------------------------------------
        gui.addPane(configPane);
        gui.addPane(navigationPane);
    }

    public ChestGui getGui(){
        return gui;
    }
}
