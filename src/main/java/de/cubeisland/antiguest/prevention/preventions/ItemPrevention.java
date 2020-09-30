package de.cubeisland.antiguest.prevention.preventions;

import java.util.EnumSet;

import org.bukkit.Material;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import de.cubeisland.antiguest.prevention.FilteredItemPrevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;

/**
 * Prevents item usage
 *
 * @author Phillip Schichtel
 */
public class ItemPrevention extends FilteredItemPrevention {
    public ItemPrevention(PreventionPlugin plugin) {
        super("item", plugin);
        setIgnoreBlocks(true);
        setFilterItems(EnumSet.of(Material.FLINT_AND_STEEL));
        setFilterMode(FilterMode.BLACKLIST);
    }

    @Override
    public String getConfigHeader() {
        return super.getConfigHeader() + "\nBlocks will not be count as items!\n";
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void interact(PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL) {
            final ItemStack itemInHand = event.getItem();
            if (itemInHand != null)
                if (checkAndPrevent(event, event.getPlayer(), event.getItem().getType())) {
                    event.setUseInteractedBlock(Result.DENY);
                    event.setUseItemInHand(Result.DENY);
                }
        }
    }
}
