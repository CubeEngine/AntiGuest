package de.cubeisland.antiguest.prevention.preventions;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;

import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;

/**
 * Prevents bow usage
 *
 * @author Phillip Schichtel
 */
public class BowPrevention extends Prevention {
    public BowPrevention(PreventionPlugin plugin) {
        super("bow", plugin);
        setEnableByDefault(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void shootBow(EntityShootBowEvent event) {
        final Entity shooter = event.getEntity();
        if (shooter instanceof Player)
            checkAndPrevent(event, (Player) shooter);
    }
}
