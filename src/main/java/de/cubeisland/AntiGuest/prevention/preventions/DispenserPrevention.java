package de.cubeisland.AntiGuest.prevention.preventions;

import de.cubeisland.AntiGuest.prevention.Prevention;
import de.cubeisland.AntiGuest.prevention.PreventionPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

/**
 * Prevents dispenser access
 *
 * @author Phillip Schichtel
 */
public class DispenserPrevention extends Prevention
{
    public DispenserPrevention(PreventionPlugin plugin)
    {
        super("dispenser", plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void open(InventoryOpenEvent event)
    {
        if (event.getInventory().getType() == InventoryType.DISPENSER)
        {
            if (event.getPlayer() instanceof Player)
            {
                checkAndPrevent(event, (Player)event.getPlayer());
            }
        }
    }
}
