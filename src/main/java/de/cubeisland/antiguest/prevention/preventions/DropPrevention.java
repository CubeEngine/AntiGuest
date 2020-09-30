package de.cubeisland.antiguest.prevention.preventions;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerDropItemEvent;

import de.cubeisland.antiguest.prevention.FilteredItemPrevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;

/**
 * Prevents item dropping
 *
 * @author Phillip Schichtel
 */
public class DropPrevention extends FilteredItemPrevention {
    public DropPrevention(PreventionPlugin plugin) {
        super("drop", plugin);
        setEnablePunishing(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void dropItem(PlayerDropItemEvent event) {
        checkAndPrevent(event, event.getPlayer(), event.getItemDrop().getItemStack().getType());
    }
}
