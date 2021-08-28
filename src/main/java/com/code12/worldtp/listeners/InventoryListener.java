package com.code12.worldtp.listeners;

import com.code12.worldtp.WorldTP;
import com.code12.worldtp.apimethods.WorldTPWorld;
import com.code12.worldtp.files.ConfigManager;
import com.code12.worldtp.files.DataManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class InventoryListener implements Listener {
    WorldTP plugin;

    public DataManager data;
    public ConfigManager config;

    public InventoryListener(WorldTP plugin, DataManager data) {
        this.plugin = plugin;
        this.data = data;
        this.config = new ConfigManager(plugin);
    }

    @EventHandler
    public void onPlayerInventoryInteractEvent(InventoryClickEvent event) {
        List<String> menuGroupList = data.getConfig().getStringList("menuGroupList");

        Player player = (Player) event.getWhoClicked();

        if (event.getView().getTitle().contains("World Menu")) {
            if (event.getCurrentItem() == null) {
                return;
            }
            if (event.getCurrentItem().getItemMeta() != null) {
                for (String menuGroup : menuGroupList) {
                    if (event.getCurrentItem().getItemMeta().getDisplayName().equals(data.getConfig().getString("menuGroupID." + menuGroup + ".displayName"))) {

                        Boolean spawn = config.getConfig().getBoolean(menuGroup + ".Spawn_Teleporting");
                        Boolean nether = config.getConfig().getBoolean(menuGroup + ".Nether_Teleporting");
                        Boolean end = config.getConfig().getBoolean(menuGroup + ".End_Teleporting");
                        Boolean home = config.getConfig().getBoolean(menuGroup + ".Home_Teleporting");

                        String worldToLeaveName = player.getWorld().getName();
                        WorldTPWorld worldToLeave = new WorldTPWorld(plugin, worldToLeaveName);
                        String worldGroupToLeave = worldToLeave.getWorldGroup();


                        String worldToEnterName;
                        WorldTPWorld worldToEnter;
                        String worldToEnterWorldGroup;

                        Location playerLocation = player.getLocation();

                        Location locationToTP = null;


                        if(spawn || nether || end || home){


                            event.getView().close();
                            Inventory dimensionChoice = Bukkit.createInventory(null, 9, "Dimension");
                            if (spawn) {
                                dimensionChoice.addItem(newItem("GRASS_BLOCK", menuGroup + " Spawn"));
                            }
                            if (nether) {
                                dimensionChoice.addItem(newItem("NETHERRACK", menuGroup + " Nether"));
                            }
                            if (end) {
                                dimensionChoice.addItem(newItem("END_STONE", menuGroup + " End"));
                            }
                            if (home) {
                                dimensionChoice.addItem(newItem("RED_BED", menuGroup + " Home"));
                            }
                            dimensionChoice.addItem(newItem("LEATHER_BOOTS", menuGroup + " Resume"));

                            player.openInventory(dimensionChoice);
                            return;


                        }else {

                            if (data.getConfig().getLocation("playerLocations." + player.getName() + "." + menuGroup) != null) {
                                locationToTP = data.getConfig().getLocation("playerLocations." + player.getName() + "." + menuGroup);
                            }

                            if (data.getConfig().getLocation("menuGroupID." + menuGroup + ".WorldTPWorldSpawnPoint") != null) {
                                locationToTP = data.getConfig().getLocation("menuGroupID." + menuGroup + ".WorldTPWorldSpawnPoint");
                            }

                            if (locationToTP != null) {
                                worldToEnterName = locationToTP.getWorld().getName();
                                worldToEnter = new WorldTPWorld(plugin, worldToEnterName);
                                worldToEnterWorldGroup = worldToEnter.getWorldGroup();

                                if (worldGroupToLeave.equals(worldToEnterWorldGroup)) {
                                    player.sendMessage(ChatColor.YELLOW + "You are already in the world: " + worldGroupToLeave);
                                    event.setCancelled(true);
                                    break;
                                }

                                player.teleport(locationToTP);
                            } else {
                                World world = Bukkit.getWorld(menuGroup);
                                player.teleport(world.getSpawnLocation());
                                if (player.getBedSpawnLocation() != null) {
                                    player.teleport(player.getBedSpawnLocation());
                                }
                            }
                        }


                        data.getConfig().set("playerLocations." + player.getName() + "." + worldGroupToLeave, playerLocation);

                        player.sendMessage(ChatColor.DARK_AQUA + "You have been teleported to " + menuGroup + ".");
                        data.saveConfig();
                        event.setCancelled(true);
                    }
                }
            }
        }else if (event.getView().getTitle().contains("Dimension")) {
            if (event.getCurrentItem() == null) {
                return;
            }
            if (event.getCurrentItem().getItemMeta() != null) {
                for (String menuGroup : menuGroupList) {
                    if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith(menuGroup)){
                        String worldToLeaveName = player.getWorld().getName();
                        WorldTPWorld worldToLeave = new WorldTPWorld(plugin, worldToLeaveName);
                        String worldGroupToLeave = worldToLeave.getWorldGroup();


                        String worldToEnterName;
                        WorldTPWorld worldin;
                        String worldinWorldGroup;

                        Location playerLocation = player.getLocation();

                        Location locationToTP = null;

                        if (event.getCurrentItem().getItemMeta().getDisplayName().equals(menuGroup + " Spawn")) {
                            if(data.getConfig().getString("worldGroup." + menuGroup + ".overworld") == null){
                                player.sendMessage(ChatColor.YELLOW + "That place does not exist.");
                                event.setCancelled(true);
                                return;
                            }
                            if(data.getConfig().getLocation("menuGroupID." + menuGroup + ".WorldTPWorldSpawnPoint") == null){
                                locationToTP = Bukkit.getWorld(data.getConfig().getString("worldGroup." + menuGroup + ".overworld")).getSpawnLocation();
                            }else{
                                locationToTP = data.getConfig().getLocation("menuGroupID." + menuGroup + ".WorldTPWorldSpawnPoint");
                            }
                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(menuGroup + " Nether")) {
                            if(data.getConfig().getString("worldGroup." + menuGroup + ".nether") == null){
                                player.sendMessage(ChatColor.YELLOW + "That place does not exist.");
                                event.setCancelled(true);
                                return;
                            }

                            locationToTP = Bukkit.getWorld(data.getConfig().getString("worldGroup." + menuGroup + ".nether")).getSpawnLocation();
                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(menuGroup + " End")) {
                            if(data.getConfig().getString("worldGroup." + menuGroup + ".the_end") == null){
                                player.sendMessage(ChatColor.YELLOW + "That place does not exist.");
                                event.setCancelled(true);
                                return;
                            }
                            locationToTP = Bukkit.getWorld(data.getConfig().getString("worldGroup." + menuGroup + ".the_end")).getSpawnLocation();

                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(menuGroup + " Home")) {
                            if(data.getConfig().getLocation("playerLocations." + player.getName() + "." + menuGroup + "_HOME") == null){
                                player.sendMessage(ChatColor.YELLOW + "That place does not exist.");
                                event.setCancelled(true);
                                return;
                            }
                            locationToTP = data.getConfig().getLocation("playerLocations." + player.getName() + "." + menuGroup + "_HOME");

                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(menuGroup + " Resume")) {
                            if (data.getConfig().getLocation("playerLocations." + player.getName() + "." + menuGroup) != null) {
                                locationToTP = data.getConfig().getLocation("playerLocations." + player.getName() + "." + menuGroup);
                            }

                            if (data.getConfig().getLocation("menuGroupID." + menuGroup + ".WorldTPWorldSpawnPoint") != null) {
                                locationToTP = data.getConfig().getLocation("menuGroupID." + menuGroup + ".WorldTPWorldSpawnPoint");
                            }

                            if(locationToTP == null){
                                locationToTP = playerLocation;
                            }

                            worldToEnterName = locationToTP.getWorld().getName();
                            worldin = new WorldTPWorld(plugin, playerLocation.getWorld().getName());
                            worldinWorldGroup = worldin.getWorldGroup();

                            if(worldToEnterName.startsWith(worldinWorldGroup)){
                                player.sendMessage(ChatColor.YELLOW + "You are already in the world: " + worldGroupToLeave);
                                event.setCancelled(true);
                                break;
                            }
                        }

                        if (locationToTP != null) {
                            player.teleport(locationToTP);
                        } else {
                            World world = Bukkit.getWorld(menuGroup);
                            player.teleport(world.getSpawnLocation());
                            if (player.getBedSpawnLocation() != null) {
                                player.teleport(player.getBedSpawnLocation());
                            }
                        }

                        data.getConfig().set("playerLocations." + player.getName() + "." + worldGroupToLeave, playerLocation);

                        player.sendMessage(ChatColor.DARK_AQUA + "You have been teleported to " + menuGroup + ".");
                        data.saveConfig();
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    private ItemStack newItem (String material, String name){
        ItemStack item = new ItemStack(Material.valueOf(material));
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(itemMeta);
        return item;
    }
}
