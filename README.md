# WorldTP-For-Multiverse

Native Minecraft Version: 1.17

Tested Minecraft Versions: 1.17, 1.18


## __About:__

I believe in making server management easy for the admins, and having a good experience for the players. WorldTP does this by making it so the admins only need to run one command to setup the plugin*. WorldTP also provides a good player experience by using a GUI menu for the players to teleport to other worlds in your server.

WorldTP uses the Multiverse-Core API, so you MUST HAVE "Multiverse-Core" for this plugin to work. Multiverse-Inventories and Multiverse-NetherPortals are also HIGHLY recommended to have installed on your server or some aspects of the plugin may not work properly on your server.

[!] NOTICE! This plugin is designed for SMPs in mind. In order for dimensions to properly link for this plugin, dimensions must follow the standard world naming format. (world, world_nether, world_the_end) Though, you CAN have a stand-alone overworld dimension that will show up on the menu. You CANNOT have a stand-alone nether or end dimensions in the menu.

[!] NOTICE! If you have a version from before 1.2 (so you don't have the option to teleport to specific dimensions with the WorldTP menu), and you want to update the plugin to a later version, uninstall the original one by deleting the WorldTP.jar and WorldTP folder in the plugins folder on your server. Then install the new version of WorldTP. This is so that the data will be stored in a separate file (data.yml) instead of being stored in the config.yml. After 1.2, the config.yml file contains options to customize how the WorldTP menu works.

*several more commands to customize the display of the worlds on the menu, depending on how many worlds are on your server.

## __Features:__

1. A "World Menu" menu (GUI).
2. Each world group in the World Menu can have customized display items and custom display names with the command /editworld.
3. Admin-only worlds! Use /editworld to make any world an admin-only world.
4. Automatically finds worlds and registers them when the command /reloadworlds is run.
5. Players will pick up right where they left off in any given world group. (So, no more teleporting to the end dimension without traveling there.)
6. Permissions!
7. Lobby world. You can set a world to be the world that all players teleport to when they join the server.
8. Instead of having players pick up where they left off in a world, you can set a WorldTP spawn point which will teleport players to a set place in that world when they teleport with the worldtp menu. A WorldTP spawn point can be set for multiple worlds.

## __Commands:__

  __worldtp:__
  
    descrition: Opens a menu for the player to navigate between worlds
    
    usage: /<command>
    
    permission: worldtp.worldtp


  __editworld:__
  
    description: Allows an admin to edit the registery of a world.
    
    usage: /<command> [world name] [display name] [item to be displayed] [admin only (true/false)] (To name the item to be displayed, enter any one of these Material names from the spigot API docs: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html)
    
    permission: worldtp.editworld


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
    
