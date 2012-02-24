package de.codeinfection.quickwango.AntiGuest.Preventions;

import de.codeinfection.quickwango.AntiGuest.AntiGuest;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChatEvent;

/**
 *
 * @author Phillip
 */
public class ChatPrevention extends Prevention
{

    public ChatPrevention()
    {
        super("chat", AntiGuest.getInstance());
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void handle(PlayerChatEvent event)
    {
        prevent(event, event.getPlayer());
    }
}
