package de.cubeisland.antiguest.prevention.preventions;

import org.bukkit.Material;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;

/**
 * Prevents trampling crops
 *
 * @author Phillip Schichtel
 */
public class TramplePrevention extends Prevention {
    public TramplePrevention(PreventionPlugin plugin) {
        super("trample", plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void interact(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.SOIL)
            if (checkAndPrevent(event, event.getPlayer()))
                event.setUseInteractedBlock(Result.DENY);
    }
}
