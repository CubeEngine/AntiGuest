package de.codeinfection.quickwango.AntiGuest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Server;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

/**
 *
 * @author Phillip
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
    
    public static PreventionManager getInstance()
    {
        if (instance == null)
        {
            instance = new PreventionManager();
        }
        return instance;
    }

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

    public Prevention getPrevention(String name)
    {
        return this.preventions.get(name);
    }

    public Collection<Prevention> getPreventions()
    {
        return new ArrayList<Prevention>(this.preventions.values());
    }
    
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
    
    public PreventionManager unregisterPrevention(String name)
    {
        this.preventions.remove(name);
        this.defaultConfigSection.set(name, null);
        return this;
    }
    
    public boolean initializePrevention(final String name, final Server server, final ConfigurationSection config)
    {
        final Prevention prevention = this.preventions.get(name);
        if (prevention != null)
        {
            prevention.initialize(server, config);
            server.getPluginManager().registerEvents(prevention, prevention.getPlugin());

            try
            {
                this.pm.addPermission(prevention.getPermission());
            }
            catch (IllegalArgumentException e)
            {}

            return true;
        }
        return false;
    }

    public PreventionManager loadPreventions(Configuration config)
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
}
