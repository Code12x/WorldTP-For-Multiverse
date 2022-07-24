package com.code12.worldtp.gui.settings;

import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import com.code12.worldtp.gui.util.ProcessItemStack;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CustomizeOrderGui {
    public DataManager data = References.data;

    @Getter
    ChestGui gui;

    public CustomizeOrderGui(int rows, List<String> menuGroupList){
        gui = new ChestGui(rows, "Customize the Order");

        StaticPane menuGroupListPane = new StaticPane(0, 0, 9, rows);

        for(String menuGroup : menuGroupList){
            String displayName = menuGroup + " (" + data.getConfig().getString("menuGroupID." + menuGroup + ".displayName") + ")";
            Material material = data.getConfig().getItemStack("menuGroupID." + menuGroup + ".item").getType();
            ItemStack menuGroupItemStack = new ProcessItemStack().setMaterial(material).setDisplayName(displayName).getItemStack();

            GuiItem menuGroupGuiItem = new GuiItem(menuGroupItemStack, event -> {
                for(String menuGroup1 : menuGroupList) {

                }
                data.saveConfig();

                gui.update();
            });
        }
    }
}
