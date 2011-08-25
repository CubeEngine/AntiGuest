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
    public static boolean debugMode = true;
    
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

        this.actions.put("build", false);
        this.messages.put("build", "&4You are not allowed to build!");
        
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
            this.actions.get("pressureplate")
        )
        {
            this.pm.registerEvent(Type.PLAYER_INTERACT, playerListener, Priority.Lowest, this);
        }
        if (this.actions.get("bucket"))
        {
            this.pm.registerEvent(Type.PLAYER_BUCKET_EMPTY, playerListener, Priority.Lowest, this);
            this.pm.registerEvent(Type.PLAYER_BUCKET_FILL, playerListener, Priority.Lowest, this);
        }
        if (this.actions.get("monster"))
        {
            this.pm.registerEvent(Type.ENTITY_TARGET, entityListener, Priority.Lowest, this);
        }
        if (this.actions.get("placeblocks"))
        {
            this.pm.registerEvent(Type.BLOCK_PLACE, blockListener, Priority.Lowest, this);
            this.pm.registerEvent(Type.PAINTING_PLACE, entityListener, Priority.Lowest, this);
        }
        if (this.actions.get("breakblocks"))
        {
            this.pm.registerEvent(Type.VEHICLE_DAMAGE, vehicleListener, Priority.Lowest, this);
            this.pm.registerEvent(Type.VEHICLE_DESTROY, vehicleListener, Priority.Lowest, this);
            this.pm.registerEvent(Type.BLOCK_DAMAGE, blockListener, Priority.Lowest, this);
            this.pm.registerEvent(Type.PAINTING_BREAK, entityListener, Priority.Lowest, this);
        }
        if (this.actions.get("pvp"))
        {
            this.pm.registerEvent(Type.ENTITY_DAMAGE, entityListener, Priority.Lowest, this);
        }
        if (this.actions.get("pickup"))
        {
            this.pm.registerEvent(Type.PLAYER_PICKUP_ITEM, playerListener, Priority.Lowest, this);
        }
        if (this.actions.get("vehicle"))
        {
            this.pm.registerEvent(Type.VEHICLE_COLLISION_ENTITY, vehicleListener, Priority.Lowest, this);
            this.pm.registerEvent(Type.VEHICLE_ENTER, vehicleListener, Priority.Lowest, this);
            this.pm.registerEvent(Type.VEHICLE_EXIT, vehicleListener, Priority.Lowest, this);
        }
        if (this.actions.get("spam"))
        {
            this.pm.registerEvent(Type.PLAYER_CHAT, playerListener, Priority.Lowest, this);
        }
        if (this.actions.get("build"))
        {
            entityListener.paintingBreakPermission = "build";
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

        this.actions.put("build", this.config.getBoolean("actions.build.enable", this.actions.get("build")));
        this.messages.put("build", this.config.getString("actions.build.message", this.messages.get("build")));

        this.actions.put("pvp", this.config.getBoolean("actions.pvp.enable", this.actions.get("pvp")));
        this.messages.put("pvp", this.config.getString("actions.pvp.message", this.messages.get("pvp")));

        this.actions.put("pickup", this.config.getBoolean("actions.pickup.enable", this.actions.get("pickup")));
        this.messages.put("pickup", this.config.getString("actions.pickup.message", this.messages.get("pickup")));

        this.actions.put("vehicle", this.config.getBoolean("actions.vehicle.enable", this.actions.get("vehicle")));
        this.messages.put("vehicle", this.config.getString("actions.vehicle.message", this.messages.get("vehicle")));

        this.actions.put("spam", this.config.getBoolean("actions.spam.enable", this.actions.get("spam")));
        this.messages.put("spam", this.config.getString("actions.spam.message", this.messages.get("spam")));
        this.chatLockDuration = this.config.getInt("actions.spam.lockDuration", this.chatLockDuration);

        this.actions.put("monster", this.config.getBoolean("actions.monster.enable", this.actions.get("monster")));
        this.messages.put("monster", this.config.getString("actions.monster.message", this.messages.get("monster")));

        this.actions.put("lever", this.config.getBoolean("actions.lever.enable", this.actions.get("lever")));
        this.messages.put("lever", this.config.getString("actions.lever.message", this.messages.get("lever")));

        this.actions.put("button", this.config.getBoolean("actions.button.enable", this.actions.get("button")));
        this.messages.put("button", this.config.getString("actions.button.message", this.messages.get("button")));

        this.actions.put("pressureplate", this.config.getBoolean("actions.pressureplate.enable", this.actions.get("pressureplate")));
        this.messages.put("pressureplate", this.config.getString("actions.pressureplate.message", this.messages.get("pressureplate")));

        this.actions.put("chest", this.config.getBoolean("actions.chest.enable", this.actions.get("chest")));
        this.messages.put("chest", this.config.getString("actions.chest.message", this.messages.get("chest")));

        this.actions.put("furnace", this.config.getBoolean("actions.furnace.enable", this.actions.get("furnace")));
        this.messages.put("furnace", this.config.getString("actions.furnace.message", this.messages.get("furnace")));

        this.actions.put("bucket", this.config.getBoolean("actions.bucket.enable", this.actions.get("bucket")));
        this.messages.put("bucket", this.config.getString("actions.bucket.message", this.messages.get("bucket")));

        this.actions.put("inventory", this.config.getBoolean("actions.inventory.enable", this.actions.get("inventory")));
        this.messages.put("inventory", this.config.getString("actions.inventory.message", this.messages.get("inventory")));

        this.actions.put("workbench", this.config.getBoolean("actions.workbench.enable", this.actions.get("workbench")));
        this.messages.put("workbench", this.config.getString("actions.workbench.message", this.messages.get("workbench")));

        this.actions.put("door", this.config.getBoolean("actions.door.enable", this.actions.get("door")));
        this.messages.put("door", this.config.getString("actions.door.message", this.messages.get("door")));

        for (Map.Entry<String, String> entry : this.messages.entrySet())
        {
            this.messages.put(entry.getKey(), entry.getValue().replaceAll("&([a-f0-9])", "\u00A7$1"));
        }
    }

    private void defaultConfig()
    {
        this.config.setProperty("actions.spam.enable", this.actions.get("spam"));
        this.config.setProperty("actions.spam.message", this.messages.get("spam"));
        this.config.setProperty("actions.spam.lockDuration", this.chatLockDuration);
        
        this.config.setProperty("actions.vehicle.enable", this.actions.get("vehicle"));
        this.config.setProperty("actions.vehicle.message", this.messages.get("vehicle"));

        this.config.setProperty("actions.pickup.enable", this.actions.get("pickup"));
        this.config.setProperty("actions.pickup.message", this.messages.get("pickup"));

        this.config.setProperty("actions.pvp.enable", this.actions.get("pvp"));
        this.config.setProperty("actions.pvp.message", this.messages.get("pvp"));

        this.config.setProperty("actions.build.enable", this.actions.get("build"));
        this.config.setProperty("actions.build.message", this.messages.get("build"));

        this.config.setProperty("actions.monster.enable", this.actions.get("monster"));
        this.config.setProperty("actions.monster.message", this.messages.get("monster"));

        this.config.setProperty("actions.lever.enable", this.actions.get("lever"));
        this.config.setProperty("actions.lever.message", this.messages.get("lever"));

        this.config.setProperty("actions.button.enable", this.actions.get("button"));
        this.config.setProperty("actions.button.message", this.messages.get("button"));

        this.config.setProperty("actions.pressureplate.enable", this.actions.get("pressureplate"));
        this.config.setProperty("actions.pressureplate.message", this.messages.get("pressureplate"));

        this.config.setProperty("actions.chest.enable", this.actions.get("chest"));
        this.config.setProperty("actions.chest.message", this.messages.get("chest"));

        this.config.setProperty("actions.furnace.enable", this.actions.get("furnace"));
        this.config.setProperty("actions.furnace.message", this.messages.get("furnace"));

        this.config.setProperty("actions.bucket.enable", this.actions.get("bucket"));
        this.config.setProperty("actions.bucket.message", this.messages.get("bucket"));

        this.config.setProperty("actions.inventory.enable", this.actions.get("inventory"));
        this.config.setProperty("actions.inventory.message", this.messages.get("inventory"));

        this.config.setProperty("actions.workbench.enable", this.actions.get("workbench"));
        this.config.setProperty("actions.workbench.message", this.messages.get("workbench"));

        this.config.setProperty("actions.door.enable", this.actions.get("door"));
        this.config.setProperty("actions.door.message", this.messages.get("door"));

        
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
        if (player.isOp())
        {
            return true;
        }
        String permission = "AntiGuest." + type;
        if (this.permissionHandler != null)
        {
            return this.permissionHandler.permission(player, permission);
        }
        else
        {
            return player.hasPermission(permission);
        }
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
