package com.code12.worldtp.gui.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ProcessItemStack {
    private ItemStack itemStack;
    private Material material;
    private String displayName;
    private List<ItemFlag> itemFlags;
    private List<String> lore;

    public ProcessItemStack setMaterial(Material material){
        this.material = material;
        return this;
    }

    public ProcessItemStack setDisplayName(String displayName){
        this.displayName = displayName;
        return this;
    }

    public ProcessItemStack setItemFlags(List<ItemFlag> itemFlags){
        this.itemFlags = itemFlags;
        return this;
    }

    public ProcessItemStack setLore(List<String> lore){
        this.lore = lore;
        return this;
    }

    public ItemStack getItemStack() {
        if(itemStack != null){
            return itemStack;
        }

        itemStack = new ItemStack(material);

        ItemMeta itemMeta = itemStack.getItemMeta();

        if(displayName != null) {
            itemMeta.setDisplayName(displayName);
        }

        if(itemFlags != null){
            for(ItemFlag itemFlag : itemFlags){
                itemMeta.addItemFlags(itemFlag);
            }
        }

        if(lore != null){
            itemMeta.setLore(lore);
        }

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
