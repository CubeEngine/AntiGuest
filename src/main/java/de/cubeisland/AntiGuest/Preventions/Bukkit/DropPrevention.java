package de.cubeisland.AntiGuest.Preventions.Bukkit;

import de.cubeisland.AntiGuest.AntiGuestBukkit;
import de.cubeisland.AntiGuest.FilteredItemPrevention;
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
    public DropPrevention()
    {
        super("drop", AntiGuestBukkit.getInstance());
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void handle(PlayerDropItemEvent event)
    {
        prevent(event, event.getPlayer(), event.getItemDrop().getItemStack().getType());
    }
}
