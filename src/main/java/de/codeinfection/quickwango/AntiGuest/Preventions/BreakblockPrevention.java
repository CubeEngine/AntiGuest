package de.codeinfection.quickwango.AntiGuest.Preventions;

import de.codeinfection.quickwango.AntiGuest.AntiGuest;
import de.codeinfection.quickwango.AntiGuest.Prevention;
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
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void handle(BlockBreakEvent event)
    {
        prevent(event, event.getPlayer());
    }
}
