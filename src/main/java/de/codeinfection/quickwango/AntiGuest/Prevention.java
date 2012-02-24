package de.codeinfection.quickwango.AntiGuest;

import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author CodeInfection
 */
public abstract class Prevention implements Listener
{
    private final String name;
    private final String permission;
    private String message;
    private int messageDelay;
    private final Plugin plugin;

    private final HashMap<Player, Long> throttleTimestamps;
    
    public Prevention(final String name, final Plugin plugin)
    {
        this(name, "antiguest.preventions." + name, plugin);
    }

    public Prevention(final String name, final String permission, final Plugin plugin)
    {
        this.name = name;
        this.permission = permission;
        this.throttleTimestamps = new HashMap<Player, Long>(0);
        
        this.message = null;
        this.messageDelay = 0;
        
        this.plugin = plugin;
    }
    
    public void initialize(final Server server, final ConfigurationSection config)
    {
        this.messageDelay = config.getInt("messageDelay") * 1000;
        this.message = config.getString("message");
        if (this.message != null)
        {
            if (this.message.length() == 0)
            {
                this.message = null;
            }
            else
            {
                this.message = this.message.replaceAll("&([a-f0-9])", ChatColor.COLOR_CHAR + "$1");
            }
        }
    }

    public String getName()
    {
        return this.name;
    }

    public String getPermission()
    {
        return this.permission;
    }
    
    public Plugin getPlugin()
    {
        return this.plugin;
    }

    public int getMessageDelay()
    {
        return this.messageDelay;
    }

    public boolean can(final Player player)
    {
        AntiGuest.debug("Checking permission: " + this.permission);
        return player.hasPermission(this.permission);
    }

    public void sendMessage(final Player player)
    {
        if (this.message != null)
        {
            player.sendMessage(this.message);
        }
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
    
    public void prevent(final Cancellable event, final Player player)
    {
        if (!this.can(player))
        {
            event.setCancelled(true);
            this.sendMessage(player);
        }
    }
    
    public void preventThrottled(final Cancellable event, final Player player)
    {
        if (!this.can(player))
        {
            event.setCancelled(true);
            this.sendThrottledMessage(player);
        }
    }
}

