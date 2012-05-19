package de.cubeisland.AntiGuest.prevention.preventions;

import de.cubeisland.AntiGuest.prevention.Prevention;
import de.cubeisland.AntiGuest.prevention.PreventionPlugin;
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

/**
 * Prevents vehicle usage
 *
 * @author Phillip Schichtel
 */
public class VehiclePrevention extends Prevention
{
    private boolean preventAccess;
    private boolean preventDestruction;
    private boolean preventCollision;
    private boolean preventCreation;

    public VehiclePrevention(PreventionPlugin plugin)
    {
        super("vehicle", plugin);
        setThrottleDelay(3);
    }

    @Override
    public void enable()
    {
        super.enable();

        this.preventAccess      = getConfig().getBoolean("preventAccess");
        this.preventDestruction = getConfig().getBoolean("preventDestruction");
        this.preventCollision   = getConfig().getBoolean("preventCollision");
        this.preventCreation    = getConfig().getBoolean("preventCreation");
    }

    @Override
    public Configuration getDefaultConfig()
    {
        Configuration defaultConfig = super.getDefaultConfig();

        defaultConfig.set("preventAccess", true);
        defaultConfig.set("preventDestruction", true);
        defaultConfig.set("preventCollision", true);
        defaultConfig.set("preventCreation", true);

        return defaultConfig;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void enter(VehicleEnterEvent event)
    {
        if (!this.preventAccess)
        {
            return;
        }
        final Entity entered = event.getEntered();
        if (entered instanceof Player)
        {
            prevent(event, (Player)entered);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void destroy(VehicleDestroyEvent event)
    {
        if (!this.preventDestruction)
        {
            return;
        }
        final Entity attacker = event.getAttacker();
        if (attacker instanceof Player)
        {
            prevent(event, (Player)attacker);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void entityCollision(VehicleEntityCollisionEvent event)
    {
        if (!this.preventCollision)
        {
            return;
        }
        final Entity collider = event.getEntity();
        if (collider instanceof Player)
        {
            if (prevent(event, (Player)collider))
            {
                event.setCollisionCancelled(true);
                event.setPickupCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void interact(PlayerInteractEvent event)
    {
        if (!this.preventCreation)
        {
            return;
        }
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            final Material clickedMaterial = event.getClickedBlock().getType();
            final Player player = event.getPlayer();
            final Material materialInHand = player.getItemInHand().getType();
            if (clickedMaterial == Material.RAILS || clickedMaterial == Material.POWERED_RAIL || clickedMaterial == Material.DETECTOR_RAIL)
            {
                if (materialInHand == Material.MINECART || materialInHand == Material.POWERED_MINECART || materialInHand == Material.STORAGE_MINECART)
                {
                    prevent(event, player);
                }
            }
            else
            {
                if (materialInHand == Material.BOAT)
                {
                    prevent(event, player);
                }
            }
        }
    }    
}
