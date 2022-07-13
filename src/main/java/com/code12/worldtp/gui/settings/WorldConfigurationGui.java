package com.code12.worldtp.gui.settings;

import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import com.code12.worldtp.gui.util.TextInputGui;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WorldConfigurationGui {

    DataManager data = References.data;

    public WorldConfigurationGui(Player player, World world){
        String worldName = world.getName();

        ChestGui gui = new ChestGui(4, "Settings for " + worldName);

        // Navigation Pane
        StaticPane navigationPane = new StaticPane(0, 4, 9, 1);

        GuiItem backArrowGuiItem = new GuiItem(processItemStack(Material.ARROW, "Back"), event -> {
            new SettingsGui((Player) event.getWhoClicked());
        });
        navigationPane.addItem(backArrowGuiItem, 0, 0);
        //-----------------

        // MainPane with the options. (DisplayName, ItemToBeDisplayed, adminOnly, Dimensions)
        StaticPane mainPane = new StaticPane(0, 0, 9, 3);

        String displayName = data.getConfig().getString("MenuGroupID." + worldName + ".DisplayName");
        GuiItem DisplayNameGuiItem = new GuiItem(processItemStack(Material.OAK_SIGN, displayName), event -> {
            TextInputGui textInputGui = new TextInputGui("Enter the Name to be Displayed");
        });

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
