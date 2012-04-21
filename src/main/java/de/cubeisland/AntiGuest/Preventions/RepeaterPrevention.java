package de.cubeisland.AntiGuest.Preventions;

import de.cubeisland.AntiGuest.Prevention;
import de.cubeisland.AntiGuest.PreventionPlugin;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Prevents repeater changing
 *
 * @author Phillip Schichtel
 */
public class RepeaterPrevention extends Prevention
{
    public RepeaterPrevention(PreventionPlugin plugin)
    {
        super("repeater", plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(PlayerInteractEvent event)
    {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            final Material material = event.getClickedBlock().getType();
            if (material == Material.DIODE_BLOCK_ON || material == Material.DIODE_BLOCK_OFF)
            {
                prevent(event, event.getPlayer());
            }
        }
    }
}
