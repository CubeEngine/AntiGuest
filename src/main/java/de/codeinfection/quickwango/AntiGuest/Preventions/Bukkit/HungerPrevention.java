package de.codeinfection.quickwango.AntiGuest.Preventions.Bukkit;

import de.codeinfection.quickwango.AntiGuest.AntiGuestBukkit;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.FoodLevelChangeEvent;

/**
 * Prevents hunger
 *
 * @author Phillip Schichtel
 */
public class HungerPrevention extends Prevention
{
    public HungerPrevention()
    {
        super("hunger", AntiGuestBukkit.getInstance());
    }

    @Override
    public Configuration getDefaultConfig()
    {
        Configuration config = super.getDefaultConfig();

        config.set("message", "");

        return config;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(FoodLevelChangeEvent event)
    {
        final Entity entity = event.getEntity();
        if (entity instanceof Player)
        {
            prevent(event, (Player)entity);
        }
    }
}
