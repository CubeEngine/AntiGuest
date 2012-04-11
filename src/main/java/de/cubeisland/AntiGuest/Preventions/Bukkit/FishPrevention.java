package de.cubeisland.AntiGuest.Preventions.Bukkit;

import de.cubeisland.AntiGuest.AntiGuestBukkit;
import de.cubeisland.AntiGuest.Prevention;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerFishEvent;

/**
 * Prevents fishing
 *
 * @author Phillip Schichtel
 */
public class FishPrevention extends Prevention
{
    public FishPrevention()
    {
        super("fish", AntiGuestBukkit.getInstance());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(PlayerFishEvent event)
    {
        prevent(event, event.getPlayer());
    }
}
