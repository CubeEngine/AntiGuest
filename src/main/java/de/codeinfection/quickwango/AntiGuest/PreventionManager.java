package de.codeinfection.quickwango.AntiGuest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
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
    private ConfigurationSection defaultConfigSection;
    private Permission parentPermission;
    
    private PreventionManager()
    {
        this.plugin = null;
        this.server = null;
        this.pm = null;
        this.defaultConfigSection = new MemoryConfiguration();
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
        this.defaultConfigSection.set(prevention.getName(), prevention.getDefaultConfig());
        
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
        this.defaultConfigSection.set(name, null);
        return this;
    }

    /**
     * Initializes hte named prevention if registered
     * 
     * @param name the preventions name
     * @param server an Server instance
     * @param config the prevention's configuration
     * @return true if the intialization was successful
     */
    public boolean initializePrevention(final String name, final Server server, final ConfigurationSection config)
    {
        final Prevention prevention = this.preventions.get(name);
        if (prevention != null)
        {
            try
            {
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
        config.getDefaultSection().set("preventions", this.defaultConfigSection);
        ConfigurationSection preventionsSection = config.getConfigurationSection("preventions");
        if (preventionsSection != null)
        {
            ConfigurationSection currentSection;
            for (String prevention : preventionsSection.getKeys(false))
            {
                currentSection = preventionsSection.getConfigurationSection(prevention);
                if (currentSection.getBoolean("enable", false))
                {
                    this.initializePrevention(prevention, this.server, currentSection);
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
