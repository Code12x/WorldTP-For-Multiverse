package com.code12.worldtp.gui;

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
    public ChestGui gui;

    public DimensionsSelectionGui(World worldGroup){
        String worldGroupName = worldGroup.getName();

        gui = new ChestGui(1, "Dimension");
        gui.setOnGlobalClick(event -> event.setCancelled(true));
        StaticPane pane = new StaticPane(0, 0, 9, 1);

        boolean allowSpawn = config.getConfig().getBoolean(worldGroupName + ".Spawn_Teleporting");
        boolean allowNether = config.getConfig().getBoolean(worldGroupName + ".Nether_Teleporting");
        boolean allowEnd = config.getConfig().getBoolean(worldGroupName + ".End_Teleporting");

        int numberOfDimensions = 0;
        if(allowSpawn) numberOfDimensions++;
        if(allowNether) numberOfDimensions++;
        if(allowEnd) numberOfDimensions ++;

        int numberOfDimensionsLeft = numberOfDimensions;

        if(allowSpawn){
            ItemStack itemStack = new ItemStack(Material.GRASS_BLOCK);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemMeta.setDisplayName(worldGroupName + " Spawn");
            itemStack.setItemMeta(itemMeta);

            GuiItem guiItem = new GuiItem(itemStack, event -> {
                Player player = (Player) event.getWhoClicked();

                WorldTPWorld worldToLeave = new WorldTPWorld(player.getWorld());
                String worldGroupToLeave = worldToLeave.getWorldGroup();

                Location playerLocation = player.getLocation();

                if (data.getConfig().getString("worldGroup." + worldGroupName + ".overworld") == null) {
                    player.sendMessage(ChatColor.YELLOW + "That place does not exist.");
                    return;
                }

                player.teleport(Bukkit.getWorld(data.getConfig().getString("worldGroup." + worldGroupName + ".overworld")).getSpawnLocation());

                data.getConfig().set("playerLocations." + player.getName() + "." + worldGroupToLeave, playerLocation);

                player.sendMessage(ChatColor.DARK_AQUA + "You have been teleported to " + worldGroupName + ".");
                data.saveConfig();
            });

            pane.addItem(guiItem, Math.abs(numberOfDimensionsLeft - numberOfDimensions), 0);
            numberOfDimensionsLeft --;
        }

        if(allowNether){
            ItemStack itemStack = new ItemStack(Material.NETHERRACK);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemMeta.setDisplayName( worldGroupName + " Nether");
            itemStack.setItemMeta(itemMeta);

            GuiItem guiItem = new GuiItem(itemStack, event -> {
                Player player = (Player) event.getWhoClicked();

                WorldTPWorld worldToLeave = new WorldTPWorld(player.getWorld());
                String worldGroupToLeave = worldToLeave.getWorldGroup();

                Location playerLocation = player.getLocation();

                if(data.getConfig().getString("worldGroup." + worldGroupName + ".nether") == null){
                   player.sendMessage(ChatColor.YELLOW + "That place doesn't exist.");
                   return;
               }

               player.teleport(Bukkit.getWorld(data.getConfig().getString("worldGroup." + worldGroupName + ".nether")).getSpawnLocation());

                data.getConfig().set("playerLocations." + player.getName() + "." + worldGroupToLeave, playerLocation);

                player.sendMessage(ChatColor.DARK_AQUA + "You have been teleported to " + worldGroupName + ".");
                data.saveConfig();
            });

            pane.addItem(guiItem, Math.abs(numberOfDimensionsLeft - numberOfDimensions), 0);
            numberOfDimensionsLeft --;
        }

        if(allowEnd){
            ItemStack itemStack = new ItemStack(Material.END_STONE);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemMeta.setDisplayName( worldGroupName + " End");
            itemStack.setItemMeta(itemMeta);

            GuiItem guiItem = new GuiItem(itemStack, event -> {
                Player player = (Player) event.getWhoClicked();

                WorldTPWorld worldToLeave = new WorldTPWorld(player.getWorld());
                String worldGroupToLeave = worldToLeave.getWorldGroup();

                Location playerLocation = player.getLocation();

                if(data.getConfig().getString("worldGroup." + worldGroupName + ".the_end") == null){
                    player.sendMessage(ChatColor.YELLOW + "That place doesn't exist.");
                    return;
                }

                player.teleport(Bukkit.getWorld(data.getConfig().getString("worldGroup." + worldGroupName + ".the_end")).getSpawnLocation());

                data.getConfig().set("playerLocations." + player.getName() + "." + worldGroupToLeave, playerLocation);

                player.sendMessage(ChatColor.DARK_AQUA + "You have been teleported to " + worldGroupName + ".");
                data.saveConfig();
            });

            pane.addItem(guiItem, Math.abs(numberOfDimensionsLeft - numberOfDimensions), 0);
            numberOfDimensionsLeft --;
        }

        gui.addPane(pane);
    }
}
