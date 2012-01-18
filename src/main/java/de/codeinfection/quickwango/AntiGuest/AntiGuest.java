package de.codeinfection.quickwango.AntiGuest;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiGuest extends JavaPlugin
{
    public static final HashMap<String, Prevention> preventions = new HashMap<String, Prevention>();

    protected Logger logger = null;
    public static boolean debugMode = false;
    public final static int messageWaitTime = 3000;
    
    protected Server server;
    protected PluginManager pm;
    protected Configuration config;
    protected File dataFolder;
    public int chatLockDuration;
    public boolean vehiclesIgnoreBuildPermissions;

    public void onEnable()
    {
        logger = this.getLogger();
        this.server = this.getServer();
        this.pm = this.server.getPluginManager();
        this.dataFolder = this.getDataFolder();
        this.dataFolder.mkdirs();
        this.config = this.getConfig();
        this.config.options().copyDefaults(true);

        this.loadPreventions();

        this.saveConfig();

        this.pm.registerEvents(new AntiGuestInteractionListener(this), this);
        this.pm.registerEvents(new AntiGuestBlockListener(this), this);
        this.pm.registerEvents(new AntiGuestPlayerListener(this), this);
        this.pm.registerEvents(new AntiGuestVehicleListener(this), this);

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
            for (String key : preventionsSection.getKeys(false))
            {
                ConfigurationSection preventionSection = preventionsSection.getConfigurationSection(key);

                if (preventionSection.getBoolean("enable"))
                {
                    prevention = new Prevention(key, preventionSection.getString("message").replaceAll("&([a-f0-9])", "\u00A7$1"));
                    this.preventions.put(prevention.name, prevention);
                }
            }
        }

        //special values
        this.vehiclesIgnoreBuildPermissions = this.config.getBoolean("preventions.vehicle.ignoreBuildPermissions");
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
        if (debugMode)
        {
            this.log("[debug] " + msg);
        }
    }
}
