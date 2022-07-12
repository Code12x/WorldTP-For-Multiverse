package com.code12.worldtp.gui.settings;

import com.code12.worldtp.files.ConfigManager;
import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class SettingsGui {
    DataManager data = References.data;

    public SettingsGui(Player player){
        ChestGui gui = new ChestGui(4, "Settings");
        StaticPane pane = new StaticPane(0, 0, 9, 4);

        List<String> menuGroupList = data.getConfig().getStringList("menuGroupList");

        int x = 0;
        int y = 0;

        for(String menuGroup : menuGroupList){
            ItemStack item = data.getConfig().getItemStack("menuGroupID." + menuGroup + ".item");
            String displayName = data.getConfig().getString("menuGroupID." + menuGroup + ".displayName");

            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(displayName);
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            item.setItemMeta(itemMeta);

            GuiItem guiItem = new GuiItem(item, event ->{
                new WorldConfigurationGui((Player) event.getWhoClicked(), );
            });

            pane.addItem(guiItem, x, y);

            x++;
            if(x >= 9){
                x = 0;
                y++;
            }
        }

        gui.addPane(pane);
        gui.show(player);
    }


}
