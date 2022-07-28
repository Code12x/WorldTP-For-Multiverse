package com.code12.worldtp.gui.settings;

import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import com.code12.worldtp.gui.util.GuiMath;
import com.code12.worldtp.gui.util.ProcessItemStack;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
        int numberOfWorlds = data.getConfig().getStringList("menuGroupList").size();

        int rows = 1;
        int slots = 9;

        while (numberOfWorlds / slots > 1) {
            rows ++;
            slots += 9;
        }

        gui = new ChestGui(rows + 1, "Settings: Select World");
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        StaticPane pane = new StaticPane(0, 0, 9, rows);

        // -------------------------------------------------------------------------------------------------------------
        // Add the worlds in menuGroupList to pane
        // -------------------------------------------------------------------------------------------------------------
        List<String> menuGroupList = data.getConfig().getStringList("menuGroupList");

        for(String menuGroup : menuGroupList){
            Material material = Material.valueOf(data.getConfig().getString("menuGroupID." + menuGroup + ".material"));
            String displayName = data.getConfig().getString("menuGroupID." + menuGroup + ".displayName");

            ItemStack item = new ProcessItemStack().setMaterial(material)
                    .setDisplayName(menuGroup + " (" + displayName + ")")
                    .setItemFlags(List.of(ItemFlag.HIDE_ATTRIBUTES)).getItemStack();

            GuiItem guiItem = new GuiItem(item, event ->{
                SettingsGui settingsGui = new SettingsGui((Player) event.getWhoClicked(), Bukkit.getWorld(menuGroup));
                settingsGui.getGui().show(player);
            });

            int itemPosition = data.getConfig().getInt("menuGroupID." + menuGroup + ".position");

            List<Integer> cords = GuiMath.cordsFromPosition(itemPosition, 9);
            int x = cords.get(0);
            int y = cords.get(1);

            pane.addItem(guiItem, x, y);
        }

        gui.addPane(pane);

        // -------------------------------------------------------------------------------------------------------------
        // customizeOrderPane
        // -------------------------------------------------------------------------------------------------------------
        StaticPane customizeOrderPane = new StaticPane(0, rows, 9, 1);

        // -------------------------------------------------------------------------------------------------------------
        // customizeOrderGuiItem
        // -------------------------------------------------------------------------------------------------------------
        ItemStack customizeOrderItem = new ProcessItemStack()
                .setMaterial(Material.HOPPER)
                .setDisplayName("Customize the order that the items appear in")
                .getItemStack();

        int finalRows = rows;
        GuiItem customizeOrderGuiItem = new GuiItem(customizeOrderItem, event -> {
            CustomizeOrderGui customizeOrderGui = new CustomizeOrderGui(finalRows, menuGroupList);
            customizeOrderGui.getGui().show(player);
        });

        customizeOrderPane.addItem(customizeOrderGuiItem, 4, 0);

        gui.addPane(customizeOrderPane);
    }
}
