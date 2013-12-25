package de.cubeisland.AntiGuest.prevention.preventions;

import de.cubeisland.AntiGuest.prevention.Prevention;
import de.cubeisland.AntiGuest.prevention.PreventionPlugin;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Prevents button usage
 *
 * @author Phillip Schichtel
 */
public class ButtonPrevention extends Prevention
{
    public ButtonPrevention(PreventionPlugin plugin)
    {
        super("button", plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void interact(PlayerInteractEvent event)
    {
        final Action action = event.getAction();
        if (action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_BLOCK)
        {
            final Material material = event.getClickedBlock().getType();
            if (material == Material.STONE_BUTTON || material == Material.WOOD_BUTTON)
            {
                checkAndPrevent(event, event.getPlayer());
            }
        }
    }
}
