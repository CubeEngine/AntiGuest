package de.cubeisland.AntiGuest.Preventions.Bukkit;

import de.cubeisland.AntiGuest.AntiGuestBukkit;
import de.cubeisland.AntiGuest.Prevention;
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
    public MilkingPrevention()
    {
        super("milking", AntiGuestBukkit.getInstance());
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
