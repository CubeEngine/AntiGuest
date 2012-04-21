package de.cubeisland.AntiGuest.Preventions.Bukkit;

import de.cubeisland.AntiGuest.Prevention;
import de.cubeisland.AntiGuest.PreventionPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Prevents chest access
 *
 * @author Phillip Schichtel
 */
public class ChestPrevention extends Prevention
{
    public ChestPrevention(PreventionPlugin plugin)
    {
        super("chest", plugin, true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(PlayerInteractEvent event)
    {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.CHEST)
        {
            if (prevent(event, event.getPlayer()))
            {
                event.setUseInteractedBlock(Result.DENY);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(InventoryOpenEvent event)
    {
        if (event.getInventory().getType() == InventoryType.CHEST)
        {
            if (event.getPlayer() instanceof Player)
            {
                prevent(event, (Player)event.getPlayer());
            }
        }
    }
}
