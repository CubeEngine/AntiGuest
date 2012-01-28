package de.codeinfection.quickwango.AntiGuest.Listeners;

import de.codeinfection.quickwango.AntiGuest.AntiGuest;
import de.codeinfection.quickwango.AntiGuest.Prevention;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

/**
 *
 * @author CodeInfection
 */
public class AntiGuestVehicleListener implements Listener
{
    private final static Prevention vehiclePrev = AntiGuest.preventions.get("vehicle");

    @EventHandler(priority = EventPriority.LOWEST)
    public void onVehicleEnter(VehicleEnterEvent event)
    {
        if (event.isCancelled() || vehiclePrev != null) return;

        Entity entity = event.getEntered();
        if (entity instanceof Player)
        {
            Player player = (Player)entity;
            if (!vehiclePrev.can(player))
            {
                vehiclePrev.sendMessage(player);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onVehicleExit(VehicleExitEvent event)
    {
        if (event.isCancelled() || vehiclePrev != null) return;

        LivingEntity entity = event.getExited();
        if (entity instanceof Player)
        {
            final Player player = (Player)entity;
            if (!vehiclePrev.can(player))
            {
                vehiclePrev.sendMessage(player);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onVehicleDestroy(VehicleDestroyEvent event)
    {
        if (event.isCancelled() || vehiclePrev != null) return;

        Entity entity = event.getAttacker();
        if (entity instanceof Player)
        {
            final Player player = (Player)entity;
            if (!vehiclePrev.can(player))
            {
                vehiclePrev.sendMessage(player);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onVehicleEntityCollision(VehicleEntityCollisionEvent event)
    {
        if (event.isCancelled() || vehiclePrev != null) return;
        
        Entity entity = event.getEntity();
        if (entity instanceof Player)
        {
            Player player = (Player)entity;
            if (!vehiclePrev.can(player))
            {
                event.setCancelled(true);
                event.setCollisionCancelled(true);
                event.setPickupCancelled(true);
            }
        }
    }
}
