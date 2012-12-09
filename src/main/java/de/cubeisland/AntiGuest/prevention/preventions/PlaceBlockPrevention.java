package de.cubeisland.AntiGuest.prevention.preventions;

import de.cubeisland.AntiGuest.prevention.FilteredItemPrevention;
import de.cubeisland.AntiGuest.prevention.PreventionPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;

/**
 * Prevents placing blocks
 *
 * @author Phillip Schichtel
 */
public class PlaceBlockPrevention extends FilteredItemPrevention
{
    public PlaceBlockPrevention(PreventionPlugin plugin)
    {
        super("placeblock", plugin);
        setEnableByDefault(true);
        setEnablePunishing(true);
        setFilterMode(FilterMode.NONE);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void place(BlockPlaceEvent event)
    {
        checkAndPrevent(event, event.getPlayer(), event.getBlockPlaced().getType());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void place(HangingPlaceEvent event)
    {
        checkAndPrevent(event, event.getPlayer());
    }
}
