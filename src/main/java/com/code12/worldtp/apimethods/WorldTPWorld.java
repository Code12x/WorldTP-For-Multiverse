package com.code12.worldtp.apimethods;

import com.code12.worldtp.WorldTP;
import com.code12.worldtp.files.DataManager;

import java.util.List;

public class WorldTPWorld {
    //Variables
    WorldTP plugin;
    private DataManager data;

    private String name;
    private String worldType;
    private String worldGroup;

    public WorldTPWorld(WorldTP plugin, String worldName){
        this.plugin = plugin;
        this.data = new DataManager(this.plugin);
        this.name = worldName;
    }

    public String getWorldGroup(){
        List<String> menuGroupList = data.getConfig().getStringList("menuGroupList");
        for(String menuGroup : menuGroupList){
            if(name.startsWith(menuGroup)){
                worldGroup = menuGroup;
            }
        }
        return worldGroup;
    }

    public String getWorldType(){
        if (name.toLowerCase().endsWith("nether")) {
            worldType = "nether";
        }else if(name.toLowerCase().endsWith("the_end")){
            worldType = "the_end";
        }else{
            worldType = "overworld";
        }

        return worldType;
    }

    public String getName() {
        return name;
    }
}
