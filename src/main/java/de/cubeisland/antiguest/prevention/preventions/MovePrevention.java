package de.cubeisland.antiguest.prevention.preventions;

import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;
import de.cubeisland.libMinecraft.Convert;
import de.cubeisland.libMinecraft.math.Square;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.concurrent.TimeUnit;

/**
 * Prevents movement
 *
 * @author Phillip Schichtel
 */
public class MovePrevention extends Prevention
{
    private int width;

    public MovePrevention(PreventionPlugin plugin)
    {
        super("move", plugin, false);
        setThrottleDelay(3, TimeUnit.SECONDS);
    }

    @Override
    public String getConfigHeader()
    {
        return super.getConfigHeader() + "\n" +
                "Configuration info:\n" +
                "    width: the number of blocks a player can move awy from the spawn\n";
    }

    @Override
    public void enable()
    {
        super.enable();
        this.width = getConfig().getInt("width");
    }

    @Override
    public Configuration getDefaultConfig()
    {
        Configuration config = super.getDefaultConfig();

        config.set("width", Math.max(5, getPlugin().getServer().getSpawnRadius()));

        return config;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void move(PlayerMoveEvent event)
    {
        final Location from = event.getFrom();
        final Location to = event.getTo();

        // only check if the player really moved
        if (from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ())
        {
            return;
        }
        
        final Player player = event.getPlayer();
        if (!can(player))
        {
            // create a square around the spawn
            final Square spawnSquare = new Square(
                Convert.toBlockVector2(player.getWorld().getSpawnLocation()).substract(this.width),
                this.width * 2
            );

            // is the new location inside the spawn square?
            if (!spawnSquare.contains(Convert.toBlockVector2(to)))
            {
                Location fallback = from;
                if (!spawnSquare.contains(Convert.toBlockVector2(fallback)))
                {
                    fallback = player.getWorld().getSpawnLocation();
                }
                sendMessage(player);
                player.teleport(fallback, PlayerTeleportEvent.TeleportCause.PLUGIN);
                event.setCancelled(true);
            }
        }
    }
}
