package de.codeinfection.quickwango.AntiGuest;

import java.util.HashMap;
import org.bukkit.entity.Player;

/**
 *
 * @author CodeInfection
 */
public class Prevention
{
    private final String name;
    private final String permission;
    private final String message;
    private final int messageDelay;

    private final HashMap<Player, Long> throttleTimestamps;

    public Prevention(final String name, final String message)
    {
        this(name, name, message);
    }

    public Prevention(final String name, final String message, final int messageDelay)
    {
        this(name, "antiguest.preventions." + name.toLowerCase(), message, messageDelay);
    }

    public Prevention(final String name, final String permission, final String message)
    {
        this(name, name, message, -1);
    }

    public Prevention(final String name, final String permission, final String message, final int messageDelay)
    {
        this.name = name;
        this.permission = permission;
        this.message = message;
        this.throttleTimestamps = new HashMap<Player, Long>();
        this.messageDelay = messageDelay;
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
        long current = System.currentTimeMillis();
        
        if (last == null || last + (delay * 1000) < current)
        {
            this.sendMessage(player);
            this.throttleTimestamps.put(player, current);
        }
    }
}
