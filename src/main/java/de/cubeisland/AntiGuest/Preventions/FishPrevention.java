package de.cubeisland.AntiGuest.Preventions;

import de.cubeisland.AntiGuest.Prevention;
import de.cubeisland.AntiGuest.PreventionPlugin;
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
    public FishPrevention(PreventionPlugin plugin)
    {
        super("fish", plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(PlayerFishEvent event)
    {
        prevent(event, event.getPlayer());
    }
}
