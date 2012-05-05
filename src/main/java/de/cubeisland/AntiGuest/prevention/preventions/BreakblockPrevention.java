package de.cubeisland.AntiGuest.prevention.preventions;

import de.cubeisland.AntiGuest.prevention.FilteredItemPrevention;
import de.cubeisland.AntiGuest.prevention.PreventionPlugin;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Prevents block breaking
 *
 * @author Phillip Schichtel
 */
public class BreakblockPrevention extends FilteredItemPrevention
{
    public BreakblockPrevention(PreventionPlugin plugin)
    {
        super("breakblock", plugin);
        setEnableByDefault(true);
        setFilterMode(FilterMode.NONE);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(BlockBreakEvent event)
    {
        prevent(event, event.getPlayer(), event.getBlock().getType());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(PaintingBreakByEntityEvent event)
    {
        final Entity remover = event.getRemover();
        if (remover instanceof Player)
        {
            prevent(event, (Player)remover, Material.PAINTING);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(PlayerInteractEvent event)
    {
        if (event.getAction() == Action.LEFT_CLICK_BLOCK)
        {
            if (event.getClickedBlock().getRelative(BlockFace.UP).getType() == Material.FIRE)
            {
                if (prevent(event, event.getPlayer(), Material.FIRE))
                {
                    event.setUseInteractedBlock(Event.Result.DENY);
                    event.setUseItemInHand(Event.Result.DENY);
                }
            }
        }
    }
}
