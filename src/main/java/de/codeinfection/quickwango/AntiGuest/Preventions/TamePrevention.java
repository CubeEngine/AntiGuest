package de.codeinfection.quickwango.AntiGuest.Preventions;

import de.codeinfection.quickwango.AntiGuest.AntiGuest;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityTameEvent;

/**
 *
 * @author Phillip
 */
public class TamePrevention extends Prevention
{

    public TamePrevention()
    {
        super("tame", AntiGuest.getInstance());
    }

    @Override
    public ConfigurationSection getDefaultConfig()
    {
        ConfigurationSection config = super.getDefaultConfig();

        config.set("message", "&4You are not allowed to tame animals!");

        return config;
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void handle(EntityTameEvent event)
    {
        final AnimalTamer tamer = event.getOwner();
        if (tamer instanceof Player)
        {
            prevent(event, (Player)tamer);
        }
    }
}
