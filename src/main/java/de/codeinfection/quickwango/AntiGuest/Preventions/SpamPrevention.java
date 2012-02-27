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
 * Prevents spamming
 *
 * @author Phillip Schichtel
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
    public ConfigurationSection getDefaultConfig()
    {
        ConfigurationSection config = super.getDefaultConfig();

        config.set("message", "&4Don't spam the chat!");
        config.set("lockDuration", 2);

        return config;
    }
    
    @Override
    public void enable(Server server, ConfigurationSection config)
    {
        super.enable(server, config);
        this.spamLockDuration = config.getInt("lockDuration");
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
