package de.codeinfection.quickwango.AntiGuest.Preventions;

import de.codeinfection.quickwango.AntiGuest.AntiGuest;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import de.codeinfection.quickwango.AntiGuest.Util.Convert;
import de.codeinfection.quickwango.AntiGuest.Util.Vector2D;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

/**
 * Prevents movement
 *
 * @author Phillip Schichtel
 */
public class MovePrevention extends Prevention
{
    private int radius;

    public MovePrevention()
    {
        super("move", AntiGuest.getInstance());
    }

    @Override
    public ConfigurationSection getDefaultConfig()
    {
        ConfigurationSection config = super.getDefaultConfig();

        config.set("message", "&4You are not allowed to move any further!");
        config.set("messageDelay", 3);
        // TODO remove Bukkit dependency
        config.set("radius", Math.max(5, Bukkit.getSpawnRadius()));

        return config;
    }
    
    @Override
    public void initialize(Server server, ConfigurationSection config)
    {
        super.initialize(server, config);
        this.radius = config.getInt("radius");
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(PlayerMoveEvent event)
    {
        final Player player = event.getPlayer();
        if (!can(player))
        {
            final Location toLocation = event.getTo();
            final Location spawnLocation = toLocation.getWorld().getSpawnLocation();
            final Vector2D to = Convert.toVector2D(toLocation);
            final Vector2D spawn = Convert.toVector2D(spawnLocation);

            if (this.radius / spawn.distance(to) < 1)
            {
                sendThrottledMessage(player);
                event.setCancelled(true);
                
                final Vector2D from = Convert.toVector2D(player.getLocation());
                // i bit less then 1 because of the inaccurate from location
                if (this.radius / spawn.distance(from) <= 0.98)
                {
                    // teleportation scheduled for the next tick to prevent kick (moved too fast)
                    player.getServer().getScheduler().scheduleSyncDelayedTask(AntiGuest.getInstance(), new Runnable()
                    {
                        public void run()
                        {
                            player.teleport(spawnLocation, TeleportCause.PLUGIN);
                        }
                    });
                }
            }
        }
    }
}
