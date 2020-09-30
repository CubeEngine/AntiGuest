package de.cubeisland.antiguest.prevention.preventions;

import java.util.concurrent.TimeUnit;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;

/**
 * Prevents the activation of trip wires
 *
 * @author Phillip Schichtel
 */
public class TripwirePrevetion extends Prevention {
    public TripwirePrevetion(PreventionPlugin plugin) {
        super("tripwire", plugin);
        setThrottleDelay(3, TimeUnit.SECONDS);
        setEnableByDefault(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void interact(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.TRIPWIRE)
            checkAndPrevent(event, event.getPlayer());
    }
}
