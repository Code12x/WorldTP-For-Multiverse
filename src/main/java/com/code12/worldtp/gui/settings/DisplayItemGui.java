package com.code12.worldtp.gui.settings;

import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.AnvilGui;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DisplayItemGui {

    // top row for recently searched and used items. also for the search item
    // middle two rows for commonly used items
    // bottom row for confirm/back

    DataManager data = References.data;
    ChestGui gui = new ChestGui(4, "Select Item to be Displayed");

    public DisplayItemGui() {

        StaticPane searchPane = new StaticPane(0, 0, 9, 1);

        GuiItem searchGuiItem = new GuiItem(processItemStack(Material.BOOK, "Search for Other Item"), event -> {
            AnvilGui searchGui = new AnvilGui("Search for an item");

            // ------------------
            StaticPane firstPane = new StaticPane(0, 0, 1, 1);

            ItemStack paperItem = processItemStack(Material.PAPER, "Search Here");

            firstPane.addItem(new GuiItem(paperItem), 0, 0);

            searchGui.getFirstItemComponent().addPane(firstPane);
            // ------------------

            StaticPane resultPane = new StaticPane(0, 0, 1, 1);

            ItemStack confirmItem = processItemStack(Material.PAPER, "Click to Confirm");

            GuiItem confirmGuiItem = new GuiItem(confirmItem, searchClick -> {
                ChestGui resultsOfSearchGui = new ChestGui(1, "Search results for \"" + searchGui.getRenameText() + "\"");

                StaticPane mainPain = new StaticPane(0, 0, 9, 1);

                //todo: here
            });

            resultPane.addItem(confirmGuiItem, 0, 0);

            searchGui.getResultComponent().addPane(resultPane);
        });
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
