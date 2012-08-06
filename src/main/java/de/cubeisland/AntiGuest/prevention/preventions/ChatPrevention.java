package de.cubeisland.AntiGuest.prevention.preventions;

import de.cubeisland.AntiGuest.prevention.Prevention;
import de.cubeisland.AntiGuest.prevention.PreventionPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChatEvent;

/**
 * Prevents chatting
 *
 * @author Phillip Schichtel
 */
public class ChatPrevention extends Prevention
{
    public ChatPrevention(PreventionPlugin plugin)
    {
        super("chat", plugin);
        setEnablePunishing(false);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void chat(PlayerChatEvent event)
    {
        prevent(event, event.getPlayer());
    }
}
