package de.codeinfection.quickwango.AntiGuest.Preventions;

import de.codeinfection.quickwango.AntiGuest.AntiGuest;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

/**
 *
 * @author Phillip
 */
public class VehiclePrevention extends Prevention
{

    public VehiclePrevention()
    {
        super("vehicle", AntiGuest.getInstance());
    }

    @Override
    public ConfigurationSection getDefaultConfig()
    {
        ConfigurationSection config = super.getDefaultConfig();

        config.set("message", "&4You are not allowed to use vehicles!");

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
    
    //@EventHandler(priority = EventPriority.LOWEST)
    public void handle(VehicleExitEvent event)
    {
        final LivingEntity entered = event.getExited();
        if (entered instanceof Player)
        {
            prevent(event, (Player)entered);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(VehicleEntityCollisionEvent event)
    {
        final Entity collider = event.getEntity();
        if (collider instanceof Player)
        {
            sendThrottledMessage((Player)collider);
            event.setCancelled(true);
            event.setCollisionCancelled(true);
            event.setPickupCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(PlayerInteractEvent event)
    {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            final Material clickedMaterial = event.getClickedBlock().getType();
            final Player player = event.getPlayer();
            // TODO returns AIR or null??
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
