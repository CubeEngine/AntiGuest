package de.codeinfection.quickwango.AntiGuest.Preventions.Bukkit;

import de.codeinfection.quickwango.AntiGuest.AntiGuestBukkit;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Prevents PVP
 *
 * @author Phillip Schichtel
 */
public class FightPrevention extends Prevention
{
    public FightPrevention()
    {
        super("fight", AntiGuestBukkit.getInstance());
    }

    @Override
    public Configuration getDefaultConfig()
    {
        Configuration config = super.getDefaultConfig();

        config.set("enable", true);
        config.set("message", "&4You are not allowed to fight!");

        return config;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(EntityDamageByEntityEvent event)
    {
        final Entity damager = event.getDamager();
        if (damager instanceof Player)
        {
            prevent(event, (Player)damager);
        }
        else if (damager instanceof Projectile)
        {
            final LivingEntity shooter = ((Projectile)damager).getShooter();
            if (shooter instanceof Player)
            {
                prevent(event, (Player)shooter);
            }
        }
    }
}
