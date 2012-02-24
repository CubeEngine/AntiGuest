package de.codeinfection.quickwango.AntiGuest.Preventions;

import de.codeinfection.quickwango.AntiGuest.AntiGuest;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerFishEvent;

/**
 *
 * @author Phillip
 */
public class FishPrevention extends Prevention
{

    public FishPrevention()
    {
        super("fish", AntiGuest.getInstance());
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void handle(PlayerFishEvent event)
    {
        prevent(event, event.getPlayer());
    }
}
