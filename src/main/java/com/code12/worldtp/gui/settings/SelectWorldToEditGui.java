package com.code12.worldtp.gui.settings;

import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SelectWorldToEditGui {
    DataManager data = References.data;

    @Getter
    ChestGui gui;

    public SelectWorldToEditGui(Player player){
        // -------------------------------------------------------------------------------------------------------------
        // gui init
        // -------------------------------------------------------------------------------------------------------------
        int numberOfWorlds = data.getConfig().getStringList("menuGroupList").size();

        int rows = 1;
        int slots = 9;

        while (numberOfWorlds / slots > 1) {
            rows ++;
            slots += 9;
        }

        gui = new ChestGui(rows, "Settings: Select World");
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        StaticPane pane = new StaticPane(0, 0, 9, 4);

        // -------------------------------------------------------------------------------------------------------------
        // Add the worlds in menuGroupList to pane
        // -------------------------------------------------------------------------------------------------------------
        List<String> menuGroupList = data.getConfig().getStringList("menuGroupList");

        int x = 0;
        int y = 0;

        for(String menuGroup : menuGroupList){
            ItemStack item = data.getConfig().getItemStack("menuGroupID." + menuGroup + ".item");
            String displayName = data.getConfig().getString("menuGroupID." + menuGroup + ".displayName");

            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(menuGroup + " (" + displayName + ")");
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            item.setItemMeta(itemMeta);

            GuiItem guiItem = new GuiItem(item, event ->{
                SettingsGui settingsGui = new SettingsGui((Player) event.getWhoClicked(), Bukkit.getWorld(menuGroup));
                settingsGui.getGui().show(player);
            });

            pane.addItem(guiItem, x, y);

            // cool math B)
            x++;
            if(x >= 9){
                x = 0;
                y++;
            }
        }

        gui.addPane(pane);

        // -------------------------------------------------------------------------------------------------------------
        // customizationPane
        // -------------------------------------------------------------------------------------------------------------
        StaticPane customizationPane = new StaticPane(0, rows + 1, 9, 1);

        // -------------------------------------------------------------------------------------------------------------
        // customizeGuiItem
        // -------------------------------------------------------------------------------------------------------------

    }
}
