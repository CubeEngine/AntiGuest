package de.codeinfection.quickwango.AntiGuest.Preventions.Bukkit;

import de.codeinfection.quickwango.AntiGuest.AntiGuestBukkit;
import de.codeinfection.quickwango.AntiGuest.FilteredItemPrevention;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;

/**
 * Prevents block breaking
 *
 * @author Phillip Schichtel
 */
public class BreakblockPrevention extends FilteredItemPrevention
{
    public BreakblockPrevention()
    {
        super("breakblock", AntiGuestBukkit.getInstance());
    }

    @Override
    public Configuration getDefaultConfig()
    {
        Configuration config = super.getDefaultConfig();

        config.set("message", "&4You are not allowed to break blocks!");
        config.set("mode", "none");

        return config;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(BlockBreakEvent event)
    {
        prevent(event, event.getPlayer(), event.getBlock().getType());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(PaintingBreakByEntityEvent event)
    {
        final Entity remover = event.getRemover();
        if (remover instanceof Player)
        {
            prevent(event, (Player)remover, Material.PAINTING);
        }
    }
}
