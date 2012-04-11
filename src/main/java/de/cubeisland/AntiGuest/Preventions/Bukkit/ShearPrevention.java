package de.cubeisland.AntiGuest.Preventions.Bukkit;

import de.cubeisland.AntiGuest.AntiGuestBukkit;
import de.cubeisland.AntiGuest.Prevention;
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
    public ShearPrevention()
    {
        super("shear", AntiGuestBukkit.getInstance());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(PlayerShearEntityEvent event)
    {
        prevent(event, event.getPlayer());
    }
}
