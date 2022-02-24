package com.code12.worldtp.files;

import com.code12.worldtp.WorldTP;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class ConfigManager {
    private WorldTP plugin;
    private FileConfiguration configConfig = null;
    private File configFile = null;

    public ConfigManager(WorldTP plugin){
        this.plugin = plugin;
        // saves/initializes the config
        saveDefaultConfig();
    }

    public void reloadConfig(){
        if(this.configFile == null) {
            this.configFile = new File(this.plugin.getDataFolder(), "config.yml");
        }
        this.configConfig = YamlConfiguration.loadConfiguration(this.configFile);

        InputStream defaultStream = this.plugin.getResource("config.yml");
        if(defaultStream != null){
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.configConfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig(){
        if(this.configConfig == null)
            reloadConfig();

        return this.configConfig;
    }

    public void saveConfig(){
        if(this.configConfig == null || this.configFile == null) {
            plugin.getLogger().warning("this.configConfig == null || this.configFile == null");
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
            this.configFile = new File(this.plugin.getDataFolder(), "config.yml");
        }

        if(!this.configFile.exists()){
            this.plugin.saveResource("config.yml", false);
        }
    }
}