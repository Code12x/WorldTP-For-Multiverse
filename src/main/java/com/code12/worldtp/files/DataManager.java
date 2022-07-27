package com.code12.worldtp.files;

import com.code12.worldtp.WorldTP;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.logging.Level;

public class DataManager {
    private WorldTP plugin;
    private FileConfiguration dataConfig = null;
    private File configFile = null;

    public DataManager(WorldTP plugin){
        this.plugin = plugin;

        // saves/initializes the config
        saveDefaultConfig();
    }

    public void reloadConfig(){
        if(this.configFile == null)
            this.configFile = new File(this.plugin.getDataFolder(), "data.yml");
        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);
    }

    public FileConfiguration getConfig(){
        if(this.dataConfig == null)
            reloadConfig();

        return this.dataConfig;
    }

    public void saveConfig(){
        if(this.dataConfig == null || this.configFile == null) {
            return;
        }

        try {
            this.getConfig().save(this.configFile);
        } catch (IOException e) {
            this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, e);
        }
    }

    public void saveDefaultConfig(){
        if(this.configFile == null){
            this.configFile = new File(this.plugin.getDataFolder(), "data.yml");
        }

        if(!this.configFile.exists()){
            this.plugin.saveResource("data.yml", false);
        }
    }
}
