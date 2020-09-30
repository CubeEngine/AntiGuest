package de.cubeisland.antiguest.prevention.preventions;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;

/**
 * Prevents water bucket usage
 *
 * @author Phillip Schichtel
 */
public class WaterbucketPrevention extends Prevention {
    public WaterbucketPrevention(PreventionPlugin plugin) {
        super("waterbucket", plugin);
        setEnableByDefault(true);
    }

    @Override
    public String getConfigHeader() {
        return super.getConfigHeader() + "\nThis prevention works, even though the client shows that water was placed!\n";
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void empty(PlayerBucketEmptyEvent event) {
        if (event.getBucket() == Material.WATER_BUCKET)
            checkAndPrevent(event, event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void fill(PlayerBucketFillEvent event) {
        if (event.getItemStack().getType() == Material.WATER_BUCKET)
            checkAndPrevent(event, event.getPlayer());
    }
}
