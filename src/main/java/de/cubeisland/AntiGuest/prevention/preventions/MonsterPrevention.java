package de.cubeisland.AntiGuest.prevention.preventions;

import de.cubeisland.AntiGuest.prevention.Prevention;
import de.cubeisland.AntiGuest.prevention.PreventionPlugin;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityTargetEvent;

/**
 * Prevents targeting by monsters
 *
 * @author Phillip Schichtel
 */
public class MonsterPrevention extends Prevention
{
    public MonsterPrevention(PreventionPlugin plugin)
    {
        super("monster", plugin, false);
        setEnableByDefault(true);
        setThrottleDelay(3);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(EntityTargetEvent event)
    {
        final Entity target = event.getTarget();
        if (target instanceof Player)
        {
            prevent(event, (Player)target);
        }
    }
}
