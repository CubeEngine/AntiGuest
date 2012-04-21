package de.cubeisland.AntiGuest.Preventions;

import de.cubeisland.AntiGuest.FilteredPrevention;
import de.cubeisland.AntiGuest.PreventionPlugin;
import java.util.HashSet;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * Prevents command usage
 *
 * @author Phillip Schichtel
 */
public class CommandPrevention extends FilteredPrevention
{
    public CommandPrevention(PreventionPlugin plugin)
    {
        super("command", plugin);
    }

    @Override
    public Configuration getDefaultConfig()
    {
        Configuration config = super.getDefaultConfig();

        config.set("list", new String[] {
            "plugins",
            "pl",
            "version"
        });

        return config;
    }

    @Override
    public void enable()
    {
        super.enable();
        
        // normalize the items
        HashSet<Object> newItems = new HashSet<Object>(this.filterItems.size());
        for (Object item : this.filterItems)
        {
            newItems.add(String.valueOf(item).trim().toLowerCase());
        }
        this.filterItems = newItems;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(PlayerCommandPreprocessEvent event)
    {
        String message = event.getMessage();
        if (message.startsWith("/"))
        {
            message = message.substring(1).trim();
            final int spaceIndex = message.indexOf(' ');
            if (spaceIndex >= 0)
            {
                message = message.substring(0, spaceIndex);
            }
            if (message.length() > 0)
            {
                prevent(event, event.getPlayer(), message);
            }
        }
    }
}
