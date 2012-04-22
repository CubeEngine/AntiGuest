package de.cubeisland.AntiGuest.Preventions;

import de.cubeisland.AntiGuest.Prevention;
import de.cubeisland.AntiGuest.PreventionPlugin;
import org.bukkit.configuration.Configuration;
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
        super("monster", plugin);
    }

    @Override
    public Configuration getDefaultConfig()
    {
        Configuration config = super.getDefaultConfig();

        config.set("throttleDelay", 3);

        return config;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(EntityTargetEvent event)
    {
        final Entity target = event.getTarget();
        if (target instanceof Player)
        {
            preventThrottled(event, (Player)target);
        }
    }
}
