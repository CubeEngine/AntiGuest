package de.codeinfection.quickwango.AntiGuest.Preventions;

import de.codeinfection.quickwango.AntiGuest.AntiGuest;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 *
 * @author Phillip
 */
public class CommandPrevention extends Prevention
{

    public CommandPrevention()
    {
        super("command", AntiGuest.getInstance());
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void handle(PlayerCommandPreprocessEvent event)
    {
        prevent(event, event.getPlayer());
    }
}
