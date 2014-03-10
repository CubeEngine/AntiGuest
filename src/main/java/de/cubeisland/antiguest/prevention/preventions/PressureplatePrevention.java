package de.cubeisland.antiguest.prevention.preventions;

import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.concurrent.TimeUnit;

import static org.bukkit.Material.*;

/**
 * Prevents pressure plate usage
 *
 * @author Phillip Schichtel
 */
public class PressureplatePrevention extends Prevention
{
    public PressureplatePrevention(PreventionPlugin plugin)
    {
        super("pressureplate", plugin);
        setThrottleDelay(3, TimeUnit.SECONDS);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void pressure(PlayerInteractEvent event)
    {
        if (event.getAction() == Action.PHYSICAL)
        {
            final Material m = event.getClickedBlock().getType();
            if (m == STONE_PLATE || m == WOOD_PLATE || m == GOLD_PLATE || m == IRON_PLATE)
            {
                checkAndPrevent(event, event.getPlayer());
            }
        }
    }
}
