package com.code12.worldtp.files;

import com.code12.worldtp.WorldTP;

public class References {
    public static DataManager data;
    public static ConfigManager config;

    public static void loadData(WorldTP plugin){
        data = new DataManager(plugin);
    }

    public static void loadConfig(WorldTP plugin){
        config = new ConfigManager(plugin);
    }
}
