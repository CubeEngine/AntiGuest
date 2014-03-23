package de.cubeisland.antiguest.prevention.preventions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerItemHeldEvent;

import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;

/**
 * Prevents changing the hotbar of the player
 */
public class HotbarPrevention extends Prevention
{
    public HotbarPrevention(PreventionPlugin plugin)
    {
        super("hotbar", plugin);
        setEnablePunishing(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onSlotChange(PlayerItemHeldEvent event)
    {
        checkAndPrevent(event, event.getPlayer());
    }
}
