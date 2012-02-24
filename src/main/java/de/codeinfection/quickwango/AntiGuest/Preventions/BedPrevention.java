package de.codeinfection.quickwango.AntiGuest.Preventions;

import de.codeinfection.quickwango.AntiGuest.AntiGuest;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerBedEnterEvent;

/**
 *
 * @author Phillip
 */
public class BedPrevention extends Prevention
{

    public BedPrevention()
    {
        super("bed", AntiGuest.getInstance());
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void handle(PlayerBedEnterEvent event)
    {
        prevent(event, event.getPlayer());
    }
}
