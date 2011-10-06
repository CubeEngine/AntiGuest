package de.codeinfection.quickwango.AntiGuest;

import java.util.HashMap;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;
import org.bukkit.event.painting.PaintingBreakEvent;
import org.bukkit.event.painting.PaintingPlaceEvent;

/**
 *
 * @author CodeInfection
 */
public class AntiGuestEntityListener extends EntityListener
{
    protected final AntiGuest plugin;
    protected final HashMap<Player, Long> monsterTimestamps;

    public AntiGuestEntityListener(AntiGuest plugin)
    {
        this.plugin = plugin;
        this.monsterTimestamps = new HashMap<Player, Long>();
    }

    protected void noMonsterTargetingMessage(Player player)
    {
        Long lastTime = this.monsterTimestamps.get(player);
        long currentTime = System.currentTimeMillis();
        if (lastTime == null || lastTime + AntiGuest.messageWaitTime < currentTime)
        {
            this.plugin.message(player, "monster");
            this.monsterTimestamps.put(player, currentTime);
        }
    }

    @Override
    public void onEntityDamage(EntityDamageEvent event)
    {
        Player player = null;
        if (event.getCause() == EntityDamageEvent.DamageCause.STARVATION)
        {
            Entity entity = event.getEntity();
            if (entity instanceof Player)
            {
                player = (Player)entity;
                if (!this.plugin.can(player, "hunger"))
                {
                    event.setCancelled(true);
                    this.plugin.message(player, "hunger");
                }
            }
            return;
        }
        if (event instanceof EntityDamageByEntityEvent)
        {
            final Entity damager = ((EntityDamageByEntityEvent)event).getDamager();
            if (damager instanceof Player)
            {
                player = (Player)damager;
            }
            else if (damager instanceof Projectile)
            {
                final LivingEntity shooter = ((Projectile)damager).getShooter();
                if (shooter instanceof Player)
                {
                    player = (Player)shooter;
                }
            }
        }
        
        if (player != null && !this.plugin.can(player, "pvp"))
        {
            event.setCancelled(true);
            this.plugin.message(player, "pvp");
        }
    }

    @Override
    public void onPaintingPlace(PaintingPlaceEvent event)
    {
        final Player player = event.getPlayer();
        if (!this.plugin.can(player, "placeblock"))
        {
            this.plugin.message(player, "placeblock");
            event.setCancelled(true);
        }
    }

    @Override
    public void onPaintingBreak(PaintingBreakEvent event)
    {
        if (event instanceof PaintingBreakByEntityEvent)
        {
            final Entity remover = ((PaintingBreakByEntityEvent)event).getRemover();
            if (remover instanceof Player)
            {
                final Player player = (Player)remover;
                if (!this.plugin.can(player, "breakblock"))
                {
                    event.setCancelled(true);
                    this.plugin.message(player, "breakblock");
                }
            }
        }
    }

    @Override
    public void onEntityTarget(EntityTargetEvent event)
    {
        Entity targetEntity = event.getTarget();
        if (event.getEntity() instanceof Monster && targetEntity != null && targetEntity instanceof Player)
        {
            final Player player = (Player)targetEntity;
            if (!this.plugin.can(player, "monster"))
            {
                event.setCancelled(true);
                this.noMonsterTargetingMessage(player);
            }
        }
    }
}
