package de.cubeisland.AntiGuest.prevention.preventions;

import de.cubeisland.AntiGuest.prevention.Prevention;
import de.cubeisland.AntiGuest.prevention.PreventionPlugin;
import gnu.trove.map.hash.TObjectLongHashMap;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChatEvent;

/**
 * Prevents spamming
 *
 * @author Phillip Schichtel
 */
public class SpamPrevention extends Prevention
{
    private int spamLockDuration;
    private TObjectLongHashMap<Player> chatTimestamps;

    public SpamPrevention(PreventionPlugin plugin)
    {
        super("spam", plugin);
        setEnableByDefault(true);
        setEnablePunishing(true);
    }

    @Override
    public String getConfigHeader()
    {
        return super.getConfigHeader() +
                "Configuration info:\n" +
                "    lockDuration: the time in seconds a player has to wait between messages";
    }

    @Override
    public Configuration getDefaultConfig()
    {
        Configuration config = super.getDefaultConfig();

        config.set("lockDuration", 2);

        return config;
    }
    
    @Override
    public void enable()
    {
        super.enable();
        this.spamLockDuration = getConfig().getInt("lockDuration") * 1000;
        this.chatTimestamps = new TObjectLongHashMap<Player>();
    }

    @Override
    public void disable()
    {
        super.disable();
        this.chatTimestamps = null;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void chat(PlayerChatEvent event)
    {
        final Player player = event.getPlayer();
        if (!can(player))
        {
            if (isChatLocked(player))
            {
                sendMessage(player);
                punish(player);
                event.setCancelled(true);
            }
            else
            {
                setChatLock(player);
            }
        }
    }

    private void setChatLock(final Player player)
    {
        this.chatTimestamps.put(player, System.currentTimeMillis() + this.spamLockDuration);
    }
    
    private boolean isChatLocked(final Player player)
    {
        final long nextPossible = this.chatTimestamps.get(player);
        if (nextPossible == 0)
        {
            return false;
        }

        final long currentTime = System.currentTimeMillis();
        if (nextPossible < currentTime)
        {
            return false;
        }
        return true;
    }
}
