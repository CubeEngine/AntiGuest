package de.cubeisland.antiguest.prevention.preventions;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;

/**
 * Prevents brewing
 *
 * @author Phillip Schichtel
 */
public class BrewPrevention extends Prevention {
    public BrewPrevention(PreventionPlugin plugin) {
        super("brew", plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void open(InventoryOpenEvent event) {
        if (event.getInventory().getType() == InventoryType.BREWING)
            if (event.getPlayer() instanceof Player)
                checkAndPrevent(event, (Player) event.getPlayer());
    }
}
