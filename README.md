 Description
=============

AntiGuest prevents guests from doing specific things like building and fighting, which can be used to grief.


 Features
=============

- prevent guests from (configurable):
    - using levers, buttons, doors and/or pressureplates
    - opening chests and/or doors
    - crafting and cooking
    - fishing
    - sleeping in beds
    - using buckets
    - opening his inventory (might not work)
    - building
    - fighting
    - getting targeted by monsters
    - picking up and/or dropping items
    - using vehicles
    - spamming the chat
- all messages are configurable and support color codes


Installation
=============

- Just put the AntiGuest into you plugins folder
- If you come from a version BEFORE 2.0.0: delete your old config.yml
- restart/reload your server.
- The configuration file will be auto-generated on the first start.


 Permissions
=============

- AntiGuest.interact - Allows the player to interact with the world
    - AntiGuest.lever - Allows the player to use levers
    - AntiGuest.button - Allows the player to push buttons
    - AntiGuest.door - Allows the player to open and close doors
    - AntiGuest.bed - Allows the player to sleep in beds
    - AntiGuest.fish - Allows the player to fish
    - AntiGuest.bucket - Allows the player to use buckets
    - AntiGuest.pressureplate - Allows the player to trigger pressure plates
- AntiGuest.build - Allows the player to build
    - AntiGuest.placeblock - Allows the player to place blocks
    - AntiGuest.breakblock - Allows the player to break blocks
- AntiGuest.craft - Allows the player to do crafting related things
    - AntiGuest.furnace - Allows the player to furnace
    - AntiGuest.workbench - Allows the player to craft
    - AntiGuest.chest - Allows the player to use chests
    - AntiGuest.inventory - Allows the player to open his inventory
- AntiGuest.fight - Allows the player to fight
    - AntiGuest.pvp - Allows the player to fight
    - AntiGuest.monster - Allows the player to get targeted by monsters
- AntiGuest.item - Allows the player to do item related things
    - AntiGuest.pickup - Allows the player to pickup items
    - AntiGuest.drop - Allows the player to drop items
- AntiGuest.vehicle - Allows the player to use vehicles
- AntiGuest.spam - Allows the player to chat as fast as he wants


  Changes
=============

Version 2.0.0

    Splitted the interact prevention into lever, button, pressureplate, door (including trap doors), chest, workbench, furnace and monster
    Added bed prevention
    Added item drop prevention
    Added bucket prevention
    Added fishing prevention
    Added a config entry to enable or disable the debug mode
    Changed the configuratiuon file a bit (You should delete your old file)

Version 1.2.1

    Fixed PAINTING_BREAK checking the wrong permission if only the interaction check was enabled
    Made the chat lock for spamming configurable (in seconds)

Version 1.2.0

    OPs can now always do everything

Version 1.1.2

    fixed block place checking for the wrong permission

Version 1.1.1

    Fixed the painting break event not being registered when only the build prevention was enabled

Version 1.1.0

    Added SuperPerms support, Permissions is now optional
    Split interaction into interaction and vehicles (minecarts, boats)
    interaction also prevents mobs from targeting guests
    Added spam prevention
    enabled pickup prevention again

Version 1.0.0

    Initial release