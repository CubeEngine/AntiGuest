package de.cubeisland.antiguest.prevention.preventions;

import java.util.concurrent.TimeUnit;

import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;

import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;

/**
 * Prevents vehicle usage
 *
 * @author Phillip Schichtel
 */
public class VehiclePrevention extends Prevention
{
    private boolean access;
    private boolean destruction;
    private boolean collision;
    private boolean creation;

    public VehiclePrevention(PreventionPlugin plugin)
    {
        super("vehicle", plugin);
        setThrottleDelay(3, TimeUnit.SECONDS);
    }

    @Override
    public void enable()
    {
        super.enable();

        if (getConfig().contains("checkAndPrevent.access"))
        {
            this.access = getConfig().getBoolean("checkAndPrevent.access");
            getConfig().set("checkAndPrevent.access", null);
        }
        if (getConfig().contains("checkAndPrevent.destruction"))
        {
            this.access = getConfig().getBoolean("checkAndPrevent.destruction");
            getConfig().set("checkAndPrevent.destruction", null);
        }
        if (getConfig().contains("checkAndPrevent.collision"))
        {
            this.access = getConfig().getBoolean("checkAndPrevent.collision");
            getConfig().set("checkAndPrevent.collision", null);
        }
        if (getConfig().contains("checkAndPrevent.creation"))
        {
            this.access = getConfig().getBoolean("checkAndPrevent.creation");
            getConfig().set("checkAndPrevent.creation", null);
        }
        this.access = getConfig().getBoolean("prevent.access");
        this.destruction = getConfig().getBoolean("prevent.destruction");
        this.collision = getConfig().getBoolean("prevent.collision");
        this.creation = getConfig().getBoolean("prevent.creation");
    }

    @Override
    public Configuration getDefaultConfig()
    {
        Configuration defaultConfig = super.getDefaultConfig();

        defaultConfig.set("prevent.access", true);
        defaultConfig.set("prevent.destruction", true);
        defaultConfig.set("prevent.collision", true);
        defaultConfig.set("prevent.creation", true);

        return defaultConfig;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void enter(VehicleEnterEvent event)
    {
        if (!this.access)
        {
            return;
        }
        final Entity entered = event.getEntered();
        if (entered instanceof Player)
        {
            checkAndPrevent(event, (Player)entered);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void destroy(VehicleDestroyEvent event)
    {
        if (!this.destruction)
        {
            return;
        }
        final Entity attacker = event.getAttacker();
        if (attacker instanceof Player)
        {
            checkAndPrevent(event, (Player)attacker);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void entityCollision(VehicleEntityCollisionEvent event)
    {
        if (!this.collision)
        {
            return;
        }
        final Entity collider = event.getEntity();
        if (collider instanceof Player)
        {
            if (checkAndPrevent(event, (Player)collider))
            {
                event.setCollisionCancelled(true);
                event.setPickupCancelled(true);
            }
        }
    }

    private static boolean isMinecart(Material material)
    {
        switch (material)
        {
            case MINECART:
            case FURNACE_MINECART:
            case CHEST_MINECART:
            case TNT_MINECART:
            case HOPPER_MINECART:
            case COMMAND_BLOCK_MINECART:
                return true;
            default:
                return false;
        }
    }

    private static boolean isRail(Material material)
    {
        switch (material)
        {
            case RAIL:
            case POWERED_RAIL:
            case DETECTOR_RAIL:
            case ACTIVATOR_RAIL:
                return true;
            default:
                return false;
        }
    }

    private static boolean isBoat(Material material)
    {
        switch (material)
        {
            case SPRUCE_BOAT:
            case BIRCH_BOAT:
            case JUNGLE_BOAT:
            case ACACIA_BOAT:
            case DARK_OAK_BOAT:
                return true;
            default:
                return false;
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void interact(PlayerInteractEvent event)
    {
        if (!this.creation)
        {
            return;
        }
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            final Material clicked = event.getClickedBlock().getType();
            event.getClickedBlock().getState();
            final Player player = event.getPlayer();
            final Material inHand = player.getInventory().getItemInMainHand().getType();
            if (isRail(clicked))
            {
                if (isMinecart(inHand))
                {
                    checkAndPrevent(event, player);
                }
            }
            else
            {
                if (isBoat(inHand))
                {
                    checkAndPrevent(event, player);
                }
            }
        }
    }
}
