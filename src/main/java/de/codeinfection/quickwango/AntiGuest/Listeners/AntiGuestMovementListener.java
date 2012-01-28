package de.codeinfection.quickwango.AntiGuest.Listeners;

import de.codeinfection.quickwango.AntiGuest.AntiGuest;
import de.codeinfection.quickwango.AntiGuest.Convert;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import de.codeinfection.quickwango.AntiGuest.Preventions.ActionPrevention;
import de.codeinfection.quickwango.AntiGuest.Vector;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

/**
 *
 * @author CodeInfection
 */
public class AntiGuestMovementListener implements Listener
{
    private final static ActionPrevention movePrev   = (ActionPrevention)AntiGuest.preventions.get("move");
    private final static Prevention sneakPrev  = AntiGuest.preventions.get("sneak");
    private final static Prevention teleportPrev = AntiGuest.preventions.get("teleport");

    private final static int radius = Math.max(movePrev.getConfig().getInt("radius", Bukkit.getSpawnRadius()), 3);

    @EventHandler( priority=EventPriority.LOWEST )
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event)
    {
        if (event.isCancelled() || sneakPrev == null) return;

        final Player player = event.getPlayer();
        if (event.isSneaking())
        {
            if (!sneakPrev.can(player))
            {
                if (!player.getGameMode().equals(GameMode.CREATIVE))
                {
                    sneakPrev.sendMessage(player);
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler( priority=EventPriority.LOWEST )
    public void onPlayerTeleport(PlayerTeleportEvent event)
    {
        if (event.isCancelled() || teleportPrev == null) return;

        final Player player = event.getPlayer();
        if (teleportPrev.can(player))
        {
            teleportPrev.sendMessage(player);
            event.setCancelled(true);
        }
    }

    /**
     * This one is hacky and glitchy
     *
     * @param event
     */
    @EventHandler( priority=EventPriority.LOWEST )
    public void onPlayerMove(PlayerMoveEvent event)
    {
        if (event.isCancelled() || movePrev == null) return;

        final Player player = event.getPlayer();
        if (!movePrev.can(player))
        {
            final Location toLocation = event.getTo();
            final Location spawnLocation = toLocation.getWorld().getSpawnLocation();
            final Vector to = Convert.toVector2D(toLocation);
            final Vector spawn = Convert.toVector2D(spawnLocation);

            if (radius / spawn.distance(to) < 1)
            {
                movePrev.sendThrottledMessage(player);
                event.setCancelled(true);
                
                final Vector from = Convert.toVector2D(player.getLocation());
                // i bit less then 1 because of in inaccurate from location
                if (radius / spawn.distance(from) <= 0.98)
                {
                    // teleportation scheduled for the next tick to prevent kick (moved too fast)
                    Bukkit.getScheduler().scheduleSyncDelayedTask(AntiGuest.getInstance(), new Runnable() {
                        public void run() {
                            player.teleport(spawnLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
                        }
                    });
                }
            }
        }
    }
}
