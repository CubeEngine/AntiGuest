package de.cubeisland.antiguest.prevention.preventions;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;

public class AnvilPrevention extends Prevention {
    public AnvilPrevention(PreventionPlugin plugin) {
        super("anvil", plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory().getType() == InventoryType.ANVIL && event.getPlayer() instanceof Player)
            checkAndPrevent(event, (Player) event.getPlayer());
    }
}
