package de.codeinfection.quickwango.AntiGuest.Preventions.Bukkit;

import de.codeinfection.quickwango.AntiGuest.AntiGuestBukkit;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityTameEvent;

/**
 * Prevents taming
 *
 * @author Phillip Schichtel
 */
public class TamePrevention extends Prevention
{

    public TamePrevention()
    {
        super("tame", AntiGuestBukkit.getInstance());
    }

    @Override
    public Configuration getDefaultConfig()
    {
        Configuration config = super.getDefaultConfig();

        config.set("message", "&4You are not allowed to tame animals!");

        return config;
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(EntityTameEvent event)
    {
        if (event.getOwner() instanceof Player)
        {
            prevent(event, (Player)event.getOwner());
        }
    }
}
