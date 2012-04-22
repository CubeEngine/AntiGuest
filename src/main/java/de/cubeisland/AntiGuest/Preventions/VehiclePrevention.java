package de.cubeisland.AntiGuest.Preventions;

import de.cubeisland.AntiGuest.Prevention;
import de.cubeisland.AntiGuest.PreventionPlugin;
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

    public VehiclePrevention(PreventionPlugin plugin)
    {
        super("vehicle", plugin);
    }

    @Override
    public Configuration getDefaultConfig()
    {
        Configuration config = super.getDefaultConfig();

        config.set("throttleDelay", 3);

        return config;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(VehicleEnterEvent event)
    {
        final Entity entered = event.getEntered();
        if (entered instanceof Player)
        {
            prevent(event, (Player)entered);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(VehicleDestroyEvent event)
    {
        final Entity attacker = event.getAttacker();
        if (attacker instanceof Player)
        {
            prevent(event, (Player)attacker);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(VehicleEntityCollisionEvent event)
    {
        final Entity collider = event.getEntity();
        if (collider instanceof Player)
        {
            if (preventThrottled(event, (Player)collider))
            {
                event.setCollisionCancelled(true);
                event.setPickupCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(PlayerInteractEvent event)
    {
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
