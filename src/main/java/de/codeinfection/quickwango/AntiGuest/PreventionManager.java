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

    private AntiGuest plugin;
    private Map<String, Prevention> preventions;

    private Server server;
    private PluginManager pm;
    private Map<String, ConfigurationSection> defaultValues;
    private Permission parentPermission;
    
    private PreventionManager()
    {
        this.plugin = null;
        this.server = null;
        this.pm = null;
        this.defaultValues = new HashMap<String, ConfigurationSection>();
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
        if (prevention == null)
        {
            throw new IllegalArgumentException("prevention must not be null!");
        }
        if (this.plugin == null)
        {
            throw new IllegalStateException("the preventionmanager has not been initialized!");
        }
        this.preventions.put(prevention.getName(), prevention);
        prevention.getPermission().addParent(this.parentPermission, true);
        this.defaultValues.put(prevention.getName(), prevention.getDefaultConfig());
        
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
        this.defaultValues.remove(name);
        return this;
    }

    /**
     * Initializes the named prevention if registered.
     * The given plugin's configuration must contain the path
     *
     * @param name the preventions name
     * @param plugin a plugin
     * @return true if the intialization was successful
     */
    public boolean enablePrevention(final String name, final Plugin plugin)
    {
        return this.enablePrevention(name, plugin.getServer(), plugin.getConfig().getConfigurationSection("prevents." + name));
    }

    /**
     * Initializes the named prevention if registered
     * 
     * @param name the preventions name
     * @param server an Server instance
     * @param config the prevention's configuration
     * @return true if the intialization was successful
     */
    public boolean enablePrevention(final String name, final Server server, ConfigurationSection config)
    {
        final Prevention prevention = this.preventions.get(name);
        if (prevention != null)
        {
            try
            {
                if (config != null)
                {
                    ConfigurationSection defaultSection = this.defaultValues.get(name);
                    if (defaultSection != null)
                    {
                        for (String key : defaultSection.getKeys(false))
                        {
                            config.addDefault(key, defaultSection.get(key));
                        }
                    }
                }
                else
                {
                    config = this.defaultValues.get(name);
                }
                prevention.enable(server, config);
                this.pm.registerEvents(prevention, prevention.getPlugin());

                try
                {
                    this.pm.addPermission(prevention.getPermission());
                }
                catch (IllegalArgumentException e)
                {}

                prevention.setEnabled(true);

                return true;
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
    public PreventionManager loadPreventions(ConfigurationSection config)
    {
        if (config == null)
        {
            throw new IllegalArgumentException("config must not be null!");
        }
        

        Object value;
        ConfigurationSection currentSection;
        for (Map.Entry<String, Object> entry : config.getValues(false).entrySet())
        {
            value = entry.getValue();
            if (value instanceof ConfigurationSection)
            {
                currentSection = (ConfigurationSection)value;
                if (currentSection.getBoolean("enable", false))
                {
                    this.enablePrevention(entry.getKey(), this.server, currentSection);
                }
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
