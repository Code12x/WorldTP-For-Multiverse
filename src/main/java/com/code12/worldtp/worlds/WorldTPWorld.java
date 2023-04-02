package com.code12.worldtp.worlds;

import org.bukkit.World;

public class WorldTPWorld {
    private String name;
    private WorldType worldType;
    private String worldGroupName;

    public WorldTPWorld(World world) {
        this.name = world.getName();

        if (name.endsWith("nether")) {
            worldType = WorldType.NETHER;
            worldGroupName = name.substring(0, name.length()-7);
        }else if (name.endsWith("the_end")) {
            worldType = WorldType.THE_END;
            worldGroupName = name.substring(0, name.length()-8);
        }else {
            worldType = WorldType.OVERWORLD;
            worldGroupName = name;
        }
    }

    public String getWorldGroupName() {
        return worldGroupName;
    }

    public WorldType getWorldType() {
        return worldType;
    }

    public String getName() {
        return name;
    }
}
