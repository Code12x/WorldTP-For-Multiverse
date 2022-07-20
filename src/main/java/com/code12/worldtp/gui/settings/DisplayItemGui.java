package com.code12.worldtp.gui.settings;

import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.AnvilGui;
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
import java.util.List;

public class DisplayItemGui {
    DataManager data = References.data;

    @Getter
    ChestGui gui = new ChestGui(4, "Select Item to be Displayed");

    public DisplayItemGui(World world) {
        // -------------------------------------------------------------------------------------------------------------
        // gui init
        // -------------------------------------------------------------------------------------------------------------
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        StaticPane searchPane = new StaticPane(0, 0, 9, 1);

        // -------------------------------------------------------------------------------------------------------------
        // The searchGuiItem
        // -------------------------------------------------------------------------------------------------------------
        GuiItem searchGuiItem = new GuiItem(processItemStack(Material.BOOK, "Search for Other Item"), event -> {
            // ---------------------------------------------------------------------------------------------------------
            // searchGui init (AnvilGui)
            // ---------------------------------------------------------------------------------------------------------
            AnvilGui searchGui = new AnvilGui("Search for an item");
            searchGui.setOnGlobalClick(e -> e.setCancelled(true));

            // ---------------------------------------------------------------------------------------------------------
            // first pane & GuiItem
            // ---------------------------------------------------------------------------------------------------------
            StaticPane firstPane = new StaticPane(0, 0, 1, 1);

            ItemStack paperItem = processItemStack(Material.PAPER, "Search Here");

            firstPane.addItem(new GuiItem(paperItem), 0, 0);

            searchGui.getFirstItemComponent().addPane(firstPane);

            // ---------------------------------------------------------------------------------------------------------
            // Result pane & GuiItem
            // ---------------------------------------------------------------------------------------------------------
            StaticPane resultPane = new StaticPane(0, 0, 1, 1);

            ItemStack confirmItem = processItemStack(Material.PAPER, "Click to Confirm");

            GuiItem confirmGuiItem = new GuiItem(confirmItem, searchClick -> {
                String renameText = searchGui.getRenameText();
                String formattedRenameText = renameText.toUpperCase().replace(" ", "_");

                // -----------------------------------------------------------------------------------------------------
                // resultsOfSearchGui init
                // -----------------------------------------------------------------------------------------------------
                ChestGui resultsOfSearchGui = new ChestGui(1, "Search results for \"" + renameText + "\"");

                resultsOfSearchGui.setOnGlobalClick(e -> e.setCancelled(true));

                StaticPane mainPain = new StaticPane(0, 0, 9, 1);

                // -----------------------------------------------------------------------------------------------------
                // try to use the searched material as the ItemStack material... if it causes an error, show a no-result
                // GuiItem
                // -----------------------------------------------------------------------------------------------------
                try{
                    ItemStack searchedItem = new ItemStack(Material.valueOf(formattedRenameText));
                    ItemMeta searchedItemMeta = searchedItem.getItemMeta();
                    searchedItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    searchedItem.setItemMeta(searchedItemMeta);
//todo: here. Working on fixing the display-name being "Change Display Item" on each recentlySearchedItem. AAAAAAAaaaaaaa
                    ItemStack blankSearchedItem = new ItemStack(searchedItem.getData().getItemType());

                    GuiItem searchedGuiItem = new GuiItem(searchedItem, selectItemEvent -> {
                        data.getConfig().set("menuGroupID." + world.getName() + ".item", blankSearchedItem);

                        ArrayList<ItemStack> recentlySearchedItems = new ArrayList<>();

                        if(data.getConfig().getList("recentlySearchedItems") != null) {
                            recentlySearchedItems = (ArrayList<ItemStack>) data.getConfig().getList("recentlySearchedItems");

                            if(recentlySearchedItems.contains(blankSearchedItem)){
                                recentlySearchedItems.remove(blankSearchedItem);
                                recentlySearchedItems.add(blankSearchedItem);
                            }
                            else if(recentlySearchedItems.size() >= 7){
                                recentlySearchedItems.remove(0);
                            }
                        }

                        recentlySearchedItems.add(blankSearchedItem);
                        
                        data.getConfig().set("recentlySearchedItems", recentlySearchedItems);
                        data.saveConfig();

                        SettingsGui settingsGui = new SettingsGui((Player) selectItemEvent.getWhoClicked(), world);
                        settingsGui.getGui().show(selectItemEvent.getWhoClicked());
                    });

                    mainPain.addItem(searchedGuiItem, 0, 0);
                }catch (Exception e){
                    ItemStack noResultsItem = new ItemStack(Material.BARRIER);
                    ItemMeta noResultsItemItemMeta = noResultsItem.getItemMeta();
                    noResultsItemItemMeta.setDisplayName("No results for \"" + renameText + "\"");
                    noResultsItem.setItemMeta(noResultsItemItemMeta);

                    GuiItem noResultsGuiItem = new GuiItem(noResultsItem, selectNoResultsItem -> {
                        SettingsGui settingsGui = new SettingsGui((Player) selectNoResultsItem.getWhoClicked(), world);
                        settingsGui.getGui().show(selectNoResultsItem.getWhoClicked());
                    });

                    mainPain.addItem(noResultsGuiItem, 0, 0);
                }

                // -----------------------------------------------------------------------------------------------------
                // Add pane to the resultsOfSearchGui
                // -----------------------------------------------------------------------------------------------------
                resultsOfSearchGui.addPane(mainPain);

                // -----------------------------------------------------------------------------------------------------
                // Show the gui to the player
                // -----------------------------------------------------------------------------------------------------
                resultsOfSearchGui.show(searchClick.getWhoClicked());
            });

            resultPane.addItem(confirmGuiItem, 0, 0);

            searchGui.getResultComponent().addPane(resultPane);

            // ---------------------------------------------------------------------------------------------------------
            // Show the player the searchGui (AnvilGui)
            // ---------------------------------------------------------------------------------------------------------
            searchGui.show(event.getWhoClicked());
        });

        // -------------------------------------------------------------------------------------------------------------
        // The row of recentlySearchedItems
        // -------------------------------------------------------------------------------------------------------------
        if(data.getConfig().getList("recentlySearchedItems") != null){
            List<ItemStack> recentlySearchedItems = (List<ItemStack>) data.getConfig().getList("recentlySearchedItems");

            int x = recentlySearchedItems.size() + 1;

            // ---------------------------------------------------------------------------------------------------------
            // Iterate through each ItemStack in the recentlySearchedItems path in the data.yml and add them to the pane
            // ---------------------------------------------------------------------------------------------------------
            for(ItemStack recentlySearchedItem : recentlySearchedItems){
                GuiItem recentlySearchedGuiItem = new GuiItem(recentlySearchedItem, event -> {
                    data.getConfig().set("menuGroupID." + world.getName() + ".item", recentlySearchedItem);

                    ArrayList<ItemStack> recentlySearchedItemsEdit = new ArrayList<>(recentlySearchedItems);

                    // Do this to put the item back at the beginning
                    recentlySearchedItemsEdit.remove(recentlySearchedItem);
                    recentlySearchedItemsEdit.add(recentlySearchedItem);

                    data.getConfig().set("recentlySearchedItems", recentlySearchedItemsEdit);
                    data.saveConfig();

                    SettingsGui settingsGui = new SettingsGui((Player) event.getWhoClicked(), world);
                    settingsGui.getGui().show(event.getWhoClicked());
                });

                searchPane.addItem(recentlySearchedGuiItem, x, 0);
                x--;
            }
        }

        searchPane.addItem(searchGuiItem, 0, 0);

        // -------------------------------------------------------------------------------------------------------------
        // The commonlyUsedItemsPane
        // -------------------------------------------------------------------------------------------------------------
        StaticPane commonlyUsedItemsPane = new StaticPane(0, 2, 9, 1);

        ArrayList<ItemStack> commonlyUsedItems = new ArrayList<>();

        commonlyUsedItems.add(processItemStack(Material.GRASS_BLOCK));
        commonlyUsedItems.add(processItemStack(Material.OAK_DOOR));
        commonlyUsedItems.add(processItemStack(Material.IRON_SWORD));
        commonlyUsedItems.add(processItemStack(Material.NETHER_STAR));
        commonlyUsedItems.add(processItemStack(Material.IRON_PICKAXE));
        commonlyUsedItems.add(processItemStack(Material.BEACON));
        commonlyUsedItems.add(processItemStack(Material.GLOWSTONE));
        commonlyUsedItems.add(processItemStack(Material.FURNACE));
        commonlyUsedItems.add(processItemStack(Material.REDSTONE_BLOCK));

        for(ItemStack commonlyUsedItem : commonlyUsedItems){
            GuiItem commonlyUsedGuiItem = new GuiItem(commonlyUsedItem, event -> {
                data.getConfig().set("menuGroupID." + world.getName() + ".item", commonlyUsedItem);
                data.saveConfig();

                SettingsGui settingsGui = new SettingsGui((Player) event.getWhoClicked(), world);
                settingsGui.getGui().show(event.getWhoClicked());
            });

            commonlyUsedItemsPane.addItem(commonlyUsedGuiItem, commonlyUsedItems.indexOf(commonlyUsedItem), 0);
        }

        // -------------------------------------------------------------------------------------------------------------
        // The navigationPane
        // -------------------------------------------------------------------------------------------------------------
        StaticPane navigationPane = new StaticPane(0, 3, 9, 1);

        navigationPane.addItem(
                new GuiItem(processItemStack(Material.ARROW, "Back"),
                        event -> new SettingsGui((Player) event.getWhoClicked(), world)),
                0, 0);

        // -------------------------------------------------------------------------------------------------------------
        // Add the panes to the gui
        // -------------------------------------------------------------------------------------------------------------
        gui.addPane(searchPane);
        gui.addPane(commonlyUsedItemsPane);
        gui.addPane(navigationPane);
    }

    ItemStack processItemStack(Material material, String name){
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.setDisplayName(name);
        item.setItemMeta(itemMeta);
        return item;
    }

    ItemStack processItemStack(Material material){
        return processItemStack(material, new ItemStack(material).getItemMeta().getDisplayName());
    }
}
