package com.code12.worldtp.files;

import com.code12.worldtp.WorldTP;
import com.onarandombox.MultiverseCore.MultiverseCore;
import org.bukkit.Bukkit;

public class References {
    public static WorldTP plugin;
    public static DataManager data;
    public static ConfigManager config;
    public static final MultiverseCore core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");

    public static void setPlugin(WorldTP p){
        plugin = p;
    }

    public static void loadData(){
        data = new DataManager();
    }

    public static void loadConfig(){
        config = new ConfigManager();
    }
}
