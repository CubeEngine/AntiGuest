package de.codeinfection.quickwango.AntiGuest;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;
import org.bukkit.event.painting.PaintingBreakEvent;
import org.bukkit.event.painting.PaintingPlaceEvent;

/**
 *
 * @author CodeInfection
 */
public class AntiGuestBlockListener implements Listener
{
    private final static Prevention placeblockPrev = AntiGuest.preventions.get("placeblock");
    private final static Prevention breakblockPrev = AntiGuest.preventions.get("breakblock");

    @EventHandler( priority=EventPriority.LOWEST )
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if (placeblockPrev == null) return;
        final Player player = event.getPlayer();
        if (!placeblockPrev.can(player))
        {
            event.setCancelled(true);
            placeblockPrev.sendMessage(player);
        }
    }

    @EventHandler( priority=EventPriority.LOWEST )
    public void onBlockBreak(BlockBreakEvent event)
    {
        if (breakblockPrev == null) return;
        final Player player = event.getPlayer();
        if (!breakblockPrev.can(player))
        {
            event.setCancelled(true);
            breakblockPrev.sendMessage(player);
        }
    }

    @EventHandler( priority=EventPriority.LOWEST )
    public void onPaintingPlace(PaintingPlaceEvent event)
    {
        if (placeblockPrev == null) return;
        final Player player = event.getPlayer();
        if (!placeblockPrev.can(player))
        {
            placeblockPrev.sendMessage(player);
            event.setCancelled(true);
        }
    }

    @EventHandler( priority=EventPriority.LOWEST )
    public void onPaintingBreak(PaintingBreakEvent event)
    {
        if (breakblockPrev == null) return;
        if (event instanceof PaintingBreakByEntityEvent)
        {
            final Entity remover = ((PaintingBreakByEntityEvent)event).getRemover();
            if (remover instanceof Player)
            {
                final Player player = (Player)remover;
                if (!breakblockPrev.can(player))
                {
                    event.setCancelled(true);
                    breakblockPrev.sendMessage(player);
                }
            }
        }
    }
}
