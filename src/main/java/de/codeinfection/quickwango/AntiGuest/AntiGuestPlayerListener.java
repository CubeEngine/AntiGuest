package de.codeinfection.quickwango.AntiGuest;

import java.util.HashMap;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.*;

/**
 *
 * @author CodeInfection
 */
public class AntiGuestPlayerListener implements Listener
{
    private static final HashMap<Player, Long> chatTimestamps    = new HashMap<Player, Long>();
//    private static final HashMap<Player, Long> pickupTimestamps  = new HashMap<Player, Long>();
//    private static final HashMap<Player, Long> monsterTimestamps = new HashMap<Player, Long>();


    private final static Prevention monsterPrev     = AntiGuest.preventions.get("monster");
    private final static Prevention chatPrev        = AntiGuest.preventions.get("chat");
    private final static Prevention spamPrev        = AntiGuest.preventions.get("spam");
    private final static Prevention movePrev        = AntiGuest.preventions.get("move");
    private final static Prevention sneakPrev       = AntiGuest.preventions.get("sneak");
    private final static Prevention sprintPrev      = AntiGuest.preventions.get("sprint");
    private final static Prevention fishPrev        = AntiGuest.preventions.get("fish");
    private final static Prevention pickupPrev      = AntiGuest.preventions.get("pickup");
    private final static Prevention dropPrev        = AntiGuest.preventions.get("drop");
    private final static Prevention lavabucketPrev  = AntiGuest.preventions.get("lavabucket");
    private final static Prevention waterbucketPrev = AntiGuest.preventions.get("waterbucket");
    private final static Prevention bedPrev         = AntiGuest.preventions.get("bed");
    private final static Prevention pvpPrev         = AntiGuest.preventions.get("pvp");
    private final static Prevention hungerPrev      = AntiGuest.preventions.get("hunger");


//    protected void noMonsterTargetingMessage(Player player)
//    {
//        Long lastTime = monsterTimestamps.get(player);
//        long currentTime = System.currentTimeMillis();
//        if (lastTime == null || lastTime + AntiGuest.messageWaitTime < currentTime)
//        {
//            monsterPrev.sendMessage(player);
//            monsterTimestamps.put(player, currentTime);
//        }
//    }

//    private void noPickupMessage(Player player)
//    {
//        Long lastTime = this.pickupTimestamps.get(player);
//        long currentTime = System.currentTimeMillis();
//        if (lastTime == null || lastTime + AntiGuest.messageWaitTime < currentTime)
//        {
//            pickupPrev.sendMessage(player);
//            this.pickupTimestamps.put(player, currentTime);
//        }
//    }

    private boolean isPlayerChatLocked(Player player)
    {
        if (spamPrev.can(player))
        {
            return false;
        }
        else
        {
            Long lastTime = this.chatTimestamps.get(player);
            long currentTime = System.currentTimeMillis();
            if (lastTime == null || lastTime + (AntiGuest.getInstance().chatLockDuration * 1000) < currentTime)
            {
                this.chatTimestamps.put(player, currentTime);
                return false;
            }
            else
            {
                return true;
            }
        }
    }

    @EventHandler( priority=EventPriority.LOWEST )
    public void onPlayerPickupItem(PlayerPickupItemEvent event)
    {
        if (pickupPrev == null) return;
        
        final Player player = event.getPlayer();
        if (!pickupPrev.can(player))
        {
            event.setCancelled(true);
            pickupPrev.sendThrottledMessage(player);
        }
    }

    @EventHandler( priority=EventPriority.LOWEST )
    public void onPlayerChat(PlayerChatEvent event)
    {
        if (chatPrev == null) return;
        final Player player = event.getPlayer();
        if (!chatPrev.can(player))
        {
            event.setCancelled(true);
            chatPrev.sendMessage(player);
        }
    }

    @EventHandler( priority=EventPriority.LOWEST )
    public void onPlayerSpam(PlayerChatEvent event)
    {
        if (spamPrev == null) return;
        final Player player = event.getPlayer();
        if (this.isPlayerChatLocked(player))
        {
            event.setCancelled(true);
            spamPrev.sendMessage(player);
        }
    }

    private void handleBucketEvent(final PlayerBucketEvent event)
    {
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

    @EventHandler( priority=EventPriority.LOWEST )
    public void onPlayerBucketFill(PlayerBucketFillEvent event)
    {
        this.handleBucketEvent(event);
    }

    @EventHandler( priority=EventPriority.LOWEST )
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event)
    {
        this.handleBucketEvent(event);
    }

    @EventHandler( priority=EventPriority.LOWEST )
    public void onPlayerDropItem(PlayerDropItemEvent event)
    {
        if (dropPrev == null) return;
        final Player player = event.getPlayer();
        if (!dropPrev.can(player))
        {
            event.setCancelled(true);
            dropPrev.sendMessage(player);
        }
    }

    @EventHandler( priority=EventPriority.LOWEST )
    public void onPlayerBedEnter(PlayerBedEnterEvent event)
    {
        if (bedPrev == null) return;
        final Player player = event.getPlayer();
        if (bedPrev.can(player))
        {
            event.setCancelled(true);
            bedPrev.sendMessage(player);
        }
    }

    @EventHandler( priority=EventPriority.LOWEST )
    public void onPlayerFish(PlayerFishEvent event)
    {
        if (fishPrev == null) return;
        final Player player = event.getPlayer();
        if (!fishPrev.can(player))
        {
            event.setCancelled(true);
            fishPrev.sendMessage(player);
        }
    }

    @EventHandler( priority=EventPriority.LOWEST )
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event)
    {
        if (sneakPrev == null) return;
        final Player player = event.getPlayer();
        if (!event.isSneaking())
        {
            if (!sneakPrev.can(player))
            {
                AntiGuest.getInstance().debug("SneakEvent triggered!");
                for (StackTraceElement elem : Thread.currentThread().getStackTrace())
                {
                    AntiGuest.getInstance().debug("\t" + elem.getClassName() + "." + elem.getMethodName() + "() [" + elem.getFileName() + ":" + elem.getLineNumber() + "]");
                }
                event.setCancelled(true);
                sneakPrev.sendMessage(player);
            }
        }
    }

    @EventHandler( priority=EventPriority.LOWEST )
    public void onPlayerToggleSprint(PlayerToggleSprintEvent event)
    {
        if (sprintPrev == null) return;
        final Player player = event.getPlayer();
        if (!event.isSprinting())
        {
            if (!sprintPrev.can(player))
            {
                AntiGuest.getInstance().debug("SprintEvent triggered!");
                for (StackTraceElement elem : Thread.currentThread().getStackTrace())
                {
                    AntiGuest.getInstance().debug("\t" + elem.getClassName() + "." + elem.getMethodName() + "() [" + elem.getFileName() + ":" + elem.getLineNumber() + "]");
                }
                event.setCancelled(true);
                sprintPrev.sendMessage(player);
            }
        }
    }

    //@EventHandler( priority=EventPriority.LOWEST )
    public void onPlayerMove(PlayerMoveEvent event)
    {
        if (movePrev == null) return;
        final Player player = event.getPlayer();
        if (!movePrev.can(player))
        {
            event.setCancelled(true);
            movePrev.sendThrottledMessage(player);
        }
    }

    @EventHandler( priority=EventPriority.LOWEST )
    public void onEntityDamage(EntityDamageEvent event)
    {
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

    @EventHandler( priority=EventPriority.LOWEST )
    public void onEntityTarget(EntityTargetEvent event)
    {
        if (monsterPrev == null) return;
        Entity targetEntity = event.getTarget();
        if (event.getEntity() instanceof Monster && targetEntity != null && targetEntity instanceof Player)
        {
            final Player player = (Player)targetEntity;
            if (!monsterPrev.can(player))
            {
                event.setCancelled(true);
                monsterPrev.sendThrottledMessage(player);
            }
        }
    }
}
