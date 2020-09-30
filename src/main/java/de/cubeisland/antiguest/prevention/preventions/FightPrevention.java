package de.cubeisland.antiguest.prevention.preventions;

import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Ambient;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;

/**
 * Prevents PVP
 *
 * @author Phillip Schichtel
 */
public class FightPrevention extends Prevention {
    private boolean players;
    private boolean monsters;
    private boolean animals;

    public FightPrevention(PreventionPlugin plugin) {
        super("fight", plugin);
        setEnableByDefault(true);
        setEnablePunishing(true);
    }

    @Override
    public void enable() {
        super.enable();

        players = getConfig().getBoolean("prevent.players");
        monsters = getConfig().getBoolean("prevent.monsters");
        animals = getConfig().getBoolean("prevent.animals");

        if (getConfig().contains("checkAndPrevent")) {
            if (getConfig().contains("checkAndPrevent.players")) {
                players = getConfig().getBoolean("checkAndPrevent.players");
                getConfig().set("prevent.players", players);
            }
            if (getConfig().contains("checkAndPrevent.monsters")) {
                monsters = getConfig().getBoolean("checkAndPrevent.monsters");
                getConfig().set("prevent.players", monsters);
            }
            if (getConfig().contains("checkAndPrevent.animals")) {
                animals = getConfig().getBoolean("checkAndPrevent.animals");
                getConfig().set("prevent.players", animals);
            }
            getConfig().set("checkAndPrevent", null);
            getConfig().safeSave();
        }
    }

    @Override
    public Configuration getDefaultConfig() {
        Configuration defaultConfig = super.getDefaultConfig();

        defaultConfig.set("prevent.players", true);
        defaultConfig.set("prevent.monsters", true);
        defaultConfig.set("prevent.animals", true);

        return defaultConfig;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void damageByEntity(EntityDamageByEntityEvent event) {
        final Entity damager = event.getDamager();
        if (damager instanceof Player)
            preventDamage(event, (Player) damager);
        else if (damager instanceof Projectile) {
            final ProjectileSource shooter = ((Projectile) damager).getShooter();
            if (shooter instanceof Player)
                preventDamage(event, (Player) shooter);
        }
    }

    public boolean preventDamage(EntityDamageByEntityEvent event, Player player) {
        Entity target = event.getEntity();
        if (target instanceof Player && players || target instanceof Monster && monsters || (target instanceof Animals || target instanceof Ambient) && animals)
            return checkAndPrevent(event, player);
        return false;
    }
}
