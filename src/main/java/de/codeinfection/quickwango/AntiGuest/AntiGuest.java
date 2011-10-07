package de.codeinfection.quickwango.AntiGuest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;

public class AntiGuest extends JavaPlugin
{
    protected static final Logger log = Logger.getLogger("Minecraft");
    public static boolean debugMode = false;
    public final static int messageWaitTime = 3000;
    
    protected Server server;
    protected PluginManager pm;
    protected Configuration config;
    protected File dataFolder;
    public final HashMap<String, Boolean> preventions;
    public final HashMap<String, String> messages;
    public int chatLockDuration;
    public boolean vehiclesIgnoreBuildPermissions;

    public AntiGuest()
    {
        this.preventions = new HashMap<String, Boolean>();
        this.messages = new HashMap<String, String>();

        this.preventions.put("monster", true);
        this.messages.put("monster", "&4You are not allowed to get targeted by monsters!");

        this.preventions.put("lever", false);
        this.messages.put("lever", "&4You are not allowed to use levers!");

        this.preventions.put("button", false);
        this.messages.put("button", "&4You are not allowed to push buttons!");

        this.preventions.put("door", true);
        this.messages.put("door", "&4You are not allowed to interact with doors!");

        this.preventions.put("chest", true);
        this.messages.put("chest", "&4You are not allowed to use chests");

        this.preventions.put("workbench", false);
        this.messages.put("workbench", "&4You are not allowed to craft!");

        this.preventions.put("furnace", false);
        this.messages.put("furnace", "&4You are not allowed to cook!");

        this.preventions.put("pressureplate", false);
        this.messages.put("pressureplate", "&4You are not allowed to pressure the plate!");

        this.preventions.put("bucket", true);
        this.messages.put("bucket", "&4You are not allowed to use buckets");

        this.preventions.put("placeblock", true);
        this.messages.put("placeblock", "&4You are not allowed to place blocks!");

        this.preventions.put("breakblock", true);
        this.messages.put("breakblock", "&4You are not allowed to break blocks!");
        
        this.preventions.put("pvp", true);
        this.messages.put("pvp", "&4You are not allowed to fight!");

        this.preventions.put("pickup", true);
        this.messages.put("pickup", "&4You are not allowed to pickup items!");

        this.preventions.put("vehicle", true);
        this.messages.put("vehicle", "&4You are not allowed to use vehicles!");
        this.vehiclesIgnoreBuildPermissions = false;

        this.preventions.put("spam", true);
        this.messages.put("spam", "&4Don't spam the chat!");
        this.chatLockDuration = 2;

        this.preventions.put("bed", false);
        this.messages.put("bed", "&4You are not allowed to sleep!");

        this.preventions.put("drop", false);
        this.messages.put("drop", "&4You are not allowed to drop items!");

        this.preventions.put("fish", false);
        this.messages.put("fish", "&4You are not allowed to fish!");

        this.preventions.put("dispenser", false);
        this.messages.put("dispenser", "&4You are not allowed to dispense!");

        this.preventions.put("chat", false);
        this.messages.put("chat", "&4You are not allowed to chat!");

        this.preventions.put("cake", false);
        this.messages.put("cake", "&4The cake is a lie!!");

        this.preventions.put("hunger", false);
        this.messages.put("hanger", "&4You are not allowed to die the starvation death!");

        this.preventions.put("sprint", false);
        this.messages.put("sprint", "&4You are not allowed to sprint!");

        this.preventions.put("sneak", false);
        this.messages.put("sneak", "&4You are not allowed to sneak!");

        this.preventions.put("move", false);
        this.messages.put("move", "&4You are not allowed to move!");
    }

    public void onEnable()
    {
        this.server = this.getServer();
        this.pm = this.server.getPluginManager();
        this.config = this.getConfiguration();
        this.dataFolder = this.getDataFolder();

        this.dataFolder.mkdirs();
        this.loadConfig();

        for (Map.Entry<String, String> entry : this.messages.entrySet())
        {
            String message = entry.getValue();
            if (message != null)
            {
                this.messages.put(entry.getKey(), message.replaceAll("&([a-f0-9])", "\u00A7$1"));
            }
        }

        AntiGuestPlayerListener playerListener = new AntiGuestPlayerListener(this);
        AntiGuestEntityListener entityListener = new AntiGuestEntityListener(this);
        AntiGuestBlockListener blockListener = new AntiGuestBlockListener(this);
        AntiGuestVehicleListener vehicleListener = new AntiGuestVehicleListener(this);
        
        if (
            this.preventions.get("lever") ||
            this.preventions.get("button") ||
            this.preventions.get("door") ||
            this.preventions.get("chest") ||
            this.preventions.get("workbench") ||
            this.preventions.get("furnace") ||
            this.preventions.get("dispenser") ||
            this.preventions.get("pressureplate") ||
            this.preventions.get("placeblock") ||
            this.preventions.get("cake")
        )
        {
            debug("interaction preventions registered");
            this.pm.registerEvent(Type.PLAYER_INTERACT, playerListener, Priority.Lowest, this);
        }
        if (this.preventions.get("bucket"))
        {
            debug("bucket prevention registered");
            this.pm.registerEvent(Type.PLAYER_BUCKET_EMPTY, playerListener, Priority.Lowest, this);
            this.pm.registerEvent(Type.PLAYER_BUCKET_FILL, playerListener, Priority.Lowest, this);
        }
        if (this.preventions.get("monster"))
        {
            debug("monster prevention registered");
            this.pm.registerEvent(Type.ENTITY_TARGET, entityListener, Priority.Lowest, this);
        }
        if (this.preventions.get("placeblock"))
        {
            debug("place blocks prevention registered");
            this.pm.registerEvent(Type.BLOCK_PLACE, blockListener, Priority.Lowest, this);
            this.pm.registerEvent(Type.PAINTING_PLACE, entityListener, Priority.Lowest, this);
        }
        if (this.preventions.get("breakblock"))
        {
            debug("break block prevention registered");
            this.pm.registerEvent(Type.VEHICLE_DESTROY, vehicleListener, Priority.Lowest, this);
            this.pm.registerEvent(Type.BLOCK_BREAK, blockListener, Priority.Lowest, this);
            this.pm.registerEvent(Type.PAINTING_BREAK, entityListener, Priority.Lowest, this);
        }
        if (this.preventions.get("pvp") || this.preventions.get("hunger"))
        {
            debug("damage preventions registered");
            this.pm.registerEvent(Type.ENTITY_DAMAGE, entityListener, Priority.Lowest, this);
        }
        if (this.preventions.get("pickup"))
        {
            debug("pickup prevention registered");
            this.pm.registerEvent(Type.PLAYER_PICKUP_ITEM, playerListener, Priority.Lowest, this);
        }
        if (this.preventions.get("drop"))
        {
            debug("drop prevention registered");
            this.pm.registerEvent(Type.PLAYER_DROP_ITEM, playerListener, Priority.Lowest, this);
        }
        if (this.preventions.get("fish"))
        {
            debug("fish prevention registered");
            this.pm.registerEvent(Type.PLAYER_FISH, playerListener, Priority.Lowest, this);
        }
        if (this.preventions.get("bed"))
        {
            debug("bed prevention registered");
            this.pm.registerEvent(Type.PLAYER_BED_ENTER, playerListener, Priority.Lowest, this);
        }
        if (this.preventions.get("vehicle"))
        {
            debug("vehicle prevention registered");
            this.pm.registerEvent(Type.VEHICLE_COLLISION_ENTITY, vehicleListener, Priority.Lowest, this);
            this.pm.registerEvent(Type.VEHICLE_ENTER, vehicleListener, Priority.Lowest, this);
            this.pm.registerEvent(Type.VEHICLE_EXIT, vehicleListener, Priority.Lowest, this);
        }
        if (this.preventions.get("spam") || this.preventions.get("chat"))
        {
            debug("chat preventions registered");
            this.pm.registerEvent(Type.PLAYER_CHAT, playerListener, Priority.Lowest, this);
        }
        if (this.preventions.get("sprint"))
        {
            debug("sprint prevention registered");
            this.pm.registerEvent(Type.PLAYER_TOGGLE_SPRINT, playerListener, Priority.Lowest, this);
        }
        if (this.preventions.get("sneak"))
        {
            debug("sneak prevention registered");
            this.pm.registerEvent(Type.PLAYER_TOGGLE_SNEAK, playerListener, Priority.Lowest, this);
        }
        if (this.preventions.get("move"))
        {
            debug("move prevention registered");
            this.pm.registerEvent(Type.PLAYER_MOVE, playerListener, Priority.Lowest, this);
        }

        log("Version " + this.getDescription().getVersion() + " enabled");
    }

    public void onDisable()
    {
        log(this.getDescription().getVersion() + " disabled");
    }

    private void loadConfig()
    {
        this.config.load();

        debugMode = this.config.getBoolean("debug", debugMode);

        this.preventions.put("breakblock", this.config.getBoolean("preventions.breakblock.enable", this.preventions.get("breakblock")));
        this.messages.put("breakblock", this.config.getString("preventions.breakblock.message", this.messages.get("breakblock")));

        this.preventions.put("placeblock", this.config.getBoolean("preventions.placeblock.enable", this.preventions.get("placeblock")));
        this.messages.put("placeblock", this.config.getString("preventions.placeblock.message", this.messages.get("placeblock")));

        this.preventions.put("pvp", this.config.getBoolean("preventions.pvp.enable", this.preventions.get("pvp")));
        this.messages.put("pvp", this.config.getString("preventions.pvp.message", this.messages.get("pvp")));

        this.preventions.put("pickup", this.config.getBoolean("preventions.pickup.enable", this.preventions.get("pickup")));
        this.messages.put("pickup", this.config.getString("preventions.pickup.message", this.messages.get("pickup")));

        this.preventions.put("vehicle", this.config.getBoolean("preventions.vehicle.enable", this.preventions.get("vehicle")));
        this.messages.put("vehicle", this.config.getString("preventions.vehicle.message", this.messages.get("vehicle")));
        this.vehiclesIgnoreBuildPermissions = this.config.getBoolean("preventions.vehicle.ignoreBuildPermissions", this.vehiclesIgnoreBuildPermissions);

        this.preventions.put("spam", this.config.getBoolean("preventions.spam.enable", this.preventions.get("spam")));
        this.messages.put("spam", this.config.getString("preventions.spam.message", this.messages.get("spam")));
        this.chatLockDuration = this.config.getInt("preventions.spam.lockDuration", this.chatLockDuration);

        this.preventions.put("monster", this.config.getBoolean("preventions.monster.enable", this.preventions.get("monster")));
        this.messages.put("monster", this.config.getString("preventions.monster.message", this.messages.get("monster")));

        this.preventions.put("lever", this.config.getBoolean("preventions.lever.enable", this.preventions.get("lever")));
        this.messages.put("lever", this.config.getString("preventions.lever.message", this.messages.get("lever")));

        this.preventions.put("button", this.config.getBoolean("preventions.button.enable", this.preventions.get("button")));
        this.messages.put("button", this.config.getString("preventions.button.message", this.messages.get("button")));

        this.preventions.put("pressureplate", this.config.getBoolean("preventions.pressureplate.enable", this.preventions.get("pressureplate")));
        this.messages.put("pressureplate", this.config.getString("preventions.pressureplate.message", this.messages.get("pressureplate")));

        this.preventions.put("chest", this.config.getBoolean("preventions.chest.enable", this.preventions.get("chest")));
        this.messages.put("chest", this.config.getString("preventions.chest.message", this.messages.get("chest")));

        this.preventions.put("furnace", this.config.getBoolean("preventions.furnace.enable", this.preventions.get("furnace")));
        this.messages.put("furnace", this.config.getString("preventions.furnace.message", this.messages.get("furnace")));

        this.preventions.put("bucket", this.config.getBoolean("preventions.bucket.enable", this.preventions.get("bucket")));
        this.messages.put("bucket", this.config.getString("preventions.bucket.message", this.messages.get("bucket")));

        this.preventions.put("workbench", this.config.getBoolean("preventions.workbench.enable", this.preventions.get("workbench")));
        this.messages.put("workbench", this.config.getString("preventions.workbench.message", this.messages.get("workbench")));

        this.preventions.put("door", this.config.getBoolean("preventions.door.enable", this.preventions.get("door")));
        this.messages.put("door", this.config.getString("preventions.door.message", this.messages.get("door")));

        this.preventions.put("bed", this.config.getBoolean("preventions.bed.enable", this.preventions.get("bed")));
        this.messages.put("bed", this.config.getString("preventions.bed.message", this.messages.get("bed")));

        this.preventions.put("drop", this.config.getBoolean("preventions.drop.enable", this.preventions.get("drop")));
        this.messages.put("drop", this.config.getString("preventions.drop.message", this.messages.get("drop")));

        this.preventions.put("fish", this.config.getBoolean("preventions.fish.enable", this.preventions.get("fish")));
        this.messages.put("fish", this.config.getString("preventions.fish.message", this.messages.get("fish")));

        this.preventions.put("dispenser", this.config.getBoolean("preventions.dispenser.enable", this.preventions.get("dispenser")));
        this.messages.put("dispenser", this.config.getString("preventions.dispenser.message", this.messages.get("dispenser")));

        this.preventions.put("chat", this.config.getBoolean("preventions.chat.enable", this.preventions.get("chat")));
        this.messages.put("chat", this.config.getString("preventions.chat.message", this.messages.get("chat")));

        this.preventions.put("cake", this.config.getBoolean("preventions.cake.enable", this.preventions.get("cake")));
        this.messages.put("cake", this.config.getString("preventions.cake.message", this.messages.get("cake")));

        this.preventions.put("hunger", this.config.getBoolean("preventions.hunger.enable", this.preventions.get("hunger")));
        this.messages.put("hunger", this.config.getString("preventions.hunger.message", this.messages.get("hunger")));

        this.preventions.put("sprint", this.config.getBoolean("preventions.sprint.enable", this.preventions.get("sprint")));
        this.messages.put("sprint", this.config.getString("preventions.sprint.message", this.messages.get("sprint")));

        this.preventions.put("sneak", this.config.getBoolean("preventions.sneak.enable", this.preventions.get("sneak")));
        this.messages.put("sneak", this.config.getString("preventions.sneak.message", this.messages.get("sneak")));

        this.preventions.put("move", this.config.getBoolean("preventions.move.enable", this.preventions.get("move")));
        this.messages.put("move", this.config.getString("preventions.move.message", this.messages.get("move")));

        this.config.removeProperty("preventions");

        this.saveConfig();
    }

    private void saveConfig()
    {
        this.config.setProperty("preventions.spam.message", this.messages.get("spam"));
        this.config.setProperty("preventions.spam.lockDuration", this.chatLockDuration);
        this.config.setProperty("preventions.spam.enable", this.preventions.get("spam"));
        
        this.config.setProperty("preventions.vehicle.message", this.messages.get("vehicle"));
        this.config.setProperty("preventions.vehicle.enable", this.preventions.get("vehicle"));
        this.config.setProperty("preventions.vehicle.ignoreBuildPermissions", this.vehiclesIgnoreBuildPermissions);

        this.config.setProperty("preventions.pickup.message", this.messages.get("pickup"));
        this.config.setProperty("preventions.pickup.enable", this.preventions.get("pickup"));

        this.config.setProperty("preventions.pvp.message", this.messages.get("pvp"));
        this.config.setProperty("preventions.pvp.enable", this.preventions.get("pvp"));

        this.config.setProperty("preventions.breakblock.message", this.messages.get("breakblock"));
        this.config.setProperty("preventions.breakblock.enable", this.preventions.get("breakblock"));

        this.config.setProperty("preventions.placeblock.message", this.messages.get("placeblock"));
        this.config.setProperty("preventions.placeblock.enable", this.preventions.get("placeblock"));

        this.config.setProperty("preventions.monster.message", this.messages.get("monster"));
        this.config.setProperty("preventions.monster.enable", this.preventions.get("monster"));

        this.config.setProperty("preventions.lever.message", this.messages.get("lever"));
        this.config.setProperty("preventions.lever.enable", this.preventions.get("lever"));

        this.config.setProperty("preventions.button.message", this.messages.get("button"));
        this.config.setProperty("preventions.button.enable", this.preventions.get("button"));

        this.config.setProperty("preventions.pressureplate.message", this.messages.get("pressureplate"));
        this.config.setProperty("preventions.pressureplate.enable", this.preventions.get("pressureplate"));

        this.config.setProperty("preventions.chest.message", this.messages.get("chest"));
        this.config.setProperty("preventions.chest.enable", this.preventions.get("chest"));

        this.config.setProperty("preventions.furnace.message", this.messages.get("furnace"));
        this.config.setProperty("preventions.furnace.enable", this.preventions.get("furnace"));

        this.config.setProperty("preventions.bucket.message", this.messages.get("bucket"));
        this.config.setProperty("preventions.bucket.enable", this.preventions.get("bucket"));
        
        this.config.setProperty("preventions.workbench.message", this.messages.get("workbench"));
        this.config.setProperty("preventions.workbench.enable", this.preventions.get("workbench"));

        this.config.setProperty("preventions.door.message", this.messages.get("door"));
        this.config.setProperty("preventions.door.enable", this.preventions.get("door"));

        this.config.setProperty("preventions.bed.message", this.messages.get("bed"));
        this.config.setProperty("preventions.bed.enable", this.preventions.get("bed"));

        this.config.setProperty("preventions.drop.message", this.messages.get("drop"));
        this.config.setProperty("preventions.drop.enable", this.preventions.get("drop"));

        this.config.setProperty("preventions.fish.message", this.messages.get("fish"));
        this.config.setProperty("preventions.fish.enable", this.preventions.get("fish"));

        this.config.setProperty("preventions.dispenser.message", this.messages.get("dispenser"));
        this.config.setProperty("preventions.dispenser.enable", this.preventions.get("dispenser"));

        this.config.setProperty("preventions.chat.message", this.messages.get("chat"));
        this.config.setProperty("preventions.chat.enable", this.preventions.get("chat"));

        this.config.setProperty("preventions.cake.message", this.messages.get("cake"));
        this.config.setProperty("preventions.cake.enable", this.preventions.get("cake"));

        this.config.setProperty("preventions.hunger.message", this.messages.get("hunger"));
        this.config.setProperty("preventions.hunger.enable", this.preventions.get("hunger"));

        this.config.setProperty("preventions.sprint.message", this.messages.get("sprint"));
        this.config.setProperty("preventions.sprint.enable", this.preventions.get("sprint"));

        this.config.setProperty("preventions.sneak.message", this.messages.get("sneak"));
        this.config.setProperty("preventions.sneak.enable", this.preventions.get("sneak"));

        this.config.setProperty("preventions.move.message", this.messages.get("move"));
        this.config.setProperty("preventions.move.enable", this.preventions.get("move"));

        this.config.setProperty("debug", debugMode);
        
        this.config.save();
    }

    public static void log(String msg)
    {
        log.log(Level.INFO, "[AntiGuest] " + msg);
    }

    public static void error(String msg)
    {
        log.log(Level.SEVERE, "[AntiGuest] " + msg);
    }

    public static void error(String msg, Throwable t)
    {
        log.log(Level.SEVERE, "[AntiGuest] " + msg, t);
    }

    public static void debug(String msg)
    {
        if (debugMode)
        {
            log("[debug] " + msg);
        }
    }

    public boolean can(Player player, String type)
    {
        final String permission = "AntiGuest." + type;
        return player.hasPermission(permission);
    }

    public void message(Player player, String type)
    {
        String message = this.messages.get(type);
        if (message != null && !message.isEmpty())
        {
            player.sendMessage(message);
        }
    }
}
