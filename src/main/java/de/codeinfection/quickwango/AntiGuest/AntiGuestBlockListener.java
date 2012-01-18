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
public class AntiGuestBlockListener extends AbstractAntiGuestListener implements Listener
{
    private final static Prevention placeblockPrev = AntiGuest.preventions.get("placeblock");
    private final static Prevention breakblockPrev = AntiGuest.preventions.get("breakblock");

    public AntiGuestBlockListener(AntiGuest plugin)
    {
        super(plugin);
    }

    @EventHandler( event=BlockPlaceEvent.class, priority=EventPriority.LOWEST )
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if (placeblockPrev == null) return;
        final Player player = event.getPlayer();
        if (!can(player, placeblockPrev))
        {
            event.setCancelled(true);
            sendMessage(player, placeblockPrev);
        }
    }

    @EventHandler( event=BlockBreakEvent.class, priority=EventPriority.LOWEST )
    public void onBlockBreak(BlockBreakEvent event)
    {
        if (breakblockPrev == null) return;
        final Player player = event.getPlayer();
        if (!can(player, breakblockPrev))
        {
            event.setCancelled(true);
            sendMessage(player, breakblockPrev);
        }
    }

    @EventHandler( event=PaintingPlaceEvent.class, priority=EventPriority.LOWEST )
    public void onPaintingPlace(PaintingPlaceEvent event)
    {
        if (placeblockPrev == null) return;
        final Player player = event.getPlayer();
        if (!can(player, placeblockPrev))
        {
            sendMessage(player, placeblockPrev);
            event.setCancelled(true);
        }
    }

    @EventHandler( event=PaintingBreakEvent.class, priority=EventPriority.LOWEST )
    public void onPaintingBreak(PaintingBreakEvent event)
    {
        if (breakblockPrev == null) return;
        if (event instanceof PaintingBreakByEntityEvent)
        {
            final Entity remover = ((PaintingBreakByEntityEvent)event).getRemover();
            if (remover instanceof Player)
            {
                final Player player = (Player)remover;
                if (!can(player, breakblockPrev))
                {
                    event.setCancelled(true);
                    sendMessage(player, breakblockPrev);
                }
            }
        }
    }
}
