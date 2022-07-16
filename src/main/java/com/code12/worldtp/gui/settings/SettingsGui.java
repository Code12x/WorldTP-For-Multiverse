package com.code12.worldtp.gui.settings;

import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SettingsGui {

    DataManager data = References.data;

    public SettingsGui(Player player, World world){
        String worldName = world.getName();

        ChestGui gui = new ChestGui(4, "Settings for " + worldName);
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        // Navigation Pane
        StaticPane navigationPane = new StaticPane(0, 3, 9, 1);

        GuiItem backArrowGuiItem = new GuiItem(processItemStack(Material.ARROW, "Back"), event -> {
            new SelectWorldToEditGui((Player) event.getWhoClicked());
        });

        GuiItem closeGuiItem = new GuiItem(processItemStack(Material.RED_WOOL, "Close"), event -> {
            event.getView().close();
        });

        navigationPane.addItem(backArrowGuiItem, 0, 0);
        navigationPane.addItem(closeGuiItem, 8, 0);
        gui.addPane(navigationPane);
        //================

        // MainPane with the options. (DisplayName, ItemToBeDisplayed, adminOnly, Dimensions)
        StaticPane mainPane = new StaticPane(0, 0, 9, 3);

        // -------------------------------------------------------------------------------------------------------------
        String displayName = data.getConfig().getString("menuGroupID." + worldName + ".displayName");
        GuiItem displayNameGuiItem = new GuiItem(processItemStack(Material.OAK_SIGN, "Change Display Name (currently: " + displayName + ")"), event -> {
            DisplayNameGui textInputGui = new DisplayNameGui("Enter the Name to be Displayed", worldName);
            textInputGui.getGui().show(event.getWhoClicked());
        });
        // -------------------------------------------------------------------------------------------------------------
        ItemStack displayItem = data.getConfig().getItemStack("menuGroupID." + worldName + ".item");
        ItemMeta displayItemMeta = displayItem.getItemMeta();
        displayItemMeta.setDisplayName("Change Display Item");
        displayItem.setItemMeta(displayItemMeta);

        GuiItem displayItemGuiItem = new GuiItem(displayItem, event -> {
            DisplayItemGui displayItemGui = new DisplayItemGui(world);
            displayItemGui.getGui().show(player);
        });
        // -------------------------------------------------------------------------------------------------------------
        Boolean adminOnly = data.getConfig().getBoolean("menuGroupID." + worldName + ".admin");

        ItemStack whitelistItem = processItemStack(Material.PAPER, "Toggle Whitelist (Currently: " + (adminOnly ? "ON" : "OFF") + ")");

        GuiItem whitelistGuiItem = new GuiItem(whitelistItem, event -> {
            data.getConfig().set("menuGroupID." + worldName + ".admin", !adminOnly);
            data.saveConfig();
            new SettingsGui(player, world);
        });
        // -------------------------------------------------------------------------------------------------------------
        ItemStack configItem = processItemStack(Material.END_PORTAL_FRAME, "Dimensions Configuration");

        GuiItem dimensionsConfigGuiItem = new GuiItem(configItem, event -> {
            DimensionsConfigGui dimensionsConfigGui = new DimensionsConfigGui(player, world);

            dimensionsConfigGui.getGui().show(player);
        });
        // =============================================================================================================
        mainPane.addItem(displayNameGuiItem, 1, 1);
        mainPane.addItem(displayItemGuiItem, 3, 1);
        mainPane.addItem(whitelistGuiItem, 5, 1);
        mainPane.addItem(dimensionsConfigGuiItem, 7, 1);

        gui.addPane(mainPane);
        gui.show(player);
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
