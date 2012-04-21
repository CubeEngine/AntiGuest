package de.cubeisland.AntiGuest.Preventions;

import de.cubeisland.AntiGuest.Prevention;
import de.cubeisland.AntiGuest.PreventionPlugin;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerBucketFillEvent;

/**
 * Prevents milking of cows
 *
 * @author Phillip Schichtel
 */
public class MilkingPrevention extends Prevention
{
    public MilkingPrevention(PreventionPlugin plugin)
    {
        super("milking", plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void fill(PlayerBucketFillEvent event)
    {
        if (event.getItemStack().getType() == Material.MILK_BUCKET)
        {
            prevent(event, event.getPlayer());
        }
    }
}
