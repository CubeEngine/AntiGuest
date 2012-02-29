package de.codeinfection.quickwango.AntiGuest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.HandlerList;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

/**
 * This class manages the Prevention's.
 *
 * @author Phillip Schichtel
 */
public class PreventionManager
{
    private static PreventionManager instance = null;

    private AntiGuest plugin;
    private Map<String, Prevention> preventions;

    private Server server;
    private PluginManager pm;
    private Map<String, ConfigurationSection> configurations;
    private Map<String, ConfigurationSection> defaultConfigurations;
    private Permission parentPermission;
    
    private PreventionManager()
    {
        this.plugin = null;
        this.server = null;
        this.pm = null;
        this.configurations = new HashMap<String, ConfigurationSection>();
        this.defaultConfigurations = new HashMap<String, ConfigurationSection>();
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
     * @param plugin an instance of AntiGuest
     * @return fluent interface
     */
    public PreventionManager initialize(AntiGuest plugin)
    {
        if (this.plugin == null)
        {
            this.plugin = plugin;
            this.server = plugin.getServer();
            this.pm = this.server.getPluginManager();

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
        this.preventions.put(prevention.getName(), prevention);
        Permission perm = prevention.getPermission();
        try
        {
            this.pm.addPermission(perm);
        }
        catch (IllegalArgumentException e)
        {}
        perm.addParent(this.parentPermission, true);
        this.defaultConfigurations.put(prevention.getName(), prevention.getDefaultConfig());
        
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
        this.defaultConfigurations.remove(name);
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
        if (prevention != null)
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

                if (config != null && config.getBoolean("enable"))
                {

                    prevention.enable(this.server, config);
                    this.pm.registerEvents(prevention, prevention.getPlugin());

                    prevention.setEnabled(true);
                    return true;
                }
            }
            catch (Throwable t)
            {
                AntiGuest.error("Failed to enable the prevention '" + name + "'...", t);
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

        ConfigurationSection currentDefault;
        for (String preventionName : this.preventions.keySet())
        {
            currentDefault = this.defaultConfigurations.get(preventionName);
            if (currentDefault != null)
            {
                preventionsSection.addDefault(preventionName, currentDefault);
            }
            this.enablePrevention(preventionName, preventionsSection.getConfigurationSection(preventionName));
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
            HandlerList.unregisterAll(prevention);
            try
            {
                prevention.disable();
            }
            catch (Throwable t)
            {
                AntiGuest.error("Failed to disable the prevention '" + prevention.getName() + "'...", t);
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
}
