package com.code12.worldtp.files;

import com.onarandombox.MultiverseCore.MultiverseCore;
import org.bukkit.Bukkit;

public class References {
    public static DataManager data;
    public static ConfigManager config;
    public static MultiverseCore core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");

    public static void loadData(){
        data = new DataManager();
    }

    public static void loadConfig(){
        config = new ConfigManager();
    }
}
