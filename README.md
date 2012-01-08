AntiGuest
=========

Description:
------------
AntiGuest prevents guests from doing specific things like building and fighting, which can be used to grief.

Features:
---------
- prevent guests from (configurable):
    - using levers, buttons, doors and/or pressureplates
    - opening chests and/or doors
    - crafting, cooking, dispensing, enchanting and/or brewing
    - eating cakes
    - fishing
    - sleeping in beds
    - using buckets
    - building
    - fighting
    - getting targeted by monsters
    - picking up and/or dropping items
    - using vehicles
    - spamming the chat or completely chatting
    - starvation
    - sprinting
    - sneaking (currently disabled)
    - moving (currently disabled)
    - changing repeater delays
    - changing or playing noteblocks
    - playing records in jukeboxes
- all messages are configurable and support color codes

Installation:
-------------
- Just put the AntiGuest into you plugins folder
- If you come from a version **BEFORE** 2.0.0: delete your old config.yml
- restart/reload your server
- The configuration file will be auto-generated on the first start

Permissions:
------------
- AntiGuest.\* - *Allows the player to do everything*
    - AntiGuest.interact - Allows the player to interact with the world
        - AntiGuest.lever - *Allows the player to use levers*
        - AntiGuest.button - *Allows the player to push buttons*
        - AntiGuest.door - *Allows the player to open and close doors*
        - AntiGuest.bed - *Allows the player to sleep in beds*
        - AntiGuest.fish - *Allows the player to fish*
            - AntiGuest.lavabucket - *Allows the player to use lava buckets*
            - AntiGuest.waterbucket - *Allows the player to use water buckets*
        - AntiGuest.bucket - *Allows the player to use buckets*
        - AntiGuest.pressureplate - *Allows the player to trigger pressure plates*
        - AntiGuest.cake - *Allows the player to eat cake*
        - AntiGuest.repeater - *Allows the player to change repeater delays*
    - AntiGuest.build - *Allows the player to build*
        - AntiGuest.placeblock - *Allows the player to place blocks*
        - AntiGuest.breakblock - *Allows the player to break blocks*
    - AntiGuest.craft - *Allows the player to do crafting related things*
        - AntiGuest.furnace - *Allows the player to furnace*
        - AntiGuest.workbench - *Allows the player to craft*
        - AntiGuest.dispenser - *Allows the player to dispense*
        - AntiGuest.chest - *Allows the player to use chests*
        - AntiGuest.enchant - *Allows the player to enchant*
        - AntiGuest.brew - *Allows the player to brew*
    - AntiGuest.fight - *Allows the player to fight*
        - AntiGuest.monster - *Allows the player to get targeted by monsters*
        - AntiGuest.pvp - *Allows the player to fight*
    - AntiGuest.item - *Allows the player to do item related things*
        - AntiGuest.drop - *Allows the player to drop items*
        - AntiGuest.pickup - *Allows the player to pickup items*
    - AntiGuest.movement - *Allows the player to move as he wants*
        - AntiGuest.move - *Allows the player to move*
        - AntiGuest.sneak - *Allows the player to sneak*
        - AntiGuest.sprint - *Allows the player to sprint*
    - AntiGuest.music - *Allows the player to do music related things*
        - AntiGuest.noteblock - *Allows the player to make music*
        - AntiGuest.jukebox - *Allows the player to play music*
    - AntiGuest.vehicle - *Allows the player to use vehicles*
    - AntiGuest.chat - *Allows the player to chat*
    - AntiGuest.hunger - *Allows the player to die by hunger*
    - AntiGuest.spam - *Allows the player to chat as fast as he wants*

Configuration:
--------------
Self-explanatory

***[Source on Github](https://github.com/quickwango/AntiGuest)***

Plugin developed by Quick_Wango - [Minecraft Portal](http://mc-portal.de)
