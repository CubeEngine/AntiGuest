package de.codeinfection.quickwango.AntiGuest.Preventions;

import de.codeinfection.quickwango.AntiGuest.AntiGuest;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerVelocityEvent;

/**
 *
 * @author Phillip
 */
public class PushPrevention extends Prevention
{

    public PushPrevention()
    {
        super("push", AntiGuest.getInstance());
    }

    @Override
    public ConfigurationSection getDefaultConfig()
    {
        ConfigurationSection config = super.getDefaultConfig();

        config.set("message", "&4You are not allowed to push other players!");

        return config;
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void handle(PlayerVelocityEvent event)
    {
        AntiGuest.debug("Pushing: " + event.getPlayer().getName());
        prevent(event, event.getPlayer());
    }
}
