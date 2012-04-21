package de.cubeisland.AntiGuest.Preventions;

import de.cubeisland.AntiGuest.FilteredItemPrevention;
import de.cubeisland.AntiGuest.PreventionPlugin;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
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
        super("item", plugin, true);
    }

    @Override
    public Configuration getDefaultConfig()
    {
        Configuration config = super.getDefaultConfig();

        config.set("list", new String[] {Material.DIAMOND_SWORD.toString().toLowerCase().replace('_', ' ')});

        return config;
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
