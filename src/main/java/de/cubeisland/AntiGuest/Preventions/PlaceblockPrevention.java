package de.cubeisland.AntiGuest.Preventions;

import de.cubeisland.AntiGuest.FilteredItemPrevention;
import de.cubeisland.AntiGuest.PreventionPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.painting.PaintingPlaceEvent;

/**
 * Prevents placing blocks
 *
 * @author Phillip Schichtel
 */
public class PlaceblockPrevention extends FilteredItemPrevention
{
    public PlaceblockPrevention(PreventionPlugin plugin)
    {
        super("placeblock", plugin);
        setEnableByDefault(true);
        setFilterMode(FilterMode.NONE);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(BlockPlaceEvent event)
    {
        prevent(event, event.getPlayer(), event.getBlockPlaced().getType());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(PaintingPlaceEvent event)
    {
        prevent(event, event.getPlayer());
    }
}
