package de.cubeisland.AntiGuest.Preventions;

import de.cubeisland.AntiGuest.FilteredItemPrevention;
import de.cubeisland.AntiGuest.PreventionPlugin;
import java.util.EnumSet;
import org.bukkit.Material;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Prevents item usage
 *
 * @author Phillip Schichtel
 */
public class ItemPrevention extends FilteredItemPrevention
{
    public ItemPrevention(PreventionPlugin plugin)
    {
        super("item", plugin);
        this.setIgnoreBlocks(true);
        this.filterItems = EnumSet.of(Material.DIAMOND_SWORD);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void handle(PlayerInteractEvent event)
    {
        if (event.getAction() != Action.PHYSICAL)
        {
            final ItemStack itemInHand = event.getItem();
            if (itemInHand != null)
            {
                if (prevent(event, event.getPlayer(), event.getItem().getType()))
                {
                    event.setUseInteractedBlock(Result.DENY);
                    event.setUseItemInHand(Result.DENY);
                }
            }
        }
    }
}
