package de.codeinfection.quickwango.AntiGuest.Preventions.Bukkit;

import de.codeinfection.quickwango.AntiGuest.AntiGuestBukkit;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
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
    public ConfigurationSection getDefaultConfig()
    {
        ConfigurationSection config = super.getDefaultConfig();

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
