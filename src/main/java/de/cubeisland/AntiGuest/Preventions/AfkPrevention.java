package de.cubeisland.AntiGuest.Preventions;

import de.cubeisland.AntiGuest.AntiGuest;
import de.cubeisland.AntiGuest.Prevention;
import de.cubeisland.AntiGuest.PreventionPlugin;
import java.util.HashMap;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * Prevents idling players
 *
 * @author Phillip Schichtel
 */
public class AfkPrevention extends Prevention
{
    private HashMap<Player, PlayerAfkTracker> trackerMap;
    private BukkitScheduler scheduler;
    private int timeout;

    public AfkPrevention(PreventionPlugin plugin)
    {
        super("afk", plugin);
        this.trackerMap = null;
    }
    
    @Override
    public Configuration getDefaultConfig()
    {
        Configuration config = super.getDefaultConfig();

        config.set("timeout", 60 * 10);

        return config;
    }

    @Override
    public void enable()
    {
        super.enable();
        this.scheduler = getPlugin().getServer().getScheduler();
        this.timeout = getConfig().getInt("timeout") * 20;

        this.trackerMap = new HashMap<Player, PlayerAfkTracker>();
    }

    @Override
    public void disable()
    {
        super.disable();
        for (PlayerAfkTracker tracker : this.trackerMap.values())
        {
            tracker.cancel();
        }
        this.trackerMap.clear();
        this.trackerMap = null;
    }

    public void updateTracker(final Player player)
    {
        final PlayerAfkTracker tracker = this.trackerMap.get(player);
        if (tracker != null)
        {
            tracker.update();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void join(PlayerJoinEvent event)
    {
        final Player player = event.getPlayer();
        if (!can(player))
        {
            this.trackerMap.put(player, new PlayerAfkTracker(player));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void quit(PlayerQuitEvent event)
    {
        final PlayerAfkTracker tracker = this.trackerMap.remove(event.getPlayer());
        if (tracker != null)
        {
            tracker.cancel();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void moveUpdater(PlayerMoveEvent event)
    {
        this.updateTracker(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void chatUpdater(PlayerCommandPreprocessEvent event)
    {
        this.updateTracker(event.getPlayer());
    }

    private class PlayerAfkTracker implements Runnable
    {
        private final static int UPDATE_DELAY = 250;
        private final Player player;
        private int taskId;
        private long nextUpdate;

        public PlayerAfkTracker(Player player)
        {
            this.player = player;
            this.taskId = -1;
            this.update();
        }

        public final void run()
        {
            if (!can(this.player))
            {
                this.player.kickPlayer(getMessage());
            }
        }

        public final void update()
        {
            final long currentTime = System.currentTimeMillis();
            if (nextUpdate <= currentTime)
            {
                this.nextUpdate = currentTime + UPDATE_DELAY;
                if (this.taskId >= 0)
                {
                    scheduler.cancelTask(this.taskId);
                    this.taskId = -1;
                }
                this.taskId = scheduler.scheduleSyncDelayedTask(getPlugin(), this, timeout);
                if (this.taskId < 0)
                {
                    AntiGuest.error("Tracker for " + this.player.getName() + " failed to schedule!");
                }
            }
        }

        public final void cancel()
        {
            if (this.taskId >= 0)
            {
                scheduler.cancelTask(this.taskId);
            }
        }
    }
}
