package de.codeinfection.quickwango.AntiGuest.Preventions;

import de.codeinfection.quickwango.AntiGuest.AntiGuest;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerPickupItemEvent;

/**
 *
 * @author Phillip
 */
public class PickupPrevention extends Prevention
{

    public PickupPrevention()
    {
        super("pickup", AntiGuest.getInstance());
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void handle(PlayerPickupItemEvent event)
    {
        preventThrottled(event, event.getPlayer());
    }
}
