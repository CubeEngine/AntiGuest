package de.codeinfection.quickwango.AntiGuest.Preventions;

import de.codeinfection.quickwango.AntiGuest.Prevention;
import org.bukkit.configuration.ConfigurationSection;

/**
 *
 * @author CodeInfection
 */
public class ActionPrevention extends Prevention
{
    private ConfigurationSection config;

    public ActionPrevention(final String name, final String message, final int messageDelay)
    {
        this(name, message, messageDelay, null);
    }

    public ActionPrevention(final String name, final String message, final int messageDelay, ConfigurationSection config)
    {
        this(name, "antiguest.preventions.actions." + name.toLowerCase(), message, messageDelay);
        this.config = config;
    }

    public ActionPrevention(final String name, final String permission, final String message, final int messageDelay)
    {
        super(name, permission, message, messageDelay);
    }

    public ConfigurationSection getConfig()
    {
        return this.config;
    }
}
