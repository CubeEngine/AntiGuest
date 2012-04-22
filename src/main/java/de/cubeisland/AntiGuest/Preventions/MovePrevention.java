package de.cubeisland.AntiGuest.Preventions;

import de.cubeisland.AntiGuest.Prevention;
import de.cubeisland.AntiGuest.PreventionPlugin;
import de.cubeisland.libMinecraft.Convert;
import de.cubeisland.libMinecraft.math.Square;
import de.cubeisland.libMinecraft.math.Vector2;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Prevents movement
 *
 * @author Phillip Schichtel
 */
public class MovePrevention extends Prevention
{
    private Vector2 width;

    public MovePrevention(PreventionPlugin plugin)
    {
        super("move", plugin);
    }

    @Override
    public Configuration getDefaultConfig()
    {
        Configuration config = super.getDefaultConfig();

        config.set("throttleDelay", 3);
        config.set("width", Math.max(5, getPlugin().getServer().getSpawnRadius()));

        return config;
    }
    
    @Override
    public void enable()
    {
        super.enable();
        int tmpWidth = getConfig().getInt("width");
        this.width = new Vector2(tmpWidth, tmpWidth);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(PlayerMoveEvent event)
    {
        final Player player = event.getPlayer();
        if (!can(player))
        {
            final Vector2 to = Convert.toVector2(event.getTo());
            final Square spawnSquare = new Square(
                Convert.toBlockVector2(player.getWorld().getSpawnLocation()).substract(this.width),
                this.width.x * 2
            );

            if (!spawnSquare.contains(to))
            {
                sendThrottledMessage(player);
                event.setCancelled(true);
            }
        }
    }
}
