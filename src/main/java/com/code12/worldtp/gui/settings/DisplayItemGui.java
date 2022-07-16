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

import java.util.ArrayList;
import java.util.List;

public class DisplayItemGui {

    // top row for recently searched items. also for the search item
    // middle two rows for commonly used items
    // bottom row for confirm/back

    DataManager data = References.data;
    ChestGui gui = new ChestGui(4, "Select Item to be Displayed");

    public DisplayItemGui(World world) {

        gui.setOnGlobalClick(event -> event.setCancelled(true));

        StaticPane searchPane = new StaticPane(0, 0, 9, 1);

        GuiItem searchGuiItem = new GuiItem(processItemStack(Material.BOOK, "Search for Other Item"), event -> {
            AnvilGui searchGui = new AnvilGui("Search for an item");
            searchGui.setOnGlobalClick(e -> e.setCancelled(true));

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

                resultsOfSearchGui.setOnGlobalClick(e -> e.setCancelled(true));

                StaticPane mainPain = new StaticPane(0, 0, 9, 1);

                try{
                    ItemStack searchedItem = new ItemStack(Material.valueOf(renameText));
                    ItemMeta searchedItemMeta = searchedItem.getItemMeta();
                    searchedItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                    searchedItem.setItemMeta(searchedItemMeta);

                    GuiItem searchedGuiItem = new GuiItem(searchedItem, selectItemEvent -> {
                        data.getConfig().set("menuGroupID." + world.getName() + ".item", searchedItem);

                        ArrayList<ItemStack> recentlySearchedItems = new ArrayList<>();

                        if(data.getConfig().getList("recentlySearchedItems") != null) {
                            recentlySearchedItems = (ArrayList<ItemStack>) data.getConfig().getList("recentlySearchedItems");

                            if(recentlySearchedItems.size() >= 7){
                                recentlySearchedItems.remove(0);
                            }
                        }

                        recentlySearchedItems.add(searchedItem);
                        
                        data.getConfig().set("recentlySearchedItems", recentlySearchedItems);
                        data.saveConfig();

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

                resultsOfSearchGui.addPane(mainPain);

                resultsOfSearchGui.show(searchClick.getWhoClicked());
            });

            resultPane.addItem(confirmGuiItem, 0, 0);

            searchGui.getResultComponent().addPane(resultPane);

            searchGui.show(event.getWhoClicked());
        });
        
        if(data.getConfig().getList("recentlySearchedItems") != null){
            List<ItemStack> recentlySearchedItems = (List<ItemStack>) data.getConfig().getList("recentlySearchedItems");

            int x = recentlySearchedItems.size() + 1;

            for(ItemStack recentlySearchedItem : recentlySearchedItems){
                GuiItem recentlySearchedGuiItem = new GuiItem(recentlySearchedItem, event -> {
                    data.getConfig().set("menuGroupID." + world.getName() + ".item", recentlySearchedItem);

                    ArrayList<ItemStack> recentlySearchedItemsEdit = new ArrayList<>(recentlySearchedItems);

                    recentlySearchedItemsEdit.remove(recentlySearchedItem);
                    recentlySearchedItemsEdit.add(recentlySearchedItem);

                    data.getConfig().set("recentlySearchedItems", recentlySearchedItemsEdit);
                    data.saveConfig();

                    new WorldConfigurationGui((Player) event.getWhoClicked(), world);
                });

                searchPane.addItem(recentlySearchedGuiItem, x, 0);
                x--;
            }
        }

        searchPane.addItem(searchGuiItem, 0, 0);
        // -------------------------------------------------------------------------------------------------------------

        StaticPane commonlyUsedItemsPane = new StaticPane(0, 2, 9, 1);

        ArrayList<ItemStack> commonlyUsedItems = new ArrayList<>();

        commonlyUsedItems.add(new ItemStack(Material.GRASS_BLOCK));
        commonlyUsedItems.add(new ItemStack(Material.OAK_DOOR));
        commonlyUsedItems.add(new ItemStack(Material.IRON_SWORD));
        commonlyUsedItems.add(new ItemStack(Material.NETHER_STAR));
        commonlyUsedItems.add(new ItemStack(Material.IRON_PICKAXE));
        commonlyUsedItems.add(new ItemStack(Material.BEACON));
        commonlyUsedItems.add(new ItemStack(Material.GLOWSTONE));
        commonlyUsedItems.add(new ItemStack(Material.FURNACE));
        commonlyUsedItems.add(new ItemStack(Material.REDSTONE_BLOCK));

        for(ItemStack commonlyUsedItem : commonlyUsedItems){
            GuiItem commonlyUsedGuiItem = new GuiItem(commonlyUsedItem, event -> {
                data.getConfig().set("menuGroupID." + world.getName() + ".item", commonlyUsedItem);
                data.saveConfig();

                new WorldConfigurationGui((Player) event.getWhoClicked(), world);
            });

            commonlyUsedItemsPane.addItem(commonlyUsedGuiItem, commonlyUsedItems.indexOf(commonlyUsedItem), 0);
        }
        // -------------------------------------------------------------------------------------------------------------
        StaticPane navigationPane = new StaticPane(0, 3, 9, 1);

        navigationPane.addItem(
                new GuiItem(processItemStack(Material.ARROW, "Back"),
                        event -> new WorldConfigurationGui((Player) event.getWhoClicked(), world)),
                0, 0);

        //  ============================================================================================================
        gui.addPane(searchPane);
        gui.addPane(commonlyUsedItemsPane);
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
