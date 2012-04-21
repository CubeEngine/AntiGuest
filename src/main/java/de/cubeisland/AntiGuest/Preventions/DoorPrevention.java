package de.cubeisland.AntiGuest.Preventions;

import de.cubeisland.AntiGuest.Prevention;
import de.cubeisland.AntiGuest.PreventionPlugin;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Prevents door usage
 *
 * @author Phillip Schichtel
 */
public class DoorPrevention extends Prevention
{
    public DoorPrevention(PreventionPlugin plugin)
    {
        super("door", plugin, true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(PlayerInteractEvent event)
    {
        final Action action = event.getAction();
        if (action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_BLOCK)
        {
            final Material material = event.getClickedBlock().getType();
            if (
                material == Material.WOODEN_DOOR ||
                material == Material.IRON_DOOR ||
                material == Material.IRON_DOOR_BLOCK ||
                material == Material.TRAP_DOOR ||
                material == Material.FENCE_GATE
            )
            {
                prevent(event, event.getPlayer());
            }
        }
    }
}
