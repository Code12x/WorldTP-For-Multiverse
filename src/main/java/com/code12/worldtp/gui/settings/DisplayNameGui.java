package com.code12.worldtp.gui.settings;

import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import com.code12.worldtp.gui.util.ProcessItemStack;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.AnvilGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class DisplayNameGui {

    DataManager data = References.data;

    @Getter
    AnvilGui gui;
    String textInput;

    public DisplayNameGui(String title, String world){
        // -------------------------------------------------------------------------------------------------------------
        // gui init
        // -------------------------------------------------------------------------------------------------------------
        gui = new AnvilGui(title);
        gui.setOnGlobalClick(event -> event.setCancelled(true));
        gui.setOnClose(event -> {
            SettingsGui settingsGui = new SettingsGui((Player) event.getPlayer(), Bukkit.getWorld(world));
            settingsGui.getGui().show(event.getPlayer());
        });

        StaticPane firstComponentPane = new StaticPane(0, 0, 1, 1);

        // -------------------------------------------------------------------------------------------------------------
        // firstComponentPane
        // -------------------------------------------------------------------------------------------------------------
        ItemStack paper = new ProcessItemStack().setMaterial(Material.PAPER).setDisplayName("Display Name")
                        .setItemFlags(List.of(ItemFlag.HIDE_ATTRIBUTES)).getItemStack();

        firstComponentPane.addItem(new GuiItem(paper), 0, 0);
        gui.getFirstItemComponent().addPane(firstComponentPane);

        // -------------------------------------------------------------------------------------------------------------
        // resultComponentPane
        // -------------------------------------------------------------------------------------------------------------
        StaticPane resultComponentPane = new StaticPane(0, 0, 1, 1);

        ItemStack item = new ProcessItemStack().setMaterial(Material.PAPER).setDisplayName("Click to Confirm")
                .setItemFlags(List.of(ItemFlag.HIDE_ATTRIBUTES)).getItemStack();

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
}
