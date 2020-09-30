package de.cubeisland.antiguest.prevention.preventions;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;

/**
 * Prevents interaction with a horse/donkey/mule
 */
public class HorsePrevention extends Prevention {
    public HorsePrevention(PreventionPlugin plugin) {
        super("horse", plugin);
        setEnablePunishing(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void interact(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType().equals(EntityType.HORSE))
            checkAndPrevent(event, event.getPlayer());
    }
}
