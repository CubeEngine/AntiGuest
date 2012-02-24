package de.codeinfection.quickwango.AntiGuest.Preventions;

import de.codeinfection.quickwango.AntiGuest.AntiGuest;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 *
 * @author Phillip
 */
public class PlaceblockPrevention extends Prevention
{
    public PlaceblockPrevention()
    {
        super("placeblock", AntiGuest.getInstance());
    }

    @Override
    public ConfigurationSection getDefaultConfig()
    {
        ConfigurationSection config = super.getDefaultConfig();

        config.addDefault("message", "&4You are not allowed to place blocks!");

        return config;
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void handle(BlockPlaceEvent event)
    {
        prevent(event, event.getPlayer());
    }
}
