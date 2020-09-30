package de.cubeisland.antiguest.prevention.preventions;

import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitScheduler;

import de.cubeisland.antiguest.AntiGuest;
import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;
import gnu.trove.map.hash.THashMap;

/**
 * Prevents idling players
 *
 * @author Phillip Schichtel
 */
public class AfkPrevention extends Prevention {
    private THashMap<Player, PlayerAfkTracker> trackerMap;
    private BukkitScheduler scheduler;
    private int timeout;

    public AfkPrevention(PreventionPlugin plugin) {
        super("afk", plugin, false, true);
        trackerMap = null;
    }

    @Override
    public Configuration getDefaultConfig() {
        Configuration config = super.getDefaultConfig();

        config.set("timeout", 60 * 10);

        return config;
    }

    @Override
    public void enable() {
        super.enable();
        scheduler = getPlugin().getServer().getScheduler();
        timeout = getConfig().getInt("timeout") * 20;

        trackerMap = new THashMap<Player, PlayerAfkTracker>();
    }

    @Override
    public void disable() {
        super.disable();
        for (PlayerAfkTracker tracker : trackerMap.values())
            tracker.cancel();
        trackerMap.clear();
        trackerMap = null;
    }

    public void updateTracker(final Player player) {
        final PlayerAfkTracker tracker = trackerMap.get(player);
        if (tracker != null)
            tracker.update();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void join(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (!can(player))
            trackerMap.put(player, new PlayerAfkTracker(player));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void quit(PlayerQuitEvent event) {
        final PlayerAfkTracker tracker = trackerMap.remove(event.getPlayer());
        if (tracker != null)
            tracker.cancel();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void moveUpdater(PlayerMoveEvent event) {
        final Location from = event.getFrom();
        final Location to = event.getTo();
        if (from.getBlockX() == to.getBlockX() && from.getBlockY() == to.getBlockY() && from.getBlockZ() == to.getBlockZ())
            return;
        updateTracker(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void chatUpdater(PlayerCommandPreprocessEvent event) {
        updateTracker(event.getPlayer());
    }

    private class PlayerAfkTracker implements Runnable {
        private final static int UPDATE_DELAY = 250;
        private final Player player;
        private int taskId;
        private long nextUpdate;

        public PlayerAfkTracker(Player player) {
            this.player = player;
            taskId = -1;
            update();
        }

        @Override
        public final void run() {
            if (!can(player)) {
                logViolation(player);
                player.kickPlayer(getMessage());
            }
        }

        public final void update() {
            final long currentTime = System.currentTimeMillis();
            if (nextUpdate <= currentTime) {
                nextUpdate = currentTime + UPDATE_DELAY;
                if (taskId >= 0) {
                    scheduler.cancelTask(taskId);
                    taskId = -1;
                }
                taskId = scheduler.scheduleSyncDelayedTask(getPlugin(), this, timeout);
                if (taskId < 0)
                    AntiGuest.error("Tracker for " + player.getName() + " failed to schedule!");
            }
        }

        public final void cancel() {
            if (taskId >= 0)
                scheduler.cancelTask(taskId);
        }
    }
}
