package de.codeinfection.quickwango.AntiGuest;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 *
 * @author CodeInfection
 */
public class AntiGuestBlockListener extends BlockListener
{
    protected final AntiGuest plugin;

    public AntiGuestBlockListener(AntiGuest plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event)
    {
        final Player player = event.getPlayer();
        if (!this.plugin.can(player, "breakblock"))
        {
            event.setCancelled(true);
            this.plugin.message(player, "breakblock");
        }
    }

    @Override
    public void onBlockPlace(BlockPlaceEvent event)
    {
        final Player player = event.getPlayer();
        if (!this.plugin.can(player, "placeblock"))
        {
            event.setCancelled(true);
            this.plugin.message(player, "placeblock");
        }
    }
}
