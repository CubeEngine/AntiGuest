Changes
=======

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
