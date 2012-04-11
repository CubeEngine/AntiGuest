package de.cubeisland.AntiGuest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
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

    private PluginManager pm;
    private Permission parentPermission;
    
    private PreventionManager()
    {
        this.plugin = null;
        this.pm = null;
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
            this.pm = plugin.getServer().getPluginManager();

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
        return this;
    }

    public boolean enablePrevention(final String name)
    {
        final Prevention prevention = this.preventions.get(name);
        if (prevention != null)
        {
            return this.enablePrevention(prevention);
        }
        return false;
    }

    /**
     * Enables the named prevention if registered
     * 
     * @param prevention the preventions name
     * @param server an Server instance
     * @param config the prevention's configuration
     * @return true if the intialization was successful
     */
    public boolean enablePrevention(final Prevention prevention)
    {
        if (prevention != null && !prevention.isEnabled())
        {
            try
            {
                prevention.enable();
                this.pm.registerEvents(prevention, prevention.getPlugin());

                prevention.setEnabled(true);
                return true;
            }
            catch (Throwable t)
            {
                AntiGuestBukkit.error("Failed to enable the prevention '" + prevention.getName() + "'...", t);
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
    public PreventionManager enablePreventions()
    {
        for (Prevention prevention : this.preventions.values())
        {
            if (prevention.getConfig().getBoolean("enable"))
            {
                this.enablePrevention(prevention);
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
        final Prevention prevention = this.preventions.get(name);
        if (prevention != null)
        {
            this.enablePrevention(prevention);
        }
        return this;
    }

    /**
     * Disables the named prevention
     *
     * @param prevention name of the prevention
     * @return fluent interface
     */
    public PreventionManager disablePrevention(Prevention prevention)
    {
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
        for (Prevention prevention : this.preventions.values())
        {
            this.disablePrevention(prevention);
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
                this.disablePrevention(prevention);
            }
        }

        return this;
    }
}
