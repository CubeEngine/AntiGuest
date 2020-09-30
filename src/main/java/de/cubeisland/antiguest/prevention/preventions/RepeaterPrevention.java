package de.cubeisland.antiguest.prevention.preventions;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;

/**
 * Prevents repeater changing
 *
 * @author Phillip Schichtel
 */
public class RepeaterPrevention extends Prevention {
    public RepeaterPrevention(PreventionPlugin plugin) {
        super("repeater", plugin);
        setEnablePunishing(true);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void interact(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            final Material material = event.getClickedBlock().getType();
            if (material == Material.REPEATER || material == Material.COMPARATOR)
                checkAndPrevent(event, event.getPlayer());
        }
    }
}
