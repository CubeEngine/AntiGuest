package de.codeinfection.quickwango.AntiGuest;

import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author CodeInfection
 */
public abstract class Prevention implements Listener
{
    private final String name;
    private final Permission permission;
    private String message;
    private int messageDelay;
    private final Plugin plugin;
    private boolean enabled;

    private final HashMap<Player, Long> throttleTimestamps;
    
    public Prevention(final String name, final Plugin plugin)
    {
        this(name, "antiguest.preventions." + name, plugin);
    }

    public Prevention(final String name, final String permission, final Plugin plugin)
    {
        this.name = name;
        this.permission = new Permission(permission, PermissionDefault.OP);
        this.throttleTimestamps = new HashMap<Player, Long>(0);
        this.message = null;
        this.messageDelay = 0;
        this.plugin = plugin;
        this.enabled = false;
    }

    public static String parseColors(final String string)
    {
        return string.replaceAll("(?i)&([a-fk0-9])", ChatColor.COLOR_CHAR + "$1");
    }

    public ConfigurationSection getDefaultConfig()
    {
        ConfigurationSection defaultConfig = new MemoryConfiguration();

        defaultConfig.set("enable", false);
        defaultConfig.set("message", "&4You are not allowed to do this.");

        return defaultConfig;
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
                this.message = parseColors(this.message);
            }
        }
    }

    public void disable()
    {
        this.throttleTimestamps.clear();
    }

    public final boolean isEnabled()
    {
        return this.enabled;
    }

    public final void setEnabled(boolean enable)
    {
        this.enabled = enable;
    }

    public String getName()
    {
        return this.name;
    }

    public Permission getPermission()
    {
        return this.permission;
    }

    public String getMessage()
    {
        return this.message;
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
        AntiGuest.debug("Checking permission: " + this.permission.getName());
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
    
    public boolean prevent(final Cancellable event, final Player player)
    {
        if (!this.can(player))
        {
            event.setCancelled(true);
            this.sendMessage(player);
            return true;
        }
        return false;
    }
    
    public boolean preventThrottled(final Cancellable event, final Player player)
    {
        if (!this.can(player))
        {
            event.setCancelled(true);
            this.sendThrottledMessage(player);
            return true;
        }
        return false;
    }
}

