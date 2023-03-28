package com.code12.worldtp.worlds;

import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class WorldTPWorldGroup {
    DataManager data = References.data;

    private String worldGroupName;
    private List<WorldTPWorld> worldTpWorlds = new ArrayList<>();

    public WorldTPWorldGroup(String worldGroupName) {
        this.worldGroupName = worldGroupName;
    }

    public void registerWorldGroup() {
        //register the world in the "worldList" List in WorldTP data.yml
        List<String> worldList = data.getConfig().getStringList("menuGroupList");
        worldList.add(worldGroupName);
        data.getConfig().set("menuGroupList", worldList);

        data.getConfig().set("menuGroupID." + worldGroupName + ".displayName", displayName);
        data.getConfig().set("menuGroupID." + worldGroupName + ".material", material.toString());
        data.getConfig().set("menuGroupID." + worldGroupName + ".admin", adminOnly);
        data.getConfig().set("menuGroupID." + worldGroupName + ".position", position);

        data.saveConfig();
    }

    public void deleteWorldGroup(CommandSender sender) {
        //Checks if the world exists
        if (!data.getConfig().getStringList("menuGroupList").contains(worldGroupName)) {
            sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "[!]" + ChatColor.RESET + ChatColor.YELLOW + "World " + worldGroupName + " could not be found.");
            sender.sendMessage(ChatColor.YELLOW + "Worlds:\n " + ChatColor.WHITE + data.getConfig().getStringList("menuGroupList"));
            return;
        }

        //remove the world from "worldGroup" in WorldTP data.yml
        data.getConfig().set("worldGroup." + worldGroupName, null);

        //remove the world from "menuGroupID" in WorldTP config.yml
        data.getConfig().set("menuGroupID." + worldGroupName, null);

        //Check if the deleted world was the lobby world. If so, remove the lobby
        World lobbyWorld = data.getConfig().getLocation("lobby").getWorld();
        WorldTPWorld lobbyWorldTPWorld = new WorldTPWorld(lobbyWorld);
        String lobbyWorldGroup = lobbyWorldTPWorld.getWorldGroup();
        System.out.println(lobbyWorldGroup);
        System.out.println(worldGroupName);;

        if (worldGroupName.equals(lobbyWorldGroup)) {
            data.getConfig().set("lobby", null);
        }

        //remove the registered world in the "menuGroupList" List in WorldTP config.yml
        List<String> menuGroupList = data.getConfig().getStringList("menuGroupList");
        menuGroupList.remove(worldGroupName);
        data.getConfig().set("menuGroupList", menuGroupList);

        //Update the positions of the worlds in menuGroupID.<world name>.position so that no gaps are created in the SelectWorldToEditGui
        int deletedWorldPosition = data.getConfig().getInt("menuGroupID." + worldGroupName + ".position");

        List<String> oldMenuGroupList = data.getConfig().getStringList("menuGroupList");
        for (String menuGroup : oldMenuGroupList) {
            int menuGroupPosition = data.getConfig().getInt("menuGroupID." + menuGroup + ".position");

            if (menuGroupPosition > deletedWorldPosition) {
                menuGroupPosition -= 1;
                data.getConfig().set("menuGroupID." + menuGroup + ".position", menuGroupPosition);
            }
        }

        data.saveConfig();

        sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "[!]" + ChatColor.RESET + ChatColor.YELLOW + "NOTICE, the command deleteworld DOES NOT actually delete a world, it only deletes the registry of the world for the WorldTP plugin. To permanently delete a world use the multiverse command: mv delete");

        sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "[!]" + ChatColor.RESET + ChatColor.YELLOW + "World " + worldGroupName + " was successfully deleted.");
        sender.sendMessage(ChatColor.YELLOW + "Remaining Worlds:\n " + ChatColor.WHITE + data.getConfig().getStringList("menuGroupList"));
    }

    public String getWorldGroupName() {
        return worldGroupName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Boolean getAdminOnly() {
        return adminOnly;
    }

    public void setAdminOnly(Boolean adminOnly) {
        this.adminOnly = adminOnly;
    }
}
