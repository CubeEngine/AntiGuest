package de.codeinfection.quickwango.AntiGuest;

import java.util.HashSet;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

/**
 * This class represents a filterable Prevention related to Materials
 *
 * @author Phillip Schichtel
 */
public abstract class FilteredItemPrevention extends FilteredPrevention
{
    public FilteredItemPrevention(String name, Plugin plugin)
    {
        super(name, plugin);
    }

    public FilteredItemPrevention(String name, String permission, Plugin plugin)
    {
        super(name, permission, plugin);
    }

    /**
     * This method changes the default value if the "list" entry
     *
     * @return the default config
     */
    @Override
    public ConfigurationSection getDefaultConfig()
    {
        ConfigurationSection config = super.getDefaultConfig();

        config.set("list", new String[] {Material.DIRT.toString().toLowerCase()});

        return config;
    }

    /**
     * This method parses the string list to Material instances
     * 
     * @param server a Server instance
     * @param config the config of this prevention
     */
    @Override
    public void enable(final Server server, final ConfigurationSection config)
    {
        super.enable(server, config);

        // normalize the items
        HashSet<Object> newItems = new HashSet<Object>(this.filterItems.size());
        for (Object item : this.filterItems)
        {
            Material material = Material.matchMaterial(String.valueOf(item));
            if (item != null)
            {
                newItems.add(material);
            }
        }
        this.filterItems = newItems;
    }
}
