package com.code12.worldtp.commands;

import com.code12.worldtp.files.ConfigManager;
import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import com.code12.worldtp.gui.DimensionsSelectionGui;
import com.code12.worldtp.gui.WorldSelectionGui;
import com.code12.worldtp.menues.AdvancedWorldTPMenu;
import com.code12.worldtp.menues.WorldTPMenu;
import com.code12.worldtp.worldtpobjects.WorldTPWorld;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CommandWorldTP implements CommandExecutor {

    private final DataManager data = References.data;
    private final ConfigManager config = References.config;

    // the command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (data.getConfig().getStringList("menuGroupList").isEmpty()) {
                player.sendMessage("The worlds have not been registered on WorldTP. To register the worlds, have an admin run the command /reloadworlds.");
                return true;
            }

            WorldSelectionGui worldSelectionGui = new WorldSelectionGui(player);
            worldSelectionGui.getGui().show(player);
        }
        return true;
    }
}
