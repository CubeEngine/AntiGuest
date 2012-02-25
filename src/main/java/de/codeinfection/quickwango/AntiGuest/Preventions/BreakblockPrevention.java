package de.codeinfection.quickwango.AntiGuest.Preventions;

import de.codeinfection.quickwango.AntiGuest.AntiGuest;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

/**
 *
 * @author Phillip
 */
public class BreakblockPrevention extends Prevention
{
    public BreakblockPrevention()
    {
        super("breakblock", AntiGuest.getInstance());
    }

    @Override
    public ConfigurationSection getDefaultConfig()
    {
        ConfigurationSection config = super.getDefaultConfig();

        config.set("message", "&4You are not allowed to break blocks!");

        return config;
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void handle(BlockBreakEvent event)
    {
        prevent(event, event.getPlayer());
    }
}
