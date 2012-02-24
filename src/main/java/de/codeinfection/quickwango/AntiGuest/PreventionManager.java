package de.codeinfection.quickwango.AntiGuest;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;

/**
 *
 * @author Phillip
 */
public class PreventionManager
{
    private static PreventionManager instance = null;
    
    private Map<String, Prevention> preventions;
    
    private PreventionManager()
    {
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
    
    public PreventionManager registerPrevention(Prevention prevention)
    {
        if (prevention == null)
        {
            throw new IllegalArgumentException("prevention must not be null!");
        }
        this.preventions.put(prevention.getName(), prevention);
        
        return this;
    }
    
    public PreventionManager unregisterPrevention(String name)
    {
        this.preventions.remove(name);
        return this;
    }
    
    public boolean initializePrevention(final String name, final Server server, final ConfigurationSection config)
    {
        final Prevention prevention = this.preventions.get(name);
        if (prevention != null)
        {
            prevention.initialize(server, config);
            server.getPluginManager().registerEvents(prevention, prevention.getPlugin());
            return true;
        }
        return false;
    }
}
