package de.codeinfection.quickwango.AntiGuest;

import java.util.HashMap;
import org.bukkit.Material;
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
public class AntiGuestPlayerListener extends AbstractAntiGuestListener implements Listener
{
    private static final HashMap<Player, Long> chatTimestamps    = new HashMap<Player, Long>();
    private static final HashMap<Player, Long> pickupTimestamps  = new HashMap<Player, Long>();
    private static final HashMap<Player, Long> monsterTimestamps = new HashMap<Player, Long>();


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


    public AntiGuestPlayerListener(AntiGuest plugin)
    {
        super(plugin);
    }

    protected void noMonsterTargetingMessage(Player player)
    {
        Long lastTime = monsterTimestamps.get(player);
        long currentTime = System.currentTimeMillis();
        if (lastTime == null || lastTime + AntiGuest.messageWaitTime < currentTime)
        {
            sendMessage(player, monsterPrev);
            monsterTimestamps.put(player, currentTime);
        }
    }

    private void noPickupMessage(Player player)
    {
        Long lastTime = this.pickupTimestamps.get(player);
        long currentTime = System.currentTimeMillis();
        if (lastTime == null || lastTime + AntiGuest.messageWaitTime < currentTime)
        {
            sendMessage(player, pickupPrev);
            this.pickupTimestamps.put(player, currentTime);
        }
    }

    private boolean isPlayerChatLocked(Player player)
    {
        if (can(player, spamPrev))
        {
            return false;
        }
        else
        {
            Long lastTime = this.chatTimestamps.get(player);
            long currentTime = System.currentTimeMillis();
            if (lastTime == null || lastTime + (getPlugin().chatLockDuration * 1000) < currentTime)
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

    @EventHandler( event=PlayerPickupItemEvent.class, priority=EventPriority.LOWEST )
    public void onPlayerPickupItem(PlayerPickupItemEvent event)
    {
        if (pickupPrev == null) return;
        final Player player = event.getPlayer();
        if (!can(player, pickupPrev))
        {
            event.setCancelled(true);
            this.noPickupMessage(player);
        }
    }

    @EventHandler( event=PlayerChatEvent.class, priority=EventPriority.LOWEST )
    public void onPlayerChat(PlayerChatEvent event)
    {
        if (chatPrev == null) return;
        final Player player = event.getPlayer();
        if (!can(player, chatPrev))
        {
            event.setCancelled(true);
            sendMessage(player, chatPrev);
        }
    }

    @EventHandler( event=PlayerChatEvent.class, priority=EventPriority.LOWEST )
    public void onPlayerSpam(PlayerChatEvent event)
    {
        if (spamPrev == null) return;
        final Player player = event.getPlayer();
        if (this.isPlayerChatLocked(player))
        {
            event.setCancelled(true);
            sendMessage(player, spamPrev);
        }
    }

    @EventHandler( event=PlayerBucketFillEvent.class, priority=EventPriority.LOWEST )
    public void onPlayerBucketFill(PlayerBucketFillEvent event)
    {
        final Player player = event.getPlayer();
        final Material bucket = event.getBucket();
        Prevention prevention;

        switch (bucket)
        {
            case LAVA_BUCKET:
                prevention = lavabucketPrev;
                break;
            case WATER_BUCKET:
                prevention = waterbucketPrev;
                break;
            default:
                getPlugin().log("Unknown bucket given to PlayerBucketFillEvent:" + bucket.toString());
                return;
        }

        if (prevention == null) return;
        if (!can(player, prevention))
        {
            event.setCancelled(true);
            sendMessage(player, prevention);
        }
    }

    @EventHandler( event=PlayerBucketEmptyEvent.class, priority=EventPriority.LOWEST )
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event)
    {
        final Player player = event.getPlayer();
        final Material bucket = event.getBucket();
        Prevention prevention;

        switch (bucket)
        {
            case LAVA_BUCKET:
                prevention = lavabucketPrev;
                break;
            case WATER_BUCKET:
                prevention = waterbucketPrev;
                break;
            default:
                getPlugin().log("Unknown bucket given to PlayerBucketEmptyEvent:" + bucket.toString());
                return;
        }

        if (prevention == null) return;
        if (!can(player, prevention))
        {
            event.setCancelled(true);
            sendMessage(player, prevention);
        }
    }

    @EventHandler( event=PlayerDropItemEvent.class, priority=EventPriority.LOWEST )
    public void onPlayerDropItem(PlayerDropItemEvent event)
    {
        if (dropPrev == null) return;
        final Player player = event.getPlayer();
        if (!can(player, dropPrev))
        {
            event.setCancelled(true);
            sendMessage(player, dropPrev);
        }
    }

    @EventHandler( event=PlayerBedEnterEvent.class, priority=EventPriority.LOWEST )
    public void onPlayerBedEnter(PlayerBedEnterEvent event)
    {
        if (bedPrev == null) return;
        final Player player = event.getPlayer();
        if (!can(player, bedPrev))
        {
            event.setCancelled(true);
            sendMessage(player, bedPrev);
        }
    }

    @EventHandler( event=PlayerFishEvent.class, priority=EventPriority.LOWEST )
    public void onPlayerFish(PlayerFishEvent event)
    {
        if (fishPrev == null) return;
        final Player player = event.getPlayer();
        if (!can(player, fishPrev))
        {
            event.setCancelled(true);
            sendMessage(player, fishPrev);
        }
    }

    @EventHandler( event=PlayerToggleSneakEvent.class, priority=EventPriority.LOWEST )
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event)
    {
        if (sneakPrev == null) return;
        final Player player = event.getPlayer();
        if (!event.isSneaking())
        {
            if (!can(player, sneakPrev))
            {
                getPlugin().debug("SneakEvent triggered!");
                for (StackTraceElement elem : Thread.currentThread().getStackTrace())
                {
                    getPlugin().debug("\t" + elem.getClassName() + "." + elem.getMethodName() + "() [" + elem.getFileName() + ":" + elem.getLineNumber() + "]");
                }
                event.setCancelled(true);
                sendMessage(player, sneakPrev);
            }
        }
    }

    @EventHandler( event=PlayerToggleSprintEvent.class, priority=EventPriority.LOWEST )
    public void onPlayerToggleSprint(PlayerToggleSprintEvent event)
    {
        if (sprintPrev == null) return;
        final Player player = event.getPlayer();
        if (!event.isSprinting())
        {
            if (!can(player, sprintPrev))
            {
                getPlugin().debug("SprintEvent triggered!");
                for (StackTraceElement elem : Thread.currentThread().getStackTrace())
                {
                    getPlugin().debug("\t" + elem.getClassName() + "." + elem.getMethodName() + "() [" + elem.getFileName() + ":" + elem.getLineNumber() + "]");
                }
                event.setCancelled(true);
                sendMessage(player, sprintPrev);
            }
        }
    }

    @EventHandler( event=PlayerMoveEvent.class, priority=EventPriority.LOWEST )
    public void onPlayerMove(PlayerMoveEvent event)
    {
        if (movePrev == null) return;
        final Player player = event.getPlayer();
        if (!can(player, movePrev))
        {
            event.setCancelled(true);
            sendMessage(player, movePrev);
        }
    }

    @EventHandler( event=EntityDamageEvent.class, priority=EventPriority.LOWEST )
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
        
        if (prevention != null && player != null && !can(player, prevention))
        {
            event.setCancelled(true);
            sendMessage(player, prevention);
        }
    }

    @EventHandler( event=EntityTargetEvent.class, priority=EventPriority.LOWEST )
    public void onEntityTarget(EntityTargetEvent event)
    {
        if (monsterPrev == null) return;
        Entity targetEntity = event.getTarget();
        if (event.getEntity() instanceof Monster && targetEntity != null && targetEntity instanceof Player)
        {
            final Player player = (Player)targetEntity;
            if (!can(player, monsterPrev))
            {
                event.setCancelled(true);
                this.noMonsterTargetingMessage(player);
            }
        }
    }
}
