 Description
=============

AntiGuest prevents guests from doing specific things like building and fighting, which can be used to grief.


 Features
=============

- prevent guests from (configurable):
    - interacting
    - building
    - fighting 
    - picking up items
    - using vehicles
    - spamming the chat
- all messages are configurable and support color codes


Installation
=============

- Just put the AntiGuest into you plugins folder
- restart/reload your server.
- The configuration file will be auto-generated on the first start.


 Permissions
=============

- AntiGuest.build - Allows the player to build
- AntiGuest.interact - Allows the player to interact with the world
- AntiGuest.pvp - Allows the player to fight
- AntiGuest.pickup - Allows the player to pickup items
- AntiGuest.vehicle - Allows the player to use vehicles
- AntiGuest.spam - Allows the player to chat as fast as he wants


  Changes
=============

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