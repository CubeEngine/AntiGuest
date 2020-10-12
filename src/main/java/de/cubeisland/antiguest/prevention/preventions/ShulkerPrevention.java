package de.cubeisland.antiguest.prevention.preventions;

import org.bukkit.block.BlockState;
import org.bukkit.block.ShulkerBox;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;

/**
 * Prevents shulker access
 *
 */
public class ShulkerPrevention extends Prevention
{
    public ShulkerPrevention(PreventionPlugin plugin)
    {
        super("shulker", plugin);
        setEnableByDefault(true);
    }

    protected static boolean isShulker(BlockState state)
    {
        return state instanceof ShulkerBox;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void interact(PlayerInteractEvent event)
    {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && isShulker(event.getClickedBlock().getState()))
            if (checkAndPrevent(event, event.getPlayer()))
                event.setUseInteractedBlock(Result.DENY);
    }
}
