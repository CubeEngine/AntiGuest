package de.cubeisland.antiguest.prevention.preventions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;

/**
 * Prevents chatting
 *
 * @author Phillip Schichtel
 */
public class ChatPrevention extends Prevention {
    public ChatPrevention(PreventionPlugin plugin) {
        super("chat", plugin);
        setEnablePunishing(false);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void chat(AsyncPlayerChatEvent event) {
        checkAndPrevent(event, event.getPlayer());
    }
}
