package de.cubeisland.AntiGuest.Preventions;

import de.cubeisland.AntiGuest.FilteredItemPrevention;
import de.cubeisland.AntiGuest.PreventionPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerPickupItemEvent;

/**
 * Prevents picking up items
 *
 * @author Phillip Schichtel
 */
public class PickupPrevention extends FilteredItemPrevention
{
    public PickupPrevention(PreventionPlugin plugin)
    {
        super("pickup", plugin);
        setThrottleDelay(3);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(PlayerPickupItemEvent event)
    {
        prevent(event, event.getPlayer(), event.getItem().getItemStack().getType());
    }
}
