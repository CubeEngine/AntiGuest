package de.codeinfection.quickwango.AntiGuest;

import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChatEvent;
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
    protected final HashMap<Player, Long> chatTimestamps;
    protected final HashMap<Player, Long> pickupTimestamps;

    public AntiGuestPlayerListener(AntiGuest plugin)
    {
        this.plugin = plugin;
        this.chatTimestamps = new HashMap<Player, Long>();
        this.pickupTimestamps = new HashMap<Player, Long>();
    }

    protected void noPickupMessage(Player player)
    {
        Long lastTime = this.pickupTimestamps.get(player);
        long currentTime = System.currentTimeMillis();
        if (lastTime == null || lastTime + 5000 < currentTime)
        {
            this.plugin.message(player, "pickup");
            this.pickupTimestamps.put(player, currentTime);
        }
    }

    protected boolean canChat(Player player)
    {
        if (this.plugin.can(player, "spam"))
        {
            return true;
        }
        else
        {
            Long lastTime = this.chatTimestamps.get(player);
            long currentTime = System.currentTimeMillis();
            if (lastTime == null || lastTime + (this.plugin.chatLockDuration * 1000) < currentTime)
            {
                this.chatTimestamps.put(player, currentTime);
                return true;
            }
            else
            {
                return false;
            }
        }
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
            this.noPickupMessage(player);
        }
    }

    @Override
    public void onPlayerChat(PlayerChatEvent event)
    {
        final Player player = event.getPlayer();
        if (!this.canChat(player))
        {
            event.setCancelled(true);
            this.plugin.message(player, "spam");
        }
    }
}
