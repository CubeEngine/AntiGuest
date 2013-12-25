package de.cubeisland.AntiGuest.prevention.preventions;

import de.cubeisland.AntiGuest.prevention.Prevention;
import de.cubeisland.AntiGuest.prevention.PreventionPlugin;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.concurrent.TimeUnit;

/**
 * Prevents the activation of trip wires
 *
 * @author Phillip Schichtel
 */
public class TripwirePrevetion extends Prevention
{
    public TripwirePrevetion(PreventionPlugin plugin)
    {
        super("tripwire", plugin);
        setThrottleDelay(3, TimeUnit.SECONDS);
        setEnableByDefault(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void interact(PlayerInteractEvent event)
    {
        if (event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.TRIPWIRE)
        {
            checkAndPrevent(event, event.getPlayer());
        }
    }
}
