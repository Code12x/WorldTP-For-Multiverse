package com.code12.worldtp.worldtpobjects;

import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class WorldTPWorldGroup {
    //Variables
    public DataManager data = References.data;

    private final String worldName;
    @Setter private String displayName;
    @Setter private int position;
    @Setter private Material material;
    @Setter private Boolean adminOnly;


    public WorldTPWorldGroup(String worldName){
        this.worldName = worldName;
    }

    public void registerWorldGroup(){
        //register the world in the "worldList" List in WorldTP data.yml
        List<String> worldList = data.getConfig().getStringList("menuGroupList");
        worldList.add(worldName);
        data.getConfig().set("menuGroupList", worldList);

        data.getConfig().set("menuGroupID." + worldName + ".displayName", displayName);
        data.getConfig().set("menuGroupID." + worldName + ".material", material.toString());
        data.getConfig().set("menuGroupID." + worldName + ".admin", adminOnly);
        data.getConfig().set("menuGroupID." + worldName + ".position", position);

        data.saveConfig();
    }

    public void deleteWorldGroup(CommandSender sender){
        //Checks if the world exists
        if(!data.getConfig().getStringList("menuGroupList").contains(worldName)){
            sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "[!]" + ChatColor.RESET + ChatColor.YELLOW + "World " + worldName + " could not be found.");
            sender.sendMessage(ChatColor.YELLOW + "Worlds:\n " + ChatColor.WHITE + data.getConfig().getStringList("menuGroupList"));
            return;
        }

        //remove the world from "worldGroup" in WorldTP data.yml
        List<String> menuGroupList = data.getConfig().getStringList("menuGroupList");
        for (String menuGroup : menuGroupList){
            if(menuGroup.equals(worldName)){
                data.getConfig().set("worldGroup." + menuGroup, null);
            }
        }

        //remove the world from "menuGroupID" in WorldTP config.yml
        data.getConfig().set("menuGroupID." + worldName, null);

        //remove the player locations for the world group


        //remove the registered world in the "menuGroupList" List in WorldTP config.yml
        List<String> worldList = data.getConfig().getStringList("menuGroupList");
        worldList.remove(worldName);
        data.getConfig().set("menuGroupList", worldList);

        data.saveConfig();

        sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "[!]" + ChatColor.RESET + ChatColor.YELLOW + "NOTICE, the command deleteworld DOES NOT actually delete a world, it only deletes the registry of the world for the WorldTP plugin. To permanently delete a world use the multiverse command: mv delete");

        sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "[!]" + ChatColor.RESET + ChatColor.YELLOW + "World " + worldName + " was successfully deleted.");
        sender.sendMessage(ChatColor.YELLOW + "Remaining Worlds:\n " + ChatColor.WHITE + data.getConfig().getStringList("menuGroupList"));
    }
}
