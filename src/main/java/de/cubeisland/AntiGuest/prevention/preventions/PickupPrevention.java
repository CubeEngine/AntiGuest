package de.cubeisland.AntiGuest.prevention.preventions;

import de.cubeisland.AntiGuest.prevention.FilteredItemPrevention;
import de.cubeisland.AntiGuest.prevention.PreventionPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.concurrent.TimeUnit;

/**
 * Prevents picking up items
 *
 * @author Phillip Schichtel
 */
public class PickupPrevention extends FilteredItemPrevention
{
    public PickupPrevention(PreventionPlugin plugin)
    {
        super("pickup", plugin, false);
        setThrottleDelay(3, TimeUnit.SECONDS);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void pickupItem(PlayerPickupItemEvent event)
    {
        checkAndPrevent(event, event.getPlayer(), event.getItem().getItemStack().getType());
    }
}
