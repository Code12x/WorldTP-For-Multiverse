package com.code12.worldtp.gui.settings;

import com.code12.worldtp.files.ConfigManager;
import com.code12.worldtp.files.References;
import com.code12.worldtp.gui.DimensionsSelectionGui;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DimensionsConfigGui {
    ConfigManager config = References.config;
    ChestGui gui;

    public DimensionsConfigGui(Player player, World world){
        gui = new ChestGui(2, "Configure Dimensions for " + world.getName());
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        OutlinePane configPane = new OutlinePane(0, 0, 9, 1);

        String[] dimensions = {"Spawn", "Nether", "End"};

        for (String dimension : dimensions){
            ItemStack itemStack;
            if(dimension.equals("Spawn")){
                itemStack = processItemStack(Material.GRASS_BLOCK, "Allow Spawn (Currently: " +
                        config.getConfig().getBoolean(world.getName() + ".Spawn_Teleporting") + ")");
            }

            else if(dimension.equals("Nether")){
                itemStack = processItemStack(Material.NETHERRACK, "Allow Nether (Currently: " +
                        config.getConfig().getBoolean(world.getName() + ".Nether_Teleporting") + ")");
            }

            else if(dimension.equals("End")){
                itemStack = processItemStack(Material.END_STONE, "Allow End (Currently: " +
                        config.getConfig().getBoolean(world.getName() + ".End_Teleporting") + ")");
            }

            else{
                itemStack = processItemStack(Material.BARRIER, "ERROR");
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

        StaticPane navigationPane = new StaticPane(0, 1, 9, 1);

        navigationPane.addItem(new GuiItem(processItemStack(Material.ARROW, "Back"), event -> {
            new SettingsGui(player, world);
        }), 0, 0);

        gui.addPane(configPane);
        gui.addPane(navigationPane);
    }

    public ChestGui getGui(){
        return gui;
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
