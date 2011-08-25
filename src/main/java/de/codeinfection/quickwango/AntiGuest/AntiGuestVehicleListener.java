package de.codeinfection.quickwango.AntiGuest;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleListener;

/**
 *
 * @author CodeInfection
 */
public class AntiGuestVehicleListener extends VehicleListener
{
    protected final AntiGuest plugin;

    public AntiGuestVehicleListener(AntiGuest plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public void onVehicleDamage(VehicleDamageEvent event)
    {
        Entity entity = event.getAttacker();
        if (entity instanceof Player)
        {
            Player player = (Player)entity;
            if (!this.plugin.can(player, "breakblocks"))
            {
                this.plugin.message(player, "breakblocks");
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void onVehicleEnter(VehicleEnterEvent event)
    {
        Entity entity = event.getEntered();
        if (entity instanceof Player)
        {
            Player player = (Player)entity;
            if (!this.plugin.can(player, "vehicle"))
            {
                this.plugin.message(player, "vehicle");
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void onVehicleExit(VehicleExitEvent event)
    {
        LivingEntity entity = event.getExited();
        if (entity instanceof Player)
        {
            Player player = (Player)entity;
            if (!this.plugin.can(player, "vehicle"))
            {
                this.plugin.message(player, "vehicle");
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void onVehicleDestroy(VehicleDestroyEvent event)
    {
        Entity entity = event.getAttacker();
        if (entity instanceof Player)
        {
            Player player = (Player)entity;
            if (!this.plugin.can(player, "breakblocks"))
            {
                this.plugin.message(player, "breakblocks");
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void onVehicleEntityCollision(VehicleEntityCollisionEvent event)
    {
        Entity entity = event.getEntity();
        if (entity instanceof Player)
        {
            Player player = (Player)entity;
            if (!this.plugin.can(player, "vehicle"))
            {
                event.setCancelled(true);
                event.setCollisionCancelled(true);
                event.setPickupCancelled(true);
            }
        }
    }
}
