package de.codeinfection.quickwango.AntiGuest;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityListener;
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

    public AntiGuestEntityListener(AntiGuest plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public void onEntityDamage(EntityDamageEvent event)
    {
        Player player = null;
        if (event instanceof EntityDamageByProjectileEvent)
        {
            final LivingEntity shooter = ((EntityDamageByProjectileEvent)event).getProjectile().getShooter();
            if (shooter instanceof Player)
            {
                player = (Player)shooter;
            }
        }
        else if (event instanceof EntityDamageByEntityEvent)
        {
            final Entity damager = ((EntityDamageByEntityEvent)event).getDamager();
            if (damager instanceof Player)
            {
                player = (Player)damager;
            }
        }
        
        if (player != null && !this.plugin.can(player, "pvp"))
        {
            event.setCancelled(true);
            this.plugin.message(player, "pvp");
        }
    }

    @Override
    public void onEntityInteract(EntityInteractEvent event)
    {
        final Entity interactor = event.getEntity();
        if (interactor instanceof Player)
        {
            Player player = (Player)interactor;
            if (!this.plugin.can(player, "interact"))
            {
                this.plugin.message(player, "interact");
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void onPaintingPlace(PaintingPlaceEvent event)
    {
        final Player player = event.getPlayer();
        if (!this.plugin.can(player, "build"))
        {
            this.plugin.message(player, "build");
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
                if (!this.plugin.can(player, "build"))
                {
                    event.setCancelled(true);
                    this.plugin.message(player, "build");
                }
            }
        }
    }
}
