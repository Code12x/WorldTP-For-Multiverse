package com.code12.worldtp.menues;

import com.code12.worldtp.WorldTP;
import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class AdvancedWorldTPMenu {
    WorldTP plugin;

    public DataManager data = References.data;

    public Inventory tpMenu;

    public AdvancedWorldTPMenu(WorldTP plugin) {
        this.plugin = plugin;

        int numberOfWorlds = data.getConfig().getStringList("menuGroupList").size();
        int numberOfSlots = 9;

        int j = 9;
        while((j/numberOfWorlds) < 1){
            j += 9;
            numberOfSlots = j;
        }

        tpMenu = Bukkit.createInventory(null, numberOfSlots, "World Menu");

        List<String> menuGroupList = data.getConfig().getStringList("menuGroupList");

        int i=0;
        for (String menuGroup : menuGroupList) {
            ItemStack item = new ItemStack(Material.GRASS_BLOCK);
            if(data.getConfig().getItemStack("menuGroupID." + menuGroup + ".item") != null){
                item = data.getConfig().getItemStack("menuGroupID." + menuGroup + ".item");
            }
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(menuGroup);
            if(data.getConfig().getString("menuGroupID." + menuGroup + ".displayName") != null)
                itemMeta.setDisplayName(data.getConfig().getString("menuGroupID." + menuGroup + ".displayName"));
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            item.setItemMeta(itemMeta);
            tpMenu.setItem(i, item);
            i++;
        }
    }
}
