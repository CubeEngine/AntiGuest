package de.codeinfection.quickwango.AntiGuest.Preventions.Bukkit;

import de.codeinfection.quickwango.AntiGuest.AntiGuestBukkit;
import de.codeinfection.quickwango.AntiGuest.Prevention;
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
        super("bow", AntiGuestBukkit.getInstance(), true);
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
