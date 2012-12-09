package de.cubeisland.AntiGuest.prevention.preventions;

import de.cubeisland.AntiGuest.prevention.Prevention;
import de.cubeisland.AntiGuest.prevention.PreventionPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerShearEntityEvent;

/**
 * Prevents shearing
 *
 * @author Phillip Schichtel
 */
public class ShearPrevention extends Prevention
{
    public ShearPrevention(PreventionPlugin plugin)
    {
        super("shear", plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void shearEntity(PlayerShearEntityEvent event)
    {
        checkAndPrevent(event, event.getPlayer());
    }
}
