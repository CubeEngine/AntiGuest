package de.cubeisland.AntiGuest.prevention.preventions;

import de.cubeisland.AntiGuest.prevention.Prevention;
import de.cubeisland.AntiGuest.prevention.PreventionPlugin;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
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
    private boolean players;
    private boolean monsters;
    private boolean animals;

    public FightPrevention(PreventionPlugin plugin)
    {
        super("fight", plugin);
        setEnableByDefault(true);
        setEnablePunishing(true);
    }

    @Override
    public void enable()
    {
        super.enable();

        this.players = getConfig().getBoolean("prevent.players");
        this.monsters = getConfig().getBoolean("prevent.monsters");
        this.animals = getConfig().getBoolean("prevent.animals");
    }

    @Override
    public Configuration getDefaultConfig()
    {
        Configuration defaultConfig = super.getDefaultConfig();

        defaultConfig.set("prevent.players", true);
        defaultConfig.set("prevent.monsters", true);
        defaultConfig.set("prevent.animals", true);

        return defaultConfig;
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

    public boolean prevent(EntityDamageByEntityEvent event, Player player)
    {
        Entity damageTarget = event.getEntity();
        if ((damageTarget instanceof Player && this.players) ||
            (damageTarget instanceof Monster && this.monsters) ||
            (damageTarget instanceof Animals && this.animals))
        {
            return super.prevent(event, player);
        }
        return false;
    }
}
