package de.cubeisland.antiguest.prevention.preventions;

import static org.bukkit.Material.GOLD_PLATE;
import static org.bukkit.Material.IRON_PLATE;
import static org.bukkit.Material.STONE_PLATE;
import static org.bukkit.Material.WOOD_PLATE;

import java.util.concurrent.TimeUnit;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;

/**
 * Prevents pressure plate usage
 *
 * @author Phillip Schichtel
 */
public class PressureplatePrevention extends Prevention {
    public PressureplatePrevention(PreventionPlugin plugin) {
        super("pressureplate", plugin);
        setThrottleDelay(3, TimeUnit.SECONDS);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void pressure(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL) {
            final Material m = event.getClickedBlock().getType();
            if (m == STONE_PLATE || m == WOOD_PLATE || m == GOLD_PLATE || m == IRON_PLATE)
                checkAndPrevent(event, event.getPlayer());
        }
    }
}
