package com.code12.worldtp.worlds;

import com.code12.worldtp.files.DataManager;
import com.code12.worldtp.files.References;
import org.bukkit.World;

import java.util.List;

public class WorldTPWorld {

    private String name;
    private WorldType worldType;
    private WorldTPWorldGroup worldGroup;

    public WorldTPWorld(World world) {
        this.name = world.getName();

        if (name.endsWith("nether")) {
            worldType = WorldType.NETHER;
        }else if (name.endsWith("the_end")) {
            worldType = WorldType.THE_END;
        }else {
            worldType = WorldType.OVERWORLD;
        }

        DataManager data = References.data;
        List<String> menuGroupList = data.getConfig().getStringList("menuGroupList");
        for (String menuGroup : menuGroupList) {
            if (name.startsWith(menuGroup)) {
                worldGroup = menuGroup;
            }
        }
    }

    public WorldTPWorldGroup getWorldGroup() {
        return worldGroup;
    }

    public WorldType getWorldType() {
        return worldType;
    }

    public String getName() {
        return name;
    }
}
