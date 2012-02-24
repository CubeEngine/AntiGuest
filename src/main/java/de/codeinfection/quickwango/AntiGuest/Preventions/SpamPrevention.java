package de.codeinfection.quickwango.AntiGuest.Preventions;

import de.codeinfection.quickwango.AntiGuest.AntiGuest;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import java.util.HashMap;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerChatEvent;

/**
 *
 * @author Phillip
 */
public class SpamPrevention extends Prevention
{
    private int spamLockDuration;
    private HashMap<Player, Long> chatTimestamps;

    public SpamPrevention()
    {
        super("spam", AntiGuest.getInstance());
    }
    
    @Override
    public void initialize(Server server, ConfigurationSection config)
    {
        super.initialize(server, config);
        this.spamLockDuration = config.getInt("lockDuration");
        this.chatTimestamps = new HashMap<Player, Long>();
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void handle(PlayerChatEvent event)
    {
        final Player player = event.getPlayer();
        if (isChatLocked(player))
        {
            prevent(event, event.getPlayer());
        }
    }
    
    private boolean isChatLocked(final Player player)
    {
        final Long lastTime = this.chatTimestamps.get(player);
        final long currentTime = System.currentTimeMillis();
        if (lastTime == null || lastTime + this.spamLockDuration < currentTime)
        {
            this.chatTimestamps.put(player, currentTime);
            return false;
        }
        return true;
    }
}
