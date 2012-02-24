package de.codeinfection.quickwango.AntiGuest;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
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
    private Permission basePermission;
    
    private PreventionManager()
    {
        this.plugin = null;
        this.server = null;
        this.pm = null;
        this.defaultConfigSection = null;
        this.basePermission = new Permission("antiguest.preventions.*", PermissionDefault.OP);
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
            this.defaultConfigSection = plugin.getConfig().getConfigurationSection("preventions").getDefaultSection();

            this.pm.addPermission(this.basePermission);
        }
        return this;
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
        this.defaultConfigSection.addDefault(prevention.getName(), prevention.getDefaultConfig());
        
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

            
            this.pm.addPermission(prevention.getPermission());
            this.basePermission.getChildren().put(prevention.getPermission().getName(), true);

            return true;
        }
        return false;
    }
}
