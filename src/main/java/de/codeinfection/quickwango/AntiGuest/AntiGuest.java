package de.codeinfection.quickwango.AntiGuest;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiGuest extends JavaPlugin
{
    private static AntiGuest instance = null;
    public static final HashMap<String, Prevention> preventions = new HashMap<String, Prevention>();

    private Logger logger = null;
    public boolean debugMode = false;
//    public final static int messageWaitTime = 3000;
    
    private PluginManager pm;
    protected Configuration config;
    protected File dataFolder;
    public int chatLockDuration;
    public boolean vehiclesIgnoreBuildPermissions;

    public AntiGuest()
    {
        instance = this;
    }

    public static AntiGuest getInstance()
    {
        return instance;
    }

    public void onEnable()
    {
        this.logger = this.getLogger();
        this.pm = this.getServer().getPluginManager();
        this.dataFolder = this.getDataFolder();
        this.dataFolder.mkdirs();
        this.config = this.getConfig();
        this.config.options().copyDefaults(true);

        this.loadPreventions();

        this.saveConfig();

        this.pm.registerEvents(new AntiGuestInteractionListener(), this);
        this.pm.registerEvents(new AntiGuestBlockListener(), this);
        this.pm.registerEvents(new AntiGuestPlayerListener(), this);
        this.pm.registerEvents(new AntiGuestVehicleListener(), this);

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
            Prevention prevention;
            String message;
            Integer messageDelay;
            for (String key : preventionsSection.getKeys(false))
            {
                ConfigurationSection preventionSection = preventionsSection.getConfigurationSection(key);

                if (preventionSection.getBoolean("enable"))
                {
                    messageDelay = preventionSection.getInt("messageDelay", -1);
                    message = preventionSection.getString("message", "").replaceAll("&([a-f0-9])", "\u00A7$1");
                    prevention = new Prevention(key, key, message, messageDelay);
                    this.pm.addPermission(new Permission(prevention.getPermission(), PermissionDefault.OP));
                    this.preventions.put(prevention.getName(), prevention);
                }
            }
        }

        //special values
        this.chatLockDuration               = this.config.getInt("preventions.spam.lockDuration");
    }

    public void log(String msg)
    {
        logger.log(Level.INFO, msg);
    }

    public void error(String msg)
    {
        logger.log(Level.SEVERE, msg);
    }

    public void error(String msg, Throwable t)
    {
        logger.log(Level.SEVERE, msg, t);
    }

    public void debug(String msg)
    {
        if (this.debugMode)
        {
            this.log("[debug] " + msg);
        }
    }
}
