package de.codeinfection.quickwango.AntiGuest.Listeners;

import de.codeinfection.quickwango.AntiGuest.AntiGuest;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import de.codeinfection.quickwango.AntiGuest.Preventions.ActionPrevention;
import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

/**
 *
 * @author CodeInfection
 */
public class AntiGuestMovementListener implements Listener
{
    private final static ActionPrevention movePrev   = (ActionPrevention)AntiGuest.preventions.get("move");
    private final static Prevention sneakPrev  = AntiGuest.preventions.get("sneak");
    private final static Prevention sprintPrev = AntiGuest.preventions.get("sprint");

    private final HashMap<Player, Location> playerSpawnLocations = new HashMap<Player, Location>();

    @EventHandler( priority=EventPriority.MONITOR )
    public void onPlayerSpawn(PlayerRespawnEvent event)
    {
        this.playerSpawnLocations.put(event.getPlayer(), event.getRespawnLocation());
    }

    @EventHandler( priority=EventPriority.MONITOR )
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        this.playerSpawnLocations.remove(event.getPlayer());
    }

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
        if (movePrev == null) return;

        final Player player = event.getPlayer();
        final Location to = event.getTo();
        if (!isInSpawnArea(to) && !movePrev.can(player))
        {
            event.setCancelled(true);
            player.teleport(to.getWorld().getSpawnLocation());
            movePrev.sendThrottledMessage(player);
        }
    }

    private boolean isInSpawnArea(Location loc)
    {
        Location spawnLocation = loc.getWorld().getSpawnLocation();

        final double deltaX = loc.getX() - spawnLocation.getX();
        final double deltaZ = loc.getZ() - spawnLocation.getZ();
        final int radius = Math.max(movePrev.getConfig().getInt("radius", 3), 3);

        return (Math.pow(deltaX, 2) + Math.pow(deltaZ, 2) <= Math.pow(radius, 2));
    }
}
