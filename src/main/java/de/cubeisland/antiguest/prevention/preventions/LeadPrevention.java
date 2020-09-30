package de.cubeisland.antiguest.prevention.preventions;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;

public class LeadPrevention extends Prevention {
    public LeadPrevention(PreventionPlugin plugin) {
        super("lead", plugin);
        setEnablePunishing(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void interact(PlayerInteractEvent event) {
        if (event.getPlayer().getItemInHand().getType().equals(Material.LEASH) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            checkAndPrevent(event, event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void interact(PlayerInteractEntityEvent event) {
        if (event.getPlayer().getItemInHand().getType().equals(Material.LEASH))
            checkAndPrevent(event, event.getPlayer());
    }
}
