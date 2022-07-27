package com.code12.worldtp.gui.settings;

import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import com.code12.worldtp.gui.util.ProcessItemStack;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.AnvilGui;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import lombok.Getter;
import org.bukkit.ChatColor;
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
        GuiItem searchGuiItem = new GuiItem(new ProcessItemStack()
                .setMaterial(Material.BOOK)
                .setDisplayName("Search for Another Item")
                .getItemStack()
                , event -> {
            // ---------------------------------------------------------------------------------------------------------
            // searchGui init (AnvilGui)
            // ---------------------------------------------------------------------------------------------------------
            AnvilGui searchGui = new AnvilGui("Search for an item");
            searchGui.setOnGlobalClick(e -> e.setCancelled(true));

            // ---------------------------------------------------------------------------------------------------------
            // first pane & GuiItem
            // ---------------------------------------------------------------------------------------------------------
            StaticPane firstPane = new StaticPane(0, 0, 1, 1);

            ItemStack paperItem = new ProcessItemStack()
                    .setMaterial(Material.PAPER)
                    .setDisplayName("Search Here")
                    .getItemStack();

            firstPane.addItem(new GuiItem(paperItem), 0, 0);

            searchGui.getFirstItemComponent().addPane(firstPane);

            // ---------------------------------------------------------------------------------------------------------
            // Result pane & GuiItem
            // ---------------------------------------------------------------------------------------------------------
            StaticPane resultPane = new StaticPane(0, 0, 1, 1);

            ItemStack confirmItem = new ProcessItemStack()
                    .setMaterial(Material.PAPER)
                    .setDisplayName("Click to Confirm")
                    .getItemStack();

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
                    Material material = Material.valueOf(formattedRenameText);
                    ItemStack searchedItem = new ProcessItemStack().setMaterial(material)
                            .setItemFlags(List.of(ItemFlag.HIDE_ATTRIBUTES)).getItemStack();

                    GuiItem searchedGuiItem = new GuiItem(searchedItem, selectItemEvent -> {
                        data.getConfig().set("menuGroupID." + world.getName() + ".material", searchedItem.getType().toString());

                        ArrayList<String> recentlySearchedMaterials = new ArrayList<>();

                        if(data.getConfig().getList("recentlySearchedMaterials") != null) {
                            recentlySearchedMaterials = (ArrayList<String>) data.getConfig().getStringList("recentlySearchedMaterials");

                            if(recentlySearchedMaterials.contains(searchedItem.getType().toString())){
                                recentlySearchedMaterials.remove(searchedItem.getType().toString());
                            }
                            else if(recentlySearchedMaterials.size() >= 7){
                                recentlySearchedMaterials.remove(0);
                            }
                        }

                        recentlySearchedMaterials.add(searchedItem.getType().toString());
                        
                        data.getConfig().set("recentlySearchedMaterials", recentlySearchedMaterials);
                        data.saveConfig();

                        SettingsGui settingsGui = new SettingsGui((Player) selectItemEvent.getWhoClicked(), world);
                        settingsGui.getGui().show(selectItemEvent.getWhoClicked());
                    });

                    mainPain.addItem(searchedGuiItem, 0, 0);
                }catch (Exception e){
                    ItemStack noResultsItem = new ProcessItemStack().setMaterial(Material.BARRIER)
                            .setDisplayName("No results for \"" + renameText + "\"").setChatColor(ChatColor.RED)
                            .setItemFlags(List.of(ItemFlag.HIDE_ATTRIBUTES)).getItemStack();

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
        if(data.getConfig().getList("recentlySearchedMaterials") != null){
            List<String> recentlySearchedMaterials = data.getConfig().getStringList("recentlySearchedMaterials");

            int x = recentlySearchedMaterials.size() + 1;

            // ---------------------------------------------------------------------------------------------------------
            // Iterate through each ItemStack in the recentlySearchedMaterials path in the data.yml and add them to the pane
            // ---------------------------------------------------------------------------------------------------------
            for(String recentlySearchedMaterial : recentlySearchedMaterials){
                Material material = Material.valueOf(recentlySearchedMaterial);
                ItemStack item = new ProcessItemStack()
                        .setMaterial(material)
                        .setItemFlags(List.of(ItemFlag.HIDE_ATTRIBUTES))
                        .getItemStack();
                GuiItem recentlySearchedGuiItem = new GuiItem(item, event -> {
                    data.getConfig().set("menuGroupID." + world.getName() + ".material", item.getType().toString());

                    ArrayList<String> recentlySearchedMaterialsEdit = new ArrayList<>(recentlySearchedMaterials);

                    // Do this to put the item back at the beginning
                    recentlySearchedMaterialsEdit.remove(item.getType().toString());
                    recentlySearchedMaterialsEdit.add(item.getType().toString());

                    data.getConfig().set("recentlySearchedMaterials", recentlySearchedMaterialsEdit);
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

        List<Material> commonlyUsedMaterials = List.of(Material.GRASS_BLOCK, Material.OAK_DOOR, Material.IRON_SWORD,
                Material.NETHER_STAR, Material.IRON_PICKAXE, Material.BEACON, Material.GLOWSTONE, Material.FURNACE, Material.REDSTONE_BLOCK);

        ArrayList<ItemStack> commonlyUsedItems = new ArrayList<>();

        commonlyUsedMaterials.forEach(material -> commonlyUsedItems.add(new ProcessItemStack()
                .setMaterial(material)
                .setItemFlags(List.of(ItemFlag.HIDE_ATTRIBUTES))
                .getItemStack()));

        for(ItemStack commonlyUsedItem : commonlyUsedItems){
            GuiItem commonlyUsedGuiItem = new GuiItem(commonlyUsedItem, event -> {
                data.getConfig().set("menuGroupID." + world.getName() + ".material", commonlyUsedItem.getType().toString());
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

        GuiItem navigationBackGuiItem = new GuiItem(new ProcessItemStack()
                .setMaterial(Material.ARROW)
                .setDisplayName("Back")
                .getItemStack()
                , event -> {
            SettingsGui settingsGui = new SettingsGui((Player) event.getWhoClicked(), world);
            settingsGui.getGui().show(event.getWhoClicked());
        });

        navigationPane.addItem(navigationBackGuiItem, 0, 0);

        // -------------------------------------------------------------------------------------------------------------
        // Add the panes to the gui
        // -------------------------------------------------------------------------------------------------------------
        gui.addPane(searchPane);
        gui.addPane(commonlyUsedItemsPane);
        gui.addPane(navigationPane);
    }
}
