package de.cubeisland.antiguest.prevention.preventions;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerBucketFillEvent;

import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;

/**
 * Prevents milking of cows
 *
 * @author Phillip Schichtel
 */
public class MilkingPrevention extends Prevention {
    public MilkingPrevention(PreventionPlugin plugin) {
        super("milking", plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void fill(PlayerBucketFillEvent event) {
        if (event.getItemStack().getType() == Material.MILK_BUCKET)
            checkAndPrevent(event, event.getPlayer());
    }
}
