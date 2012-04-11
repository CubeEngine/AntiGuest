package de.cubeisland.AntiGuest.Preventions.Bukkit;

import de.cubeisland.AntiGuest.AntiGuestBukkit;
import de.cubeisland.AntiGuest.FilteredItemPrevention;
import org.bukkit.configuration.Configuration;
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
    public PlaceblockPrevention()
    {
        super("placeblock", AntiGuestBukkit.getInstance(), true, true);
    }

    @Override
    public Configuration getDefaultConfig()
    {
        Configuration config = super.getDefaultConfig();

        config.set("mode", "none");

        return config;
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
