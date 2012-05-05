package de.cubeisland.AntiGuest.prevention.preventions;

import de.cubeisland.AntiGuest.prevention.Prevention;
import de.cubeisland.AntiGuest.prevention.PreventionPlugin;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.FoodLevelChangeEvent;

/**
 * Prevents hunger
 *
 * @author Phillip Schichtel
 */
public class HungerPrevention extends Prevention
{
    public HungerPrevention(PreventionPlugin plugin)
    {
        super("hunger", plugin);
        setEnableByDefault(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(FoodLevelChangeEvent event)
    {
        final Entity entity = event.getEntity();
        if (entity instanceof Player)
        {
            prevent(event, (Player)entity);
        }
    }
}
