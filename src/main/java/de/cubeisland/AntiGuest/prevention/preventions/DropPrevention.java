package de.cubeisland.AntiGuest.prevention.preventions;

import de.cubeisland.AntiGuest.prevention.FilteredItemPrevention;
import de.cubeisland.AntiGuest.prevention.PreventionPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerDropItemEvent;

/**
 * Prevents item dropping
 *
 * @author Phillip Schichtel
 */
public class DropPrevention extends FilteredItemPrevention
{
    public DropPrevention(PreventionPlugin plugin)
    {
        super("drop", plugin);
        setEnablePunishing(true);
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void dropItem(PlayerDropItemEvent event)
    {
        prevent(event, event.getPlayer(), event.getItemDrop().getItemStack().getType());
    }
}