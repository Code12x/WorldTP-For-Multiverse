package com.code12.worldtp.gui.settings;

import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import com.code12.worldtp.gui.util.ProcessItemStack;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingsGui {

    DataManager data = References.data;

    @Getter
    ChestGui gui;

    public SettingsGui(Player player, World world){
        // -------------------------------------------------------------------------------------------------------------
        // gui init
        // -------------------------------------------------------------------------------------------------------------
        String worldName = world.getName();

        gui = new ChestGui(4, "Settings for " + worldName);
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        // -------------------------------------------------------------------------------------------------------------
        // navigationPane
        // -------------------------------------------------------------------------------------------------------------
        StaticPane navigationPane = new StaticPane(0, 3, 9, 1);

        GuiItem backArrowGuiItem = new GuiItem(new ProcessItemStack()
                .setMaterial(Material.ARROW)
                .setDisplayName("Back")
                .getItemStack()
                , event -> {
            SelectWorldToEditGui selectWorldToEditGui = new SelectWorldToEditGui(player);
            selectWorldToEditGui.getGui().show(player);
        });

        navigationPane.addItem(backArrowGuiItem, 0, 0);
        gui.addPane(navigationPane);

        // -------------------------------------------------------------------------------------------------------------
        // settingsPane
        // -------------------------------------------------------------------------------------------------------------
        StaticPane settingsPane = new StaticPane(0, 0, 9, 3);

        // -------------------------------------------------------------------------------------------------------------
        // displayNameGuiItem
        // -------------------------------------------------------------------------------------------------------------
        String displayName = data.getConfig().getString("menuGroupID." + worldName + ".displayName");
        GuiItem displayNameGuiItem = new GuiItem(new ProcessItemStack()
                .setMaterial(Material.OAK_SIGN)
                .setDisplayName("Change Display Name (currently: " + displayName + ")")
                .getItemStack(),
                event -> {
            DisplayNameGui textInputGui = new DisplayNameGui("Enter the Name to be Displayed", worldName);
            textInputGui.getGui().show(event.getWhoClicked());
        });

        settingsPane.addItem(displayNameGuiItem, 1, 1);

        // -------------------------------------------------------------------------------------------------------------
        // displayItemGuiItem
        // -------------------------------------------------------------------------------------------------------------
        ItemStack displayItem = data.getConfig().getItemStack("menuGroupID." + worldName + ".item");
        ItemMeta displayItemMeta = displayItem.getItemMeta();
        displayItemMeta.setDisplayName("Change Display Item");
        displayItem.setItemMeta(displayItemMeta);

        GuiItem displayItemGuiItem = new GuiItem(displayItem, event -> {
            DisplayItemGui displayItemGui = new DisplayItemGui(world);
            displayItemGui.getGui().show(player);
        });

        settingsPane.addItem(displayItemGuiItem, 3, 1);

        // -------------------------------------------------------------------------------------------------------------
        // whitelistGuiItem + whitelistInfoGuiItem
        // -------------------------------------------------------------------------------------------------------------
        Boolean adminOnly = data.getConfig().getBoolean("menuGroupID." + worldName + ".admin");

        ItemStack whitelistItem =new ProcessItemStack()
                .setMaterial(Material.PAPER)
                .setDisplayName("Toggle Whitelist (Currently: " + (adminOnly ? "ON" : "OFF") + ")")
                .getItemStack();

        GuiItem whitelistGuiItem = new GuiItem(whitelistItem, event -> {
            data.getConfig().set("menuGroupID." + worldName + ".admin", !adminOnly);
            data.saveConfig();
            SettingsGui settingsGui = new SettingsGui(player, world);
            settingsGui.getGui().show(player);
        });

        ArrayList<String> lore = new ArrayList<>();
        lore.add("Players without the permission");
        lore.add("\"worldtp.worldtp\" will not be");
        lore.add("able to see this world in the");
        lore.add("WorldTP menu if this world's");
        lore.add("whitelist is on");

        ItemStack whitelistInfoItem = new ProcessItemStack()
                .setMaterial(Material.BOOK)
                .setDisplayName("About WorldTP Whitelist")
                .setItemFlags(List.of(ItemFlag.HIDE_ATTRIBUTES))
                .setLore(lore)
                .getItemStack();

        GuiItem whitelistInfoGuiItem = new GuiItem(whitelistInfoItem);

        settingsPane.addItem(whitelistGuiItem, 5, 1);
        settingsPane.addItem(whitelistInfoGuiItem, 5, 2);

        // -------------------------------------------------------------------------------------------------------------
        // dimensionsConfigGuiItem
        // -------------------------------------------------------------------------------------------------------------
        ItemStack dimensionsConfigItem = new ProcessItemStack()
                .setMaterial(Material.END_PORTAL_FRAME)
                .setDisplayName("Dimensions Configuration")
                .getItemStack();

        GuiItem dimensionsConfigGuiItem = new GuiItem(dimensionsConfigItem, event -> {
            DimensionsConfigGui dimensionsConfigGui = new DimensionsConfigGui(player, world);

            dimensionsConfigGui.getGui().show(player);
        });

        settingsPane.addItem(dimensionsConfigGuiItem, 7, 1);

        // -------------------------------------------------------------------------------------------------------------
        // Add settingsPane to gui
        // -------------------------------------------------------------------------------------------------------------
        gui.addPane(settingsPane);
    }
}
