package de.codeinfection.quickwango.AntiGuest.Preventions.Bukkit;

import de.codeinfection.quickwango.AntiGuest.AntiGuestBukkit;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;

/**
 * Prevents bow usage
 *
 * @author Phillip Schichtel
 */
public class BowPrevention extends Prevention
{
    public BowPrevention()
    {
        super("bow", AntiGuestBukkit.getInstance());
    }

    @Override
    public Configuration getDefaultConfig()
    {
        Configuration config = super.getDefaultConfig();

        config.set("enable", true);
        config.set("message", "&4You are not allowed to shoot bows!");

        return config;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(EntityShootBowEvent event)
    {
        final Entity shooter = event.getEntity();
        if (shooter instanceof Player)
        {
            prevent(event, (Player)shooter);
        }
    }
}
