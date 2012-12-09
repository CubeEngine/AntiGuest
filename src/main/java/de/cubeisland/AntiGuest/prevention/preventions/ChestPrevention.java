package de.cubeisland.AntiGuest.prevention.preventions;

import de.cubeisland.AntiGuest.prevention.Prevention;
import de.cubeisland.AntiGuest.prevention.PreventionPlugin;
import org.bukkit.Material;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
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
        super("chest", plugin);
        setEnableByDefault(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void interact(PlayerInteractEvent event)
    {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.CHEST)
        {
            if (checkAndPrevent(event, event.getPlayer()))
            {
                event.setUseInteractedBlock(Result.DENY);
            }
        }
    }
}
