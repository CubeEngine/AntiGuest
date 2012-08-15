package de.cubeisland.AntiGuest.prevention.preventions;

import de.cubeisland.AntiGuest.prevention.Prevention;
import de.cubeisland.AntiGuest.prevention.PreventionPlugin;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Prevents trampling crops
 *
 * @author Phillip Schichtel
 */
public class TramplePrevention extends Prevention
{
    public TramplePrevention(PreventionPlugin plugin)
    {
        super("trample", plugin);
        setThrottleDelay(3);
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void interact(PlayerInteractEvent event)
    {
        if (event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.SOIL)
        {
            prevent(event, event.getPlayer());
        }
    }
}
