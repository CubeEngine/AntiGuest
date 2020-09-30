package de.cubeisland.antiguest.prevention.preventions;

import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import de.cubeisland.antiguest.prevention.Prevention;
import de.cubeisland.antiguest.prevention.PreventionPlugin;

/**
 * Prevents chest access
 *
 * @author Phillip Schichtel
 */
public class ChestPrevention extends Prevention {
    public ChestPrevention(PreventionPlugin plugin) {
        super("chest", plugin);
        setEnableByDefault(true);
    }

    protected static boolean isChest(BlockState state) {
        return state instanceof Chest || state instanceof DoubleChest;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void interact(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && isChest(event.getClickedBlock().getState()))
            if (checkAndPrevent(event, event.getPlayer()))
                event.setUseInteractedBlock(Result.DENY);
    }
}
