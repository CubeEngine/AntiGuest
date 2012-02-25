package de.codeinfection.quickwango.AntiGuest;

import java.util.HashSet;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author CodeInfection
 */
public abstract class FilteredItemPrevention extends FilteredPrevention
{
    public FilteredItemPrevention(String name, Plugin plugin)
    {
        super(name, plugin);
    }

    @Override
    public ConfigurationSection getDefaultConfig()
    {
        ConfigurationSection config = super.getDefaultConfig();

        config.set("list", new String[] {Material.DIRT.toString().toLowerCase()});

        return config;
    }

    @Override
    public void initialize(final Server server, final ConfigurationSection config)
    {
        super.initialize(server, config);

        // normalize the items
        HashSet<String> newItems = new HashSet<String>(this.filterItems.size());
        for (String item : this.filterItems)
        {
            Material material = Material.matchMaterial(item);
            if (item != null)
            {
                newItems.add(material.name());
            }
        }
        this.filterItems = newItems;
    }

    public FilteredItemPrevention(String name, String permission, Plugin plugin)
    {
        super(name, permission, plugin);
    }

    public boolean can(final Player player, Material material)
    {
        return can(player, material.name());
    }

    public void prevent(final Cancellable event, final Player player, final Material material)
    {
        prevent(event, player, material.name());
    }

    public void preventThrottled(final Cancellable event, final Player player, final Material material)
    {
        preventThrottled(event, player, material.name());
    }
}
