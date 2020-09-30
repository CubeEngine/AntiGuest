package de.cubeisland.antiguest.prevention.preventions;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.projectiles.ProjectileSource;

import de.cubeisland.antiguest.prevention.FilteredPrevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;

/**
 * Prevents damage
 *
 * @author Phillip Schichtel
 */
public class DamagePrevention extends FilteredPrevention<DamageCause> {
    private String damagerMessage;
    private boolean preventPotions;
    private String potionMessage;

    public DamagePrevention(PreventionPlugin plugin) {
        super("damage", plugin, false);
        setThrottleDelay(3, TimeUnit.SECONDS);
        setFilterMode(FilterMode.WHITELIST);
        setFilterItems(EnumSet.of(DamageCause.VOID));
        damagerMessage = null;
    }

    @Override
    public String getConfigHeader() {
        return super.getConfigHeader() + "\n" + "Configuration info:\n" + "    damagerMessage: this message will be send to the player who attacked a guest\n" + "    preventPostions: if this is enabled potion effects also get prevented\n" + "    postionMessage: this will be send to players protected from postions\n";
    }

    @Override
    public Configuration getDefaultConfig() {
        Configuration config = super.getDefaultConfig();

        config.set("damagerMessage", getPlugin().getTranslation().translate("damagerMessage"));
        config.set("preventPotions", true);
        config.set("potionMessage", getPlugin().getTranslation().translate("potionMessage"));

        return config;
    }

    @Override
    public List<String> encodeSet(Set<DamageCause> set) {
        List<String> damageCauses = super.encodeSet(set);

        for (int i = 0; i < damageCauses.size(); ++i)
            damageCauses.set(i, damageCauses.get(i).toLowerCase().replace('_', ' '));

        return damageCauses;
    }

    @Override
    public Set<DamageCause> decodeList(List<String> list) {
        Set<DamageCause> damageCauses = EnumSet.noneOf(DamageCause.class);
        for (String entry : list)
            try {
                damageCauses.add(DamageCause.valueOf(entry.trim().replace(" ", "_").toUpperCase()));
            } catch (IllegalArgumentException ignored) {
            }
        return damageCauses;
    }

    @Override
    public void enable() {
        super.enable();
        final Configuration config = getConfig();

        damagerMessage = parseMessage(config.getString("damagerMessage"));
        preventPotions = config.getBoolean("preventPotions");
        potionMessage = parseMessage(config.getString("potionMessage"));
    }

    @Override
    public void disable() {
        super.disable();
        damagerMessage = null;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void damage(EntityDamageEvent event) {
        final Entity entity = event.getEntity();
        if (entity instanceof Player) {
            final Player player = (Player) entity;
            if (checkAndPrevent(event, player, event.getCause()) && damagerMessage != null) {
                player.setFireTicks(0);
                if (event instanceof EntityDamageByEntityEvent) {
                    final Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
                    if (damager instanceof Player)
                        ((Player) damager).sendMessage(damagerMessage);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void splash(PotionSplashEvent event) {
        if (!preventPotions)
            return;
        Collection<LivingEntity> affectedEntities = event.getAffectedEntities();
        Player affectedPlayer;
        ProjectileSource shooter = event.getPotion().getShooter();
        int affectedCount = 0;

        Iterator<LivingEntity> iter = affectedEntities.iterator();
        LivingEntity entity;
        while (iter.hasNext()) {
            entity = iter.next();
            if (entity instanceof Player) {
                affectedPlayer = (Player) entity;
                if (!can(affectedPlayer)) {
                    ++affectedCount;
                    iter.remove();
                    if (potionMessage != null)
                        affectedPlayer.sendMessage(potionMessage);
                }
            }
        }
        if (affectedCount > 0 && damagerMessage != null && shooter != null && shooter instanceof Player)
            ((Player) shooter).sendMessage(damagerMessage);
    }
}
