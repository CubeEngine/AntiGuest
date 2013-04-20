package de.cubeisland.AntiGuest.prevention.preventions;

import de.cubeisland.AntiGuest.prevention.Prevention;
import de.cubeisland.AntiGuest.prevention.PreventionPlugin;

import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Ambient;
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

        if (getConfig().contains("checkAndPrevent"))
        {
            if (getConfig().contains("checkAndPrevent.players"))
            {
                this.players = getConfig().getBoolean("checkAndPrevent.players");
                getConfig().set("prevent.players", this.players);
            }
            if (getConfig().contains("checkAndPrevent.monsters"))
            {
                this.monsters = getConfig().getBoolean("checkAndPrevent.monsters");
                getConfig().set("prevent.players", this.monsters);
            }
            if (getConfig().contains("checkAndPrevent.animals"))
            {
                this.animals = getConfig().getBoolean("checkAndPrevent.animals");
                getConfig().set("prevent.players", this.animals);
            }
            getConfig().set("checkAndPrevent", null);
            getConfig().safeSave();
        }
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
    public void damageByEntity(EntityDamageByEntityEvent event)
    {
        final Entity damager = event.getDamager();
        if (damager instanceof Player)
        {
            preventDamage(event, (Player) damager);
        }
        else if (damager instanceof Projectile)
        {
            final LivingEntity shooter = ((Projectile)damager).getShooter();
            if (shooter instanceof Player)
            {
                preventDamage(event, (Player) shooter);
            }
        }
    }

    public boolean preventDamage(EntityDamageByEntityEvent event, Player player)
    {
        Entity target = event.getEntity();
        if ((target instanceof Player && this.players) ||
            (target instanceof Monster && this.monsters) ||
            ((target instanceof Animals || target instanceof Ambient) && this.animals))
        {
            return checkAndPrevent(event, player);
        }
        return false;
    }
}
