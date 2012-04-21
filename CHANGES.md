Changes
=======

Version 3.2.0
-------------
- fixed disabling preventions via command
- added temporary enabling/disabling
- add 6 new command
    - enableall -- enables all preventions
    - disableall -- disables all preventions
    - setmessage -- sets the message of a prevention
    - language -- changes the language
    - reset -- resets all configurations
    - badword -- adds a badword to the swear prevention
- enabled some preventions by default
- improved all inventory related preventions
- fixed the breakblock preventions to prevent taking out fire
- added translation (languages: en, de)
- added a converter (old config.yml => prevention configs)

Version 3.1.0
-------------
- added CraftBukkitUpToDate support
- fixed color parsing (this may break 1.1 compatibility)
- added a swear prevention
- throttled the messages for fire related damage causes and void damage
- fixed filtering of the damage prevention
- made breakblock and placeblock filterable
- made the item prevention ignore blocks (item ID <= 256)
- fixed enabling preventions that are disabled in the config
- made the commands case-insensitive
- removed 1.1-R4 compatibility code
- splitted the huge configuration into small prevent configurations
- the enable and disable commands save the prevention's state to its config
- added a parameter to the reload command to be able to reload specific preventions

Version 3.0.3
-------------
- fixed and improved the spam prevention

Version 3.0.2
-------------
- Partially supports 1.1-R4
    - incompatible preventions will fail to load
    - unloading preventions (eg on plugin reload) will print error messages, this should be avoided

Version 3.0.1
-------------
- improved some preventions
    - item: prevents non-block interactions now
    - damage: can prevent splash potion effects
    - command: trims the list items
- improved performance of filtered preventions using the none-mode
- fixed chests pseudo opening by also preventing the interaction
- fixed a possible NPE

Version 3.0.0
-------------
- completely rewritten
- depends now on Bukkit 1.1-R5 !
- dynamic default configuration generation
- permissions reworked
    - dynamic permission registration
    - permissions are all lowercased
    - prevention permissions are: antiguest.preventions.<preventionname\>
    - command permissions are: antiguest.commands.<command\>
- added a API for other plugins (currently undocumented)
- plugin is reloadable now
- added filtered preventions (item, pickup, drop and enchant)
- added a bow prevention (shooting the bow)
- added a tame prevention
- added a changesign prevention
- added a shear prevention
- added a afk prevention
- added a damage prevention
- added a milking prevention
- changed following preventions to use the new inventory API: chest, furnace, enchant, dispenser, brew, workbench
- changed the hungerprevention to prevent the hunger value from changing
- renamed the pvp prevention to fight
- added commands /antiguest <command\> (alias: /ag <command\>)
    - commands: help, list, can, reload, debug

Version 2.5.0
-------------
- split bukkit into waterbucket and lavabucket
- added additional lowercase permission check
- added noteblock prevention
- added jukebox prevention
- disabled move prevention (buggy)
- disabled sneak prevention (buggy, seems to be a CraftBukkit bug)

Version 2.4.0
-------------
- fixed JDK 1.5 compatibility
- switched to the new Configuration API
- added 1.0 stuff (enchanting and brewing)
- added repeater change prevention
- some improvements, more to come

Version 2.3.0
------------
- Added the burning furnace to the furnace prevention
- Added a hunger prevetion
- Added a sneak prevetion
- Added a sprint prevention
- Added a move prevention

Version 2.2.0
-------------
- Added the fence gate to the door prevention
- Changed the vehicle prevention to check place-/breakblock and/or vehicle

Version 2.1.0
-------------
- Added complete chat prevention
- Added cake prevention
- Fixed vehicle prevention to include powered minecarts

Version 2.0.3
-------------
- Fixed breaking vehicles

Version 2.0.2
-------------
- Fixed a typo in the permissions

Version 2.0.1
-------------
- Added dispenser prevention
- removed useless config entry of inventory prevention, because it's impossible
- new preventions will be automaticly added to the config (no deleting needed)

Version 2.0.0
-------------
- Splitted the interact prevention into lever, button, pressureplate, door (including trap doors), chest, workbench, furnace and monster
- Added bed prevention
- Added item drop prevention
- Added bucket prevention
- Added fishing prevention
- Added a config entry to enable or disable the debug mode
- Changed the configuratiuon file a bit (You should delete your old file)

Version 1.2.1
-------------
- Fixed PAINTING_BREAK checking the wrong permission if only the interaction check was enabled
- Made the chat lock for spamming configurable (in seconds)

Version 1.2.0
-------------
- OPs can now always do everything

Version 1.1.2
-------------
- fixed block place checking for the wrong permission

Version 1.1.1
-------------
- Fixed the painting break event not being registered when only the build prevention was enabled

Version 1.1.0
-------------
- Added SuperPerms support, Permissions is now optional
- Split interaction into interaction and vehicles (minecarts, boats)
- interaction also prevents mobs from targeting guests
- Added spam prevention
- enabled pickup prevention again

Version 1.0.0
-------------
- Initial release
