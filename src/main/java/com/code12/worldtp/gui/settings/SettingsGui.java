package com.code12.worldtp.gui.settings;

import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
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

        GuiItem backArrowGuiItem = new GuiItem(processItemStack(Material.ARROW, "Back"), event -> {
            SelectWorldToEditGui selectWorldToEditGui = new SelectWorldToEditGui(player);
            selectWorldToEditGui.getGui().show(player);
        });

        GuiItem closeGuiItem = new GuiItem(processItemStack(Material.RED_WOOL, "Close"), event -> {
            event.getView().close();
        });

        navigationPane.addItem(backArrowGuiItem, 0, 0);
        navigationPane.addItem(closeGuiItem, 8, 0);
        gui.addPane(navigationPane);

        // -------------------------------------------------------------------------------------------------------------
        // settingsPane
        // -------------------------------------------------------------------------------------------------------------
        StaticPane settingsPane = new StaticPane(0, 0, 9, 3);

        // -------------------------------------------------------------------------------------------------------------
        // displayNameGuiItem
        // -------------------------------------------------------------------------------------------------------------
        String displayName = data.getConfig().getString("menuGroupID." + worldName + ".displayName");
        GuiItem displayNameGuiItem = new GuiItem(processItemStack(Material.OAK_SIGN, "Change Display Name (currently: " + displayName + ")"), event -> {
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

        ItemStack whitelistItem = processItemStack(Material.PAPER, "Toggle Whitelist (Currently: " + (adminOnly ? "ON" : "OFF") + ")");

        GuiItem whitelistGuiItem = new GuiItem(whitelistItem, event -> {
            data.getConfig().set("menuGroupID." + worldName + ".admin", !adminOnly);
            data.saveConfig();
            SettingsGui settingsGui = new SettingsGui(player, world);
            settingsGui.getGui().show(player);
        });

        GuiItem whitelistInfoGuiItem = new GuiItem(processItemStack(Material.BOOK, "Permission worldtp.worldtp is needed to select this world."));

        settingsPane.addItem(whitelistGuiItem, 5, 1);
        settingsPane.addItem(whitelistInfoGuiItem, 5, 2);

        // -------------------------------------------------------------------------------------------------------------
        // dimensionsConfigGuiItem
        // -------------------------------------------------------------------------------------------------------------
        ItemStack dimensionsConfigItem = processItemStack(Material.END_PORTAL_FRAME, "Dimensions Configuration");

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

    public ItemStack processItemStack(Material material, String name){
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.setDisplayName(name);
        item.setItemMeta(itemMeta);
        return item;
    }
}
