package com.code12.worldtp.worldtpobjects;

import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import org.bukkit.World;

import java.util.List;

public class WorldTPWorld {
    //Variables
    private final DataManager data = References.data;

    private String name;
    private String worldType;
    private String worldGroup;

    public WorldTPWorld(World world){
        this.name = world.getName();
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
        if (name.endsWith("nether")) {
            worldType = "nether";
        }else if(name.endsWith("the_end")){
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
