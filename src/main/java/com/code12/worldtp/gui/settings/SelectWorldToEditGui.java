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

import java.util.List;

public class SelectWorldToEditGui {
    DataManager data = References.data;

    @Getter
    ChestGui gui;

    public SelectWorldToEditGui(Player player){
        // -------------------------------------------------------------------------------------------------------------
        // gui init
        // -------------------------------------------------------------------------------------------------------------
        gui = new ChestGui(4, "Settings: Select World");
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

        // -------------------------------------------------------------------------------------------------------------
        // Add pane to gui
        // -------------------------------------------------------------------------------------------------------------
        gui.addPane(pane);
    }
}
