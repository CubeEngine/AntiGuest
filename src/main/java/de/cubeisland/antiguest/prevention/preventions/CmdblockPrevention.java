package de.cubeisland.antiguest.prevention.preventions;

import de.cubeisland.antiguest.prevention.*;

import org.bukkit.block.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;

/**
 * This prevents command block usage
 */
public class CmdblockPrevention extends Prevention
{
    public CmdblockPrevention(PreventionPlugin plugin)
    {
        super("cmdblock", plugin);
        setEnableByDefault(true);
        setEnablePunishing(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCommandBlock(PlayerInteractEvent event)
    {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
        {
            return;
        }
        final BlockState clickedState = event.getClickedBlock().getState();
        if (clickedState instanceof CommandBlock && checkAndPrevent(event, event.getPlayer()))
        {
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setUseItemInHand(Event.Result.DENY);
        }
    }
}
