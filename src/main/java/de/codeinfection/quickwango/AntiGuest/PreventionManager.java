package de.codeinfection.quickwango.AntiGuest;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.HandlerList;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/**
 * This class manages the Prevention's.
 *
 * @author Phillip Schichtel
 */
public class PreventionManager
{
    private static PreventionManager instance = null;

    private PreventionPlugin plugin;
    private Map<String, Prevention> preventions;

    private Server server;
    private PluginManager pm;
    private Map<String, ConfigurationSection> configurations;
    private Permission parentPermission;
    private File configFolder;
    
    private PreventionManager()
    {
        this.plugin = null;
        this.server = null;
        this.pm = null;
        this.configFolder = null;
        this.configurations = new HashMap<String, ConfigurationSection>();
        this.parentPermission = new Permission("antiguest.preventions.*", PermissionDefault.OP);
        this.preventions = new HashMap<String, Prevention>();
    }

    /**
     * Returns the singleton instance of the PreventionManager
     *
     * @return the instance
     */
    public static PreventionManager getInstance()
    {
        if (instance == null)
        {
            instance = new PreventionManager();
        }
        return instance;
    }

    /**
     * This method initializes the PreventionManager.
     * This has to be done before any other method call!
     *
     * @param plugin an instance of AntiGuestBukkit
     * @return fluent interface
     */
    public PreventionManager initialize(PreventionPlugin plugin)
    {
        if (this.plugin == null)
        {
            this.plugin = plugin;
            this.server = plugin.getServer();
            this.pm = this.server.getPluginManager();
            this.configFolder = plugin.getConfigurationFolder();

            this.pm.addPermission(this.parentPermission);
        }
        return this;
    }

    /**
     * Returns the named prevention or null if not available
     *
     * @param name the name of the prevention
     * @return the prevention or null
     */
    public Prevention getPrevention(String name)
    {
        return this.preventions.get(name);
    }

    /**
     * Returns all registered preventions
     *
     * @return a collection of all preventions
     */
    public Collection<Prevention> getPreventions()
    {
        return new ArrayList<Prevention>(this.preventions.values());
    }

    /**
     * Registeres a prevention
     *
     * @param prevention the prevention to register
     * @return fluent interface
     */
    public PreventionManager registerPrevention(Prevention prevention)
    {
        if (this.plugin == null)
        {
            throw new IllegalStateException("the preventionmanager has not been initialized!");
        }
        if (prevention == null)
        {
            throw new IllegalArgumentException("prevention must not be null!");
        }
        if (!this.preventions.containsValue(prevention))
        {
            this.preventions.put(prevention.getName(), prevention);
            Permission perm = prevention.getPermission();
            try
            {
                this.pm.addPermission(perm);
            }
            catch (IllegalArgumentException e)
            {}
            perm.addParent(this.parentPermission, true);
            //this.configurations.put(prevention.getName(), prevention.getDefaultConfig());
        }
        
        return this;
    }

    /**
     * Unregisteres a prevention if registered
     *
     * @param name the name of the prevention
     * @return fluent interface
     */
    public PreventionManager unregisterPrevention(String name)
    {
        this.preventions.remove(name);
        this.configurations.remove(name);
        return this;
    }

    /**
     * Enables the named prevention if registered with it's default configuration
     *
     * @param name the preventions name
     * @param server an Server instance
     * @return true if the intialization was successful
     */
    public boolean enablePrevention(final String name)
    {
        return this.enablePrevention(name, null);
    }

    /**
     * Enables the named prevention if registered
     * 
     * @param name the preventions name
     * @param server an Server instance
     * @param config the prevention's configuration
     * @return true if the intialization was successful
     */
    public boolean enablePrevention(final String name, ConfigurationSection config)
    {
        final Prevention prevention = this.getPrevention(name);
        if (prevention != null && !prevention.isEnabled())
        {
            try
            {
                if (config == null)
                {
                    config = this.configurations.get(name);
                }
                else
                {
                    this.configurations.put(name, config);
                }

                if (config != null)
                {

                    prevention.enable(config);
                    this.pm.registerEvents(prevention, prevention.getPlugin());

                    prevention.setEnabled(true);
                    return true;
                }
            }
            catch (Throwable t)
            {
                AntiGuestBukkit.error("Failed to enable the prevention '" + name + "'...", t);
            }
        }
        return false;
    }

    /**
     * This method loads all registered preventions based on the given ConfigurationSection and the default configuraiton.
     * The given ConfigurationSection should have a key "preventions" on top level, otherwise this will fail
     * 
     * @param config the configuration
     * @return fluent interface
     */
    public PreventionManager enablePreventions(ConfigurationSection preventionsSection)
    {
        if (preventionsSection == null)
        {
            throw new IllegalArgumentException("config must not be null!");
        }

        ConfigurationSection currentDefault, preventionConfig;
        for (String preventionName : this.preventions.keySet())
        {
            currentDefault = this.configurations.get(preventionName);
            if (currentDefault != null)
            {
                preventionsSection.addDefault(preventionName, currentDefault);
            }
            preventionConfig = preventionsSection.getConfigurationSection(preventionName);
            if (preventionConfig.getBoolean("enable"))
            {
                this.enablePrevention(preventionName, preventionConfig);
            }
        }

        return this;
    }

    /**
     * Disables the named prevention
     *
     * @param name name of the prevention
     * @return fluent interface
     */
    public PreventionManager disablePrevention(String name)
    {
        Prevention prevention =  this.getPrevention(name);
        if (prevention != null && prevention.isEnabled())
        {
            prevention.setEnabled(false);
            try
            {
                HandlerList.unregisterAll(prevention);
            }
            catch (Throwable t)
            {
                AntiGuestBukkit.error("Failed to unregister the prevention event handlers.");
                AntiGuestBukkit.error("It seems like you're using a CraftBukkit build that doesn't support it.");
            }
            try
            {
                prevention.disable();
            }
            catch (Throwable t)
            {
                AntiGuestBukkit.error("Failed to disable the prevention '" + prevention.getName() + "'...", t);
            }
        }
        return this;
    }

    /**
     * Disables all preventions
     *
     * @return fluent interface
     */
    public PreventionManager disablePreventions()
    {
        for (String name : this.preventions.keySet())
        {
            this.disablePrevention(name);
        }
        return this;
    }

    /**
     * Disables all preventions of a plugin
     *
     * @param plugin the plugin that registered the preventions
     * @return fluent interface
     */
    public PreventionManager disablePreventions(Plugin plugin)
    {
        for (Prevention prevention : this.preventions.values())
        {
            if (prevention.getPlugin() == plugin)
            {
                this.disablePrevention(prevention.getName());
            }
        }

        return this;
    }
}
