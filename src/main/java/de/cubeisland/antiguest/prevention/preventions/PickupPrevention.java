package de.cubeisland.antiguest.prevention.preventions;

import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityPickupItemEvent;

import de.cubeisland.antiguest.prevention.FilteredItemPrevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;

/**
 * Prevents picking up items
 *
 * @author Phillip Schichtel
 */
public class PickupPrevention extends FilteredItemPrevention {
    public PickupPrevention(PreventionPlugin plugin) {
        super("pickup", plugin, false);
        setThrottleDelay(3, TimeUnit.SECONDS);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void pickupItem(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player)
            checkAndPrevent(event, (Player) event.getEntity(), event.getItem().getItemStack().getType());
    }
}
