package com.code12.worldtp.gui.settings;

import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import com.code12.worldtp.gui.util.GuiMath;
import com.code12.worldtp.gui.util.ProcessItemStack;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CustomizeOrderGui {
    public DataManager data = References.data;

    @Getter
    ChestGui gui;

    public CustomizeOrderGui(int rows, List<String> menuGroupList){
        gui = new ChestGui(rows + 1, "Customize the Order");
        gui.setOnGlobalClick(event -> event.setCancelled(true));

        // -------------------------------------------------------------------------------------------------------------
        // menuGroupListPane
        // -------------------------------------------------------------------------------------------------------------
        StaticPane menuGroupListPane = new StaticPane(0, 0, 9, rows);

        for(String menuGroup : menuGroupList){
            String displayName = menuGroup + " (" + data.getConfig().getString("menuGroupID." + menuGroup + ".displayName") + ")";
            Material material = data.getConfig().getItemStack("menuGroupID." + menuGroup + ".item").getType();
            ItemStack menuGroupItemStack = new ProcessItemStack().setMaterial(material)
                    .setDisplayName(displayName).setItemFlags(List.of(ItemFlag.HIDE_ATTRIBUTES)).getItemStack();

            int menuGroupPos = data.getConfig().getInt("menuGroupID." + menuGroup + ".position");

            GuiItem menuGroupGuiItem = new GuiItem(menuGroupItemStack, event -> {
                for(String menuGroup1 : menuGroupList) {
                    int menuGroup1CurrentPos = data.getConfig().getInt("menuGroupID." + menuGroup1 + ".position");
                    if(menuGroup1CurrentPos < menuGroupPos) {
                        data.getConfig().set("menuGroupID." + menuGroup1 + ".position", menuGroup1CurrentPos + 1);
                    }
                }
                data.getConfig().set("menuGroupID." + menuGroup + ".position", 0);

                data.saveConfig();

                CustomizeOrderGui customizeOrderGui = new CustomizeOrderGui(rows, menuGroupList);
                customizeOrderGui.getGui().show(event.getWhoClicked());
            });
            List<Integer> cords = GuiMath.cordsFromPosition(menuGroupPos, 9);

            menuGroupListPane.addItem(menuGroupGuiItem, cords.get(0), cords.get(1));
        }
        gui.addPane(menuGroupListPane);

        // -------------------------------------------------------------------------------------------------------------
        // navigationPane
        // -------------------------------------------------------------------------------------------------------------
        OutlinePane navigationPane = new OutlinePane(0, rows, 9, 1);

        navigationPane.addItem(new GuiItem(new ProcessItemStack()
                .setMaterial(Material.ARROW)
                .setDisplayName("Back")
                .getItemStack(), event -> {
            SelectWorldToEditGui selectWorldToEditGui = new SelectWorldToEditGui((Player) event.getWhoClicked());
            selectWorldToEditGui.getGui().show(event.getWhoClicked());
        }));

        gui.addPane(navigationPane);
        gui.update();
    }
}
