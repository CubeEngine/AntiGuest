package de.cubeisland.AntiGuest.prevention.preventions;

import de.cubeisland.AntiGuest.prevention.Prevention;
import de.cubeisland.AntiGuest.prevention.PreventionPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityTameEvent;

/**
 * Prevents taming
 *
 * @author Phillip Schichtel
 */
public class TamePrevention extends Prevention
{

    public TamePrevention(PreventionPlugin plugin)
    {
        super("tame", plugin);
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void tame(EntityTameEvent event)
    {
        if (event.getOwner() instanceof Player)
        {
            prevent(event, (Player)event.getOwner());
        }
    }
}
