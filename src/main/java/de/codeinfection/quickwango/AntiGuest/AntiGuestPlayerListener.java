package de.codeinfection.quickwango.AntiGuest;

import java.util.HashMap;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerPickupItemEvent;

/**
 *
 * @author CodeInfection
 */
public class AntiGuestPlayerListener extends PlayerListener
{
    protected final AntiGuest plugin;
    protected final HashMap<Player, Long> chatTimestamps;
    protected final HashMap<Player, Long> pickupTimestamps;

    public AntiGuestPlayerListener(AntiGuest plugin)
    {
        this.plugin = plugin;
        this.chatTimestamps = new HashMap<Player, Long>();
        this.pickupTimestamps = new HashMap<Player, Long>();
    }

    protected void noPickupMessage(Player player)
    {
        Long lastTime = this.pickupTimestamps.get(player);
        long currentTime = System.currentTimeMillis();
        if (lastTime == null || lastTime + 5000 < currentTime)
        {
            this.plugin.message(player, "pickup");
            this.pickupTimestamps.put(player, currentTime);
        }
    }

    protected boolean canChat(Player player)
    {
        if (this.plugin.can(player, "spam"))
        {
            return true;
        }
        else
        {
            Long lastTime = this.chatTimestamps.get(player);
            long currentTime = System.currentTimeMillis();
            if (lastTime == null || lastTime + (this.plugin.chatLockDuration * 1000) < currentTime)
            {
                this.chatTimestamps.put(player, currentTime);
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        final Player player = event.getPlayer();
        Action action = event.getAction();
        Material material = event.getMaterial();
        if (action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_BLOCK)
        {
            if (this.plugin.actions.get("lever") && material == Material.LEVER) // lever
            {
                if (!this.plugin.can(player, "lever"))
                {
                    this.plugin.message(player, "lever");
                    event.setCancelled(true);
                    return;
                }
            }
            else if (this.plugin.actions.get("button") && material == Material.STONE_BUTTON) // buttons
            {
                if (!this.plugin.can(player, "button"))
                {
                    this.plugin.message(player, "button");
                    event.setCancelled(true);
                    return;
                }
            }
            else if (this.plugin.actions.get("door") && (material == Material.WOODEN_DOOR || material == Material.IRON_DOOR || material == Material.TRAP_DOOR)) // doors
            {
                if (!this.plugin.can(player, "door"))
                {
                    this.plugin.message(player, "door");
                    event.setCancelled(true);
                    return;
                }
            }
            else if (this.plugin.actions.get("chest") && material == Material.CHEST) // chests
            {
                if (!this.plugin.can(player, "chest"))
                {
                    this.plugin.message(player, "chest");
                    event.setCancelled(true);
                    return;
                }
            }
            else if (this.plugin.actions.get("workbench") && material == Material.WORKBENCH) // workbenches
            {
                if (!this.plugin.can(player, "workbench"))
                {
                    this.plugin.message(player, "workbench");
                    event.setCancelled(true);
                    return;
                }
            }
            else if (this.plugin.actions.get("furnace") && material == Material.FURNACE) // furnaces
            {
                if (!this.plugin.can(player, "furnace"))
                {
                    this.plugin.message(player, "furnace");
                    event.setCancelled(true);
                    return;
                }
            }
        }
        else if (this.plugin.actions.get("pressureplate") && action == Action.PHYSICAL)
        {
            if (material == Material.WOOD_PLATE || material == Material.STONE_PLATE) // pressure plates
            {
                if (!this.plugin.can(player, "pressureplate"))
                {
                    this.plugin.message(player, "pressureplate");
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @Override
    public void onPlayerPickupItem(PlayerPickupItemEvent event)
    {
        final Player player = event.getPlayer();
        if (!this.plugin.can(player, "pickup"))
        {
            event.setCancelled(true);
            this.noPickupMessage(player);
        }
    }

    @Override
    public void onPlayerChat(PlayerChatEvent event)
    {
        final Player player = event.getPlayer();
        if (!this.canChat(player))
        {
            event.setCancelled(true);
            this.plugin.message(player, "spam");
        }
    }

    @Override
    public void onPlayerBucketFill(PlayerBucketFillEvent event)
    {
        final Player player = event.getPlayer();
        if (!this.plugin.can(player, "bucket"))
        {
            event.setCancelled(true);
            this.noPickupMessage(player);
        }
    }

    @Override
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event)
    {
        final Player player = event.getPlayer();
        if (!this.plugin.can(player, "bucket"))
        {
            event.setCancelled(true);
            this.noPickupMessage(player);
        }
    }
}
