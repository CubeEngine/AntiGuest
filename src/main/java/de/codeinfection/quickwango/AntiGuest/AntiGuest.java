package de.codeinfection.quickwango.AntiGuest;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
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
    
    protected Server server;
    protected PluginManager pm;
    protected Configuration config;
    protected File dataFolder;
    protected PermissionHandler permissionHandler;
    protected HashMap<String, Boolean> actions;
    protected HashMap<String, String> messages;
    public int chatLockDuration;

    public AntiGuest()
    {
        this.actions = new HashMap<String, Boolean>();
        this.messages = new HashMap<String, String>();

        this.actions.put("monster", true);
        this.messages.put("monster", "&4You are not allowed to get targeted by monsters!");

        this.actions.put("lever", true);
        this.messages.put("lever", "&4You are not allowed to use levers!");

        this.actions.put("button", true);
        this.messages.put("button", "&4You are not allowed to use buttons!");

        this.actions.put("door", true);
        this.messages.put("door", "&4You are not allowed to interact with doors!");

        this.actions.put("chest", true);
        this.messages.put("chest", "&4You are not allowed to use chests");

        this.actions.put("workbench", true);
        this.messages.put("workbench", "&4You are not allowed to craft!");

        this.actions.put("furnace", true);
        this.messages.put("furnace", "&4You are not allowed to cook!");

        this.actions.put("pressureplate", true);
        this.messages.put("pressureplate", "&4You are not allowed to use pressure plates!");

        this.actions.put("bucket", true);
        this.messages.put("bucket", "&4You are not allowed to use buckets");

        this.actions.put("placeblock", false);
        this.messages.put("placeblock", "&4You are not allowed to place blocks!");

        this.actions.put("breakblock", false);
        this.messages.put("breakblock", "&4You are not allowed to break blocks!");
        
        this.actions.put("pvp", false);
        this.messages.put("pvp", "&4You are not allowed to fight!");

        this.actions.put("pickup", false);
        this.messages.put("pickup", "&4You are not allowed to pickup items!");

        this.actions.put("vehicle", false);
        this.messages.put("vehicle", "&4You are not allowed to use vehicles!");

        this.actions.put("spam", false);
        this.messages.put("spam", "&4Don't spam the chat!");
        this.chatLockDuration = 2;

        this.actions.put("inventory", false);
        this.messages.put("inventory", "&4You are not allowed to use your inventory!");

        this.actions.put("bed", false);
        this.messages.put("bed", "&4You are not allowed to use beds!");

        this.actions.put("drop", false);
        this.messages.put("drop", "&4You are not allowed to drop items!");

        this.actions.put("fish", false);
        this.messages.put("fish", "&4You are not allowed to fish!");
    }

    public void onEnable()
    {
        this.server = this.getServer();
        this.pm = this.server.getPluginManager();
        this.config = this.getConfiguration();
        this.dataFolder = this.getDataFolder();

        this.dataFolder.mkdirs();
        // Create default config if it doesn't exist.
        if (!(new File(this.dataFolder, "config.yml")).exists())
        {
            this.defaultConfig();
        }
        this.loadConfig();

        Permissions permissions = (Permissions)this.pm.getPlugin("Permissions");
        if (permissions != null)
        {
            this.permissionHandler = permissions.getHandler();
        }

        AntiGuestPlayerListener playerListener = new AntiGuestPlayerListener(this);
        AntiGuestEntityListener entityListener = new AntiGuestEntityListener(this);
        AntiGuestBlockListener blockListener = new AntiGuestBlockListener(this);
        AntiGuestVehicleListener vehicleListener = new AntiGuestVehicleListener(this);
        
        if (
            this.actions.get("lever") ||
            this.actions.get("button") ||
            this.actions.get("door") ||
            this.actions.get("chest") ||
            this.actions.get("workbench") ||
            this.actions.get("furnace") ||
            this.actions.get("pressureplate") ||
            this.actions.get("placeblock")
        )
        {
            debug("interaction preventions registered");
            this.pm.registerEvent(Type.PLAYER_INTERACT, playerListener, Priority.Lowest, this);
        }
        if (this.actions.get("bucket"))
        {
            debug("bucket prevention registered");
            this.pm.registerEvent(Type.PLAYER_BUCKET_EMPTY, playerListener, Priority.Lowest, this);
            this.pm.registerEvent(Type.PLAYER_BUCKET_FILL, playerListener, Priority.Lowest, this);
        }
        if (this.actions.get("monster"))
        {
            debug("monster prevention registered");
            this.pm.registerEvent(Type.ENTITY_TARGET, entityListener, Priority.Lowest, this);
        }
        if (this.actions.get("placeblock"))
        {
            debug("place blocks prevention registered");
            this.pm.registerEvent(Type.BLOCK_PLACE, blockListener, Priority.Lowest, this);
            this.pm.registerEvent(Type.PAINTING_PLACE, entityListener, Priority.Lowest, this);
        }
        if (this.actions.get("breakblock"))
        {
            debug("break block prevention registered");
            this.pm.registerEvent(Type.VEHICLE_DAMAGE, vehicleListener, Priority.Lowest, this);
            this.pm.registerEvent(Type.VEHICLE_DESTROY, vehicleListener, Priority.Lowest, this);
            this.pm.registerEvent(Type.BLOCK_BREAK, blockListener, Priority.Lowest, this);
            this.pm.registerEvent(Type.PAINTING_BREAK, entityListener, Priority.Lowest, this);
        }
        if (this.actions.get("pvp"))
        {
            debug("pvp prevention registered");
            this.pm.registerEvent(Type.ENTITY_DAMAGE, entityListener, Priority.Lowest, this);
        }
        if (this.actions.get("pickup"))
        {
            debug("pickup prevention registered");
            this.pm.registerEvent(Type.PLAYER_PICKUP_ITEM, playerListener, Priority.Lowest, this);
        }
        if (this.actions.get("drop"))
        {
            debug("drop prevention registered");
            this.pm.registerEvent(Type.PLAYER_DROP_ITEM, playerListener, Priority.Lowest, this);
        }
        if (this.actions.get("fish"))
        {
            debug("fish prevention registered");
            this.pm.registerEvent(Type.PLAYER_FISH, playerListener, Priority.Lowest, this);
        }
        if (this.actions.get("bed"))
        {
            debug("bed prevention registered");
            this.pm.registerEvent(Type.PLAYER_BED_ENTER, playerListener, Priority.Lowest, this);
        }
        if (this.actions.get("vehicle"))
        {
            debug("vehicle prevention registered");
            this.pm.registerEvent(Type.VEHICLE_COLLISION_ENTITY, vehicleListener, Priority.Lowest, this);
            this.pm.registerEvent(Type.VEHICLE_ENTER, vehicleListener, Priority.Lowest, this);
            this.pm.registerEvent(Type.VEHICLE_EXIT, vehicleListener, Priority.Lowest, this);
        }
        if (this.actions.get("spam"))
        {
            debug("SPAM prevention registered");
            this.pm.registerEvent(Type.PLAYER_CHAT, playerListener, Priority.Lowest, this);
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

        this.actions.put("breakblock", this.config.getBoolean("preventions.breakblock.enable", this.actions.get("breakblock")));
        this.messages.put("breakblock", this.config.getString("preventions.breakblock.message", this.messages.get("breakblock")));

        this.actions.put("placeblock", this.config.getBoolean("preventions.placeblock.enable", this.actions.get("placeblock")));
        this.messages.put("placeblock", this.config.getString("preventions.placeblock.message", this.messages.get("placeblock")));

        this.actions.put("pvp", this.config.getBoolean("preventions.pvp.enable", this.actions.get("pvp")));
        this.messages.put("pvp", this.config.getString("preventions.pvp.message", this.messages.get("pvp")));

        this.actions.put("pickup", this.config.getBoolean("preventions.pickup.enable", this.actions.get("pickup")));
        this.messages.put("pickup", this.config.getString("preventions.pickup.message", this.messages.get("pickup")));

        this.actions.put("vehicle", this.config.getBoolean("preventions.vehicle.enable", this.actions.get("vehicle")));
        this.messages.put("vehicle", this.config.getString("preventions.vehicle.message", this.messages.get("vehicle")));

        this.actions.put("spam", this.config.getBoolean("preventions.spam.enable", this.actions.get("spam")));
        this.messages.put("spam", this.config.getString("preventions.spam.message", this.messages.get("spam")));
        this.chatLockDuration = this.config.getInt("preventions.spam.lockDuration", this.chatLockDuration);

        this.actions.put("monster", this.config.getBoolean("preventions.monster.enable", this.actions.get("monster")));
        this.messages.put("monster", this.config.getString("preventions.monster.message", this.messages.get("monster")));

        this.actions.put("lever", this.config.getBoolean("preventions.lever.enable", this.actions.get("lever")));
        this.messages.put("lever", this.config.getString("preventions.lever.message", this.messages.get("lever")));

        this.actions.put("button", this.config.getBoolean("preventions.button.enable", this.actions.get("button")));
        this.messages.put("button", this.config.getString("preventions.button.message", this.messages.get("button")));

        this.actions.put("pressureplate", this.config.getBoolean("preventions.pressureplate.enable", this.actions.get("pressureplate")));
        this.messages.put("pressureplate", this.config.getString("preventions.pressureplate.message", this.messages.get("pressureplate")));

        this.actions.put("chest", this.config.getBoolean("preventions.chest.enable", this.actions.get("chest")));
        this.messages.put("chest", this.config.getString("preventions.chest.message", this.messages.get("chest")));

        this.actions.put("furnace", this.config.getBoolean("preventions.furnace.enable", this.actions.get("furnace")));
        this.messages.put("furnace", this.config.getString("preventions.furnace.message", this.messages.get("furnace")));

        this.actions.put("bucket", this.config.getBoolean("preventions.bucket.enable", this.actions.get("bucket")));
        this.messages.put("bucket", this.config.getString("preventions.bucket.message", this.messages.get("bucket")));

        this.actions.put("inventory", this.config.getBoolean("preventions.inventory.enable", this.actions.get("inventory")));
        this.messages.put("inventory", this.config.getString("preventions.inventory.message", this.messages.get("inventory")));

        this.actions.put("workbench", this.config.getBoolean("preventions.workbench.enable", this.actions.get("workbench")));
        this.messages.put("workbench", this.config.getString("preventions.workbench.message", this.messages.get("workbench")));

        this.actions.put("door", this.config.getBoolean("preventions.door.enable", this.actions.get("door")));
        this.messages.put("door", this.config.getString("preventions.door.message", this.messages.get("door")));

        this.actions.put("bed", this.config.getBoolean("preventions.bed.enable", this.actions.get("bed")));
        this.messages.put("bed", this.config.getString("preventions.bed.message", this.messages.get("bed")));

        this.actions.put("drop", this.config.getBoolean("preventions.drop.enable", this.actions.get("drop")));
        this.messages.put("drop", this.config.getString("preventions.drop.message", this.messages.get("drop")));

        this.actions.put("fish", this.config.getBoolean("preventions.fish.enable", this.actions.get("fish")));
        this.messages.put("fish", this.config.getString("preventions.fish.message", this.messages.get("fish")));

        for (Map.Entry<String, String> entry : this.messages.entrySet())
        {
            String value = entry.getValue();
            if (value != null)
            {
                this.messages.put(entry.getKey(), entry.getValue().replaceAll("&([a-f0-9])", "\u00A7$1"));
            }
        }
    }

    private void defaultConfig()
    {
        this.config.setProperty("preventions.spam.enable", this.actions.get("spam"));
        this.config.setProperty("preventions.spam.message", this.messages.get("spam"));
        this.config.setProperty("preventions.spam.lockDuration", this.chatLockDuration);
        
        this.config.setProperty("preventions.vehicle.enable", this.actions.get("vehicle"));
        this.config.setProperty("preventions.vehicle.message", this.messages.get("vehicle"));

        this.config.setProperty("preventions.pickup.enable", this.actions.get("pickup"));
        this.config.setProperty("preventions.pickup.message", this.messages.get("pickup"));

        this.config.setProperty("preventions.pvp.enable", this.actions.get("pvp"));
        this.config.setProperty("preventions.pvp.message", this.messages.get("pvp"));

        this.config.setProperty("preventions.breakblock.enable", this.actions.get("breakblock"));
        this.config.setProperty("preventions.breakblock.message", this.messages.get("breakblock"));

        this.config.setProperty("preventions.placeblock.enable", this.actions.get("placeblock"));
        this.config.setProperty("preventions.placeblock.message", this.messages.get("placeblock"));

        this.config.setProperty("preventions.monster.enable", this.actions.get("monster"));
        this.config.setProperty("preventions.monster.message", this.messages.get("monster"));

        this.config.setProperty("preventions.lever.enable", this.actions.get("lever"));
        this.config.setProperty("preventions.lever.message", this.messages.get("lever"));

        this.config.setProperty("preventions.button.enable", this.actions.get("button"));
        this.config.setProperty("preventions.button.message", this.messages.get("button"));

        this.config.setProperty("preventions.pressureplate.enable", this.actions.get("pressureplate"));
        this.config.setProperty("preventions.pressureplate.message", this.messages.get("pressureplate"));

        this.config.setProperty("preventions.chest.enable", this.actions.get("chest"));
        this.config.setProperty("preventions.chest.message", this.messages.get("chest"));

        this.config.setProperty("preventions.furnace.enable", this.actions.get("furnace"));
        this.config.setProperty("preventions.furnace.message", this.messages.get("furnace"));

        this.config.setProperty("preventions.bucket.enable", this.actions.get("bucket"));
        this.config.setProperty("preventions.bucket.message", this.messages.get("bucket"));

        this.config.setProperty("preventions.inventory.enable", this.actions.get("inventory"));
        this.config.setProperty("preventions.inventory.message", this.messages.get("inventory"));

        this.config.setProperty("preventions.workbench.enable", this.actions.get("workbench"));
        this.config.setProperty("preventions.workbench.message", this.messages.get("workbench"));

        this.config.setProperty("preventions.door.enable", this.actions.get("door"));
        this.config.setProperty("preventions.door.message", this.messages.get("door"));

        this.config.setProperty("preventions.bed.enable", this.actions.get("bed"));
        this.config.setProperty("preventions.bed.message", this.messages.get("bed"));

        this.config.setProperty("preventions.drop.enable", this.actions.get("drop"));
        this.config.setProperty("preventions.drop.message", this.messages.get("drop"));

        this.config.setProperty("preventions.fish.enable", this.actions.get("fish"));
        this.config.setProperty("preventions.fish.message", this.messages.get("fish"));

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
        boolean result = false;
        if (player.isOp())
        {
            result = true;
        }
        else
        {
            final String permission = "AntiGuest." + type;
            if (this.permissionHandler != null)
            {
                result = this.permissionHandler.permission(player, permission);
            }
            else
            {
                result = player.hasPermission(permission);
            }
        }
        debug(player.getName() + " - " + type + " - " + String.valueOf(result));
        return result;
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
