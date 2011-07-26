package de.codeinfection.quickwango.AntiGuest;

import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerPickupItemEvent;

/**
 *
 * @author CodeInfection
 */
public class AntiGuestPlayerListener extends PlayerListener
{
    protected final AntiGuest plugin;

    public AntiGuestPlayerListener(AntiGuest plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        final Player player = event.getPlayer();
        Action action = event.getAction();
        if (action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_BLOCK || action == Action.PHYSICAL)
        {
            if (!this.plugin.can(player, "interact"))
            {
                event.setCancelled(true);
                this.plugin.message(player, "interact");
            }
        }
    }

    @Override
    public void onPlayerPickupItem(PlayerPickupItemEvent event)
    {
        final Player player = event.getPlayer();
        if (!this.plugin.can(player, "pickup"))
        {
            event.setCancelled(true);
            this.plugin.message(player, "pickup");
        }
    }
}
