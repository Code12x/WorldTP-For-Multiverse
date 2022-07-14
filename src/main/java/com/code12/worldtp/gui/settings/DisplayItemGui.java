package com.code12.worldtp.gui.settings;

import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.AnvilGui;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DisplayItemGui {

    // top row for recently searched and used items. also for the search item
    // middle two rows for commonly used items
    // bottom row for confirm/back

    DataManager data = References.data;
    ChestGui gui = new ChestGui(4, "Select Item to be Displayed");

    public DisplayItemGui(World world) {

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
                String renameText = searchGui.getRenameText();

                ChestGui resultsOfSearchGui = new ChestGui(1, "Search results for \"" + renameText + "\"");

                StaticPane mainPain = new StaticPane(0, 0, 9, 1);

                try{
                    ItemStack searchedItem = new ItemStack(Material.valueOf(renameText));
                    ItemMeta searchedItemMeta = searchedItem.getItemMeta();
                    searchedItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    searchedItem.setItemMeta(searchedItemMeta);

                    GuiItem searchedGuiItem = new GuiItem(searchedItem, selectItemEvent -> {
                        data.getConfig().set("menuGroupID." + world.getName() + ".item", searchedItem);

                        new WorldConfigurationGui((Player) selectItemEvent.getWhoClicked(), world);
                    });

                    mainPain.addItem(searchedGuiItem, 0, 0);
                }catch (Exception e){
                    ItemStack noResultsItem = new ItemStack(Material.BARRIER);
                    ItemMeta noResultsItemItemMeta = noResultsItem.getItemMeta();
                    noResultsItemItemMeta.setDisplayName("No results for \"" + renameText + "\"");
                    noResultsItem.setItemMeta(noResultsItemItemMeta);

                    GuiItem noResultsGuiItem = new GuiItem(noResultsItem, selectNoResultsItem -> {
                        new WorldConfigurationGui((Player) selectNoResultsItem.getWhoClicked(), world);
                    });

                    mainPain.addItem(noResultsGuiItem, 0, 0);
                }
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
