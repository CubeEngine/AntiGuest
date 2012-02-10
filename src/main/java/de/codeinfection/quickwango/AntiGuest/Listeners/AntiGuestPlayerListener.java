package de.codeinfection.quickwango.AntiGuest.Listeners;

import de.codeinfection.quickwango.AntiGuest.AntiGuest;
import de.codeinfection.quickwango.AntiGuest.PlayerState;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import de.codeinfection.quickwango.AntiGuest.Preventions.ActionPrevention;
import java.util.HashMap;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
/**
 *
 * @author CodeInfection
 */
public class AntiGuestPlayerListener implements Listener
{
    private final static Prevention monsterPrev     = AntiGuest.preventions.get("monster");
    private final static Prevention chatPrev        = AntiGuest.preventions.get("chat");
    private final static Prevention spamPrev        = AntiGuest.preventions.get("spam");
    private final static Prevention fishPrev        = AntiGuest.preventions.get("fish");
    private final static Prevention pickupPrev      = AntiGuest.preventions.get("pickup");
    private final static Prevention dropPrev        = AntiGuest.preventions.get("drop");
    private final static Prevention lavabucketPrev  = AntiGuest.preventions.get("lavabucket");
    private final static Prevention waterbucketPrev = AntiGuest.preventions.get("waterbucket");
    private final static Prevention bedPrev         = AntiGuest.preventions.get("bed");
    private final static Prevention pvpPrev         = AntiGuest.preventions.get("pvp");
    private final static Prevention hungerPrev      = AntiGuest.preventions.get("hunger");

    
    private static final HashMap<Player, Long> chatTimestamps = new HashMap<Player, Long>();
    private int spamLockDuration = 0;

    public AntiGuestPlayerListener()
    {
        if (spamPrev != null)
        {
            this.spamLockDuration = ((ActionPrevention)spamPrev).getConfig().getInt("lockDuration") * 1000;
        }
    }

    private boolean isPlayerChatLocked(Player player)
    {
        Long lastTime = this.chatTimestamps.get(player);
        long currentTime = System.currentTimeMillis();
        if (lastTime == null || lastTime + this.spamLockDuration < currentTime)
        {
            this.chatTimestamps.put(player, currentTime);
            return false;
        }
        else
        {
            return true;
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerPickupItem(PlayerPickupItemEvent event)
    {
        if (event.isCancelled() || pickupPrev == null) return;
        
        final Player player = event.getPlayer();
        if (!pickupPrev.can(player))
        {
            event.setCancelled(true);
            pickupPrev.sendThrottledMessage(player);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(PlayerChatEvent event)
    {
        if (event.isCancelled() || chatPrev == null) return;

        final Player player = event.getPlayer();

        if (!PlayerState.has(player, PlayerState.CHATTED))
        {
            AntiGuest.getInstance().debug("Ignoring the first chat event after connect");
            PlayerState.set(player, PlayerState.CHATTED);
            return;
        }

        if (!chatPrev.can(player))
        {
            event.setCancelled(true);
            chatPrev.sendMessage(player);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerSpam(PlayerChatEvent event)
    {
        if (event.isCancelled() || spamPrev == null) return;
        final Player player = event.getPlayer();
        if (chatPrev != null && !chatPrev.can(player)) return;
            
        if (this.isPlayerChatLocked(player))
        {
            event.setCancelled(true);
            spamPrev.sendMessage(player);
        }
    }

    private void handleBucketEvent(final PlayerBucketEvent event)
    {
        if (event.isCancelled()) return;

        Prevention prevention;
        switch (event.getBucket())
        {
            case LAVA_BUCKET:
                prevention = lavabucketPrev;
                break;
            case WATER_BUCKET:
                prevention = waterbucketPrev;
                break;
            default:
                AntiGuest.getInstance().log("Unknown bucket!");
                return;
        }

        if (prevention == null) return;

        final Player player = event.getPlayer();
        if (!prevention.can(player))
        {
            event.setCancelled(true);
            prevention.sendMessage(player);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerBucketFill(PlayerBucketFillEvent event)
    {
        this.handleBucketEvent(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event)
    {
        this.handleBucketEvent(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDropItem(PlayerDropItemEvent event)
    {
        if (event.isCancelled() || dropPrev == null) return;

        final Player player = event.getPlayer();
        if (!dropPrev.can(player))
        {
            event.setCancelled(true);
            dropPrev.sendMessage(player);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerBedEnter(PlayerBedEnterEvent event)
    {
        if (event.isCancelled() || bedPrev == null) return;

        final Player player = event.getPlayer();
        if (bedPrev.can(player))
        {
            event.setCancelled(true);
            bedPrev.sendMessage(player);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerFish(PlayerFishEvent event)
    {
        if (event.isCancelled() || fishPrev == null) return;

        final Player player = event.getPlayer();
        if (!fishPrev.can(player))
        {
            event.setCancelled(true);
            fishPrev.sendMessage(player);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageEvent event)
    {
        if (event.isCancelled()) return;
        
        Prevention prevention = null;
        Player player = null;
        if (event.getCause() == EntityDamageEvent.DamageCause.STARVATION)
        {
            Entity entity = event.getEntity();
            if (entity instanceof Player)
            {
                player = (Player)entity;
                prevention = hungerPrev;
            }
        }
        if (event instanceof EntityDamageByEntityEvent)
        {
            final Entity damager = ((EntityDamageByEntityEvent)event).getDamager();
            prevention = pvpPrev;
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
        
        if (prevention != null && player != null && !prevention.can(player))
        {
            event.setCancelled(true);
            prevention.sendMessage(player);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityTarget(EntityTargetEvent event)
    {
        if (event.isCancelled() || monsterPrev == null) return;
        
        Entity targetEntity = event.getTarget();
        if (event.getEntity() instanceof Monster && targetEntity != null && targetEntity instanceof Player)
        {
            final Player player = (Player)targetEntity;
            if (!monsterPrev.can(player))
            {
                monsterPrev.sendThrottledMessage(player);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler()
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        PlayerState.removePlayer(event.getPlayer());
    }
}
