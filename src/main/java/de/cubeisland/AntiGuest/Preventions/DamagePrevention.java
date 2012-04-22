package de.cubeisland.AntiGuest.Preventions;

import de.cubeisland.AntiGuest.AntiGuest;
import static de.cubeisland.AntiGuest.AntiGuest._;
import de.cubeisland.AntiGuest.FilteredPrevention;
import de.cubeisland.AntiGuest.PreventionPlugin;
import java.util.Collection;
import java.util.HashSet;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PotionSplashEvent;

/**
 * Prevents damage
 *
 * @author Phillip Schichtel
 */
public class DamagePrevention extends FilteredPrevention
{
    private String damagerMessage;
    private boolean preventPotions;
    private String potionMessage;

    public DamagePrevention(PreventionPlugin plugin)
    {
        super("damage", plugin);
        this.damagerMessage = null;
    }

    @Override
    public Configuration getDefaultConfig()
    {
        Configuration config = super.getDefaultConfig();

        config.set("throttleDelay", 3);
        config.set("damagerMessage", _("damagerMessage"));
        config.set("preventPotions", true);
        config.set("potionMessage", _("potionMessage"));
        config.set("mode", "whitelist");
        config.set("list", new String[]{"void"});

        return config;
    }

    @Override
    public void enable()
    {
        super.enable();
        final Configuration config = getConfig();

        this.damagerMessage = parseMessage(config.getString("damagerMessage"));
        this.preventPotions = config.getBoolean("preventPotions");
        this.potionMessage = parseMessage(config.getString("potionMessage"));

        HashSet<Object> newList = new HashSet<Object>();
        String itemString;
        for (Object item : this.filterItems)
        {
            itemString = String.valueOf(item).trim().replace(" ", "_").toUpperCase();
            try
            {
                newList.add(DamageCause.valueOf(itemString));
            }
            catch (IllegalArgumentException e)
            {}
        }
        this.filterItems = newList;
    }

    @Override
    public void disable()
    {
        super.disable();
        this.damagerMessage = null;
    }

    private boolean prevent(final Cancellable event, final Player player, final DamageCause cause)
    {
        if (!this.can(player, cause))
        {
            if (cause == DamageCause.LAVA || cause == DamageCause.FIRE || cause == DamageCause.FIRE_TICK || cause == DamageCause.VOID)
            {
                this.sendThrottledMessage(player);
            }
            else
            {
                this.sendMessage(player);
            }
            event.setCancelled(true);
            return true;
        }
        return false;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(EntityDamageEvent event)
    {
        final Entity entity = event.getEntity();
        if (entity instanceof Player)
        {
            final Player player = (Player)entity;
            if (prevent(event, player, event.getCause()) && this.damagerMessage != null)
            {
                player.setFireTicks(0);
                if (event instanceof EntityDamageByEntityEvent)
                {
                    final Entity damager = ((EntityDamageByEntityEvent)event).getDamager();
                    if (damager instanceof Player)
                    {
                        ((Player)damager).sendMessage(this.damagerMessage);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(PotionSplashEvent event)
    {
        if (!this.preventPotions)
        {
            return;
        }
        Collection<LivingEntity> affectedEntities = event.getAffectedEntities();
        Player affectedPlayer;
        Entity shooter = event.getPotion().getShooter();
        int affectedCount = 0;
        for (LivingEntity entity : affectedEntities)
        {
            if (entity instanceof Player)
            {
                affectedPlayer = (Player)entity;
                if (!can(affectedPlayer))
                {
                    ++affectedCount;
                    affectedEntities.remove(entity);
                    if (this.potionMessage != null)
                    {
                        affectedPlayer.sendMessage(this.potionMessage);
                    }
                }
            }
        }
        if (affectedCount > 0 && this.damagerMessage != null && shooter != null && shooter instanceof Player)
        {
            ((Player)shooter).sendMessage(this.damagerMessage);
        }
    }
}
