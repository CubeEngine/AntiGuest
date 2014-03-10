package de.cubeisland.antiguest.prevention.preventions;

import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;

import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class ItemFramePrevention extends Prevention
{
    public ItemFramePrevention(PreventionPlugin plugin)
    {
        super("itemframe", plugin);
        setEnableByDefault(true);
        setEnablePunishing(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void interactEntity(PlayerInteractEntityEvent event)
    {
        if (event.getRightClicked() instanceof ItemFrame)
        {
            checkAndPrevent(event, event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void entityDamage(EntityDamageByEntityEvent event)
    {
        if (event.getEntity() instanceof ItemFrame && event.getDamager() instanceof Player)
        {
            checkAndPrevent(event, (Player)event.getDamager());
        }
    }
}
