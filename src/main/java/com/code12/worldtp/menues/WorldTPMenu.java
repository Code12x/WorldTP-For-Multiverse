package com.code12.worldtp.menues;

import com.code12.worldtp.WorldTP;
import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class WorldTPMenu{
    WorldTP plugin;

    public DataManager data = References.data;

    public Inventory tpMenu;

    public WorldTPMenu(WorldTP plugin){
        this.plugin = plugin;

        List<String> worldList = data.getConfig().getStringList("menuGroupList");
        ArrayList<String> notAdminWorlds = new ArrayList<String>();

        for(String world : worldList){
            if(!data.getConfig().getBoolean("menuGroupID." + world + ".admin")){
                notAdminWorlds.add(world);
            }
        }

        int numberOfSlots = 9;

        int j = 9;
        while((j/notAdminWorlds.size()) < 1){
            j += 9;
            numberOfSlots = j;
        }

        tpMenu = Bukkit.createInventory(null, numberOfSlots, "World Menu");

        for (String menuGroup : notAdminWorlds) {
            ItemStack item = data.getConfig().getItemStack("menuGroupID." + menuGroup + ".item");
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(menuGroup);
            if(data.getConfig().getString("menuGroupID." + menuGroup + ".displayName") != null)
                itemMeta.setDisplayName(data.getConfig().getString("menuGroupID." + menuGroup + ".displayName"));
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            item.setItemMeta(itemMeta);
            tpMenu.addItem(item);
        }
    }
}
