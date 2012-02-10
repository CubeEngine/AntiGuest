package de.codeinfection.quickwango.AntiGuest;

import java.util.HashMap;
import org.bukkit.entity.Player;

/**
 *
 * @author CodeInfection
 */
public abstract class Prevention
{
    private final String name;
    private final String permission;
    private final String message;
    private final int messageDelay;

    private final HashMap<Player, Long> throttleTimestamps;

    public Prevention(final String name, final String permission, final String message, final int messageDelay)
    {
        this.name = name;
        this.permission = permission;
        this.message = message;
        this.messageDelay = messageDelay;
        this.throttleTimestamps = new HashMap<Player, Long>(0);
    }

    public String getName()
    {
        return this.name;
    }

    public String getPermission()
    {
        return this.permission;
    }

    public int getMessageDelay()
    {
        return this.messageDelay;
    }

    public boolean can(final Player player)
    {
        return player.hasPermission(this.permission);
    }

    public void sendMessage(final Player player)
    {
        player.sendMessage(this.message);
    }

    public void sendThrottledMessage(final Player player)
    {
        this.sendThrottledMessage(player, this.messageDelay);
    }

    public void sendThrottledMessage(final Player player, final int delay)
    {
        Long last = this.throttleTimestamps.get(player);
        last = (last == null ? 0 : last);
        final long current = System.currentTimeMillis();
        
        if (last + delay < current)
        {
            this.sendMessage(player);
            this.throttleTimestamps.put(player, current);
        }
    }

    @Override
    public String toString()
    {
        return this.name;
    }
}
