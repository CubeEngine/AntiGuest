package de.cubeisland.AntiGuest.Preventions;

import de.cubeisland.AntiGuest.Prevention;
import de.cubeisland.AntiGuest.PreventionPlugin;
import java.util.HashMap;
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
    private HashMap<Player, Long> chatTimestamps;

    public SpamPrevention(PreventionPlugin plugin)
    {
        super("spam", plugin, true);
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
        this.chatTimestamps = new HashMap<Player, Long>();
    }

    @Override
    public void disable()
    {
        super.disable();
        this.chatTimestamps = null;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void handle(PlayerChatEvent event)
    {
        final Player player = event.getPlayer();
        if (!can(player))
        {
            if (isChatLocked(player))
            {
                sendMessage(player);
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
        final Long nextPossible = this.chatTimestamps.get(player);
        if (nextPossible == null)
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
