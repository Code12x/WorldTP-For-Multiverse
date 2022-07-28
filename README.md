## __IMPORTANT!!!__
If you are updating from version 1.3.2 to v1.4.0, you must run the command `/reloadworlds` so WorldTP can fix the world registries in the data.yml.

# WorldTP-For-Multiverse

Native Minecraft Version: 1.17

Tested Minecraft Versions: 1.17, 1.18, 1.19


## __About:__

I believe in making server management easy for the admins, and having a good experience for the players. WorldTP does this by making it so the admins only need to run one command to setup the plugin, and one more command to open a GUI to customize how those worlds look in the WorldTP menu! WorldTP also provides a good player experience by using a GUI menu for the players to teleport to other worlds in your server.

WorldTP uses the Multiverse-Core API, so you MUST HAVE "Multiverse-Core" for this plugin to work. Multiverse-Inventories and Multiverse-NetherPortals are also HIGHLY recommended to have installed on your server or some aspects of the plugin may not work properly on your server.

[!] NOTICE! This plugin is designed for SMPs in mind. In order for dimensions to properly link for this plugin, dimensions must follow the standard world naming format. (world, world_nether, world_the_end) Though, you CAN have a stand-alone overworld dimension that will show up on the menu. You CANNOT have a stand-alone nether or end dimensions in the menu.

## __Features:__

* A "World Menu" menu (GUI).
* [NEW] Each world "group" can be customized with a GUI that can be opened by running the command `/worldtpsettings`.
* Whitelisted worlds! Players with the permission `worldtp.worldtp` can access whitelisted worlds.
* Automatically finds worlds and registers them when the command `/reloadworlds` is run.
* Players will pick up right where they left off in any given world group. (So, no more teleporting to the end dimension without traveling there.)
* Lobby world. You can set a world to be the world that all players teleport to when they join the server.
* Instead of having players pick up where they left off in a world, you can set a WorldTP spawn point which will teleport players to a set place in that world when they teleport with the worldtp menu. A WorldTP spawn point can be set for multiple worlds.

## __Commands:__

  __worldtp:__
  
    descrition: Opens a menu for the player to navigate between worlds
    
    usage: /<command>
    
    permission: worldtp.worldtp


  __worldtpsettings:__
  
    description: Allows an admin to edit the registery of a world.
    
    usage: /<command>
    
    permission: worldtp.worldtpsettings


  __deleteworld:__
  
    description: Allows an admin to delete a registered world group. (only a virtual delete, doesn't acturally delete the world file)
    
    usage: /<command> [world to delete]
    
    permission: worldtp.deleteworld
    

  __listworlds:__
  
    description: Outputs a list of the worlds on a server to the player who issued the command.
    
    usage: /<command>
    
    permission: worldtp.listworlds
    

  __reloadworlds:__
  
    description: Reregisters all the worlds for the WorldTP menu.
    
    usage: /<command>
    
    permission: worldtp.reloadworlds
    

  __setlobby:__
  
    description: Sets a world to be the world that players spawn at when they join the server.
    
    usage: /<command>
    
    permission: worldtp.setlobby
    

  __setworldtpworldspawnpoint:__
  
    description: Sets a location that players teleport to when they enter that world.
    
    usage: /<command>
    
    permission: worldtp.setworldtpworldspawnpoint
    

  __removelobby:__
  
    description: Removes the lobby spawn point location from the server.
    
    usage: /<command>
    
    permission: worldtp.removelobby
    

  __removeworldtpworldspawnpoint:__
  
    description: Removes the WorldTP World Spawn Point for the world that the player is in when the command is run.
    
    usage: /<command>
    
    permission: worldtp.removeworldtpworldspawnpoint
    

  __spawn:__
  
    description: Teleports the player to spawn of the world he is in.
    
    usage: /<command>
    
    permission: worldtp.spawn
    
