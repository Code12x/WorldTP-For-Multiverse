package com.code12.worldtp.gui.settings;

import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.AnvilGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DisplayNameGui {

    DataManager data = References.data;

    AnvilGui gui;
    String textInput;

    public DisplayNameGui(String title, String world){
        gui = new AnvilGui(title);
        gui.setOnGlobalClick(event -> event.setCancelled(true));
        gui.setOnClose(event -> {
            new SettingsGui((Player) event.getPlayer(), Bukkit.getWorld(world));
        });

        StaticPane firstComponentPane = new StaticPane(0, 0, 1, 1);

        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta paperMeta = paper.getItemMeta();
        paperMeta.setDisplayName("Display Name");
        paper.setItemMeta(paperMeta);

        firstComponentPane.addItem(new GuiItem(paper), 0, 0);
        gui.getFirstItemComponent().addPane(firstComponentPane);

        StaticPane resultComponentPane = new StaticPane(0, 0, 1, 1);

        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("Click to Confirm");
        item.setItemMeta(itemMeta);

        GuiItem resultGuiItem = new GuiItem(item, event -> {
            if(!gui.getRenameText().isEmpty()){
                textInput = gui.getRenameText();

                data.getConfig().set("menuGroupID." + world + ".displayName", textInput);
                data.saveConfig();
            }

            event.getView().close();
        });

        resultComponentPane.addItem(resultGuiItem, 0, 0);

        gui.getResultComponent().addPane(resultComponentPane);
    }

    public AnvilGui getGui() {
        return gui;
    }

    public String getTextInput(){
        return textInput;
    }
}
