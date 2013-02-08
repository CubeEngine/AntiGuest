package de.cubeisland.AntiGuest.prevention.preventions;

import de.cubeisland.AntiGuest.prevention.Prevention;
import de.cubeisland.AntiGuest.prevention.PreventionPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

public class AnvilPrevention extends Prevention
{
    public AnvilPrevention(PreventionPlugin plugin)
    {
        super("anvil", plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent event)
    {
        if (event.getInventory().getType() == InventoryType.ANVIL && event.getPlayer() instanceof Player)
        {
            checkAndPrevent(event, (Player)event.getPlayer());
        }
    }
}
