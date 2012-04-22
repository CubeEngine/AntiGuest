package de.cubeisland.AntiGuest.Preventions;

import de.cubeisland.AntiGuest.FilteredItemPrevention;
import de.cubeisland.AntiGuest.PreventionPlugin;
import org.bukkit.configuration.Configuration;
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
    }

    @Override
    public Configuration getDefaultConfig()
    {
        Configuration config = super.getDefaultConfig();

        config.set("throttleDelay", 3);

        return config;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(PlayerPickupItemEvent event)
    {
        preventThrottled(event, event.getPlayer(), event.getItem().getItemStack().getType());
    }
}
