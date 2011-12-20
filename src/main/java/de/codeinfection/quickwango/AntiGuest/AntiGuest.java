package de.codeinfection.quickwango.AntiGuest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Server;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
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
    }

    public void onEnable()
    {
        this.server = this.getServer();
        this.pm = this.server.getPluginManager();
        this.dataFolder = this.getDataFolder();
        this.dataFolder.mkdirs();
        this.config = this.getConfig();
        this.config.options().copyDefaults(true);

        this.loadPreventions();

        this.saveConfig();

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
            this.preventions.get("cake") ||
            this.preventions.get("brew") ||
            this.preventions.get("enchant")

        )
        {
            debug("interaction preventions registered");
            this.pm.registerEvent(Type.PLAYER_INTERACT, playerListener, Priority.Lowest, this);
        }
        if (this.preventions.get("lavabucket") || this.preventions.get("waterbucket"))
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

    private void loadPreventions()
    {
        debugMode = this.config.getBoolean("debug");

        ConfigurationSection preventionsSection = this.config.getConfigurationSection("preventions");
        if (preventionsSection != null)
        {
            for (String key : preventionsSection.getKeys(false))
            {
                ConfigurationSection prevention = preventionsSection.getConfigurationSection(key);

                this.preventions.put(key, prevention.getBoolean("enable"));
                this.messages.put(key, prevention.getString("message"));
            }
        }

        //special values
        this.vehiclesIgnoreBuildPermissions = this.config.getBoolean("preventions.vehicle.ignoreBuildPermissions");
        this.chatLockDuration               = this.config.getInt("preventions.spam.lockDuration");

        debug("## Preventions:");
        for (Map.Entry<String, Boolean> entry : this.preventions.entrySet())
        {
            debug("\tName: " + entry.getKey());
            debug("\t\tenabled: " + (entry.getValue() ? "True" : "False"));
            debug("\t\tmessage: " + this.messages.get(entry.getKey()));
        }
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
        boolean allowed =  player.hasPermission(permission);

        debug("Player: " + player.getName() + " - Permission: " + type + " - Allowed: " + (allowed ? "Yes" : "No"));

        return allowed;
    }

    public void message(Player player, String type)
    {
        String message = this.messages.get(type);
        if (message != null && message.length() > 0)
        {
            player.sendMessage(message);
        }
    }
}
