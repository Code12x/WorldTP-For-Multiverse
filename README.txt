To install:
	1. Stop your server.
	2. Place WorldTP.jar into the plugins folder of your server.
	3. Start your server.

	Steps 4-7 are if you want to edit which dimensions players can teleport to.

	4. Run "/reloadworlds" from either the consol, or in-game.
	5. Stop the server.
	6. Edit and save the config.yml file in the plugins/WorldTP folder.
	7. Start the server, and your customized configurations will be applied to the WorldTP menu.

To uninstall:
	1. Stop your server.
	2. Delete WorldTP.jar in your plugins folder of your server.
	3. Delete WorldTP folder in your plugins folder of your server.
	4. Restart your server.

About:
I believe in making server management easy for the admins, and having a good experience for the players. WorldTP does this by making it so the admins only need to run one command to setup the plugin*. WorldTP also provides a good player experience by using a GUI menu for the players to teleport to other worlds in your server.

WorldTP uses the Multiverse-Core API AND the Multiveres-inventories api, so you MUST HAVE "Multiverse-Core" AND "Multiverse-Inventories" for this plugin to work. Multiverse-NetherPortals is also HIGHLY recommended to have installed on your server or some aspects of the plugin may not work on your server.

[!] NOTICE! This plugin is designed for SMPs in mind. In order for dimensions to properly link for this plugin, dimensions must follow the standard world naming format. (world, world_nether, world_the_end) Though, you CAN have a stand-alone overworld dimension that will show up on the menu. You CANNOT have a stand-alone nether or end dimensions in the menu.

*several more commands to customize the display of the worlds on the menu, depending on how many worlds are on your server.

Features:
1. A "World Menu" menu (GUI).
2. Each world group in the World Menu can have customized display items and custom display names with the command /editworld.
3. Admin-only worlds! Use /editworld to make any world an admin-only world.
4. Automatically finds worlds and registers them when the command /reloadworlds is run.
5. Players will pick up right where they left off in any given world group. (So, no more teleporting to the end dimension without traveling there.)
6. Permissions!
7. Lobby world. You can set a world to be the world that all players teleport to when they join the server.
8. Instead of having players pick up where they left off in a world, you can set a WorldTP spawn point worlds which will teleport players to a set place in that world when they teleport with the worldtp menu. A WorldTP spawn point can be set for multiple worlds.
9. Dimension options for each world for the WorldTP menu.
10. /sethome and /home (to disable /sethome and /home, don't give the players the permission worldtp.sethome or worldtp.home)
11. /spawn