package de.cubeisland.AntiGuest.prevention.preventions;

import de.cubeisland.AntiGuest.prevention.FilteredEntityPrevention;
import de.cubeisland.AntiGuest.prevention.PreventionPlugin;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityTargetEvent;

/**
 * Prevents targeting by monsters
 *
 * @author Phillip Schichtel
 */
public class MonsterPrevention extends FilteredEntityPrevention
{
    public MonsterPrevention(PreventionPlugin plugin)
    {
        super("monster", plugin, false);
        setEnableByDefault(true);
        setThrottleDelay(3);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void target(EntityTargetEvent event)
    {
        if (event.getEntity() instanceof Monster)
        {
            final Entity target = event.getTarget();
            if (target instanceof Player)
            {
                prevent(event, (Player)target, event.getEntityType());
            }
        }
    }
}