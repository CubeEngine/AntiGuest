package de.codeinfection.quickwango.AntiGuest.Listeners;

import de.codeinfection.quickwango.AntiGuest.AntiGuest;
import de.codeinfection.quickwango.AntiGuest.Convert;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import de.codeinfection.quickwango.AntiGuest.Preventions.ActionPrevention;
import de.codeinfection.quickwango.AntiGuest.Vector;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;

/**
 *
 * @author CodeInfection
 */
public class AntiGuestMovementListener implements Listener
{
    private final static ActionPrevention movePrev   = (ActionPrevention)AntiGuest.preventions.get("move");
    private final static Prevention sneakPrev  = AntiGuest.preventions.get("sneak");
    private final static Prevention sprintPrev = AntiGuest.preventions.get("sprint");

    @EventHandler( priority=EventPriority.LOWEST )
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event)
    {
        if (event.isCancelled() || sneakPrev == null) return;

        final Player player = event.getPlayer();
        if (event.isSneaking())
        {
            if (!sneakPrev.can(player))
            {
                sneakPrev.sendMessage(player);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler( priority=EventPriority.LOWEST )
    public void onPlayerToggleSprint(PlayerToggleSprintEvent event)
    {
        if (event.isCancelled() || sprintPrev == null) return;

        final Player player = event.getPlayer();
        if (event.isSprinting())
        {
            if (!sprintPrev.can(player))
            {
                sprintPrev.sendMessage(player);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler( priority=EventPriority.LOWEST )
    public void onPlayerMove(PlayerMoveEvent event)
    {
        if (event.isCancelled() || movePrev == null) return;

        final Player player = event.getPlayer();
        if (!movePrev.can(player))
        {
            final Location to = event.getTo();
            if (to != null)
            {
                final int radius = Math.max(movePrev.getConfig().getInt("radius", 3), 3);
                final Vector target = Convert.toVector(to);
                final Vector spawn = Convert.toVector(to.getWorld().getSpawnLocation());

                if (radius / spawn.distance(target) < 1)
                {
                    movePrev.sendThrottledMessage(player);
                    event.setCancelled(true);
                }
            }
        }
    }
}
