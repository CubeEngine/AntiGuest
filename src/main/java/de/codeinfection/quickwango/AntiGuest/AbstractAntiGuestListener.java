package de.codeinfection.quickwango.AntiGuest;

import org.bukkit.entity.Player;

/**
 *
 * @author CodeInfection
 */
public class AbstractAntiGuestListener
{
    private final AntiGuest plugin;

    public AbstractAntiGuestListener(final AntiGuest plugin)
    {
        this.plugin = plugin;
    }

    public final AntiGuest getPlugin()
    {
        return this.plugin;
    }

    public final boolean can(final Player player, final String preventionName)
    {
        final Prevention prevention = AntiGuest.preventions.get(preventionName);
        if (prevention == null)
        {
            throw new UnknownPreventionException();
        }
        return can(player, prevention);
    }

    public final boolean can(final Player player, final Prevention prevention)
    {
        return player.hasPermission(prevention.permission);
    }

    public final void sendMessage(final Player player, final String preventionName)
    {
        final Prevention prevention = AntiGuest.preventions.get(preventionName);
        if (prevention == null)
        {
            throw new UnknownPreventionException();
        }
        sendMessage(player, prevention);
    }

    public final void sendMessage(final Player player, final Prevention prevention)
    {
        player.sendMessage(prevention.message);
    }

    public final boolean enabled(final String preventionName)
    {
        return AntiGuest.preventions.containsKey(preventionName);
    }
}
