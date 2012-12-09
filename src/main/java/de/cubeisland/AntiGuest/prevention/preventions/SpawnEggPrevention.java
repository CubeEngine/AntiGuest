package de.cubeisland.AntiGuest.prevention.preventions;

import de.cubeisland.AntiGuest.prevention.FilteredEntityPrevention;
import de.cubeisland.AntiGuest.prevention.PreventionPlugin;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Prevents spawnegg usage
 *
 * @author Phillip Schichtel
 */
public class SpawnEggPrevention extends FilteredEntityPrevention
{
    public SpawnEggPrevention(PreventionPlugin plugin)
    {
        super("spawnegg", plugin, true);
        setEnablePunishing(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void interact(PlayerInteractEvent event)
    {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            ItemStack item = event.getItem();
            if (item != null && item.getType() == Material.MONSTER_EGG)
            {
                if (checkAndPrevent(event, event.getPlayer(), EntityType.fromId(item.getData().getData())))
                {
                    event.setUseInteractedBlock(Event.Result.DENY);
                    event.setUseItemInHand(Event.Result.DENY);
                }
            }
        }
    }
}
