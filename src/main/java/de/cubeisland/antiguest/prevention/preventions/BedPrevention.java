package de.cubeisland.antiguest.prevention.preventions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerBedEnterEvent;

import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;

/**
 * Prevents bed usage
 *
 * @author Phillip Schichtel
 */
public class BedPrevention extends Prevention {
    public BedPrevention(PreventionPlugin plugin) {
        super("bed", plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void bedEnter(PlayerBedEnterEvent event) {
        checkAndPrevent(event, event.getPlayer());
    }
}
