package de.cubeisland.antiguest.prevention.preventions;

import java.util.concurrent.TimeUnit;

import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;
import gnu.trove.map.hash.TObjectLongHashMap;

/**
 * Prevents spamming
 *
 * @author Phillip Schichtel
 */
public class SpamPrevention extends Prevention {
    private long spamLockDuration;
    private TObjectLongHashMap<String> chatTimestamps;

    public SpamPrevention(PreventionPlugin plugin) {
        super("spam", plugin);
        setEnableByDefault(true);
        setEnablePunishing(true);
    }

    @Override
    public String getConfigHeader() {
        return super.getConfigHeader() + "Configuration info:\n" + "    lockDuration: the time in seconds a player has to wait between messages";
    }

    @Override
    public Configuration getDefaultConfig() {
        Configuration config = super.getDefaultConfig();

        config.set("lockDuration", 2);

        return config;
    }

    @Override
    public void enable() {
        super.enable();
        spamLockDuration = TimeUnit.SECONDS.toMillis(getConfig().getLong("lockDuration"));
        chatTimestamps = new TObjectLongHashMap<String>();
    }

    @Override
    public void disable() {
        super.disable();
        chatTimestamps.clear();
        chatTimestamps = null;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void chat(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        if (!can(player))
            if (isChatLocked(player))
                prevent(event, player);
            else
                setChatLock(player);
    }

    private synchronized void setChatLock(final Player player) {
        chatTimestamps.put(player.getName(), System.currentTimeMillis() + spamLockDuration);
    }

    private synchronized boolean isChatLocked(final Player player) {
        final long nextPossible = chatTimestamps.get(player.getName());
        if (nextPossible == 0)
            return false;

        final long currentTime = System.currentTimeMillis();
        if (nextPossible < currentTime)
            return false;
        chatTimestamps.remove(player.getName());
        return true;
    }
}
