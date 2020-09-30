package de.cubeisland.antiguest.prevention.preventions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerShearEntityEvent;

import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;

/**
 * Prevents shearing
 *
 * @author Phillip Schichtel
 */
public class ShearPrevention extends Prevention {
    public ShearPrevention(PreventionPlugin plugin) {
        super("shear", plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void shearEntity(PlayerShearEntityEvent event) {
        checkAndPrevent(event, event.getPlayer());
    }
}
